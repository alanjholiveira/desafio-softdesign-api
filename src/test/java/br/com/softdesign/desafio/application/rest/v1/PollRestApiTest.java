package br.com.softdesign.desafio.application.rest.v1;

import br.com.softdesign.desafio.application.rest.v1.request.PollRequest;
import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.domain.service.PollService;
import br.com.softdesign.desafio.infrastructure.config.testcontainers.AbstractIntegrationTest;
import br.com.softdesign.desafio.infrastructure.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PollRestApiTest extends AbstractIntegrationTest {

    private static final String URL = "/v1/polls";

    @Autowired
    private PollRestApi restApi;

    @MockitoBean
    private PollService service;

    @BeforeEach
    public void setup() {
        standaloneSetup(this.restApi);
    }

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
    void when_get_all_returns_success() {
        Poll entity = buildPoll();
        when(service.findAll()).thenReturn(List.of(entity));

        given()
                .accept(MediaType.APPLICATION_JSON)
        .when()
                .get(URL)
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void when_create_poll_return_sucess() throws IOException {
        PollRequest request = PollRequest.builder()
                        .name("Nova Pauta")
                        .description("Descrição")
                        .build();
        Poll entity = buildPoll();

        when(service.save(any(Poll.class))).thenReturn(entity);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType("application/json")
                .body(TestUtil.convertObjectToJsonBytes(request))
        .when()
                .post(URL)
        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

}
