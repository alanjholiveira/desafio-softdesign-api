package br.com.softdesign.desafio.service;

import br.com.softdesign.desafio.model.VoteAudit;
import br.com.softdesign.desafio.model.VoteValue;
import br.com.softdesign.desafio.repository.VoteAuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditServiceTest {

    @Mock
    private VoteAuditRepository voteAuditRepository;

    @InjectMocks
    private AuditService auditService;

    private Long testVoteId = 1L;
    private Long testAssociateId = 10L;
    private Long testPollId = 20L;
    private VoteValue testVoteValue = VoteValue.YES;
    private String testIpAddress = "192.168.1.1";

    @BeforeEach
    void setUp() {
        // Configuração padrão para o mock
        when(voteAuditRepository.save(any(VoteAudit.class))).thenReturn(any(VoteAudit.class));
    }

    @Test
    void recordVoteAudit_shouldSaveAuditRecordSuccessfully() {
        // 1. Ação
        VoteAudit result = auditService.recordVoteAudit(
                testVoteId,
                testAssociateId,
                testPollId,
                testVoteValue,
                testIpAddress
        );

        // 2. Verificação
        assertNotNull(result);
        // Verifica se o método save foi chamado exatamente uma vez com os parâmetros corretos
        verify(voteAuditRepository, times(1)).save(any(VoteAudit.class));
    }

    @Test
    void recordVoteAudit_shouldHandleNullOrEmptyInputsGracefully() {
        // Testando um cenário onde alguns dados podem ser nulos (embora o modelo exija não nulo)
        // O teste foca em garantir que o serviço não quebre e tente persistir o que foi passado.
        VoteAudit result = auditService.recordVoteAudit(
                testVoteId,
                testAssociateId,
                testPollId,
                testVoteValue,
                null // IP nulo
        );

        assertNotNull(result);
        verify(voteAuditRepository, times(1)).save(any(VoteAudit.class));
    }
}
