package com.fleencorp.detectivez.service;

import com.fleencorp.detectivez.model.response.CrawlerWebsiteResponse;

public interface CrawlerService {

  CrawlerWebsiteResponse investigateTarget(String target);
}
