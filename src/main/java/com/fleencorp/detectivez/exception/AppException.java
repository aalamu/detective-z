package com.fleencorp.detectivez.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
  private final String errorCode;

  public AppException(String message, String errorCode) {
    super(message);
    this.errorCode = errorCode;
  }
}
