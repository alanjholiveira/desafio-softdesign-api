package br.com.softdesign.desafio.application.mapper;

import br.com.softdesign.desafio.application.rest.v1.request.AssociateRequest;
import br.com.softdesign.desafio.application.rest.v1.response.AssociateResponse;
import br.com.softdesign.desafio.domain.entity.Associate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AssociateMapper {

    public static AssociateResponse toResponse(Associate entity) {
        return AssociateResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .taxId(entity.getTaxId())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public static Associate toEntity(AssociateRequest request) {
        return Associate.builder()
                .name(request.getName())
                .taxId(request.getTaxId())
                .build();
    }
}
