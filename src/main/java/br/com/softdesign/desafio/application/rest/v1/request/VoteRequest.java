package br.com.softdesign.desafio.application.rest.v1.request;

import br.com.softdesign.desafio.infrastructure.enums.VoteType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {

    @Schema(description = "Identificador único do associado votante", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull
    private UUID associate;

    @Schema(description = "Opção de voto escolhida", example = "YES")
    @NotNull
    private VoteType vote;

    @Schema(description = "Identificador da sessão aberta desta pauta", example = "8a3d6cb4-5b72-4d2c-8ab5-eabb8bdfc737")
    @NotNull
    private UUID session;

}
