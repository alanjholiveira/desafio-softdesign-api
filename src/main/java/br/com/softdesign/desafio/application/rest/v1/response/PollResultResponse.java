package br.com.softdesign.desafio.application.rest.v1.response;

import br.com.softdesign.desafio.infrastructure.enums.SessionStatus;
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
public class PollResultResponse {

    @Schema(description = "Identificador da pauta", example = "c56a4180-65aa-42ec-a945-5fd21dec0538")
    private UUID pollId;

    @Schema(description = "Título da pauta", example = "Aprovação do Orçamento 2026")
    private String pollTitle;

    @Schema(description = "Total de votos contabilizados", example = "150")
    private Long totalVotes;

    @Schema(description = "Quantidade de votos SIM", example = "120")
    private Long yesVotes;

    @Schema(description = "Quantidade de votos NÃO", example = "30")
    private Long noVotes;

    @Schema(description = "Resultado final da votação", example = "APPROVED")
    private ResultEnum result;

    @Schema(description = "Status da sessão", example = "CLOSED")
    private SessionStatus sessionStatus;

    @Schema(description = "Indica se a sessão ainda está aberta (resultado parcial)", example = "true")
    private boolean partial;

    public enum ResultEnum {
        APPROVED,
        REJECTED,
        TIE
    }
}
