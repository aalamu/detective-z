package com.fleencorp.detectivez.constant;

import lombok.Getter;

@Getter
public enum DataSource {

  GENERAL("%s %s", false),
  FACEBOOK("site:facebook.com %s %s", true),
  X("site:x.com %s %s", true),
  REDDIT("site:reddit.com %s %s", true),
  YOUTUBE("site:youtube.com %s %s", true);

  private final String queryPattern;
  private final boolean isSiteSpecific;

  DataSource(final String queryPattern, final boolean isSiteSpecific) {
    this.queryPattern = queryPattern;
    this.isSiteSpecific = isSiteSpecific;
  }

}
