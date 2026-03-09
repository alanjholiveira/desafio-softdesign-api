package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.domain.entity.Associate;
import br.com.softdesign.desafio.infrastructure.client.AssociateStatusClient;
import br.com.softdesign.desafio.infrastructure.client.response.StatusResponse;
import br.com.softdesign.desafio.infrastructure.enums.AssociateStatus;
import br.com.softdesign.desafio.infrastructure.exception.AssociateNotFoundException;
import br.com.softdesign.desafio.infrastructure.repository.AssociateRepository;
import feign.FeignException;
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
class AssociateServiceTest {

    @InjectMocks
    private AssociateService service;

    @Mock
    private AssociateRepository repository;

    @Mock
    private AssociateStatusClient statusClient;

    private Associate buildAssociate() {
        return Associate.builder()
                .id(UUID.randomUUID())
                .name("Nome Associado")
                .taxId("58382140076")
                .status(AssociateStatus.ABLE_TO_VOTE)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Test
    void when_getAll_returns_success() {
        Associate associateBuild = buildAssociate();
        when(repository.findAll()).thenReturn(List.of(associateBuild));

        List<Associate> associate = service.findAll();

        assertNotNull(associate);
        assertEquals(List.of(associateBuild), associate);
    }

    @Test
    void when_save_return_success() {
        Associate associateBuild = buildAssociate();
        when(repository.save(associateBuild)).thenReturn(associateBuild);
        when(statusClient.getStatus(associateBuild.getTaxId()))
                .thenReturn(StatusResponse.builder().status(AssociateStatus.ABLE_TO_VOTE).build());

        Associate associate = service.save(associateBuild);

        assertNotNull(associate);
        assertEquals(associateBuild, associate);
    }

    @Test
    void when_save_return_status_not_found() {
        Associate associateBuild = buildAssociate();
        when(statusClient.getStatus(associateBuild.getTaxId()))
                .thenThrow(FeignException.NotFound.class);

        assertThrows(AssociateNotFoundException.class, () -> service.save(associateBuild));
    }

    @Test
    void when_save_fallback_when_service_unavailable() {
        Associate associateBuild = buildAssociate();
        when(statusClient.getStatus(associateBuild.getTaxId()))
                .thenThrow(FeignException.ServiceUnavailable.class);
        when(repository.save(associateBuild)).thenReturn(associateBuild);

        Associate result = service.save(associateBuild);

        assertNotNull(result);
        assertEquals(AssociateStatus.ABLE_TO_VOTE, result.getStatus());
    }

}
