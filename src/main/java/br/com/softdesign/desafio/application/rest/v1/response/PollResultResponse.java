package br.com.softdesign.desafio.application.rest.v1.response;

import br.com.softdesign.desafio.infrastructure.enums.Result;
import br.com.softdesign.desafio.infrastructure.enums.SessionStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PollResultResponse {
    private UUID pollId;
    private String pollTitle;
    private Long totalVotes;
    private Long yesVotes;
    private Long noVotes;
    private Result result; // Nullable if session is open
    private SessionStatus sessionStatus;
    private Boolean partial;
}
