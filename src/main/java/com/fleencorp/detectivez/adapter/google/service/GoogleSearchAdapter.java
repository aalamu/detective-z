package com.fleencorp.detectivez.adapter.google.service;

import com.fleencorp.detectivez.adapter.google.model.GoogleSearchResult;
import reactor.core.publisher.Mono;

public interface GoogleSearchAdapter {

  Mono<GoogleSearchResult> investigate(String query);
}
