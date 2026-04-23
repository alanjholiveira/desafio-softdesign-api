package br.com.softdesign.desafio.model;

import java.time.LocalDateTime;

/**
 * Evento de Mensageria que encapsula os dados necessários para o registro de auditoria.
 * Este objeto será serializado e enviado via RabbitMQ.
 */
public class VoteAuditEvent {
    private Long voteId;
    private Long associateId;
    private Long pollId;
    private VoteValue votedValue;
    private String ipAddress;
    private LocalDateTime eventTimestamp;

    public VoteAuditEvent(Long voteId, Long associateId, Long pollId, VoteValue votedValue, String ipAddress) {
        this.voteId = voteId;
        this.associateId = associateId;
        this.pollId = pollId;
        this.votedValue = votedValue;
        this.ipAddress = ipAddress;
        this.eventTimestamp = LocalDateTime.now();
    }

    // Getters and Setters

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

    public VoteValue getVotedValue() {
        return votedValue;
    }

    public void setVotedValue(VoteValue votedValue) {
        this.votedValue = votedValue;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }
}
