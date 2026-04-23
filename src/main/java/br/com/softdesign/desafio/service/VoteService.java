package br.com.softdesign.desafio.service;

import br.com.softdesign.desafio.model.Poll;
import br.com.softdesign.desafio.model.Vote;
import br.com.softdesign.desafio.model.VoteValue;
import br.com.softdesign.desafio.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de negócio responsável por gerenciar o ciclo de vida dos votos.
 * Inclui a lógica de registro e auditoria de votos.
 */
@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final AuditService auditService;
    // Adicionar outros repositórios necessários (e.g., PollRepository, AssociateRepository)

    public VoteService(VoteRepository voteRepository, AuditService auditService) {
        this.voteRepository = voteRepository;
        this.auditService = auditService;
    }

    /**
     * Registra um voto para um associado em uma sessão de votação.
     * Este método é transacional e garante que o voto e o registro de auditoria sejam persistidos juntos.
     *
     * @param associateId O ID do associado votante.
     * @param pollId O ID da pauta.
     * @param voteValue O valor do voto (YES ou NO).
     * @param ipAddress O endereço IP de origem.
     * @return O objeto Vote persistido.
     * @throws IllegalArgumentException Se o voto for inválido ou o associado não for elegível.
     */
    @Transactional
    public Vote registerVote(Long associateId, Long pollId, VoteValue voteValue, String ipAddress) {
        // 1. Validações de Negócio (Simulação)
        // Aqui deveria haver a checagem de elegibilidade do associado e se a sessão está aberta.
        // Exemplo: if (!associateService.isEligible(associateId)) throw new Exception("Ineligible");

        // 2. Criação da Entidade de Voto
        Vote vote = new Vote();
        vote.setAssociateId(associateId);
        vote.setPollId(pollId);
        vote.setValue(voteValue);
        // Adicionar outros campos de sessão, etc.

        // 3. Persistência do Voto
        Vote savedVote = voteRepository.save(vote);

        // 4. Lógica de Auditoria (NOVA IMPLEMENTAÇÃO)
        // Registra o evento de voto na tabela de auditoria.
        // Isso deve ocorrer dentro da mesma transação para garantir atomicidade.
        auditService.recordVoteAudit(
                savedVote.getId(),
                associateId,
                pollId,
                voteValue,
                ipAddress
        );

        return savedVote;
    }

    /**
     * Busca todos os votos registrados para uma determinada pauta.
     * @param pollId O ID da pauta.
     * @return Lista de votos.
     */
    public List<Vote> findVotesByPollId(Long pollId) {
        return voteRepository.findByPollId(pollId);
    }
}
