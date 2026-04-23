package br.com.softdesign.desafio.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO de Request para abrir uma nova sessão de votação.
 */
public class SessionRequest {

    @NotNull(message = "O ID da pauta é obrigatório.")
    private Long pollId;

    /**
     * Data e hora programada para o encerramento da sessão.
     * Se for nulo, o sistema pode usar um valor padrão (ex: 1 hora).
     */
    private LocalDateTime scheduledEndTime;

    // Getters and Setters

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public LocalDateTime getScheduledEndTime() {
        return scheduledEndTime;
    }

    public void setScheduledEndTime(LocalDateTime scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }
}
