package com.desafio.softdesign.api.domain.model;

/**
 * Enum que define os tipos de ações que podem ser registradas no histórico de um voto.
 */
public enum AuditAction {
    VOTE_CAST("Voto registrado pela primeira vez"),
    VOTE_UPDATED("Voto alterado/revisado"),
    VOTE_REVOVED("Voto revogado ou invalidado");

    private final String description;

    AuditAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
