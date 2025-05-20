package com.fleencorp.detectivez.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.Executor;

@Configuration
@ComponentScan(basePackages = {"com.fleencorp.detectivez"})
public class DetectiveConfiguration {

  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder.build();
  }

  @Bean
  public RestClient restClient(final RestTemplate restTemplate) {
    return RestClient.create(restTemplate);
  }

  /**
   * Creates and configures a {@link ThreadPoolTaskExecutor} for asynchronous task execution.
   *
   * <p>The executor is configured with a core pool size of 5, a maximum pool size of 10,
   * a queue capacity of 100, and a thread name prefix of {@code "Async-"}. This setup allows
   * for efficient handling of asynchronous tasks with a bounded thread pool.</p>
   *
   * @return a configured {@link Executor} instance for asynchronous operations
   */
  @Bean(name = "asyncExecutor")
  public Executor asyncExecutor() {
    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(100);
    executor.setThreadNamePrefix("Async-");
    executor.initialize();

    return executor;
  }

  /**
   * Configures Cross-Origin Resource Sharing (CORS) settings for the application.
   *
   * <p>This method returns a {@code WebMvcConfigurer} that defines CORS mappings. It allows all origins
   * ({@code *}) and supports the HTTP methods GET, POST, PUT, DELETE, and OPTIONS for all endpoints
   * ({@code /**}).</p>
   *
   * @return a {@code WebMvcConfigurer} instance with configured CORS mappings
   */
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {

      @Override
      public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
          .allowedOrigins("*")
          .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
      }
    };
  }
}
