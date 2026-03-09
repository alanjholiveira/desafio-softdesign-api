package br.com.softdesign.desafio.infrastructure.config.rabbitmq;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitCreateResultConfig {

    @Value("${event.rabbitmq.desafio.exchange}")
    private String exchangeValue;

    @Value("${event.rabbitmq.desafio.queue}")
    private String queueValue;

    @Value("${event.rabbitmq.desafio.routing-key}")
    private String routingKeyValue;

    private final RabbitAdmin rabbitAdmin;

    public RabbitCreateResultConfig(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @PostConstruct
    public void init() {
        rabbitAdmin.declareExchange(exchange());
        rabbitAdmin.declareQueue(queue());
        rabbitAdmin.declareBinding(binding());
    }

    private Exchange exchange() {
        TopicExchange exchange = ExchangeBuilder.topicExchange(exchangeValue)
                .durable(true)
                .build();
        exchange.setAdminsThatShouldDeclare(rabbitAdmin);
        return exchange;
    }

    private Queue queue() {
        Queue queue = QueueBuilder.durable(queueValue).build();
        queue.setAdminsThatShouldDeclare(rabbitAdmin);
        return queue;
    }

    private Binding binding() {
        Binding binding = BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKeyValue)
                .noargs();
        binding.setAdminsThatShouldDeclare(rabbitAdmin);
        return binding;
    }

}
