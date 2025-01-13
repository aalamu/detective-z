package com.fleencorp.detectivez.service;

import com.fleencorp.detectivez.exception.FailedOperationException;
import com.fleencorp.detectivez.model.response.CrawlerWebsiteResponse;

public interface CrawlerService {

    /**
     * Fetches and wraps the HTML content of the given target URL.
     *
     * @param target the URL to investigate.
     * @return a CrawlerWebsiteResponse containing the website's HTML content.
     * @throws FailedOperationException if an error occurs while connecting to the target URL.
     */
    CrawlerWebsiteResponse investigateTarget(String target);
}
