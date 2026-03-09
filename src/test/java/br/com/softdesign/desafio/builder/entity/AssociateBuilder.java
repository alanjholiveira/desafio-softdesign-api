package br.com.softdesign.desafio.builder.entity;

import br.com.softdesign.desafio.builder.ConstrutorDeEntidade;
import br.com.softdesign.desafio.domain.entity.Associate;
import br.com.softdesign.desafio.infrastructure.enums.AssociateStatus;
import br.com.softdesign.desafio.infrastructure.repository.AssociateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Component
public class AssociateBuilder extends ConstrutorDeEntidade<Associate, UUID> {

    @Autowired
    private AssociateRepository repository;

    @Override
    public Associate construirEntidade() throws ParseException {
        setCustomizacao(null);
        return Associate.builder()
                .id(UUID.randomUUID())
                .name("Nome Associado")
                .taxId("58382140076")
                .status(AssociateStatus.ABLE_TO_VOTE)
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Override
    public Associate persistir(Associate entidade) {
        return repository.save(entidade);
    }

    @Override
    protected Collection<Associate> obterTodos() {
        return repository.findAll();
    }

    @Override
    protected Associate obterPorId(UUID id) {
        return repository.findById(id).get();
    }

}
