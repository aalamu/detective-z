package com.fleencorp.detectivez.constant.external;

import com.fleencorp.detectivez.adapter.base.constant.ApiParameter;
import lombok.Getter;

@Getter
public enum ExternalSystemType implements ApiParameter {

  GOOGLE_SEARCH("Google Search");

  private final String value;

  ExternalSystemType(final String value) {
    this.value = value;
  }

  public static ExternalSystemType googleSearch() {
    return ExternalSystemType.GOOGLE_SEARCH;
  }
}
