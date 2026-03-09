package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.domain.entity.Associate;
import br.com.softdesign.desafio.infrastructure.client.AssociateStatusClient;
import br.com.softdesign.desafio.infrastructure.client.response.StatusResponse;
import br.com.softdesign.desafio.infrastructure.enums.AssociateStatus;
import br.com.softdesign.desafio.infrastructure.exception.AssociateNotFoundException;
import br.com.softdesign.desafio.infrastructure.repository.AssociateRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class AssociateService {

    private final AssociateRepository repository;
    private final AssociateStatusClient associateStatusClient;

    @Transactional(readOnly = true)
    public List<Associate> findAll() {
        log.info("Searching for all associated registration in the base.");
        return repository.findAll();
    }

    public Associate save(Associate associate) {
        try {
            log.info("Verifying that the member is allowed to vote. taxId={}", associate.getTaxId());
            StatusResponse status = associateStatusClient.getStatus(associate.getTaxId());
            associate.setStatus(status.getStatus());
            log.info("Member status from external service: {}", status.getStatus());
        } catch (FeignException.NotFound ex) {
            // CPF inválido: o serviço externo retornou 404 → não deve cadastrar
            log.error("Invalid taxId (CPF not found in external service). taxId={}", associate.getTaxId());
            throw new AssociateNotFoundException();
        } catch (FeignException ex) {
            // Serviço externo indisponível (5xx, timeout, etc.) → falha aberta: permite cadastro com ABLE_TO_VOTE
            log.warn("External CPF validation service unavailable (status={}). Proceeding with ABLE_TO_VOTE as fallback. taxId={}",
                    ex.status(), associate.getTaxId());
            associate.setStatus(AssociateStatus.ABLE_TO_VOTE);
        }

        log.info("Saving member data in the base.");
        return repository.save(associate);
    }

}
