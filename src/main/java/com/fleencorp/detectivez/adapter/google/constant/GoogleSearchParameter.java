package com.fleencorp.detectivez.adapter.google.constant;

import com.fleencorp.detectivez.adapter.base.constant.ApiParameter;
import lombok.Getter;

@Getter
public enum GoogleSearchParameter implements ApiParameter {

  CX("cx"),
  KEY("key"),
  Q("q");

  private final String value;

  GoogleSearchParameter(final String value) {
    this.value = value;
  }
}
