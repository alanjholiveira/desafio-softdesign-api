package br.com.softdesign.desafio.application.rest.v1.request;

import jakarta.validation.constraints.NotNull;
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
public class SessionRequest {

    @Schema(description = "Identificador único da pauta atrelada a esta sessão", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull
    private UUID pollId;

    @Schema(description = "Data limite de expiração. Se não fornecido, expira em 1 minuto.", example = "2026-03-09T22:00:00")
    private LocalDateTime expiration;

}
