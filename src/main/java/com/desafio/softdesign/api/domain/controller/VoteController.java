package com.desafio.softdesign.api.domain.controller;

import com.desafio.softdesign.api.domain.dto.VoteAuditResponseDTO;
import com.desafio.softdesign.api.domain.exception.VotingException;
import com.desafio.softdesign.api.domain.model.VoteAudit;
import com.desafio.softdesign.api.domain.model.Vote;
import com.desafio.softdesign.api.domain.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsável pela gestão de votos.
 * Contém endpoints para criação, atualização e consulta de histórico de votos.
 * Documentado com OpenAPI/Swagger.
 */
@RestController
@RequestMapping("/api/v1/votes")
@Tag(name = "Votos", description = "Endpoints para gerenciar e consultar votos.")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    /**
     * Endpoint para criar um novo voto.
     * POST /api/v1/votes
     * @param vote O objeto Vote a ser votado.
     * @param associateId O ID do associado votante.
     * @return O voto criado.
     */
    @PostMapping
    @Operation(summary = "Criar Voto", description = "Registra um novo voto no sistema de votação.")
    @ApiResponse(responseCode = "201", description = "Voto criado com sucesso.", content = @Content(schema = @Schema(implementation = Vote.class)))
    @ApiResponse(responseCode = "400", description = "Erro de negócio (ex: sessão inativa, prazo expirado).", content = @Content(schema = @Schema(implementation = Object.class)))
    public ResponseEntity<Vote> createVote(@RequestBody Vote vote, @RequestParam Long associateId) {
        // O tratamento de exceções de negócio (VotingException) é feito pelo GlobalExceptionHandler
        Vote savedVote = voteService.createVote(vote, associateId);
        return new ResponseEntity<>(savedVote, HttpStatus.CREATED);
    }

    /**
     * Endpoint para atualizar um voto existente.
     * PUT /api/v1/votes/{voteId}
     * @param voteId O ID do voto a ser atualizado.
     * @param newVoteData Os dados atualizados do voto.
     * @param associateId O ID do associado que está fazendo a alteração.
     * @return O voto atualizado.
     */
    @PutMapping("/{voteId}")
    @Operation(summary = "Atualizar Voto", description = "Permite a atualização de um voto existente, sujeito a regras de negócio.")
    @ApiResponse(responseCode = "200", description = "Voto atualizado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou permissão negada.")
    public void updateVote(Long id, @PathVariable Long id) {
        // Implementação do método de atualização
        // ...
    }

    /**
     * Endpoint para visualizar o histórico de alterações de um voto específico.
     * @param id O ID do voto.
     * @return A lista de registros de auditoria.
     */
    @GetMapping("/history/{id}")
    public List<AuditRecord> getVoteHistory(@PathVariable Long id) {
        // Implementação do método de histórico
        // ...
    }
}
