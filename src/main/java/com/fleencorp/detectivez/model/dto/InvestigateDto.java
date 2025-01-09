package com.fleencorp.detectivez.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message ="URL cant be null")
    @URL(message = "URL must be valid")
    @JsonProperty("link")
    private String link;
}
