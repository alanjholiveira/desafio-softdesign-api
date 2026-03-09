package br.com.softdesign.desafio.infrastructure.producer;

import br.com.softdesign.desafio.builder.entity.SessionBuilder;
import br.com.softdesign.desafio.builder.entity.VoteBuilder;
import br.com.softdesign.desafio.domain.entity.Result;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.domain.entity.Vote;
import br.com.softdesign.desafio.infrastructure.config.testcontainers.AbstractIntegrationTest;
import br.com.softdesign.desafio.infrastructure.enums.VoteType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ResultPollVotesProducerTest extends AbstractIntegrationTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Spy
    @InjectMocks
    private ResultPollVotesProducer producerEvent;

    @Autowired
    private SessionBuilder sessionBuilder;

    @Autowired
    private VoteBuilder voteBuilder;

    @Test
    void sendEvent() throws ParseException {
        Session session = sessionBuilder.construirEntidade();
        Vote vote = voteBuilder.construirEntidade();
        session.setVotes(List.of(vote));

        Result result = Result.builder()
                .countVotes(session.getVotes().size())
                .poll(session.getPoll())
                .questions(questions(session))
                .build();


        producerEvent.send(result);

        verify(producerEvent, times(1)).send(result);
    }

    private Map<String, Integer> questions(Session session) {
        return Arrays.stream(VoteType.values())
                .collect(Collectors.toMap(VoteType::name, v -> getVotesByType(session, v).size()));
    }

    private List<Vote> getVotesByType(Session session, VoteType voteType) {
        return session.getVotes().stream().filter(v -> voteType.equals(v.getVoteType())).collect(Collectors.toList());
    }

}
