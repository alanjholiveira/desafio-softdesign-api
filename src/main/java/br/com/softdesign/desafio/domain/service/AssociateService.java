package br.com.softdesign.desafio.domain.service;

import br.com.softdesign.desafio.domain.entity.Associate;
import br.com.softdesign.desafio.infrastructure.client.AssociateStatusClient;
import br.com.softdesign.desafio.infrastructure.client.response.StatusResponse;
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
            log.info("Verifying that the member is allowed to vote.");
            StatusResponse status = associateStatusClient.getStatus(associate.getTaxId());
            associate.setStatus(status.getStatus());

            log.info("Saving member data in the base.");
            return repository.save(associate);
        } catch (FeignException ex) {
            log.error("There was an error getting member status. {0}", ex);
            throw new AssociateNotFoundException();
        }

    }

}
