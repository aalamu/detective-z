package com.fleencorp.detectivez.controller;


import com.fleencorp.detectivez.model.dto.InvestigateDto;
import com.fleencorp.detectivez.model.response.investigation.InvestigateResponse;
import com.fleencorp.detectivez.service.InvestigationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api")
public class InvestigationController {

  private final InvestigationService investigationService;

  public InvestigationController(final InvestigationService investigationService) {
    this.investigationService = investigationService;
  }

  @PostMapping(value = "/investigate")
  public Mono<InvestigateResponse> investigate(final @Valid @RequestBody InvestigateDto investigateDto) {
    return investigationService.investigate(investigateDto);
  }

}
