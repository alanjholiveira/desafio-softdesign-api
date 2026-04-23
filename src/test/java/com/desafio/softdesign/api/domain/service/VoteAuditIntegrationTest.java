package com.desafio.softdesign.api.domain.service;

import com.desafio.softdesign.api.domain.model.AuditAction;
import com.desafio.softdesign.api.domain.model.Vote;
import com.desafio.softdesign.api.domain.model.VoteAudit;
import com.desafio.softdesign.api.domain.repository.VoteAuditRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class to verify that the vote history is correctly recorded in the database.
 * Uses @DataJpaTest to ensure database connectivity for testing.
 */
@DataJpaTest
@ActiveProfiles("test")
class VoteServiceIntegrationTest {

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void shouldRecordAuditTrailAfterSuccessfulVote() {
        // 1. Setup: Criar um voto inicial
        Vote initialVote = voteService.createVote(1L, 100L, "Voto Inicial");
        assertThat(initialVote).isNotNull();

        // 2. Action: Simular um novo voto
        Vote newVote = voteService.createVote(1L, 200L, "Voto Atualizado");

        // 3. Assertion: Verificar se o novo voto foi salvo corretamente
        assertThat(newVote).isNotNull();

        // 4. Assertion: Verificar se o histórico de auditoria foi atualizado
        Optional<Vote> updatedVote = voteRepository.findById(initialVote.getId());
        assertThat(updatedVote).isPresent();

        Vote savedVote = updatedVote.get();
        
        // Verifica se o histórico de votos foi atualizado
        assertThat(savedVote.getVoteHistory()).hasSize(2);
        
        // Verifica se o último voto registrado corresponde ao que foi passado
        assertThat(savedVote.getVoteHistory().get(1).getVoteId()).isEqualTo(200L);
        assertThat(savedVote.getVoteHistory().get(1).getVoteName()).isEqualTo("Voto Atualizado");
    }

    @Test
    void shouldNotUpdateHistoryIfVoteFails() {
        // 1. Setup: Criar um voto inicial
        Vote initialVote = voteService.createVote(1L, 100L, "Voto Inicial");
        assertThat(initialVote).isNotNull();

        // 2. Action: Tentar criar um voto com dados inválidos (simulando falha)
        Vote failedVote = voteService.createVote(1L, 999L, "Voto Falho");

        // 3. Assertion: Verificar se o voto falhou
        assertThat(failedVote).isNull();

        // 4. Assertion: Verificar se o histórico de auditoria permaneceu inalterado
        Optional<Vote> retrievedVote = voteRepository.findById(initialVote.getId());
        assertThat(retrievedVote).isPresent();

        Vote savedVote = retrievedVote.get();
        
        // O histórico deve ter apenas o voto inicial
        assertThat(savedVote.getVoteHistory()).hasSize(1);
        assertThat(savedVote.getVoteHistory().get(0).getVoteId()).isEqualTo(100L);
    }
}
