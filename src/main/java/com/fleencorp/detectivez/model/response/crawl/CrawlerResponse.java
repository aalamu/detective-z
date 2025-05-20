package com.fleencorp.detectivez.model.response.crawl;

import com.fleencorp.detectivez.constant.SecurityScoreRulePattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrawlerResponse {

  private Map<SecurityScoreRulePattern, Object> websiteSecurityScoreRules;
  private String content;
  private String originalQuery;

  public static CrawlerResponse of(final Map<SecurityScoreRulePattern, Object> websiteSecurityScoreRules, final String content, final String originalQuery) {
    return new CrawlerResponse(websiteSecurityScoreRules, content, originalQuery);
  }

  public static CrawlerResponse of() {
    return new CrawlerResponse();
  }
}
