package br.com.softdesign.desafio.application.rest.v1.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

}
