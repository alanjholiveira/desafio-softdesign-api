package br.com.softdesign.desafio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma sessão de votação ativa.
 */
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Foreign Key para a pauta (poll) que está sendo votada.
     */
    @Column(name = "poll_id", nullable = false)
    private Long pollId;

    /**
     * Status da sessão (OPEN, CLOSED, EXPIRED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SessionStatus status;

    /**
     * Data e hora de abertura da sessão.
     */
    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt;

    /**
     * Data e hora prevista para o encerramento automático da sessão.
     */
    @Column(name = "scheduled_end_time")
    private LocalDateTime scheduledEndTime;

    // --- Construtores ---

    public Session() {
        this.openedAt = LocalDateTime.now();
        this.status = SessionStatus.OPEN;
    }

    public Session(Long pollId, LocalDateTime scheduledEndTime) {
        this.pollId = pollId;
        this.status = SessionStatus.OPEN;
        this.openedAt = LocalDateTime.now();
        this.scheduledEndTime = scheduledEndTime;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPollId() {
        return pollId;
    }

    public void setPollId(Long pollId) {
        this.pollId = pollId;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDateTime getScheduledEndTime() {
        return scheduledEndTime;
    }

    public void setScheduledEndTime(LocalDateTime scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }
}
