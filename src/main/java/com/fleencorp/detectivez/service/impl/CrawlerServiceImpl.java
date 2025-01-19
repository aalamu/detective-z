package com.fleencorp.detectivez.service.impl;

import com.fleencorp.detectivez.exception.FailedOperationException;
import com.fleencorp.detectivez.model.response.CrawlerWebsiteResponse;
import com.fleencorp.detectivez.service.CrawlerService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CrawlerServiceImpl implements CrawlerService {

  /**
   * Fetches and wraps the HTML content of the given target URL.
   *
   * @param target the URL to investigate.
   * @return a CrawlerWebsiteResponse containing the website's HTML content.
   * @throws FailedOperationException if an error occurs while connecting to the target URL.
   */
  @Override
  public CrawlerWebsiteResponse investigateTarget(String target) {
    Document result = null;
    try {
      result = Jsoup.connect(target).get();
    } catch (IOException e) {
      throw new FailedOperationException();
    }
    CrawlerWebsiteResponse response = new CrawlerWebsiteResponse(result != null ? result.html() : "");
    return response;
  }
}
