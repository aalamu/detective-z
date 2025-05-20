package com.fleencorp.detectivez.service.impl;

import com.fleencorp.detectivez.constant.SecurityScoreRulePattern;
import com.fleencorp.detectivez.model.response.crawl.CrawlerResponse;
import com.fleencorp.detectivez.service.CrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static com.fleencorp.detectivez.util.CommonUtil.isValidUrl;

@Component
public class CrawlerServiceImpl implements CrawlerService {

  /**
   * Crawls the specified URL to extract textual content and detect security-related features.
   *
   * <p>This method first validates the provided URL. If the URL is not valid, it returns an empty
   * {@code CrawlerResponse}. If the URL is valid, it attempts to fetch and parse the HTML content
   * using Jsoup. It then extracts the plain text from the body of the page and analyzes it along with
   * the parsed document to identify various security-related features, which are added to a feature map.</p>
   *
   * <p>In case of a successful crawl, a {@code CrawlerResponse} containing the extracted features,
   * body text, and original URL is returned. If an I/O error occurs during the request,
   * the method returns an empty response.</p>
   *
   * @param url the website link to crawl and analyze
   * @return a {@code CrawlerResponse} containing extracted features and content if successful;
   * otherwise, an empty {@code CrawlerResponse}
   */
  @Override
  public CrawlerResponse crawlWebsiteOrLink(final String url) {
    final CrawlerResponse emptyCrawlerResponse = CrawlerResponse.of();

    if (!isValidUrl(url)) {
      return emptyCrawlerResponse;
    }

    final Map<SecurityScoreRulePattern, Object> features = new HashMap<>();
    try {
      final Document doc = Jsoup.connect(url).timeout(10000).get();
      final String text = doc.body().text();

      extractFeatures(url, features, text, doc);

      return CrawlerResponse.of(features, text, url);
    } catch (final IOException ex) {
      return emptyCrawlerResponse;
    }
  }

  /**
   * Extracts various security-related features from a given web page and stores them in the provided feature map.
   *
   * <p>This method analyzes the given URL, the raw text content of the page, and its parsed HTML document
   * to detect patterns associated with website security and trust. The results of each check are stored in the
   * provided map, with keys representing the type of feature being evaluated.</p>
   *
   * <p>The checks include whether the site uses HTTPS, whether a footer is present in the page layout,
   * whether contact information is available in the text, whether there are sensitive forms such as login or payment forms,
   * whether social media links are present, and whether a privacy policy or terms of service is referenced.</p>
   *
   * @param url the URL of the page being analyzed
   * @param features a map where each extracted feature is stored using its corresponding pattern as key
   * @param textContent the plain text content of the web page
   * @param doc the parsed HTML document representing the structure of the web page
   */
  protected void extractFeatures(final String url, final Map<SecurityScoreRulePattern, Object> features, final String textContent, final Document doc) {
    if (features == null) {
      return;
    }

    features.put(SecurityScoreRulePattern.https(), checkHttps(url));
    features.put(SecurityScoreRulePattern.footer(), checkFooter(doc));
    features.put(SecurityScoreRulePattern.contactInfo(), checkContactInfo(textContent));
    features.put(SecurityScoreRulePattern.sensitiveForms(), checkSensitiveForms(doc));
    features.put(SecurityScoreRulePattern.socialMediaLink(), checkSocialMediaLinks(doc));
    features.put(SecurityScoreRulePattern.privacyPolicy(), checkPrivacyPolicyOrTerms(doc));
  }

  /**
   * Checks if the given URL uses the HTTPS protocol.
   *
   * <p>This method verifies whether the provided URL is not null and begins with the
   * {@code "https"} scheme, indicating that it is secured using SSL/TLS encryption.</p>
   *
   * @param url the URL to inspect
   * @return {@code true} if the URL starts with {@code "https"}; {@code false} otherwise
   */
  protected boolean checkHttps(final String url) {
    return url != null && url.startsWith("https");
  }

  /**
   * Checks if the given HTML document contains a footer element.
   *
   * <p>This method verifies the presence of a {@code <footer>} tag in the document,
   * which is commonly used to define footer content such as contact information,
   * legal disclaimers, or site navigation links.</p>
   *
   * @param doc the parsed HTML document to inspect
   * @return {@code true} if a footer element is present; {@code false} otherwise
   */
  protected boolean checkFooter(final Document doc) {
    return doc != null && !doc.select("footer").isEmpty();
  }

  /**
   * Checks if the given HTML document contains a link to a privacy policy or terms of service.
   *
   * <p>This method looks for anchor tags whose {@code href} attributes contain the substrings
   * {@code "privacy"} or {@code "terms"}. It assumes that the presence of such links
   * indicates that the document references either a privacy policy or terms of use.</p>
   *
   * @param doc the parsed HTML document to inspect
   * @return {@code true} if a privacy policy or terms link is found; {@code false} otherwise
   */
  protected boolean checkPrivacyPolicyOrTerms(final Document doc) {
    return doc != null && !doc.select("a[href*=privacy], a[href*=terms]").isEmpty();
  }

  /**
   * Checks if the given text contains contact information such as an email address,
   * a phone number, or a street address.
   *
   * <p>This method uses regular expressions to detect common patterns for contact details.
   * It matches email addresses like "example@domain.com", phone numbers in formats like
   * "123-456-7890", "123.456.7890", or "123 456 7890", and street addresses such as
   * "123 Main Street" or "456 Elm Road".</p>
   *
   * @param text the text to scan for contact information
   * @return {@code true} if the text contains an email, phone number, or street address;
   *         {@code false} otherwise
   */
  protected boolean checkContactInfo(final String text) {
    final String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
    final String phoneRegex = "\\b\\d{3}[-.\\s]?\\d{3}[-.\\s]?\\d{4}\\b";
    final String addressRegex = "\\b\\d+\\s+[A-Za-z]+\\s+(Street|Avenue|Road|Blvd|Lane)\\b";

    return
      Pattern.compile(emailRegex).matcher(text).find() ||
      Pattern.compile(phoneRegex).matcher(text).find() ||
      Pattern.compile(addressRegex).matcher(text).find();
  }

  /**
   * Determines whether the HTML document contains hyperlinks to known social media platforms.
   *
   * <p>This method selects all anchor tags with an {@code href} attribute from the document and checks
   * if any of the links point to a known social media domain. The domains considered include popular platforms
   * such as Facebook, Twitter, LinkedIn, YouTube, Instagram, and X (formerly Twitter).</p>
   *
   * <p>If any link's URL contains one of the recognized social media domains, the method returns {@code true},
   * indicating that the page likely includes social media references.</p>
   *
   * @param doc the parsed HTML document to inspect
   * @return {@code true} if at least one social media link is found, otherwise {@code false}
   */
  protected boolean checkSocialMediaLinks(final Document doc) {
    final Set<String> socialMediaDomains = Set.of("facebook.com", "twitter.com", "linkedin.com", "youtube.com", "instagram.com", "x.com");

    return doc.select("a[href]")
      .stream()
      .map(link -> link.attr("href").toLowerCase())
      .anyMatch(href -> socialMediaDomains.stream().anyMatch(href::contains));
  }

  /**
   * Checks whether the HTML document contains form input fields with names that suggest the collection of sensitive user information.
   *
   * <p>This method selects all input fields within form elements that have a {@code name} attribute, then compares
   * their names (converted to lowercase) against a predefined set of keywords that indicate sensitive data,
   * such as credit card numbers, BVN, NIN, passwords, SSNs, login credentials, or account numbers.</p>
   *
   * <p>If any input field name contains one of these sensitive keywords, the method returns {@code true},
   * indicating that the page likely contains sensitive forms.</p>
   *
   * @param doc the parsed HTML document to analyze
   * @return {@code true} if any sensitive fields are detected, otherwise {@code false}
   */
  protected boolean checkSensitiveForms(final Document doc) {
    final Set<String> sensitiveFields = Set.of("credit_card", "bvn", "nin", "password", "ssn", "login", "accountNumber");

    return doc.select("form input[name]")
      .stream()
      .map(input -> input.attr("name").toLowerCase())
      .anyMatch(name -> sensitiveFields.stream().anyMatch(name::contains));
  }
}
