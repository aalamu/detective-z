package com.fleencorp.detectivez.exception.external;

import com.fleencorp.localizer.model.exception.LocalizedException;

public class ExternalSystemException extends LocalizedException {

  @Override
  public String getMessageCode() {
    return "external.system";
  }

  public ExternalSystemException(final String message) {
    super(message);
  }
}
