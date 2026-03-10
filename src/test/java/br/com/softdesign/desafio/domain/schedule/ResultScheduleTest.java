package br.com.softdesign.desafio.domain.schedule;

import br.com.softdesign.desafio.domain.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ResultScheduleTest {

    @Mock
    private VoteService voteService;

    @InjectMocks
    private ResultSchedule resultSchedule;

    @Test
    @DisplayName("Should trigger countingVotesEvent when getResultPoll is called")
    void shouldTriggerVoteCountingOnSchedule() {
        resultSchedule.getResultPoll();

        verify(voteService, times(1)).countingVotesEvent();
    }
}
