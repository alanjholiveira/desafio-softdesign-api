package br.com.softdesign.desafio.application.rest.v1;

import br.com.softdesign.desafio.application.rest.v1.request.VoteRequest;
import br.com.softdesign.desafio.application.rest.v1.response.VoteResponse;
import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.domain.entity.Result;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.domain.entity.Vote;
import br.com.softdesign.desafio.domain.service.VoteService;
import br.com.softdesign.desafio.infrastructure.config.testcontainers.AbstractIntegrationTest;
import br.com.softdesign.desafio.infrastructure.enums.SessionStatus;
import br.com.softdesign.desafio.infrastructure.enums.VoteType;
import br.com.softdesign.desafio.infrastructure.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

class VoteRestApiTest extends AbstractIntegrationTest {

    private static final String URL = "/v1/votes";

    @Autowired
    private VoteRestApi restApi;

    @MockitoBean
    private VoteService service;

    @BeforeEach
    public void setup() {
        standaloneSetup(this.restApi);
    }

    private Poll buildPoll() {
        return Poll.builder()
                .id(UUID.randomUUID())
                .name("Pauta Teste")
                .description("Descrição")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    private Session buildSession() {
        return Session.builder()
                .id(UUID.randomUUID())
                .poll(buildPoll())
                .status(SessionStatus.OPEN)
                .expiration(LocalDateTime.now().minusMinutes(1))
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .votes(List.of())
                .build();
    }

    @Test
    void when_count_votes_returns_success() {
        Session session = buildSession();
        Vote vote = Vote.builder()
                .id(UUID.randomUUID())
                .voteType(VoteType.YES)
                .session(session)
                .createdAt(LocalDateTime.now())
                .build();
        session.setVotes(List.of(vote));

        Map<String, Integer> questions = Arrays.stream(VoteType.values())
                .collect(Collectors.toMap(VoteType::name,
                        v -> (int) session.getVotes().stream()
                                .filter(vt -> v.equals(vt.getVoteType())).count()));

        Result result = Result.builder()
                .poll(session.getPoll())
                .countVotes(session.getVotes().size())
                .questions(questions)
                .build();

        when(service.countVotes(session.getId().toString())).thenReturn(result);

        given()
                .accept(MediaType.APPLICATION_JSON)
        .when()
                .get(URL + "/{sessionId}", session.getId())
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void when_vote_return_sucess() throws IOException {
        Session session = buildSession();

        VoteRequest request = VoteRequest.builder()
                        .associate(UUID.randomUUID())
                        .session(session.getId())
                        .vote(VoteType.NO)
                        .build();

        VoteResponse response = VoteResponse.builder()
                        .message("Vote registered successfully.")
                        .build();

        when(service.vote(org.mockito.ArgumentMatchers.any(Vote.class)))
                .thenReturn("Vote registered successfully.");

        given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType("application/json")
                .body(TestUtil.convertObjectToJsonBytes(request))
        .when()
                .post(URL)
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("message", equalTo(response.getMessage()));
    }

}
