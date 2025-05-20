package com.fleencorp.detectivez.configuration;

import com.fleencorp.localizer.service.ErrorLocalizer;
import com.fleencorp.localizer.service.Localizer;
import com.fleencorp.localizer.service.adapter.DefaultLocalizer;
import com.fleencorp.localizer.service.adapter.DefaultLocalizerAdapter;
import com.fleencorp.localizer.service.adapter.ErrorLocalizerAdapter;
import com.fleencorp.localizer.service.adapter.LocalizerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class DetectiveMessageSourceConfiguration {

  @Value("${spring.messages.encoding}")
  private String messageSourceEncoding;

  @Value("${spring.messages.message.base-name}")
  private String defaultMessageBaseName;

  @Value("${spring.messages.error.base-name}")
  private String errorMessageBaseName;

  @Value("${spring.messages.response.base-name}")
  private String responseMessageBaseName;

  private ReloadableResourceBundleMessageSource baseMessageSource() {
    final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setCacheSeconds(60);
    messageSource.setDefaultLocale(Locale.US);
    messageSource.setAlwaysUseMessageFormat(true);
    messageSource.setUseCodeAsDefaultMessage(false);
    messageSource.setFallbackToSystemLocale(false);
    messageSource.setDefaultEncoding(messageSourceEncoding);
    return messageSource;
  }

  @Bean
  public MessageSource messageSource() {
    final ReloadableResourceBundleMessageSource messageSource = baseMessageSource();
    messageSource.setBasenames(defaultMessageBaseName);
    return messageSource;
  }

  public MessageSource errorMessageSource() {
    final ReloadableResourceBundleMessageSource messageSource = baseMessageSource();
    messageSource.setBasenames(errorMessageBaseName);
    return messageSource;
  }

  public MessageSource responseMessageSource() {
    final ReloadableResourceBundleMessageSource messageSource = baseMessageSource();
    messageSource.setBasenames(responseMessageBaseName);
    return messageSource;
  }

  @Bean
  public DefaultLocalizer defaultLocalizer(final MessageSource messageSource) {
    return new DefaultLocalizerAdapter(messageSource);
  }

  @Bean
  public Localizer localizer() {
    return new LocalizerAdapter(responseMessageSource());
  }

  @Bean
  public ErrorLocalizer errorLocalizer() {
    return new ErrorLocalizerAdapter(errorMessageSource());
  }
}
