package com.fleencorp.detectivez.adapter.google.service.impl;

import com.fleencorp.detectivez.adapter.base.constant.ApiParameter;
import com.fleencorp.detectivez.adapter.base.service.impl.CustomRestAdapter;
import com.fleencorp.detectivez.adapter.google.constant.GoogleSearchParameter;
import com.fleencorp.detectivez.adapter.google.model.GoogleSearchResult;
import com.fleencorp.detectivez.adapter.google.service.GoogleSearchAdapter;
import com.fleencorp.detectivez.constant.external.ExternalSystemType;
import com.fleencorp.detectivez.exception.external.ExternalSystemException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.fleencorp.detectivez.util.LoggingUtil.logIfEnabled;

@Component
@Getter
@Slf4j
public class GoogleSearchAdapterImpl extends CustomRestAdapter implements GoogleSearchAdapter {

  private final String apiKey;
  private final String searchEngineKey;

  public GoogleSearchAdapterImpl(
      @Value("${google.search.api-key}") final String apiKey,
      @Value("${google.search.api-base-url}") final String baseUrl,
      @Value("${google.search.engine-key}") final String searchEngineKey,
      final WebClient.Builder webClientBuilder) {
    super(baseUrl, webClientBuilder);
    this.apiKey = apiKey;
    this.searchEngineKey = searchEngineKey;
  }

  /**
   * Performs a Google search investigation using the specified query.
   *
   * <p>Constructs the required API parameters including the query, API key, and search engine key.
   * It then builds the URI and makes a GET request to the Google Search API.</p>
   *
   * <p>If the response has a successful 2xx status code, the search results are returned as a {@code Mono}.
   * Otherwise, an error is logged and an {@link ExternalSystemException} is emitted to indicate failure
   * in calling the external Google Search service. Any exceptions during the API call are also caught,
   * logged, and mapped to the same exception type.</p>
   *
   * @param query the search query string
   * @return a {@code Mono} emitting the {@link GoogleSearchResult} or an error if the call fails
   */
  @Override
  public Mono<GoogleSearchResult> investigate(final String query) {
    final Map<ApiParameter, String> parameters = new HashMap<>();
    parameters.put(GoogleSearchParameter.Q, query);
    parameters.put(GoogleSearchParameter.KEY, apiKey);
    parameters.put(GoogleSearchParameter.CX, searchEngineKey);

    final URI uri = buildUri(parameters);

    return doCall(uri, HttpMethod.GET, GoogleSearchResult.class)
      .flatMap(response -> {
        if (response.getStatusCode().is2xxSuccessful()) {
          return Mono.justOrEmpty(response.getBody());
        } else {
          final String errorMessage = String.format(
            "An error occurred while calling investigate method of %s: %s",
            getClass().getName(),
            response
          );
          logIfEnabled(log::isErrorEnabled, () -> log.error(errorMessage));

          return Mono.error(new ExternalSystemException(ExternalSystemType.googleSearch().getValue()));
        }
      })
      .onErrorResume(e -> {
        log.error("Exception during Google Search API call", e);
        return Mono.error(new ExternalSystemException(ExternalSystemType.googleSearch().getValue()));
      });
  }
}
