package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.domain.entity.Associate;
import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.domain.entity.Result;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.domain.entity.Vote;
import br.com.softdesign.desafio.infrastructure.enums.AssociateStatus;
import br.com.softdesign.desafio.infrastructure.enums.SessionStatus;
import br.com.softdesign.desafio.infrastructure.enums.VoteType;
import br.com.softdesign.desafio.infrastructure.exception.AssociateUnableToVoteException;
import br.com.softdesign.desafio.infrastructure.exception.AssociateVoteUniqueException;
import br.com.softdesign.desafio.infrastructure.exception.SessionNotCountVoteException;
import br.com.softdesign.desafio.infrastructure.exception.SessionNotFoundException;
import br.com.softdesign.desafio.infrastructure.exception.VotingClosedException;
import br.com.softdesign.desafio.infrastructure.producer.ResultPollVotesProducer;
import br.com.softdesign.desafio.infrastructure.repository.AssociateRepository;
import br.com.softdesign.desafio.infrastructure.repository.SessionRepository;
import br.com.softdesign.desafio.infrastructure.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @InjectMocks
    private VoteService service;

    @Mock
    private VoteRepository repository;

    @Mock
    private AssociateRepository associateRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private ResultPollVotesProducer producerEvent;

    // ------------------------------------------------------------------ helpers

    private Poll buildPoll() {
        return Poll.builder()
                .id(UUID.randomUUID())
                .name("Teste Pauta")
                .description("Descrição da Pauta")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    private Session buildOpenSession() {
        return Session.builder()
                .id(UUID.randomUUID())
                .poll(buildPoll())
                .status(SessionStatus.OPEN)
                .expiration(LocalDateTime.now().plusMinutes(30))
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .votes(List.of())
                .build();
    }

    private Session buildExpiredSession() {
        Session s = buildOpenSession();
        s.setExpiration(LocalDateTime.now().minusMinutes(1));
        return s;
    }

    private Associate buildAssociate() {
        return Associate.builder()
                .id(UUID.randomUUID())
                .name("Nome Associado")
                .taxId("58382140076")
                .status(AssociateStatus.ABLE_TO_VOTE)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    private Vote buildVote(Associate associate, Session session) {
        return Vote.builder()
                .id(UUID.randomUUID())
                .voteType(VoteType.YES)
                .associate(associate)
                .session(session)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // ------------------------------------------------------------------ vote()

    @Test
    void when_vote_returns_success() {
        Session session = buildOpenSession();
        Associate associate = buildAssociate();
        Vote vote = buildVote(associate, session);

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(associateRepository.findById(associate.getId())).thenReturn(Optional.of(associate));
        when(repository.existsVoteByAssociateAndSession(associate, session)).thenReturn(Boolean.FALSE);
        when(repository.save(vote)).thenReturn(vote);

        String response = service.vote(vote);

        assertNotNull(response);
        assertEquals("Vote registered successfully.", response);
    }

    @Test
    void when_vote_returns_voting_closed_exception() {
        Session session = buildExpiredSession();
        Associate associate = buildAssociate();
        Vote vote = buildVote(associate, session);

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(repository.existsVoteByAssociateAndSession(associate, session)).thenReturn(Boolean.FALSE);
        when(associateRepository.findById(associate.getId())).thenReturn(Optional.of(associate));

        assertThrows(VotingClosedException.class, () -> service.vote(vote));
    }

    @Test
    void when_vote_returns_session_not_found_exception() {
        Session session = buildOpenSession();
        Associate associate = buildAssociate();
        Vote vote = buildVote(associate, session);

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class, () -> service.vote(vote));
    }

    @Test
    void when_vote_returns_associate_unable_to_vote_exception() {
        Session session = buildOpenSession();
        Associate associate = buildAssociate();
        associate.setStatus(AssociateStatus.UNABLE_TO_VOTE);
        Vote vote = buildVote(associate, session);

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(associateRepository.findById(associate.getId())).thenReturn(Optional.of(associate));
        when(repository.existsVoteByAssociateAndSession(associate, session)).thenReturn(Boolean.FALSE);

        assertThrows(AssociateUnableToVoteException.class, () -> service.vote(vote));
    }

    @Test
    void when_vote_returns_associate_vote_unique_exception() {
        Session session = buildOpenSession();
        Associate associate = buildAssociate();
        Vote vote = buildVote(associate, session);

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(repository.existsVoteByAssociateAndSession(associate, session)).thenReturn(Boolean.TRUE);
        when(associateRepository.findById(associate.getId())).thenReturn(Optional.of(associate));

        assertThrows(AssociateVoteUniqueException.class, () -> service.vote(vote));
    }

    // ------------------------------------------------------------------ countVotes()

    @Test
    void when_count_votes_session_return_success() {
        Session session = buildExpiredSession();
        Vote vote = buildVote(buildAssociate(), session);
        session.setVotes(List.of(vote));

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        Result response = service.countVotes(session.getId().toString());
        assertNotNull(response);
    }

    @Test
    void when_count_votes_session_return_session_not_count_vote_exception() {
        Session session = buildOpenSession();
        Vote vote = buildVote(buildAssociate(), session);
        session.setVotes(List.of(vote));

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        assertThrows(SessionNotCountVoteException.class,
                () -> service.countVotes(session.getId().toString()));
    }

    // ------------------------------------------------------------------ countingVotesEvent()

    @Test
    void when_counting_votes_session_return_success() {
        Session session = buildExpiredSession();
        Vote vote = buildVote(buildAssociate(), session);
        session.setVotes(List.of(vote));

        when(sessionRepository.findByStatus(SessionStatus.OPEN)).thenReturn(List.of(session));
        when(sessionRepository.saveAll(anyList())).thenReturn(List.of(session));

        service.countingVotesEvent();

        verify(producerEvent).send(isA(Result.class));
    }

}
