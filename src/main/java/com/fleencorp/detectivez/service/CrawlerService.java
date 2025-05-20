package com.fleencorp.detectivez.service;

import com.fleencorp.detectivez.model.response.crawl.CrawlerResponse;

public interface CrawlerService {

  CrawlerResponse crawlWebsiteOrLink(String url);
}
