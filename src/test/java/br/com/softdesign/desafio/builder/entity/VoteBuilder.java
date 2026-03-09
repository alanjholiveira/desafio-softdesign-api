package br.com.softdesign.desafio.builder.entity;

import br.com.softdesign.desafio.builder.ConstrutorDeEntidade;
import br.com.softdesign.desafio.domain.entity.Vote;
import br.com.softdesign.desafio.infrastructure.enums.VoteType;
import br.com.softdesign.desafio.infrastructure.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Component
public class VoteBuilder extends ConstrutorDeEntidade<Vote, UUID> {

    @Autowired
    private VoteRepository repository;

    @Autowired
    private AssociateBuilder associateBuilder;

    @Autowired
    private SessionBuilder sessionBuilder;

    @Override
    public Vote construirEntidade() throws ParseException {
        return Vote.builder()
                .id(UUID.randomUUID())
                .voteType(VoteType.YES)
                .associate(associateBuilder.construirEntidade())
                .session(sessionBuilder.construirEntidade())
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public Vote persistir(Vote entidade) {
        return repository.save(entidade);
    }

    @Override
    protected Collection<Vote> obterTodos() {
        return repository.findAll();
    }

    @Override
    protected Vote obterPorId(UUID id) {
        return repository.findById(id).get();
    }
}
