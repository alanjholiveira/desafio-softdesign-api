package br.com.softdesign.desafio.application.rest.v1;

import br.com.softdesign.desafio.application.rest.v1.request.AssociateRequest;
import br.com.softdesign.desafio.domain.entity.Associate;
import br.com.softdesign.desafio.domain.service.AssociateService;
import br.com.softdesign.desafio.infrastructure.config.testcontainers.AbstractIntegrationTest;
import br.com.softdesign.desafio.infrastructure.enums.AssociateStatus;
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

class AssociateRestApiTest extends AbstractIntegrationTest {

    private static final String URL = "/v1/associates";

    @Autowired
    private AssociateRestApi associateRestApi;

    @MockitoBean
    private AssociateService service;

    @BeforeEach
    public void setup() {
        standaloneSetup(this.associateRestApi);
    }

    private Associate buildAssociate() {
        return Associate.builder()
                .id(UUID.randomUUID())
                .name("Nome Associado")
                .taxId("58382140076")
                .status(AssociateStatus.ABLE_TO_VOTE)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Test
    void when_getAll_returns_success() {
        Associate associate = buildAssociate();
        when(service.findAll()).thenReturn(List.of(associate));

        given()
                .accept(MediaType.APPLICATION_JSON)
        .when()
                .get(URL)
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void when_create_associate_return_sucess() throws IOException {
        AssociateRequest associateRequest = AssociateRequest.builder()
                        .name("Novo Associado")
                        .taxId("96979542087")
                        .build();
        Associate associate = buildAssociate();

        when(service.save(any(Associate.class))).thenReturn(associate);

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType("application/json")
                .body(TestUtil.convertObjectToJsonBytes(associateRequest))
        .when()
                .post(URL)
        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

}
