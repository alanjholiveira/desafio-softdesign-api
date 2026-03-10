package br.com.softdesign.desafio.application.rest.v1.response;

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
public class VoteResponse {

    @Schema(description = "Mensagem de confirmação que o voto foi registrado", example = "Voto computado com sucesso.")
    private String message;

}
