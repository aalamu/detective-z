package com.fleencorp.detectivez.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
  private final String code;
  private final String message;
  private final int status;

}
