package com.fleencorp.detectivez.controller;


import com.fleencorp.detectivez.model.dto.InvestigateDto;
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
    model.addAttribute("message", "Link investigated successfully.");
    return "investigate";
  }

}
