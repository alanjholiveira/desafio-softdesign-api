package br.com.softdesign.desafio.infrastructure.repository;

import br.com.softdesign.desafio.domain.entity.Associate;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.domain.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<Vote, UUID> {

    Boolean existsVoteByAssociateAndSession(Associate associate, Session session);

}
