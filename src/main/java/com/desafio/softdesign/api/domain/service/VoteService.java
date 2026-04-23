package com.desafio.softdesign.api.domain.service;

import com.desafio.softdesign.api.domain.exception.VotingException;
import com.desafio.softdesign.api.domain.model.AuditAction;
import com.desafio.softdesign.api.domain.model.Vote;
import com.desafio.softdesign.api.domain.model.VoteAudit;
import com.desafio.softdesign.api.domain.repository.VoteRepository;
import com.desafio.softdesign.api.domain.service.SessionService; // Assumindo a existência
import com.desafio.softdesign.api.domain.service.AssociateService; // Assumindo a existência
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * Serviço principal de negócio para gerenciamento de votos.
 * Responsável por orquestrar a lógica de voto e garantir a auditoria.
 */
@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final VoteAuditService voteAuditService;
    private final SessionService sessionService; // Injeção de dependência para validação de sessão
    private final AssociateService associateService; // Injeção de dependência para validação de associado

    public VoteService(VoteRepository voteRepository, VoteAuditService voteAuditService,
                        SessionService sessionService, AssociateService associateService) {
        this.voteRepository = voteRepository;
        this.voteAuditService = voteAuditService;
        this.sessionService = sessionService;
        this.associateService = associateService;
    }

    /**
     * Cria um novo voto, aplicando validações de negócio antes de persistir e auditar.
     * @param vote O objeto Vote a ser salvo.
     * @param associateId O ID do associado que votou.
     * @return O voto persistido.
     * @throws VotingException Se qualquer regra de negócio falhar.
     */
    @Transactional
    public Vote createVote(Vote vote, Long associateId) {
        // 1. Validações de Negócio
        validateVoteCreation(vote, associateId);

        // 2. Persistência do Voto
        Vote savedVote = voteRepository.save(vote);

        // 3. Auditoria (Só é chamada se as validações acima passarem)
        Map<String, Object> details = Map.of(
                "voto_id", savedVote.getId(),
                "pauta_id", savedVote.getPollId(),
                "status", "VOTO_CAST"
        );
        voteAuditService.recordVoteAudit(savedVote.getId(), associateId, AuditAction.VOTE_CAST, details);

        return savedVote;
    }

    /**
     * Atualiza um voto existente, aplicando validações de negócio antes de persistir e auditar.
     * @param voteId O ID do voto a ser atualizado.
     * @param associateId O ID do associado que está fazendo a alteração.
     * @param newVoteData Os dados atualizados do voto.
     * @return O voto atualizado.
     * @throws VotingException Se qualquer regra de negócio falhar.
     */
    @Transactional
    public Vote updateVote(Long voteId, Long associateId, Vote newVoteData) {
        // 1. Buscar o voto existente
        Optional<Vote> optionalVote = voteRepository.findById(voteId);
        if (optionalVote.isEmpty()) {
            throw new VotingException("Voto não encontrado para atualização.", "VOTE_NOT_FOUND");
        }

        Vote existingVote = optionalVote.get();

        // 2. Validações de Negócio
        validateVoteUpdate(existingVote, newVoteData, associateId);

        // 3. Aplicar atualizações (lógica de merge)
        existingVote.setPollId(newVoteData.getPollId());
        // ... outros campos ...

        // 4. Persistência do Voto
        Vote updatedVote = voteRepository.save(existingVote);

        // 5. Auditoria (Só é chamada se as validações acima passarem)
        Map<String, Object> details = Map.of(
                "voto_id", updatedVote.getId(),
                "novo_status", newVoteData.getStatus(),
                "motivo", "Atualização de dados pelo associado."
        );
        voteAuditService.recordVoteAudit(updatedVote.getId(), associateId, AuditAction.VOTE_UPDATED, details);

        return updatedVote;
    }

    /**
     * Valida se a sessão está ativa e se o voto é possível.
     */
    private void validateVoteCreation(Vote vote, Long associateId) {
        // 1. Verificar se a sessão está ativa
        if (!sessionService.isSessionActive(vote.getPollId())) {
            throw new VotingException("Não é possível votar. A sessão para esta pauta está inativa.", "SESSION_INACTIVE");
        }

        // 2. Verificar se o associado está ativo
        if (!associateService.isAssociateActive(associateId)) {
            throw new VotingException("O associado não está ativo no sistema.", "ASSOCIATE_INACTIVE");
        }

        // 3. Verificar se o voto é possível (Ex: prazo)
        if (LocalDateTime.now().isAfter(vote.getDeadline())) {
            throw new VotingException("O prazo para votação expirou.", "VOTE_DEADLINE_EXPIRED");
        }
    }

    /**
     * Valida se a atualização do voto é permitida.
     */
    private void validateVoteUpdate(Vote existingVote, Vote newVoteData, Long associateId) {
        // Exemplo: Apenas o administrador pode alterar o voto após o fechamento da sessão.
        // Se o voto já foi finalizado, impede a alteração.
        if (existingVote.getStatus().equals("FINALIZED")) {
            throw new VotingException("Não é possível alterar o voto após o fechamento da sessão.", "VOTE_FINALIZED");
        }
    }

    /**
     * Recupera o histórico de auditoria de um voto.
     * @param voteId O ID do voto.
     * @return Lista de registros VoteAudit.
     */
    public java.util.List<VoteAudit> getVoteAuditHistory(Long voteId) {
        return voteAuditService.getAuditHistory(voteId);
    }
}
