package br.com.softdesign.desafio.service;

import br.com.softdesign.desafio.model.Poll;
import br.com.softdesign.desafio.model.Vote;
import br.com.softdesign.desafio.model.VoteValue;
import br.com.softdesign.desafio.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private VoteService voteService;

    private Long testAssociateId = 10L;
    private Long testPollId = 20L;
    private VoteValue testVoteValue = VoteValue.YES;
    private String testIpAddress = "192.168.1.1";
    private Long savedVoteId = 50L;

    @BeforeEach
    void setUp() {
        // Configuração padrão para o mock do VoteRepository.save()
        // Simula que o save retorna a entidade com o ID gerado.
        Vote mockVote = new Vote();
        mockVote.setId(savedVoteId);
        mockVote.setAssociateId(testAssociateId);
        mockVote.setPollId(testPollId);
        mockVote.setValue(testVoteValue);
        
        when(voteRepository.save(any(Vote.class))).thenReturn(mockVote);
    }

    @Test
    void registerVote_shouldSuccessfullyRegisterVoteAndCallAuditService() {
        // 1. Ação
        Vote result = voteService.registerVote(
                testAssociateId,
                testPollId,
                testVoteValue,
                testIpAddress
        );

        // 2. Verificação
        assertNotNull(result);
        assertEquals(savedVoteId, result.getId());

        // Verifica se o voto foi salvo no repositório
        verify(voteRepository, times(1)).save(any(Vote.class));

        // Verifica se o serviço de auditoria foi chamado exatamente uma vez com os dados corretos
        verify(auditService, times(1)).recordVoteAudit(
                eq(savedVoteId), // ID retornado pelo save
                eq(testAssociateId),
                eq(testPollId),
                eq(testVoteValue),
                eq(testIpAddress)
        );
    }

    @Test
    void registerVote_shouldNotCallAuditServiceIfVoteRepositoryFails() {
        // Cenário: O salvamento do voto falha (ex: constraint violation, falha de conexão)
        // O serviço deve lançar a exceção e NÃO chamar o auditService.
        doThrow(new RuntimeException("Database connection failed")).when(voteRepository).save(any(Vote.class));

        // 1. Ação e Verificação
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
                voteService.registerVote(
                        testAssociateId,
                        testPollId,
                        testVoteValue,
                        testIpAddress
                );
        });

        assertEquals("Database connection failed", thrown.getMessage());

        // Verifica se o auditService NÃO foi chamado
        verify(auditService, never()).recordVoteAudit(any(), any(), any(), any(), any());
    }

    @Test
    void findVotesByPollId_shouldReturnAllVotesForPoll() {
        // 1. Setup
        Vote vote1 = new Vote();
        vote1.setId(1L);
        vote1.setPollId(testPollId);

        Vote vote2 = new Vote();
        vote2.setId(2L);
        vote2.setPollId(testPollId);

        List<Vote> expectedVotes = Arrays.asList(vote1, vote2);
        when(voteRepository.findByPollId(testPollId)).thenReturn(expectedVotes);

        // 2. Ação
        List<Vote> result = voteService.findVotesByPollId(testPollId);

        // 3. Verificação
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(voteRepository, times(1)).findByPollId(testPollId);
    }
}
