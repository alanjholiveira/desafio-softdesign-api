package br.com.softdesign.desafio.domain.schedule;

import br.com.softdesign.desafio.domain.service.VoteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ResultSchedule {

    private final VoteService service;

    @Scheduled(cron = "${event.cron}")
    @SchedulerLock(name = "TaskScheduler_ResultPollVotes")
    public void getResultPoll() {
        log.info("Generating poll result accounting event");
        service.countingVotesEvent();
    }

}
