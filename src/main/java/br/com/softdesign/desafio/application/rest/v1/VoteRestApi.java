package br.com.softdesign.desafio.application.rest.v1;

import br.com.softdesign.desafio.application.mapper.ResultMapper;
import br.com.softdesign.desafio.application.mapper.VoteMapper;
import br.com.softdesign.desafio.application.rest.v1.request.VoteRequest;
import br.com.softdesign.desafio.application.rest.v1.response.ResultResponse;
import br.com.softdesign.desafio.application.rest.v1.response.VoteResponse;
import br.com.softdesign.desafio.domain.entity.Result;
import br.com.softdesign.desafio.domain.service.VoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Tag(name = "Votação")
@RequestMapping("v1/votes")
@RestController
@AllArgsConstructor
@Validated
public class VoteRestApi {

    private final VoteService service;

    @PostMapping
    public ResponseEntity<VoteResponse> vote(@RequestBody @Valid VoteRequest request) {
        log.info("Receiving request to compute vote. {}", request.getSession());
        String message = service.vote(VoteMapper.toEntity(request));

        return ResponseEntity.ok(VoteMapper.toResponse(message));

    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ResultResponse> countVotes(@PathVariable String sessionId) {
        log.info("Receiving request to post result. {}", sessionId);
        Result result = service.countVotes(sessionId);
        return ResponseEntity.ok(ResultMapper.toResponse(result));
    }

}
