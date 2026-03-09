package br.com.softdesign.desafio.application.rest.v1.request;

import br.com.softdesign.desafio.infrastructure.enums.VoteType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {

    @NotNull
    private UUID associate;

    @NotNull
    private VoteType vote;

    @NotNull
    private UUID session;

}
