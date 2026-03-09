package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.infrastructure.repository.PollRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class PollService {

    private PollRepository repository;

    @Transactional(readOnly = true)
    public List<Poll> findAll() {
        log.info("Searching every poll registered in the base.");
        return repository.findAll();
    }

    public Poll save(Poll poll) {
        log.info("Saving poll at base");
        return repository.save(poll);
    }

}
