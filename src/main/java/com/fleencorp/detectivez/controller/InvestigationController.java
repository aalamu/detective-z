package com.fleencorp.detectivez.controller;


import com.fleencorp.detectivez.exception.InvalidQueryException;
import com.fleencorp.detectivez.exception.InvestigationException;
import com.fleencorp.detectivez.exception.external.ExternalSystemException;
import com.fleencorp.detectivez.model.dto.InvestigateDto;
import com.fleencorp.detectivez.model.response.investigation.InvestigateResponse;
import com.fleencorp.detectivez.service.InvestigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(
    summary = "Initiate an investigation process",
    description = "Takes in investigation details and initiates an investigation. May throw exceptions on validation, processing, or external system issues."
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Investigation completed successfully",
      content = @Content(schema = @Schema(implementation = InvestigateResponse.class))),
    @ApiResponse(responseCode = "400", description = "Invalid query parameters",
      content = @Content(schema = @Schema(implementation = InvalidQueryException.class))),
    @ApiResponse(responseCode = "422", description = "Investigation processing error",
      content = @Content(schema = @Schema(implementation = InvestigationException.class))),
    @ApiResponse(responseCode = "502", description = "Error communicating with external system",
      content = @Content(schema = @Schema(implementation = ExternalSystemException.class)))
  })
  @PostMapping(value = "/investigate")
  public Mono<InvestigateResponse> investigate(final @Valid @RequestBody InvestigateDto investigateDto) {
    return investigationService.investigate(investigateDto);
  }

}
