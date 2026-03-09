package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.builder.entity.AssociateBuilder;
import br.com.softdesign.desafio.domain.entity.Associate;
import br.com.softdesign.desafio.infrastructure.client.AssociateStatusClient;
import br.com.softdesign.desafio.infrastructure.client.response.StatusResponse;
import br.com.softdesign.desafio.infrastructure.config.testcontainers.AbstractIntegrationTest;
import br.com.softdesign.desafio.infrastructure.enums.AssociateStatus;
import br.com.softdesign.desafio.infrastructure.exception.AssociateNotFoundException;
import br.com.softdesign.desafio.infrastructure.repository.AssociateRepository;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AssociateServiceTest extends AbstractIntegrationTest {

    @InjectMocks
    private AssociateService service;

    @Mock
    private AssociateRepository repository;

    @Mock
    private AssociateStatusClient statusClient;

    @Autowired
    private AssociateBuilder associateBuilder;

    @Test
    void when_getAll_returns_success() throws ParseException {
        Associate associateBuild = associateBuilder.construirEntidade();
        when(repository.findAll()).thenReturn(List.of(associateBuild));

        List<Associate> associate = service.findAll();

        assertNotNull(associate);
        assertEquals(List.of(associateBuild), associate);
    }

    @Test
    void when_save_return_success() throws ParseException {
        Associate associateBuild = associateBuilder.construirEntidade();
        when(repository.save(associateBuild)).thenReturn(associateBuild);
        when(statusClient.getStatus(associateBuild.getTaxId()))
                .thenReturn(StatusResponse.builder().status(AssociateStatus.ABLE_TO_VOTE).build());

        Associate associate = service.save(associateBuild);

        assertNotNull(associate);
        assertEquals(associateBuild, associate);
    }

    @Test
    void when_save_return_status_not_found() throws ParseException {
        Associate associateBuild = associateBuilder.construirEntidade();
        when(repository.save(associateBuild)).thenReturn(associateBuild);
        when(statusClient.getStatus(associateBuild.getTaxId()))
                .thenThrow(FeignException.class);

        assertThrows(AssociateNotFoundException.class, () -> {
            service.save(associateBuild);
        });

    }

}
