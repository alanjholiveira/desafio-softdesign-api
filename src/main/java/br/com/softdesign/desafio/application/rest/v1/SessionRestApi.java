package br.com.softdesign.desafio.application.rest.v1;

import br.com.softdesign.desafio.application.mapper.SessionMapper;
import br.com.softdesign.desafio.application.rest.v1.request.SessionRequest;
import br.com.softdesign.desafio.application.rest.v1.response.SessionResponse;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.domain.service.SessionService;
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
@Tag(name = "Sessão")
@RequestMapping("v1/sessions")
@RestController
@AllArgsConstructor
@Validated
public class SessionRestApi {

    private final SessionService service;

    @Operation(summary = "API List of all Session", description = "Search All Registered Sessions")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
                            @ApiResponse(responseCode = "400", description = "Bad Request"),
                            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                            @ApiResponse(responseCode = "422", description = "Unprocessable Entity"),
                            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<SessionResponse>> getAll() {
        log.info("Receiving request to fetch all session.");
        List<SessionResponse> responseList = service.findAll().stream()
                .map(SessionMapper::toResponse).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "API Open Session", description = "Open Session for Poll Voting")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Created"),
                            @ApiResponse(responseCode = "400", description = "Bad Request"),
                            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                            @ApiResponse(responseCode = "422", description = "Unprocessable Entity"),
                            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<SessionResponse> openSession(@RequestBody @Valid SessionRequest request) {
        log.info("Receiving session opening request. {}", request);
        Session session = service.openSession(SessionMapper.toEntity(request));

        return new ResponseEntity<>(SessionMapper.toResponse(session), HttpStatus.CREATED);
    }


}
