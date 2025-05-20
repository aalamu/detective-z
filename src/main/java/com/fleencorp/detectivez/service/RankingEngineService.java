package com.fleencorp.detectivez.service;

import com.fleencorp.detectivez.constant.SecurityScoreRulePattern;

import java.util.Map;

public interface RankingEngineService {

  Map<String, Object> calculateScore(Map<SecurityScoreRulePattern, Object> features);
}
