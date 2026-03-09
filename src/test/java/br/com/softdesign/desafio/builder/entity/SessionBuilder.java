package br.com.softdesign.desafio.builder.entity;

import br.com.softdesign.desafio.builder.ConstrutorDeEntidade;
import br.com.softdesign.desafio.domain.entity.Session;
import br.com.softdesign.desafio.infrastructure.enums.SessionStatus;
import br.com.softdesign.desafio.infrastructure.repository.SessionRepository;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Component
public class SessionBuilder extends ConstrutorDeEntidade<Session, UUID> {

    private SessionRepository repository;

    private PollBuilder pollBuilder;

    public SessionBuilder(SessionRepository repository, PollBuilder pollBuilder) {
        this.repository = repository;
        this.pollBuilder = pollBuilder;
    }

    @Override
    public Session construirEntidade() throws ParseException {
        return Session.builder()
                .id(UUID.randomUUID())
                .poll(pollBuilder.construirEntidade())
                .status(SessionStatus.OPEN)
                .expiration(LocalDateTime.now().plusMinutes(30))
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Override
    public Session persistir(Session entidade) {
        return repository.save(entidade);
    }

    @Override
    protected Collection<Session> obterTodos() {
        return repository.findAll();
    }

    @Override
    protected Session obterPorId(UUID id) {
        return repository.findById(id).get();
    }
}
