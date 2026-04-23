package br.com.softdesign.desafio.dto;

import br.com.softdesign.desafio.model.VoteValue;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de Response para o registro de um voto.
 */
public class VoteResponse {

    private Long voteId;

    @NotNull(message = "O ID do associado não pode ser nulo.")
    private Long associateId;

    @NotNull(message = "O ID da pauta não pode ser nulo.")
    private Long pollId;

    @NotNull(message = "O valor do voto não pode ser nulo.")
    private VoteValue value;

    private String message;

    public VoteResponse(Long voteId, Long associateId, Long pollId, VoteValue value, String message) {
        this.voteId = voteId;
        this.associateId = associateId;
        this.pollId = pollId;
        this.value = value;
        this.message = message;
    }

    // Getters and Setters (omitted for brevity, assuming standard Lombok/boilerplate)

    public Long getVoteId() {
        return voteId;
    }

    public void setVoteId(Long voteId) {
        this.voteId = voteId;
    }

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

    public VoteValue getValue() {
        return value;
    }

    public void setValue(VoteValue value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
