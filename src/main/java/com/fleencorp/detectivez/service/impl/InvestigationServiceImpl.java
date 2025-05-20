package com.fleencorp.detectivez.service.impl;

import com.fleencorp.detectivez.adapter.base.service.InvestigationAdapter;
import com.fleencorp.detectivez.model.dto.InvestigateDto;
import com.fleencorp.detectivez.model.request.InvestigateRequest;
import com.fleencorp.detectivez.model.response.external.ExternalInvestigateResponse;
import com.fleencorp.detectivez.model.response.investigation.InvestigateResponse;
import com.fleencorp.detectivez.service.CrawlerService;
import com.fleencorp.detectivez.service.InvestigationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class InvestigationServiceImpl implements InvestigationService {

  private final CrawlerService crawlerService;
  private final InvestigationAdapter investigationAdapter;
  private final RankingEngineServiceImpl rankingEngineService;

  public InvestigationServiceImpl(
      final CrawlerService crawlerService,
      final InvestigationAdapter investigationAdapter,
      final RankingEngineServiceImpl rankingEngineService) {
    this.crawlerService = crawlerService;
    this.investigationAdapter = investigationAdapter;
    this.rankingEngineService = rankingEngineService;
  }

  /**
   * Initiates an investigation based on the provided {@link InvestigateDto}.
   *
   * <p>Extracts the query and asynchronously crawls the website or link, calculates a security score,
   * and creates an {@link InvestigateRequest} which is then investigated by the {@code investigationAdapter}.
   * The final investigation result is mapped to an {@link InvestigateResponse}.</p>
   *
   * @param investigateDto the DTO containing the query for investigation
   * @return a {@code Mono} emitting the {@link InvestigateResponse} with investigation results
   * @throws RuntimeException if crawling or scoring fails during the asynchronous operations
   */
  @Override
  public Mono<InvestigateResponse> investigate(final InvestigateDto investigateDto) {
    final String query = investigateDto.getQuery();

    return Mono.fromCallable(() -> crawlerService.crawlWebsiteOrLink(query))
      .subscribeOn(Schedulers.boundedElastic())
      .flatMap(crawlerResponse -> Mono.fromCallable(() ->
          rankingEngineService.calculateScore(crawlerResponse.getWebsiteSecurityScoreRules()))
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(score -> {
          final InvestigateRequest investigateRequest =
            InvestigateRequest.of(query, score, crawlerResponse.getContent());
          return investigationAdapter.investigate(investigateRequest);
        }))
      .map(ExternalInvestigateResponse::response)
      .map(InvestigateResponse::of);
  }


}
