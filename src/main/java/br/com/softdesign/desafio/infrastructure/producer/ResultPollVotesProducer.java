package br.com.softdesign.desafio.infrastructure.producer;

import br.com.softdesign.desafio.domain.entity.Result;
import br.com.softdesign.desafio.domain.event.out.ResultPollVotesOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResultPollVotesProducer {

    @Value("${event.rabbitmq.desafio.exchange}")
    private String exchange;

    @Value("${event.rabbitmq.desafio.exchange}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public ResultPollVotesProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Result result) {
        log.info("Sending event containing poll results.");
        ResultPollVotesOutput eventOutput = ResultPollVotesOutput.create(result);
        rabbitTemplate.convertAndSend(exchange, routingKey, eventOutput);
    }

}
