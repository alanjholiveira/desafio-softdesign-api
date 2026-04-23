package com.desafio.softdesign.api.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entidade responsável por registrar o histórico e a auditoria de qualquer ação realizada em um voto.
 * Este log garante rastreabilidade de quem, quando e por que o voto foi alterado.
 */
@Entity
@Table(name = "vote_audit")
public class VoteAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * O ID do voto que está sendo auditado.
     * Usamos como chave estrangeira para rastrear o registro principal.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    /**
     * O ID do associado que realizou a ação.
     */
    @Column(name = "associate_id", nullable = false)
    private Long associateId;

    /**
     * Tipo de ação realizada (ex: VOTE_CAST, VOTE_UPDATED, VOTE_REVOVED).
     */
    @Column(name = "action_type", nullable = false)
    private String actionType;

    /**
     * Timestamp da ocorrência do evento.
     */
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    /**
     * Detalhes adicionais do evento. Pode conter o estado do voto antes/depois da mudança,
     * ou o motivo da alteração. Armazenado como JSON ou String.
     */
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    // --- Construtores ---

    public VoteAudit() {
    }

    public VoteAudit(Vote vote, Long associateId, String actionType, String details) {
        this.vote = vote;
        this.associateId = associateId;
        this.actionType = actionType;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public Long getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Long associateId) {
        this.associateId = associateId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
