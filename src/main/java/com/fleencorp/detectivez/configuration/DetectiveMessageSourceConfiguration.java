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

  /**
   * Creates and configures a base {@link ReloadableResourceBundleMessageSource} used for message resolution.
   *
   * <p>This message source is set to cache messages for 60 seconds. The default locale is set to {@link Locale#US}.
   * Message formatting is always enabled. Message codes will not be used as default messages when not found.
   * The system locale will not be used as a fallback. The default encoding for message files is set using
   * the {@code messageSourceEncoding} field.</p>
   *
   * @return a configured instance of {@link ReloadableResourceBundleMessageSource}
   */
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

  /**
   * Defines the primary {@link MessageSource} bean for application-level messages.
   *
   * <p>Uses the base configuration from {@link #baseMessageSource()} and sets the
   * message basename to {@code defaultMessageBaseName}.</p>
   *
   * @return the primary {@link MessageSource} bean
   */
  @Bean
  public MessageSource messageSource() {
    final ReloadableResourceBundleMessageSource messageSource = baseMessageSource();
    messageSource.setBasenames(defaultMessageBaseName);
    return messageSource;
  }

  /**
   * Defines the primary {@link MessageSource} bean for application-level messages.
   *
   * <p>Uses the base configuration from {@link #baseMessageSource()} and sets the
   * message basename to {@code defaultMessageBaseName}.</p>
   *
   * @return the primary {@link MessageSource} bean
   */
  public MessageSource errorMessageSource() {
    final ReloadableResourceBundleMessageSource messageSource = baseMessageSource();
    messageSource.setBasenames(errorMessageBaseName);
    return messageSource;
  }

  /**
   * Creates a {@link MessageSource} configured for response messages.
   *
   * <p>This method reuses the base message source configuration from {@link #baseMessageSource()}
   * and sets the message bundle basename to {@code responseMessageBaseName}, which should
   * point to property files containing localized response messages.</p>
   *
   * @return a {@link MessageSource} for resolving localized response messages
   */
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
