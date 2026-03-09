package br.com.softdesign.desafio.infrastructure.client;

import br.com.softdesign.desafio.infrastructure.client.response.StatusResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ObterSituacaoAssociado", url = "https://user-info.herokuapp.com/")
public interface AssociateStatusClient {

    @GetMapping(value = "/users/{taxId}", produces = "application/json")
    StatusResponse getStatus(@PathVariable("taxId") String taxId);

}
