package com.fleencorp.detectivez.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvestigateDto {

    @NotBlank(message = "{investigate.link}")
    @URL(message = "{investigate.link}")

    @JsonProperty("link")
    private String link;
}
