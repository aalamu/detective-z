package com.fleencorp.detectivez.exception;

import com.fleencorp.detectivez.exception.external.ExternalSystemException;
import com.fleencorp.localizer.model.exception.ApiException;
import com.fleencorp.localizer.model.response.ErrorResponse;
import com.fleencorp.localizer.service.ErrorLocalizer;
import jakarta.ws.rs.core.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private final ErrorLocalizer localizer;

  public GlobalExceptionHandler(final ErrorLocalizer localizer) {
    this.localizer = localizer;
  }

  @ExceptionHandler(value = {
    InvestigationException.class,
    InvalidQueryException.class,
    ExternalSystemException.class,
    RuntimeException.class,
  })
  @ResponseStatus(value = BAD_REQUEST)
  public ErrorResponse handleBadRequest(final ApiException e) {
    return localizer.withStatus(e, Response.Status.BAD_REQUEST);
  }
}
