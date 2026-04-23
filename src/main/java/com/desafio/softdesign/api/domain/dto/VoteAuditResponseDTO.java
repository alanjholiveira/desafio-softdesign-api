package com.desafio.softdesign.api.domain.dto;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para expor o histórico de auditoria de votos via API.
 * Garante que apenas os dados necessários sejam transmitidos ao cliente.
 */
public class VoteAuditResponseDTO {
    private Long id;
    private Long associateId;
    private String actionType;
    private LocalDateTime timestamp;
    private String details;

    // Construtor para mapeamento
    public VoteAuditResponseDTO(Long id, Long associateId, String actionType, LocalDateTime timestamp, String details) {
        this.id = id;
        this.associateId = associateId;
        this.actionType = actionType;
        this.timestamp = timestamp;
        this.details = details;
    }

    // Getters and Setters (necessários para JSON serialization)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
