package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.builder.entity.PollBuilder;
import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.infrastructure.config.testcontainers.AbstractIntegrationTest;
import br.com.softdesign.desafio.infrastructure.repository.PollRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class PollServiceTest extends AbstractIntegrationTest {

    @InjectMocks
    private PollService service;

    @Mock
    private PollRepository repository;

    @Autowired
    private PollBuilder builder;

    @Test
    void when_getAll_returns_success() throws ParseException {
        Poll pollBuilder = builder.construirEntidade();
        when(repository.findAll()).thenReturn(List.of(pollBuilder));

        List<Poll> response = service.findAll();

        assertNotNull(response);
        assertEquals(List.of(pollBuilder), response);
    }

    @Test
    void when_save_return_success() throws ParseException {
        Poll pollBuilder = builder.construirEntidade();
        when(repository.save(pollBuilder)).thenReturn(pollBuilder);

        Poll response = service.save(pollBuilder);

        assertNotNull(response);
        assertEquals(pollBuilder, response);
    }

}
