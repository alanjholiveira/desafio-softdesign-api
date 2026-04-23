package br.com.softdesign.desafio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade responsável por armazenar o histórico de auditoria de cada voto.
 * Garante rastreabilidade completa da ação de votação.
 */
@Entity
@Table(name = "vote_audit")
public class VoteAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Foreign Key para o voto registrado.
     */
    @Column(name = "vote_id", nullable = false)
    private Long voteId;

    /**
     * Foreign Key para o associado que realizou o voto.
     */
    @Column(name = "associate_id", nullable = false)
    private Long associateId;

    /**
     * Foreign Key para a pauta (poll) em que o voto foi registrado.
     */
    @Column(name = "poll_id", nullable = false)
    private Long pollId;

    /**
     * O valor do voto (YES ou NO).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "voted_value", nullable = false)
    private VoteValue votedValue;

    /**
     * Timestamp da ocorrência do voto.
     */
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    /**
     * Endereço IP de onde o voto foi registrado (opcional).
     */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    // --- Construtores ---

    public VoteAudit() {
        this.timestamp = LocalDateTime.now();
    }

    public VoteAudit(Long voteId, Long associateId, Long pollId, VoteValue votedValue, String ipAddress) {
        this.voteId = voteId;
        this.associateId = associateId;
        this.pollId = pollId;
        this.votedValue = votedValue;
        this.timestamp = LocalDateTime.now();
        this.ipAddress = ipAddress;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
