package com.fleencorp.detectivez.util;

import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public final class LoggingUtil {

  private LoggingUtil() {}

  public static void logIfEnabled(final Supplier<Boolean> logCheck, final Runnable logStatement) {
    if (nonNull(logCheck) && nonNull(logStatement)) {
      logStatement.run();
    }
  }
}
