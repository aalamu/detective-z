package com.fleencorp.detectivez.service.impl;

import com.fleencorp.detectivez.constant.SecurityScoreRulePattern;
import com.fleencorp.detectivez.service.RankingEngineService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RankingEngineServiceImpl implements RankingEngineService {

  /**
   * Calculates a security score based on the provided feature flags and returns a map containing
   * the score, a risk level description, and explanations for each failed security rule.
   *
   * <p>If the input features map is {@code null}, the method returns a default score of 0 with a
   * risk level indicating analysis failure and a relevant explanation message.</p>
   *
   * <p>For each security rule pattern, if the corresponding feature is missing or false, the score
   * increases and the related warning message is added to the explanations list. Sensitive forms
   * contribute a higher score penalty when present.</p>
   *
   * <p>The final risk level is categorized as "Low risk", "Medium risk", or "High risk" depending
   * on the accumulated score.</p>
   *
   * @param features a map of {@link SecurityScoreRulePattern} to their evaluation result (usually {@code Boolean})
   * @return a map containing keys "score" (Integer), "riskLevel" (String), and "explanations" (List of String)
   */
  @Override
  public Map<String, Object> calculateScore(final Map<SecurityScoreRulePattern, Object> features) {
    int score = 0;
    final List<String> explanations = new ArrayList<>();


    if (features == null) {
      return Map.of(
        "score", 0,
        "riskLevel", "Unable to analyze",
        "explanations", List.of("Failed to scrape the webpage")
      );
    }

    if (!(boolean) features.get(SecurityScoreRulePattern.contactInfo())) {
      score += 1;
      explanations.add(SecurityScoreRulePattern.contactInfo().getWarningMessage());
    }

    if (!(boolean) features.get(SecurityScoreRulePattern.socialMediaLink())) {
      score += 1;
      explanations.add(SecurityScoreRulePattern.socialMediaLink().getWarningMessage());
    }

    if (!(boolean) features.get(SecurityScoreRulePattern.footer())) {
      score += 1;
      explanations.add(SecurityScoreRulePattern.footer().getWarningMessage());
    }

    if (!(boolean) features.get(SecurityScoreRulePattern.privacyPolicy())) {
      score += 1;
      explanations.add(SecurityScoreRulePattern.privacyPolicy().getWarningMessage());
    }

    if ((boolean) features.get(SecurityScoreRulePattern.sensitiveForms())) {
      score += 2;
      explanations.add(SecurityScoreRulePattern.sensitiveForms().getWarningMessage());
    }

    if (!(boolean) features.get(SecurityScoreRulePattern.https())) {
      score += 1;
      explanations.add(SecurityScoreRulePattern.https().getWarningMessage());
    }

    final String riskLevel = score <= 2 ? "Low risk" : score <= 5 ? "Medium risk" : "High risk";

    return Map.of(
      "score", score,
      "riskLevel", riskLevel,
      "explanations", explanations
    );
  }
}
