package br.com.softdesign.desafio.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    private Poll poll;
    private Integer countVotes;
    private Map<String, Integer> questions;

}
