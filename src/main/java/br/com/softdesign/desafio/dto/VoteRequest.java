package br.com.softdesign.desafio.dto;

import br.com.softdesign.desafio.model.VoteValue;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Request para o registro de um voto.
 */
public class VoteRequest {

    @NotNull(message = "O ID do associado é obrigatório.")
    private Long associateId;

    @NotNull(message = "O ID da pauta é obrigatório.")
    private Long pollId;

    @NotNull(message = "O valor do voto é obrigatório.")
    private VoteValue voteValue;

    // Getters and Setters

    public Long getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Long associateId) {
        this.associateId = associateId;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public VoteValue getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(VoteValue voteValue) {
        this.voteValue = voteValue;
    }
}
