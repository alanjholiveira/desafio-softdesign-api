package br.com.softdesign.desafio.repository;

import br.com.softdesign.desafio.model.VoteAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório de acesso a dados para a entidade VoteAudit.
 * Fornece métodos CRUD básicos e específicos para o histórico de votos.
 */
@Repository
public interface VoteAuditRepository extends JpaRepository<VoteAudit, Long> {
    // Métodos customizados podem ser adicionados aqui conforme a necessidade de consulta
}
