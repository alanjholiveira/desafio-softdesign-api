package br.com.softdesign.desafio.application.rest.v1.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "PollResultResponse")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PollResultResponse {

    @Schema(description = "ID da pauta")
    private String pollId;

    @Schema(description = "Título da pauta")
    private String pollTitle;

    @Schema(description = "Total de votos")
    private Integer totalVotes;

    @Schema(description = "Total de votos SIM")
    private Integer yesVotes;

    @Schema(description = "Total de votos NÃO")
    private Integer noVotes;

    @Schema(description = "Resultado da votação", example = "APPROVED")
    private String result;

    @Schema(description = "Status da sessão", example = "OPEN")
    private String sessionStatus;

    @Schema(description = "Indica se a sessão ainda está aberta (votação parcial)", example = "false")
    private Boolean partial;

}