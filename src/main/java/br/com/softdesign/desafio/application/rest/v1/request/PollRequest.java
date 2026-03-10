package br.com.softdesign.desafio.application.rest.v1.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollRequest {

    @Schema(description = "Título / Nome da pauta", example = "Aprovação do Orçamento 2026")
    @NotEmpty
    private String name;

    @Schema(description = "Descrição detalhada do assunto em votação", example = "Votação para aprovação do balanço financeiro e planejamento anual.")
    @NotEmpty
    private String description;

}
