package br.com.softdesign.desafio.application.rest.v1;

import br.com.softdesign.desafio.application.mapper.PollMapper;
import br.com.softdesign.desafio.application.rest.v1.request.PollRequest;
import br.com.softdesign.desafio.builder.entity.PollBuilder;
import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.domain.service.PollService;
import br.com.softdesign.desafio.infrastructure.config.testcontainers.AbstractIntegrationTest;
import br.com.softdesign.desafio.infrastructure.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.Mockito.when;

@SpringBootTest
class PollRestApiTest extends AbstractIntegrationTest {

    private static final String URL = "/v1/polls";

    @Autowired
    private PollRestApi restApi;

    @Autowired
    private PollBuilder builder;

    @Mock
    private PollService service;


    @BeforeEach
    public void setup() {
        standaloneSetup(this.restApi);
    }

    @Test
    void when_get_all_returns_success() throws ParseException {
        Poll entity = builder.construirEntidade();

        when(service.findAll()).thenReturn(List.of(entity));

        given()
                .accept(MediaType.APPLICATION_JSON)
        .when()
                .get(URL)
        .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    void when_create_associate_return_sucess() throws IOException, ParseException {
        PollRequest request = PollRequest.builder()
                        .name("Nova Pauta")
                        .description("Descrição")
                        .build();
        Poll entity = builder.construirEntidade();

        when(service.save(PollMapper.toEntity(request))).thenReturn(entity);

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
