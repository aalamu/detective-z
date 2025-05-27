package com.fleencorp.detectivez.service;

import com.fleencorp.detectivez.exception.InvalidQueryException;
import com.fleencorp.detectivez.exception.InvestigationException;
import com.fleencorp.detectivez.model.dto.InvestigateDto;
import com.fleencorp.detectivez.model.response.investigation.InvestigateResponse;
import reactor.core.publisher.Mono;

public interface InvestigationService {

  Mono<InvestigateResponse> investigate(InvestigateDto investigateDto) throws InvalidQueryException, InvestigationException;
}
