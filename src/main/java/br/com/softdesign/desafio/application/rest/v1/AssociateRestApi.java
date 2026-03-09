package br.com.softdesign.desafio.application.rest.v1;

import br.com.softdesign.desafio.application.mapper.AssociateMapper;
import br.com.softdesign.desafio.application.rest.v1.request.AssociateRequest;
import br.com.softdesign.desafio.application.rest.v1.response.AssociateResponse;
import br.com.softdesign.desafio.domain.entity.Associate;
import br.com.softdesign.desafio.domain.service.AssociateService;
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
@Tag(name = "Associado")
@RequestMapping("v1/associates")
@RestController
@AllArgsConstructor
@Validated
public class AssociateRestApi {

    private AssociateService service;

    @Operation(summary = "API list all Associate")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
                            @ApiResponse(responseCode = "400", description = "Bad Request"),
                            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                            @ApiResponse(responseCode = "422", description = "Unprocessable Entity"),
                            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<AssociateResponse>> getAll() {
        log.info("Receiving request to fetch all members.");
        List<AssociateResponse> responseList = service.findAll().stream()
                .map(AssociateMapper::toResponse).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "API to Create Associate")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Created"),
                            @ApiResponse(responseCode = "400", description = "Bad Request"),
                            @ApiResponse(responseCode = "401", description = "Unauthorized"),
                            @ApiResponse(responseCode = "422", description = "Unprocessable Entity"),
                            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<AssociateResponse> createAssociate(@RequestBody @Valid AssociateRequest request) {
        log.info("Receiving a request to register a new member. {}", request);
        Associate associate = service.save(AssociateMapper.toEntity(request));

        return new ResponseEntity<>(AssociateMapper.toResponse(associate), HttpStatus.CREATED);
    }

}
