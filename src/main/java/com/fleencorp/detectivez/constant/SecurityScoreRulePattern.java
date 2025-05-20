package com.fleencorp.detectivez.constant;

import lombok.Getter;

@Getter
public enum SecurityScoreRulePattern {


  HAS_CONTACT_INFO("hasContactInfo", "No contact information found (+1)"),
  HAS_SOCIAL_MEDIA_LINK("hasSocialMediaLink", "No social media links found (+1)"),
  HAS_FOOTER("hasFooter", "No footer found (+1)"),
  HAS_PRIVACY_POLICY("hasPrivacyPolicy", "No privacy policy link found (+1)"),
  HAS_SENSITIVE_FORMS("hasSensitiveForms", "Forms asking for sensitive data (+2)"),
  USE_HTTPS("useHttps", "HTTP used instead of HTTPS (+1)");

  private final String value;
  private final String warningMessage;

  SecurityScoreRulePattern(
    final String value,
    final String warningMessage) {
    this.value = value;
    this.warningMessage = warningMessage;
  }

  public static SecurityScoreRulePattern contactInfo() {
    return SecurityScoreRulePattern.HAS_CONTACT_INFO;
  }

  public static SecurityScoreRulePattern socialMediaLink() {
    return SecurityScoreRulePattern.HAS_SOCIAL_MEDIA_LINK;
  }

  public static SecurityScoreRulePattern footer() {
    return SecurityScoreRulePattern.HAS_FOOTER;
  }

  public static SecurityScoreRulePattern privacyPolicy() {
    return SecurityScoreRulePattern.HAS_PRIVACY_POLICY;
  }

  public static SecurityScoreRulePattern sensitiveForms() {
    return SecurityScoreRulePattern.HAS_SENSITIVE_FORMS;
  }

  public static SecurityScoreRulePattern https() {
    return SecurityScoreRulePattern.USE_HTTPS;
  }
}
