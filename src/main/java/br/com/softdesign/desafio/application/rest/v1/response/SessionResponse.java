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
public class SessionResponse {

    @Schema(description = "Identificador único da sessão gerada", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Título da pauta associada", example = "Aprovação do Orçamento 2026")
    private String poll;

    @Schema(description = "Data e hora de abertura da sessão", example = "2026-03-09 22:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Data e hora de fechamento configurada", example = "2026-03-09 22:15:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiration;

    @Schema(description = "Indicador boolean de sessão aberta e apta para votos", example = "true")
    private Boolean isOpenSession;


}
