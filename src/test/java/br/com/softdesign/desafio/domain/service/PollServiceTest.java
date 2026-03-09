package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.infrastructure.repository.PollRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PollServiceTest {

    @InjectMocks
    private PollService service;

    @Mock
    private PollRepository repository;

    private Poll buildPoll() {
        return Poll.builder()
                .id(UUID.randomUUID())
                .name("Teste Pauta")
                .description("Descrição da Pauta")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Test
    void when_getAll_returns_success() {
        Poll pollBuild = buildPoll();
        when(repository.findAll()).thenReturn(List.of(pollBuild));

        List<Poll> response = service.findAll();

        assertNotNull(response);
        assertEquals(List.of(pollBuild), response);
    }

    @Test
    void when_save_return_success() {
        Poll pollBuild = buildPoll();
        when(repository.save(pollBuild)).thenReturn(pollBuild);

        Poll response = service.save(pollBuild);

        assertNotNull(response);
        assertEquals(pollBuild, response);
    }

}
