package com.fleencorp.detectivez.adapter.base.service.impl;

import com.fleencorp.detectivez.adapter.base.service.InvestigationAdapter;
import com.fleencorp.detectivez.adapter.google.model.GoogleSearchResult;
import com.fleencorp.detectivez.adapter.google.model.GoogleSearchResultItem;
import com.fleencorp.detectivez.adapter.google.service.GoogleSearchAdapter;
import com.fleencorp.detectivez.adapter.openai.OpenAiAdapter;
import com.fleencorp.detectivez.constant.DataSource;
import com.fleencorp.detectivez.exception.InvalidQueryException;
import com.fleencorp.detectivez.exception.InvestigationException;
import com.fleencorp.detectivez.model.request.InvestigateRequest;
import com.fleencorp.detectivez.model.response.external.ExternalInvestigateResponse;
import com.fleencorp.detectivez.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.fleencorp.detectivez.util.CommonUtil.*;

@Slf4j
@Component
public class ExternalInvestigationAdapter implements InvestigationAdapter {

  private static final String INVESTIGATION_KEYWORDS = "scam OR fraud OR phishing";
  private static final String INSTRUCTION_TEMPLATE =
    """
    Hey, use your existing dataset and analyze the result I'm providing. Determine if the content is related to a scam, fraud, or phishing.
    
    Return a single JSON object with one property: `content`.  
    The value of `content` should be a **Markdown-formatted string** (e.g., using `**bold**`, lists, headers, etc.).  
    ⚠️ Do not include code blocks or wrap the response in triple backticks.  
    ⚠️ Return only the JSON object, without any explanation or surrounding text.
    
    If you're not certain whether the subject is related to scam, fraud, or phishing based on your dataset and the provided analysis, **do not make unreliable guesses**.  
    Instead, advise the user cautiously, explain the limitations of your certainty, and share what signs or red flags the user should look out for to protect themselves from potential scams or fraud.
    
    Example:
    {
      "content": "This appears to be a scam because:\n\n- **Red Flag 1**\n- **Red Flag 2**\n\nAuthorities should be alerted."
    }
    
    Query: %s  
    Analysis: %s  
    Analysis 2: %s  
    Other content: %s
    """;

  private final GoogleSearchAdapter googleSearchAdapter;
  private final OpenAiAdapter openAiAdapter;

  public ExternalInvestigationAdapter(
      final GoogleSearchAdapter googleSearchAdapter,
      final OpenAiAdapter openAiAdapter) {
    this.googleSearchAdapter = googleSearchAdapter;
    this.openAiAdapter = openAiAdapter;
  }

  /**
   * Performs an external investigation based on the given {@link InvestigateRequest}.
   *
   * <p>The method first validates that the request and its query string are not null or empty,
   * throwing an {@link InvalidQueryException} if validation fails.</p>
   *
   * <p>It then searches for relevant data using the query, processes and extracts information
   * from the search results, combines the formatted results, and formats instructions for analysis.</p>
   *
   * <p>These instructions are passed to the {@code openAiAdapter} to perform analysis and generate a report.
   * Any errors during the process are logged, and an {@link InvestigationException} is returned.</p>
   *
   * @param request the investigation request containing the website link or query
   * @return a {@code Mono} emitting an {@link ExternalInvestigateResponse} with the investigation results
   * @throws InvalidQueryException if the request or query string is null or empty
   * @throws InvestigationException if any error occurs during the investigation process
   */
  @Override
  public Mono<ExternalInvestigateResponse> investigate(final InvestigateRequest request) {
    CommonUtil.checkNotNull(request, InvalidQueryException::new);

    final String query = request.websiteLinkOrQuery();
    if (!StringUtils.hasText(query)) {
      return Mono.error(InvalidQueryException.of());
    }

    return findData(query)
      .map(ExternalInvestigationAdapter::processAndExtractInformation)
      .map(ExternalInvestigationAdapter::combineFormattedResults)
      .map(combinedContents -> formatInstructions(request, combinedContents))
      .flatMap(openAiAdapter::analyseAndReport)
      .onErrorResume(ex -> {
        log.error("Investigation failed {}", ex.getMessage());
        return Mono.error(InvestigationException.of());
      });
  }

  /**
   * Executes searches across all available data sources using the provided query.
   *
   * <p>This method iterates through all {@link DataSource} enum values and performs a search for each one by calling
   * {@link #performSearch(DataSource, String)}. If a search fails for a particular data source, the error is logged
   * and the search for that data source is skipped without interrupting the overall process.</p>
   *
   * <p>The results from all successful searches are collected into a list and returned as a {@code Mono}.</p>
   *
   * @param query the search query string to be used across data sources
   * @return a {@code Mono} emitting a list of {@link GoogleSearchResult} objects from all successful searches
   */
  private Mono<List<GoogleSearchResult>> findData(final String query) {
    return Flux.fromArray(DataSource.values())
      .flatMap(dataSource ->
        performSearch(dataSource, query)
          .onErrorResume(e -> {
            log.warn("Search failed for dataSource {}: {}", dataSource.name(), e.getMessage());
            return Mono.empty(); // continue on error
          })
      )
      .collectList();
  }

  /**
   * Performs a Google search using the specified data source and query or link.
   *
   * <p>The search query is constructed by applying the data source's query pattern. If the data source is site-specific
   * or the input is not a valid URL, the query is used as is. Otherwise, a site-specific prefix is prepended to the query.</p>
   *
   * <p>The formatted query is then passed to the {@code googleSearchAdapter} to perform the investigation and return results.</p>
   *
   * @param dataSource the data source containing the query pattern and site-specific flag
   * @param queryOrLink the query string or URL to search
   * @return a {@code Mono} emitting the {@link GoogleSearchResult} returned from the search adapter
   */
  private Mono<GoogleSearchResult> performSearch(final DataSource dataSource, final String queryOrLink) {
    final String pattern = dataSource.getQueryPattern();
    final String queryPrefix = (dataSource.isSiteSpecific() || !isValidUrl(queryOrLink)) ? "" : "site:" + validateAndSanitizeUrl(queryOrLink) + " ";
    final String formattedQuery = String.format(pattern, queryPrefix + queryOrLink, INVESTIGATION_KEYWORDS);

    return googleSearchAdapter.investigate(formattedQuery);
  }

  /**
   * Formats the investigation instructions using the predefined instruction template. It inserts values
   * from the given {@link InvestigateRequest} and the provided contents into the template.
   *
   * <p>This method ensures that null values for query, contents, analysis, or other text content are
   * replaced with empty strings to avoid formatting errors.</p>
   *
   * @param request the investigation request containing query, analysis, and additional text
   * @param contents the compiled textual content to be included in the instructions
   * @return the formatted instruction string ready for analysis
   */
  private static String formatInstructions(final InvestigateRequest request, final String contents) {
    return INSTRUCTION_TEMPLATE.formatted(
      nullToEmpty(request.query()),
      nullToEmpty(contents),
      nullToEmpty(request.analysis()),
      nullToEmpty(request.otherTextContent())
    );
  }

  /**
   * Processes a collection of {@link GoogleSearchResult} objects and extracts formatted information
   * from each non-null {@link GoogleSearchResultItem}.
   *
   * <p>Each result's items are flattened and passed to {@link #formatItem(GoogleSearchResultItem)} to produce
   * a list of formatted strings. {@code null} results or items are ignored.</p>
   *
   * @param searchResults the collection of GoogleSearchResult objects to process
   * @return a list of formatted strings extracted from each item's title and snippet;
   *         returns an empty list if the input is {@code null} or empty
   */
  private static List<String> processAndExtractInformation(final Collection<GoogleSearchResult> searchResults) {
    if (CollectionUtils.isEmpty(searchResults)) {
      return List.of();
    }

    return searchResults.stream()
      .filter(Objects::nonNull)
      .flatMap(result -> Optional.ofNullable(result.getItems())
        .orElse(List.of())
        .stream()
        .filter(Objects::nonNull)
        .map(ExternalInvestigationAdapter::formatItem))
      .toList();
  }

  /**
   * Formats a {@link GoogleSearchResultItem} into a single string containing the title and snippet.
   *
   * <p>Both values are separated by {@code " | "}. If the item is {@code null}, an empty string is returned.
   * {@code null} values for title or snippet are treated as empty strings.</p>
   *
   * @param item the GoogleSearchResultItem to format
   * @return a formatted string with title and snippet, or an empty string if the item is {@code null}
   */
  private static String formatItem(final GoogleSearchResultItem item) {
    if (Objects.isNull(item)) {
      return "";
    }

    final String delimiter = " | ";
    final String title = nullToEmpty(item.getTitle());
    final String snippet = nullToEmpty(item.getSnippet());

    return title + delimiter + snippet;
  }

  /**
   * Combines a collection of formatted strings into a single string, separated by a line delimiter.
   *
   * <p>Each entry is separated by {@code "\n---\n"}. If the collection is {@code null} or empty,
   * an empty string is returned.</p>
   *
   * @param formattedResults the collection of formatted strings to combine
   * @return a single string with each entry separated by the line delimiter
   */
  private static String combineFormattedResults(final Collection<String> formattedResults) {
    if (CollectionUtils.isEmpty(formattedResults)) {
      return "";
    }

    return String.join("\n---\n", formattedResults);
  }

}
