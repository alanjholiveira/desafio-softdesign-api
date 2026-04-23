package br.com.softdesign.desafio.controller;

import br.com.softdesign.desafio.model.Associate;
import br.com.softdesign.desafio.service.AssociateService;
import br.com.softdesign.desafio.dto.AssociateRequest;
import br.com.softdesign.desafio.dto.AssociateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável por gerenciar o cadastro de associados.
 * Endpoint: POST /v1/associates
 */
@RestController
@RequestMapping("/v1/associates")
public class AssociateController {

    private final AssociateService associateService;

    public AssociateController(AssociateService associateService) {
        this.associateService = associateService;
    }

    /**
     * Cadastra um novo associado no sistema.
     * @param request O DTO contendo os dados do associado.
     * @return Resposta contendo os dados do associado cadastrado.
     */
    @Operation(summary = "Cadastra um novo associado", description = "Registra um novo membro no sistema de votação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", message = "Associado cadastrado com sucesso.",
                    content = @Content(schema = @Schema(implementation = AssociateResponse.class))),
            @ApiResponse(responseCode = "400", message = "Requisição inválida. Verifique os dados do associado.",
                    content = @Content(schema = @Schema(implementation = java.util.Map.class))),
            @ApiResponse(responseCode = "409", message = "Conflito: Associado já existe com este CPF.",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<AssociateResponse> registerAssociate(@RequestBody AssociateRequest request) {
        // 1. Mapeamento do DTO para a Entidade
        Associate associate = new Associate();
        associate.setCpf(request.getCpf());
        associate.setNome(request.getNome());
        associate.setEmail(request.getEmail());

        // 2. Chamada ao serviço de negócio
        Associate savedAssociate = associateService.registerAssociate(associate);

        // 3. Construção da resposta
        AssociateResponse response = new AssociateResponse(
                savedAssociate.getId(),
                savedAssociate.getCpf(),
                savedAssociate.getNome(),
                savedAssociate.getEmail()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
