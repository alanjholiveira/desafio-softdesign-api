package br.com.softdesign.desafio.infrastructure.client.response;

import br.com.softdesign.desafio.infrastructure.enums.AssociateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {

    private AssociateStatus status;

}
