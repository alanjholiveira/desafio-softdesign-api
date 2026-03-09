package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.infrastructure.enums.SessionStatus;
import br.com.softdesign.desafio.infrastructure.exception.PollNotFoundException;
import br.com.softdesign.desafio.infrastructure.exception.SessionOpenException;
import br.com.softdesign.desafio.infrastructure.repository.PollRepository;
import br.com.softdesign.desafio.infrastructure.repository.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class SessionService {

    private final SessionRepository repository;
    private final PollRepository pollRepository;

    public List<Session> findAll() {
        log.info("Searching all registered sessions");
        return repository.findAll();
    }

    @Transactional
    public Session openSession(Session entity) {
        log.info("Opening session.");
        entity.setExpiration(getExpiration(entity.getExpiration()));
        entity.setStatus(SessionStatus.OPEN);

        checkPollOpen(entity.getPoll());

        Poll poll = getPoll(entity);

        entity.setPoll(poll);

        log.info("Saving poll session to database");
        return repository.save(entity);
    }

    private void checkPollOpen(Poll poll) {
        log.info("Checking if the poll has a registered section.");
        Boolean check = repository.existsByPoll(poll);
        if (check.equals(Boolean.TRUE)) {
            log.error("Poll already has a registered section.");
            throw new SessionOpenException();
        }
    }

    private Poll getPoll(Session entity) {
        log.info("Searching for poll at the base");
        return pollRepository.findById(entity.getPoll().getId())
                .orElseThrow(PollNotFoundException::new);
    }

    private LocalDateTime getExpiration(LocalDateTime expiration) {
        log.info("Checking session expiration.");
        return ObjectUtils.isEmpty(expiration) ? LocalDateTime.now().plusMinutes(1) : expiration;
    }

}
