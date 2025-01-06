package com.fleencorp.detectivez.service;

import com.fleencorp.detectivez.model.dto.InvestigateDto;
import com.fleencorp.detectivez.model.response.InvestigateResponse;

public interface InvestigationService {

  InvestigateResponse investigate(InvestigateDto investigateDto);
}
