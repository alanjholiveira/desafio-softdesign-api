package br.com.softdesign.desafio.infrastructure.repository;

import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.infrastructure.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    Boolean existsByPoll(Poll poll);

    List<Session> findByStatus(SessionStatus closed);

}
