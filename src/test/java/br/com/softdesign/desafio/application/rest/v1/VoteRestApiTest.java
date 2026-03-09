package br.com.softdesign.desafio.application.rest.v1;

import br.com.softdesign.desafio.application.mapper.VoteMapper;
import br.com.softdesign.desafio.application.rest.v1.request.VoteRequest;
import br.com.softdesign.desafio.application.rest.v1.response.VoteResponse;
import br.com.softdesign.desafio.builder.entity.AssociateBuilder;
import br.com.softdesign.desafio.builder.entity.SessionBuilder;
import br.com.softdesign.desafio.builder.entity.VoteBuilder;
import br.com.softdesign.desafio.domain.entity.Associate;
import br.com.softdesign.desafio.domain.entity.Result;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.domain.entity.Vote;
import br.com.softdesign.desafio.domain.service.VoteService;
import br.com.softdesign.desafio.infrastructure.config.testcontainers.AbstractIntegrationTest;
import br.com.softdesign.desafio.infrastructure.enums.VoteType;
import br.com.softdesign.desafio.infrastructure.repository.AssociateRepository;
import br.com.softdesign.desafio.infrastructure.repository.SessionRepository;
import br.com.softdesign.desafio.infrastructure.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest
class VoteRestApiTest extends AbstractIntegrationTest {

    private static final String URL = "/v1/votes";

    @Autowired
    private VoteRestApi restApi;

    @Autowired
    private VoteBuilder builder;

    @Autowired
    private SessionBuilder sessionBuilder;

    @Autowired
    private AssociateBuilder associateBuilder;

    @Mock
    private VoteService service;

    @MockitoBean
    private SessionRepository sessionRepository;

    @MockitoBean
    private AssociateRepository associateRepository;


    @BeforeEach
    public void setup() {
        standaloneSetup(this.restApi);
    }

    @Test
    void when_count_votes_returns_success() throws ParseException {
        Vote entity = builder.construirEntidade();
        Session session = sessionBuilder.construirEntidade();
        session.setVotes(List.of(entity));
        session.setExpiration(session.getCreatedAt());

        Result result = Result.builder()
                        .poll(session.getPoll())
                        .countVotes(session.getVotes().size())
                        .questions(questions(session))
                        .build();

        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));

        when(service.countVotes(session.getId().toString())).thenReturn(result);

        given()
                .accept(MediaType.APPLICATION_JSON)
        .when()
                .get(URL + "/{sessionId}", session.getId())
        .then()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    void when_vote_return_sucess() throws IOException, ParseException {
        Session session = sessionBuilder.construirEntidade();
        Associate associate = associateBuilder.construirEntidade();

        VoteRequest request = VoteRequest.builder()
                        .associate(associate.getId())
                        .session(session.getId())
                        .vote(VoteType.NO)
                        .build();

        VoteResponse response = VoteResponse.builder()
                        .message("Vote registered successfully.")
                                .build();

        when(sessionRepository.findById(session.getId()))
                .thenReturn(Optional.of(session));
        when(associateRepository.findById(associate.getId()))
                .thenReturn(Optional.of(associate));

        when(service.vote(VoteMapper.toEntity(request))).thenReturn("Vote registered successfully.");


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

    private Map<String, Integer> questions(Session session) {
        return Arrays.stream(VoteType.values())
                .collect(Collectors.toMap(VoteType::name, v -> getVotesByType(session, v).size()));
    }

    private List<Vote> getVotesByType(Session session, VoteType voteType) {
        return session.getVotes().stream().filter(v -> voteType.equals(v.getVoteType())).collect(Collectors.toList());
    }

}
