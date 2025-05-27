package com.fleencorp.detectivez.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "")
public class HomepageController {

  @Operation(
    summary = "Homepage endpoint",
    description = "Returns the index view for the homepage."
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Homepage returned successfully")
  })
  @GetMapping(value = "")
  public String homepage() {
    return "index";
  }
}
