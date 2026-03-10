package br.com.softdesign.desafio.application.rest.v1.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociateRequest {

    @Schema(description = "Nome do associado", example = "João da Silva")
    @NotEmpty
    private String name;

    @Schema(description = "CPF do associado (somente números matematicamente válidos)", example = "27603748666")
    @NotEmpty
    private String taxId;

}
