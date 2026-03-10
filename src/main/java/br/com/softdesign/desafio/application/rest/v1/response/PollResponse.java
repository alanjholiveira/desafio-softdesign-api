package br.com.softdesign.desafio.application.rest.v1.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollResponse {

    @Schema(description = "Identificador único gerado para a pauta", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Título / Nome da pauta", example = "Aprovação do Orçamento 2026")
    private String name;

    @Schema(description = "Descrição detalhada do assunto", example = "Votação para aprovação do balanço financeiro e planejamento anual.")
    private String description;

    @Schema(description = "Data e hora de criação do registro", example = "2026-03-09 22:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;



}
