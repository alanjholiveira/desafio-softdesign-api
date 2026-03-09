package br.com.softdesign.desafio.infrastructure.config.testcontainers;

import br.com.softdesign.desafio.DesafioApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;

@SpringBootTest(classes = DesafioApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    static final RabbitMQContainer rabbitMQContainer;
    static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer =  new PostgreSQLContainer<>("postgres:14-alpine")
            .withUsername("test")
            .withPassword("password")
            .withDatabaseName("test")
            .withReuse(true);
        rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.9.10-management-alpine")
                .withReuse(true);
        rabbitMQContainer.start();
        postgreSQLContainer.start();
    }
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("cloud.local.messaging.host", rabbitMQContainer::getContainerIpAddress);
        registry.add("event.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("event.rabbitmq.port", rabbitMQContainer::getFirstMappedPort);
        registry.add("event.rabbitmq.password", rabbitMQContainer::getAdminPassword);
        registry.add("event.rabbitmq.username", rabbitMQContainer::getAdminUsername);

        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

}
