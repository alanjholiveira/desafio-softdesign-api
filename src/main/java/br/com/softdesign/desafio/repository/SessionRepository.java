package br.com.softdesign.desafio.repository;

import br.com.softdesign.desafio.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório de acesso a dados para a entidade Session.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    /**
     * Busca uma sessão ativa para uma pauta específica.
     * @param pollId O ID da pauta.
     * @return Optional da sessão.
     */
    Optional<Session> findByPollIdAndStatus(Long pollId, br.com.softdesign.desafio.model.SessionStatus status);
}
