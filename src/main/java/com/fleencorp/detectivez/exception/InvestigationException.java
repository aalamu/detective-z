package com.fleencorp.detectivez.exception;

import com.fleencorp.localizer.model.exception.LocalizedException;
import lombok.Getter;

@Getter
public class InvestigationException extends LocalizedException {

  @Override
  public String getMessageCode() {
    return "investigation";
  }

  public InvestigationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static InvalidQueryException of() {
    return new InvalidQueryException();
  }
}