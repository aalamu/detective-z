package com.fleencorp.detectivez.adapter.base.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleencorp.detectivez.adapter.base.constant.ApiParameter;
import com.fleencorp.detectivez.adapter.base.constant.EndpointBlock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

import static com.fleencorp.detectivez.util.LoggingUtil.logIfEnabled;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
public class CustomRestAdapter {

  protected final String baseUrl;
  protected final WebClient webClient;

  public CustomRestAdapter(
    final String baseUrl,
    final WebClient.Builder webClientBuilder) {
    this.baseUrl = baseUrl;
    this.webClient = webClientBuilder.baseUrl(baseUrl).build();
  }

  /**
   * Constructs a {@code URI} by combining endpoint blocks and query parameters.
   *
   * <p>This method initializes a {@code UriComponentsBuilder} using the provided {@code urlBlocks} via
   * {@code initUriBuilder(EndpointBlock...)}. It then adds the query parameters from the provided
   * {@code queryParams} map, using the {@code ApiParameter} key's value as the parameter name and the
   * corresponding string value. The resulting {@code URI} is built and returned.</p>
   *
   * @param queryParams a map of {@code ApiParameter} keys and their string values to be added as query parameters
   * @param urlBlocks   variable number of {@code EndpointBlock} instances to append to the base URL
   * @return a {@code URI} constructed from the base URL, endpoint blocks, and query parameters
   */
  protected URI buildUri(final Map<ApiParameter, String> queryParams, final EndpointBlock... urlBlocks) {
    final UriComponentsBuilder uriComponentsBuilder = initUriBuilder(urlBlocks);
    queryParams.forEach((k, v) -> uriComponentsBuilder.queryParam(k.getValue(), v));

    return uriComponentsBuilder.build().toUri();
  }

  /**
   * Initializes a {@code UriComponentsBuilder} by constructing a URL from a base URL and optional endpoint blocks.
   *
   * <p>This method appends the values of the provided {@code EndpointBlock} instances to the {@code baseUrl}
   * to form a complete URL string. Non-null {@code EndpointBlock} values are concatenated in the order provided.
   * The resulting URL string is used to create a {@code UriComponentsBuilder} instance.</p>
   *
   * @param urlBlocks variable number of {@code EndpointBlock} instances to append to the base URL
   * @return a {@code UriComponentsBuilder} initialized with the constructed URL
   */
  protected UriComponentsBuilder initUriBuilder(final EndpointBlock... urlBlocks) {
    final StringBuilder urlBuilder = new StringBuilder(baseUrl);
    for (final EndpointBlock block : urlBlocks) {
      if (block != null) {
        urlBuilder.append(block.value());
      }
    }
    return UriComponentsBuilder.fromUriString(urlBuilder.toString());
  }

  /**
   * Performs an HTTP request using WebClient with no headers or body and returns a reactive {@code Mono} containing a {@code ResponseEntity}.
   *
   * <p>This method is a convenience wrapper around {@code doCall(URI, HttpMethod, Map, Object, Class, boolean)},
   * invoking it with no headers ({@code null}), no body ({@code null}), and {@code withBody} set to {@code false}.
   * It constructs and executes an HTTP request with the specified URI and HTTP method, mapping the response
   * to the specified {@code responseModel} type.</p>
   *
   * @param <T>           the type of the response body
   * @param uri           the URI for the HTTP request
   * @param method        the HTTP method (e.g., GET, POST)
   * @param responseModel the {@code Class} type to which the response body should be deserialized
   * @return a {@code Mono} containing a {@code ResponseEntity} with the response data or an error response
   */
  public <T> Mono<ResponseEntity<T>> doCall(final URI uri, final HttpMethod method, final Class<T> responseModel) {
    return doCall(uri, method, null, null, responseModel, false);
  }

  /**
   * Performs an HTTP request using WebClient and returns a reactive {@code Mono} containing a {@code ResponseEntity}.
   *
   * <p>This method constructs and executes an HTTP request with the specified URI, HTTP method, headers, and optional body.
   * It logs the request before execution, sets headers, and includes the body if {@code withBody} is {@code true}. The response
   * is mapped to the specified {@code responseModel} type. Errors are logged after execution, and any
   * {@code WebClientResponseException} is transformed into a {@code ResponseEntity} using
   * {@code createErrorResponse}.</p>
   *
   * @param <T>           the type of the response body
   * @param uri           the URI for the HTTP request
   * @param method        the HTTP method (e.g., GET, POST)
   * @param headers       a map of header names and values, or {@code null} if no headers are needed
   * @param body          the request body, or {@code null} if no body is needed
   * @param responseModel the {@code Class} type to which the response body should be deserialized
   * @param withBody      indicates whether to include a body in the request
   * @return a {@code Mono} containing a {@code ResponseEntity} with the response data or an error response
   */
  public <T> Mono<ResponseEntity<T>> doCall(final URI uri, final HttpMethod method, final Map<String, String> headers, final Object body, final Class<T> responseModel, final boolean withBody) {
    logRequestBeforeExecution(uri, method, body);

    final String payload = getPayloadBodyAsString(body);

    final WebClient.RequestBodySpec request = webClient.method(method).uri(uri);
    setHeaders(headers, request);
    final WebClient.ResponseSpec responseSpec = setBodyOptionally(withBody, request, payload);

    return responseSpec
      .toEntity(responseModel)
      .doOnError(ex -> logRequestErrorAfterExecution(uri, method, body, ex))
      .onErrorResume(WebClientResponseException.class, CustomRestAdapter::createErrorResponse);
  }

  /**
   * Creates a reactive {@code Mono} containing a {@code ResponseEntity} for an error response.
   *
   * <p>This method constructs a {@code ResponseEntity} using the HTTP status code, headers, and response body
   * from the provided {@code WebClientResponseException}. The response body is cast to the specified type {@code T}.</p>
   *
   * @param <T> the type of the response body
   * @param ex  the {@code WebClientResponseException} containing error details
   * @return a {@code Mono} containing a {@code ResponseEntity} with the error status, headers, and body
   */
  private static <T> Mono<ResponseEntity<T>> createErrorResponse(final WebClientResponseException ex) {
    final HttpHeaders errorHeaders = ex.getHeaders();
    final String errorBody = ex.getResponseBodyAsString();

    return Mono.just(ResponseEntity
      .status(ex.getStatusCode())
      .headers(errorHeaders)
      .body((T) errorBody));
  }

  /**
   * Configures a WebClient request with an optional body and retrieves the response.
   *
   * <p>If {@code withBody} is {@code true}, the method sets the content type to {@code application/json}
   * and includes the provided {@code payload} in the request body using {@code BodyInserters.fromValue}.
   * If {@code withBody} is {@code false}, the request is sent without a body. The method then calls
   * {@code retrieve()} to obtain the {@code ResponseSpec} for the request.</p>
   *
   * @param withBody indicates whether to include a body in the request
   * @param request  the {@code WebClient.RequestBodySpec} to configure
   * @param payload  the payload to include in the request body if {@code withBody} is {@code true}
   * @return a {@code WebClient.ResponseSpec} for the configured request
   */
  private static WebClient.ResponseSpec setBodyOptionally(final boolean withBody, final WebClient.RequestBodySpec request, final String payload) {
    return withBody
      ? request.contentType(APPLICATION_JSON).body(BodyInserters.fromValue(payload)).retrieve()
      : request.retrieve();
  }

  /**
   * Sets HTTP headers on a WebClient request using the provided header key-value pairs.
   *
   * <p>This method creates an {@code HttpHeaders} instance using {@code getHttpHeaders(Map)} and applies
   * the headers to the provided {@code WebClient.RequestBodySpec}. If the input headers map is
   * {@code null}, no headers are added to the request.</p>
   *
   * @param headers the map containing header names and their corresponding values, or {@code null}
   * @param request the {@code WebClient.RequestBodySpec} to which headers will be applied
   */
  protected static void setHeaders(final Map<String, String> headers, final WebClient.RequestBodySpec request) {
    final HttpHeaders requestHeaders = getHttpHeaders(headers);
    if (headers != null) {
      request.headers(httpHeaders -> httpHeaders.addAll(requestHeaders));
    }
  }

  /**
   * Creates a default {@code HttpHeaders} instance with JSON content type.
   *
   * <p>This method initializes a new {@code HttpHeaders} object and sets its content type to
   * {@code application/json}.</p>
   *
   * @return a new {@code HttpHeaders} instance with content type set to {@code application/json}
   */
  private static HttpHeaders getHttpHeaders() {
    final HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(APPLICATION_JSON);

    return requestHeaders;
  }

  /**
   * Creates and populates an {@code HttpHeaders} instance with the provided header key-value pairs.
   * <p>
   * This method initializes an {@code HttpHeaders} object using a default factory method
   * ({@code getHttpHeaders()}) and adds the provided headers to it. If the input headers map is
   * {@code null}, an empty {@code HttpHeaders} instance is returned.
   * </p>
   *
   * @param headers a map containing header names and their corresponding values, or {@code null}
   * @return an {@code HttpHeaders} instance populated with the provided headers
   */
  private static HttpHeaders getHttpHeaders(final Map<String, String> headers) {
    final HttpHeaders requestHeaders = getHttpHeaders();
    if (headers != null) {
      headers.forEach(requestHeaders::add);
    }
    return requestHeaders;
  }

  /**
   * Converts the request body object to its string representation.
   *
   * <p>If the body is already a {@code String}, it is returned as is. Otherwise, the body is serialized
   * to a JSON string using an {@code ObjectMapper}. If serialization fails due to a
   * {@code JsonProcessingException}, an empty string is returned.</p>
   *
   * @param body the request body object to convert to a string
   * @return the string representation of the body, or an empty string if serialization fails
   */
  private static String getPayloadBodyAsString(final Object body) {
    String payloadAsString = "";
    if (body instanceof String) {
      payloadAsString = (String) body;
    } else {
      try {
        payloadAsString = new ObjectMapper().writeValueAsString(body);
      } catch (final JsonProcessingException ignored) {}
    }

    return payloadAsString;
  }

  /**
   * Logs details of an HTTP request before its execution.
   *
   * <p>This method constructs a message containing the target URI, HTTP method, and request body,
   * which is logged at the INFO level if error logging is enabled.</p>
   *
   * @param uri        the URI of the HTTP request
   * @param httpMethod the HTTP method used for the request (e.g., GET, POST)
   * @param body       the request body, which will be converted to a string for logging
   */
  protected void logRequestBeforeExecution(final URI uri, final HttpMethod httpMethod, final Object body) {
    final String message = String.format(
      "HTTP call to url=%s with method=%s and body=%s",
      uri,
      httpMethod.name(),
      getPayloadBodyAsString(body));

    logIfEnabled(log::isErrorEnabled, () -> log.info(message));
  }

  /**
   * Logs an error that occurred during an HTTP request after its execution.
   *
   * <p>This method constructs a detailed error message including the target URI, HTTP method,
   * request body, and the exception message. The error is logged at the INFO level if error
   * logging is enabled.</p>
   *
   * @param uri        the URI of the HTTP request
   * @param httpMethod the HTTP method used for the request (e.g., GET, POST)
   * @param body       the request body, which will be converted to a string for logging
   * @param ex         the throwable exception that occurred during the request
   */
  protected void logRequestErrorAfterExecution(final URI uri, final HttpMethod httpMethod, final Object body, final Throwable ex) {
    final String message = String.format(
      "An error occurred while HTTP call to url=%s with method=%s and body=%s: %s",
      uri,
      httpMethod.name(),
      getPayloadBodyAsString(body),
      ex.getMessage());

    logIfEnabled(log::isErrorEnabled, () -> log.info(message));
  }
}