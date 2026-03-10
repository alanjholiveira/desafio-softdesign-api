package br.com.softdesign.desafio.application.rest.v1.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultResponse {

    @Schema(description = "Título da pauta avaliada", example = "Aprovação do Orçamento 2026")
    private String poll;
    
    @Schema(description = "Ocorrência total de votos contabilizados", example = "150")
    private long countVotes;
    
    @Schema(description = "Mapa detalhado computando os votos 'Sim' (YES) e 'Não' (NO)", example = "{\"YES\": 120, \"NO\": 30}")
    private Map<String, Integer> questions;

}
