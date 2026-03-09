package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.infrastructure.enums.SessionStatus;
import br.com.softdesign.desafio.infrastructure.exception.PollNotFoundException;
import br.com.softdesign.desafio.infrastructure.exception.SessionOpenException;
import br.com.softdesign.desafio.infrastructure.repository.PollRepository;
import br.com.softdesign.desafio.infrastructure.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @InjectMocks
    private SessionService service;

    @Mock
    private SessionRepository repository;

    @Mock
    private PollRepository pollRepository;

    private Poll buildPoll() {
        return Poll.builder()
                .id(UUID.randomUUID())
                .name("Teste Pauta")
                .description("Descrição da Pauta")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    private Session buildSession(Poll poll) {
        return Session.builder()
                .id(UUID.randomUUID())
                .poll(poll)
                .status(SessionStatus.OPEN)
                .expiration(LocalDateTime.now().plusMinutes(30))
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Test
    void when_getAll_returns_success() {
        Poll poll = buildPoll();
        Session session = buildSession(poll);
        when(repository.findAll()).thenReturn(List.of(session));

        List<Session> response = service.findAll();

        assertNotNull(response);
        assertEquals(List.of(session), response);
    }

    @Test
    void when_open_session_return_success() {
        Poll poll = buildPoll();
        Session session = buildSession(poll);
        when(repository.existsByPoll(session.getPoll())).thenReturn(Boolean.FALSE);
        when(pollRepository.findById(session.getPoll().getId())).thenReturn(Optional.of(poll));
        when(repository.save(any(Session.class))).thenReturn(session);

        Session response = service.openSession(session);

        assertNotNull(response);
        assertEquals(session, response);
    }

    @Test
    void when_open_session_return_bad_request_exist_poll() {
        Poll poll = buildPoll();
        Session session = buildSession(poll);
        when(repository.existsByPoll(session.getPoll())).thenReturn(Boolean.TRUE);

        assertThrows(SessionOpenException.class, () -> service.openSession(session));
    }

    @Test
    void when_open_session_return_not_found_poll() {
        Poll poll = buildPoll();
        Session session = buildSession(poll);
        when(repository.existsByPoll(session.getPoll())).thenReturn(Boolean.FALSE);
        when(pollRepository.findById(session.getPoll().getId())).thenReturn(Optional.empty());

        assertThrows(PollNotFoundException.class, () -> service.openSession(session));
    }

}
