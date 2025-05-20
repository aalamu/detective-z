package com.fleencorp.detectivez.adapter.openai.service.impl;

import com.fleencorp.detectivez.adapter.openai.OpenAiAdapter;
import com.fleencorp.detectivez.model.response.external.ExternalInvestigateResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class OpenAiAdapterImpl implements OpenAiAdapter {

  private final ChatClient chatClient;

  public OpenAiAdapterImpl(final ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
  }

  @Override
  public Mono<ExternalInvestigateResponse> analyseAndReport(final String query) {
    return Mono.fromCallable(() -> {
        final String response = chatClient.prompt(query).call().content();
        return ExternalInvestigateResponse.of(response);
      })
      .subscribeOn(Schedulers.boundedElastic());
  }

}
