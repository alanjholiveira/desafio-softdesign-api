package br.com.softdesign.desafio.infrastructure.config.testcontainers;

import br.com.softdesign.desafio.DesafioApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;

@SpringBootTest(classes = DesafioApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    static final RabbitMQContainer rabbitMQContainer;
    static final OracleContainer oracleContainer;

    static {
        oracleContainer =  new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withUsername("test")
            .withPassword("password")
            .withReuse(true);
        rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.9.10-management-alpine")
                .withReuse(true);
        rabbitMQContainer.start();
        oracleContainer.start();
    }
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("cloud.local.messaging.host", rabbitMQContainer::getContainerIpAddress);
        registry.add("event.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("event.rabbitmq.port", rabbitMQContainer::getFirstMappedPort);
        registry.add("event.rabbitmq.password", rabbitMQContainer::getAdminPassword);
        registry.add("event.rabbitmq.username", rabbitMQContainer::getAdminUsername);

        registry.add("spring.datasource.url", oracleContainer::getJdbcUrl);
        registry.add("spring.datasource.username", oracleContainer::getUsername);
        registry.add("spring.datasource.password", oracleContainer::getPassword);
    }

}
