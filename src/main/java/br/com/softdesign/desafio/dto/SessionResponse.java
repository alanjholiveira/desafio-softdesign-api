package br.com.softdesign.desafio.dto;

import br.com.softdesign.desafio.model.SessionStatus;
import java.time.LocalDateTime;

/**
 * DTO de Response para o retorno da abertura de sessão.
 */
public class SessionResponse {

    private Long sessionId;
    private Long pollId;
    private SessionStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime scheduledEndTime;
    private String message;

    public SessionResponse(Long sessionId, Long pollId, SessionStatus status, LocalDateTime openedAt, LocalDateTime scheduledEndTime, String message) {
        this.sessionId = sessionId;
        this.pollId = pollId;
        this.status = status;
        this.openedAt = openedAt;
        this.scheduledEndTime = scheduledEndTime;
        this.message = message;
    }

    // Getters and Setters (omitted for brevity)

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
