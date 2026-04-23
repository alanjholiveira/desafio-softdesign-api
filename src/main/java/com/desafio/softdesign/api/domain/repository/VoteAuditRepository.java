package com.desafio.softdesign.api.domain.repository;

import com.desafio.softdesign.api.domain.model.VoteAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteAuditRepository extends JpaRepository<VoteAudit, Long> {

    /**
     * Busca todos os registros de auditoria associados a um voto específico.
     * @param voteId O ID do voto.
     * @return Lista de registros VoteAudit.
     */
    List<VoteAudit> findByVote_Id(Long voteId);
}
