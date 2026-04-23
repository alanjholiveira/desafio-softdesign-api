package com.desafio.softdesign.api.domain.service;

import com.desafio.softdesign.api.domain.model.AuditAction;
import com.desafio.softdesign.api.domain.model.VoteAudit;
import com.desafio.softdesign.api.domain.model.Vote;
import com.desafio.softdesign.api.domain.repository.VoteAuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteAuditServiceTest {

    @Mock
    private VoteAuditRepository voteAuditRepository;

    @InjectMocks
    private VoteAuditService voteAuditService;

    private final Long TEST_VOTE_ID = 100L;
    private final Long TEST_ASSOCIATE_ID = 1L;

    @BeforeEach
    void setUp() {
        // Configuração básica antes de cada teste
    }

    @Test
    void recordVoteAudit_shouldPersistAuditRecordSuccessfully() {
        // Arrange
        Vote dummyVote = new Vote();
        dummyVote.setId(TEST_VOTE_ID);
        AuditAction action = AuditAction.VOTE_CAST;
        Map<String, Object> details = Map.of("status", "CAST");

        // Mock o comportamento de salvar
        when(voteAuditRepository.save(any(VoteAudit.class))).thenReturn(
                createMockVoteAudit(1L, TEST_VOTE_ID, TEST_ASSOCIATE_ID, action.name(), "details")
        );

        // Act
        VoteAudit result = voteAuditService.recordVoteAudit(TEST_VOTE_ID, TEST_ASSOCIATE_ID, action, details);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(voteAuditRepository, times(1)).save(any(VoteAudit.class));
    }

    @Test
    void getAuditHistory_shouldReturnAllAuditRecordsForGivenVoteId() {
        // Arrange
        VoteAudit audit1 = createMockVoteAudit(1L, TEST_VOTE_ID, TEST_ASSOCIATE_ID, "VOTE_CAST", "details");
        VoteAudit audit2 = createMockVoteAudit(2L, TEST_VOTE_ID, TEST_ASSOCIATE_ID, "VOTE_UPDATED", "details");
        List<VoteAudit> expectedHistory = Arrays.asList(audit1, audit2);

        // Mock o comportamento de busca
        when(voteAuditRepository.findByVote_Id(TEST_VOTE_ID)).thenReturn(expectedHistory);

        // Act
        List<VoteAudit> actualHistory = voteAuditService.getAuditHistory(TEST_VOTE_ID);

        // Assert
        assertNotNull(actualHistory);
        assertEquals(2, actualHistory.size());
        assertEquals(audit1, actualHistory.get(0));
        assertEquals(audit2, actualHistory.get(1));
        verify(voteAuditRepository, times(1)).findByVote_Id(TEST_VOTE_ID);
    }

    // Helper method para criar um objeto VoteAudit mockado
    private VoteAudit createMockVoteAudit(Long id, Long voteId, Long associateId, String actionType, String details) {
        VoteAudit audit = new VoteAudit();
        audit.setId(id);
        audit.setVote(new Vote().setId(voteId));
        audit.setAssociateId(associateId);
        audit.setActionType(actionType);
        audit.setDetails(details);
        // Não definimos o timestamp aqui para evitar problemas de mock
        return audit;
    }
}
