package br.com.softdesign.desafio.integration;

import br.com.softdesign.desafio.model.Poll;
import br.com.softdesign.desafio.model.Vote;
import br.com.softdesign.desafio.model.VoteAudit;
import br.com.softdesign.desafio.model.VoteValue;
import br.com.softdesign.desafio.repository.VoteAuditRepository;
import br.com.softdesign.desafio.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Teste de Integração para o fluxo completo de votação, incluindo a persistência
 * e validação do registro de auditoria (Audit Trail).
 *
 * Nota: Este teste assume que o ambiente de Testcontainers (Oracle XE) está configurado
 * e disponível para o contexto Spring Boot.
 */
@SpringBootTest
@ActiveProfiles("test") // Assumindo um perfil de teste que usa Testcontainers
@Transactional
public class VoteIntegrationTest {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private VoteAuditRepository voteAuditRepository;

    // IDs mockados para o teste
    private Long mockAssociateId = 1L;
    private Long mockPollId = 10L;

    /**
     * Configura dados iniciais no banco de dados antes de cada teste.
     * Simula a existência de um associado e uma pauta.
     */
    @BeforeEach
    void setUp() {
        // Limpa tabelas para garantir isolamento
        voteRepository.deleteAll();
        voteAuditRepository.deleteAll();

        // 1. Simula o cadastro de um associado (necessário para o foreign key)
        // Em um cenário real, usaríamos o AssociateRepository.
        // Aqui, apenas garantimos que o ID exista no contexto.
        // (Não persistimos a entidade Associate, apenas o ID para o teste de fluxo)

        // 2. Simula a criação de uma pauta
        Poll poll = new Poll();
        poll.setId(mockPollId);
        poll.setTitle("Teste de Votação Auditável");
        // Persiste a pauta para que o foreign key exista
        // (Assumindo que PollRepository existe e é injetável)
        // pollRepository.save(poll);
    }

    /**
     * Limpa dados após cada teste para garantir que o estado do banco seja limpo.
     */
    @AfterEach
    void tearDown() {
        voteRepository.deleteAll();
        voteAuditRepository.deleteAll();
    }

    @Test
    void registerVote_shouldPersistVoteAndAuditRecordSuccessfully() {
        // ARRANGE: Dados de entrada
        String ipAddress = "10.0.0.1";
        VoteValue voteValue = VoteValue.NO;

        // ACT: Executa o fluxo de votação (simulando a chamada do Controller/Service)
        // Nota: Aqui, estamos chamando o serviço diretamente para focar na lógica de persistência.
        // Em um teste de integração mais completo, chamaríamos o VoteController.
        Vote vote = voteService.registerVote(
                mockAssociateId,
                mockPollId,
                voteValue,
                ipAddress
        );

        // ASSERT 1: Validação do Voto Principal
        assertNotNull(vote.getId(), "O voto deve ter um ID após o registro.");
        assertEquals(mockAssociateId, vote.getAssociateId());
        assertEquals(mockPollId, vote.getPollId());
        assertEquals(voteValue, vote.getValue());

        // ASSERT 2: Validação do Registro de Auditoria (O ponto principal do teste)
        Optional<VoteAudit> auditOptional = voteAuditRepository.findByVoteId(vote.getId());
        assertTrue(auditOptional.isPresent(), "O registro de auditoria deve existir no banco de dados.");

        VoteAudit audit = auditOptional.get();
        assertEquals(vote.getId(), audit.getVoteId(), "O ID do voto deve corresponder ao ID do registro de auditoria.");
        assertEquals(mockAssociateId, audit.getAssociateId(), "O ID do associado deve ser auditado.");
        assertEquals(mockPollId, audit.getPollId(), "O ID da pauta deve ser auditado.");
        assertEquals(voteValue, audit.getVotedValue(), "O valor do voto deve ser auditado corretamente.");
        assertNotNull(audit.getTimestamp(), "O timestamp de auditoria deve ser preenchido.");
    }
}
