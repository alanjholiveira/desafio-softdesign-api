package br.com.softdesign.desafio.application.rest.v1;

import br.com.softdesign.desafio.application.mapper.PollMapper;
import br.com.softdesign.desafio.application.rest.v1.request.PollRequest;
import br.com.softdesign.desafio.application.rest.v1.response.PollResponse;
import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.domain.service.PollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Tag(name = "Pauta")
@RequestMapping("v1/polls")
@RestController
@AllArgsConstructor
@Validated
public class PollRestApi {

    private PollService service;

    @Operation(summary = "API list all Poll")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
                            @ApiResponse(responseCode = "400", description = "Bad Request"),
                            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                            @ApiResponse(responseCode = "422", description = "Unprocessable Entity"),
                            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<PollResponse>> getAll() {
        log.info("Receiving request to search all poll");
        List<PollResponse> responseList = service.findAll().stream()
                .map(PollMapper::toResponse).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "API to Create Poll")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Created"),
                            @ApiResponse(responseCode = "400", description = "Bad Request"),
                            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                            @ApiResponse(responseCode = "422", description = "Unprocessable Entity"),
                            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<PollResponse> createPoll(@RequestBody @Valid PollRequest request) {
        log.info("Receiving a request to register a new poll. {}", request);
        Poll poll = service.save(PollMapper.toEntity(request));

        return new ResponseEntity<>(PollMapper.toResponse(poll), HttpStatus.CREATED);
    }

}
