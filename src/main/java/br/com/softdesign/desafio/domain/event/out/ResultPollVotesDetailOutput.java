package br.com.softdesign.desafio.domain.event.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultPollVotesDetailOutput {

    @JsonProperty("pauta")
    private String poll;

    @JsonProperty("quantidadeVoto")
    private Integer countVotes;

    @JsonProperty("perguntas")
    private Map<String, Integer> questions;

}
