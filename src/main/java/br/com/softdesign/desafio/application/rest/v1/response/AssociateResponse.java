package br.com.softdesign.desafio.application.rest.v1.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociateResponse {
    @Schema(description = "Identificador único gerado para o associado", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Nome do associado", example = "João da Silva")
    private String name;
    
    @Schema(description = "CPF mascarado ou validado do associado", example = "27603748666")
    private String taxId;

    @Schema(description = "Data e hora de criação do registro", example = "2026-03-09 22:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

}
