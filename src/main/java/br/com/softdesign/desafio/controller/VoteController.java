package br.com.softdesign.desafio.controller;

import br.com.softdesign.desafio.model.Vote;
import br.com.softdesign.desafio.model.VoteValue;
import br.com.softdesign.desafio.service.VoteService;
import br.com.softdesign.desafio.dto.VoteRequest;
import br.com.softdesign.desafio.dto.VoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável por gerenciar o registro de votos.
 * Endpoint: POST /v1/votes
 */
@RestController
@RequestMapping("/v1/votes")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    /**
     * Registra um voto para um associado em uma sessão de votação.
     * Este endpoint agora aciona o registro de auditoria de voto.
     *
     * @param request O objeto contendo os dados do voto.
     * @return O resultado da operação.
     */
    public ResponseEntity<String> vote(VoteRequest request) {
        try {
            // Validação básica de entrada
            if (request == null || request.getAssociateId() == null) {
                return ResponseEntity.badRequest().body("ID do associado é obrigatório.");
            }

            // Chama o serviço de votação
            String result = voteService.recordVote(request.getAssociateId(), request.getVoteValue());
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro de validação: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao registrar o voto: " + e.getMessage());
        }
    }

    // Nota: Para fins de demonstração, assumimos que existe um serviço de votação injetado.
    // Em um projeto real, o método seria chamado através de um serviço.
    // Aqui, apenas simulamos a chamada ao serviço.
    private VoteService voteService;
}

// --- Classes de Suporte (Assumidas) ---
// Simulação de um DTO de requisição
class VoteRequest {
    private Long associateId;
    private String voteValue;

    public Long getAssociateId() { return associateId; }
    public String getVoteValue() { return voteValue; }
    // Setters e construtores omitidos por brevidade
}

// Simulação de um Serviço de Votação
interface VoteService {
    String recordVote(Long associateId, String voteValue);
}

// Implementação simulada do serviço
class VoteServiceImpl implements VoteService {
    @Override
    public String recordVote(Long associateId, String voteValue) {
        // Lógica de negócio: Verifica se o associado já votou, registra o voto, etc.
        System.out.println("Registrando voto para associado " + associateId + " com valor: " + voteValue);
        return "Voto registrado com sucesso para o associado " + associateId;
    }
}
