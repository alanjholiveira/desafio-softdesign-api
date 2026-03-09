package br.com.softdesign.desafio.infrastructure.producer;

import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.domain.entity.Result;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.domain.entity.Vote;
import br.com.softdesign.desafio.infrastructure.enums.VoteType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultPollVotesProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Spy
    @InjectMocks
    private ResultPollVotesProducer producerEvent;

    private Poll buildPoll() {
        return Poll.builder()
                .id(UUID.randomUUID())
                .name("Teste Pauta")
                .description("Descrição da Pauta")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Test
    void sendEvent() {
        Poll poll = buildPoll();
        Vote vote = Vote.builder()
                .id(UUID.randomUUID())
                .voteType(VoteType.YES)
                .createdAt(LocalDateTime.now())
                .build();

        List<Vote> votes = List.of(vote);
        Map<String, Integer> questions = Arrays.stream(VoteType.values())
                .collect(Collectors.toMap(VoteType::name,
                        v -> (int) votes.stream().filter(vt -> v.equals(vt.getVoteType())).count()));

        Result result = Result.builder()
                .countVotes(votes.size())
                .poll(poll)
                .questions(questions)
                .build();

        producerEvent.send(result);

        verify(producerEvent, times(1)).send(result);
    }

}
