package com.fleencorp.detectivez.controller;


import com.fleencorp.detectivez.model.dto.InvestigateDto;
import com.fleencorp.detectivez.model.response.CrawlerWebsiteResponse;
import com.fleencorp.detectivez.service.CrawlerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class InvestigationController {

  private final CrawlerService crawlerService;

  public InvestigationController(CrawlerService crawlerService) {
    this.crawlerService = crawlerService;
  }

  @GetMapping(value = "/")
  public String investigate() {
    return "index";
  }

  @PostMapping(value = "investigate")
  public String investigate(@Valid @RequestBody InvestigateDto investigateDto, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("errors", bindingResult.getAllErrors());
      return "index";
    }
    CrawlerWebsiteResponse crawlerWebsiteResponse = crawlerService.investigateTarget(investigateDto.getLink());
    model.addAttribute("message", "Link investigated successfully.");
    return crawlerWebsiteResponse.textContent() == null || crawlerWebsiteResponse.textContent().isBlank() ? crawlerWebsiteResponse.textContent() : null;
  }

}
