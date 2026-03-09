package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.builder.entity.PollBuilder;
import br.com.softdesign.desafio.builder.entity.SessionBuilder;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.infrastructure.config.testcontainers.AbstractIntegrationTest;
import br.com.softdesign.desafio.infrastructure.exception.PollNotFoundException;
import br.com.softdesign.desafio.infrastructure.exception.SessionOpenException;
import br.com.softdesign.desafio.infrastructure.repository.PollRepository;
import br.com.softdesign.desafio.infrastructure.repository.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SessionServiceTest extends AbstractIntegrationTest {

    @InjectMocks
    private SessionService service;

    @Mock
    private SessionRepository repository;

    @Mock
    private PollRepository pollRepository;

    @Autowired
    private SessionBuilder builder;

    @Autowired
    private PollBuilder pollBuilder;

    @Test
    void when_getAll_returns_success() throws ParseException {
        Session sessionBuilder = builder.construirEntidade();
        when(repository.findAll()).thenReturn(List.of(sessionBuilder));

        List<Session> response = service.findAll();

        assertNotNull(response);
        assertEquals(List.of(sessionBuilder), response);
    }

    @Test
    void when_open_session_return_success() throws ParseException {
        Session sessionBuilder = builder.construirEntidade();
        when(repository.save(sessionBuilder)).thenReturn(sessionBuilder);
        when(repository.findById(sessionBuilder.getId())).thenReturn(Optional.of(sessionBuilder));
        when(pollRepository.findById(sessionBuilder.getPoll().getId()))
                .thenReturn(Optional.of(pollBuilder.construirEntidade()));

        Session response = service.openSession(sessionBuilder);

        assertNotNull(response);
        assertEquals(sessionBuilder, response);
    }

    @Test
    void when_open_session_return_bad_request_exist_poll() throws ParseException {
        Session sessionBuilder = builder.construirEntidade();

        when(repository.save(sessionBuilder)).thenReturn(sessionBuilder);
        when(repository.findById(sessionBuilder.getId())).thenReturn(Optional.of(sessionBuilder));
        when(repository.existsByPoll(sessionBuilder.getPoll())).thenReturn(Boolean.TRUE);
        when(pollRepository.findById(sessionBuilder.getPoll().getId()))
                .thenReturn(Optional.of(pollBuilder.construirEntidade()));

        assertThrows(SessionOpenException.class, () -> {
            service.openSession(sessionBuilder);
        });

    }

    @Test
    void when_open_session_return_not_found_poll() throws ParseException {
        Session sessionBuilder = builder.construirEntidade();

        when(repository.save(sessionBuilder)).thenReturn(sessionBuilder);
        when(repository.findById(sessionBuilder.getId())).thenReturn(Optional.of(sessionBuilder));
        when(pollRepository.findById(sessionBuilder.getPoll().getId()))
                .thenReturn(Optional.empty());

        assertThrows(PollNotFoundException.class, () -> {
            service.openSession(sessionBuilder);
        });

    }

}
