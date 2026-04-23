package com.desafio.softdesign.api.domain.service;

import com.desafio.softdesign.api.domain.exception.VotingException;
import com.desafio.softdesign.api.domain.model.AuditAction;
import com.desafio.softdesign.api.domain.model.Vote;
import com.desafio.softdesign.api.domain.model.VoteAudit;
import com.desafio.softdesign.api.domain.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VoteAuditService voteAuditService;

    // Mock de serviços de dependência para validações
    @Mock
    private SessionService sessionService;
    @Mock
    private AssociateService associateService;

    @InjectMocks
    private VoteService voteService;

    private final Long TEST_ASSOCIATE_ID = 1L;
    private final Long TEST_POLL_ID = 10L;
    private Vote mockVote;

    @BeforeEach
    void setUp() {
        // Configuração de um voto base para os testes
        mockVote = new Vote();
        mockVote.setId(1L);
        mockVote.setPollId(TEST_POLL_ID);
        mockVote.setStatus("ACTIVE");
        mockVote.setDeadline(LocalDateTime.now().plusDays(7));
    }

    // =================================================================================
    // TESTES DE CRIAÇÃO DE VOTO (createVote)
    // =================================================================================

    @Test
    void createVote_shouldSaveVoteAndRecordAudit_whenValid() {
        // Arrange
        when(voteRepository.save(any(Vote.class))).thenReturn(mockVote);
        // Mock o retorno do registro de auditoria
        when(voteAuditService.recordVoteAudit(any(Long.class), any(Long.class), any(AuditAction.class), any(Map.class)))
                .thenReturn(mock(VoteAudit.class));

        // Mock validações de sucesso
        when(sessionService.isSessionActive(TEST_POLL_ID)).thenReturn(true);
        when(associateService.isAssociateActive(TEST_ASSOCIATE_ID)).thenReturn(true);

        // Act
        Vote result = voteService.createVote(mockVote, TEST_ASSOCIATE_ID);

        // Assert
        assertNotNull(result);
        // 1. Verifica se o voto foi salvo
        verify(voteRepository, times(1)).save(mockVote);
        // 2. Verifica se a auditoria foi chamada APÓS o save
        verify(voteAuditService, times(1)).recordVoteAudit(
                eq(1L), eq(TEST_ASSOCIATE_ID), eq(AuditAction.VOTE_CAST), any(Map.class)
        );
    }

    @Test
    void createVote_shouldThrowVotingException_whenSessionInactive() {
        // Arrange
        when(sessionService.isSessionActive(TEST_POLL_ID)).thenReturn(false);

        // Act & Assert
        VotingException thrown = assertThrows(VotingException.class, () -> {
            voteService.createVote(mockVote, TEST_ASSOCIATE_ID);
        });
        assertEquals("SESSION_INACTIVE", thrown.getErrorCode());
        verify(voteRepository, never()).save(any(Vote.class));
        verify(voteAuditService, never()).recordVoteAudit(any(), any(), any(), any());
    }

    @Test
    void createVote_shouldThrowVotingException_whenDeadlineExpired() {
        // Arrange
        mockVote.setDeadline(LocalDateTime.now().minusHours(1)); // Data passada
        when(sessionService.isSessionActive(TEST_POLL_ID)).thenReturn(true);
        when(associateService.isAssociateActive(TEST_ASSOCIATE_ID)).thenReturn(true);

        // Act & Assert
        VotingException thrown = assertThrows(VotingException.class, () -> {
            voteService.createVote(mockVote, TEST_ASSOCIATE_ID);
        });
        assertEquals("VOTE_DEADLINE_EXPIRED", thrown.getErrorCode());
        verify(voteRepository, never()).save(any(Vote.class));
        verify(voteAuditService, never()).recordVoteAudit(any(), any(), any(), any());
    }

    // =================================================================================
    // TESTES DE ATUALIZAÇÃO DE VOTO (updateVote)
    // =================================================================================

    @Test
    void updateVote_shouldUpdateVoteAndRecordAudit_whenValid() {
        // Arrange
        Long voteId = 1L;
        Vote updatedData = new Vote();
        updatedData.setPollId(20L);
        updatedData.setStatus("UPDATED");

        // Mock o voto existente
        Vote existingVote = new Vote();
        existingVote.setId(voteId);
        when(voteRepository.findById(voteId)).thenReturn(Optional.of(existingVote));
        when(voteRepository.save(any(Vote.class))).thenReturn(existingVote);
        when(sessionService.isSessionActive(any())).thenReturn(true); // Mock de validação

        // Mock o retorno do registro de auditoria
        when(voteAuditService.recordVoteAudit(any(Long.class), any(Long.class), any(AuditAction.class), any(Map.class)))
                .thenReturn(mock(VoteAudit.class));

        // Act
        Vote result = voteService.updateVote(voteId, TEST_ASSOCIATE_ID, updatedData);

        // Assert
        assertNotNull(result);
        // 1. Verifica se o voto foi atualizado e salvo
        verify(voteRepository, times(1)).findById(voteId);
        verify(voteRepository, times(1)).save(existingVote);
        // 2. Verifica se a auditoria foi chamada
        verify(voteAuditService, times(1)).recordVoteAudit(
                eq(voteId), eq(TEST_ASSOCIATE_ID), eq(AuditAction.VOTE_UPDATED), any(Map.class)
        );
    }

    @Test
    void updateVote_shouldThrowVotingException_whenVoteNotFound() {
        // Arrange
        Long nonExistentId = 999L;
        Vote updatedData = new Vote();

        when(voteRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        VotingException thrown = assertThrows(VotingException.class, () -> {
            voteService.updateVote(nonExistentId, TEST_ASSOCIATE_ID, updatedData);
        });
        assertEquals("VOTE_NOT_FOUND", thrown.getErrorCode());
        verify(voteRepository, times(1)).findById(nonExistentId);
        verify(voteRepository, never()).save(any(Vote.class));
        verify(voteAuditService, never()).recordVoteAudit(any(), any(), any(), any());
    }

    @Test
    void updateVote_shouldThrowVotingException_whenVoteFinalized() {
        // Arrange
        Long voteId = 1L;
        Vote updatedData = new Vote();

        // Simula um voto que não pode ser alterado
        Vote existingVote = new Vote();
        existingVote.setId(voteId);
        existingVote.setStatus("FINALIZED");
        when(voteRepository.findById(voteId)).thenReturn(Optional.of(existingVote));

        // Act & Assert
        VotingException thrown = assertThrows(VotingException.class, () -> {
            voteService.updateVote(voteId, TEST_ASSOCIATE_ID, updatedData);
        });
        assertEquals("VOTE_FINALIZED", thrown.getErrorCode());
        verify(voteRepository, times(1)).findById(voteId);
        verify(voteRepository, never()).save(any(Vote.class));
        verify(voteAuditService, never()).recordVoteAudit(any(), any(), any(), any());
    }
}
