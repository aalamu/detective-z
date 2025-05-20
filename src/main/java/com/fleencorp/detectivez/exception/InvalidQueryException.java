package com.fleencorp.detectivez.exception;

import com.fleencorp.localizer.model.exception.LocalizedException;

public class InvalidQueryException extends LocalizedException {

  @Override
  public String getMessageCode() {
    return "invalid.query";
  }

  public static InvalidQueryException of() {
    return new InvalidQueryException();
  }
}
