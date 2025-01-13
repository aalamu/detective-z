package com.fleencorp.detectivez.controller;

import com.fleencorp.detectivez.model.dto.InvestigateDto;
import com.fleencorp.detectivez.model.response.CrawlerWebsiteResponse;
import com.fleencorp.detectivez.service.CrawlerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class CrawlerController {

    private CrawlerService crawlerService;

    @PostMapping(value = "/crawler")
    public CrawlerWebsiteResponse investigateTarget(@Valid @RequestBody InvestigateDto investigateDto) {
        return crawlerService.investigateTarget(investigateDto.getLink());
    }
}
