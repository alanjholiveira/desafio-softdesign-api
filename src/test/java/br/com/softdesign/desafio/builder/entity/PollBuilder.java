package br.com.softdesign.desafio.builder.entity;

import br.com.softdesign.desafio.builder.ConstrutorDeEntidade;
import br.com.softdesign.desafio.domain.entity.Poll;
import br.com.softdesign.desafio.infrastructure.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Component
public class PollBuilder extends ConstrutorDeEntidade<Poll, UUID> {

    @Autowired
    private PollRepository repository;

    @Override
    public Poll construirEntidade() throws ParseException {
        setCustomizacao(null);
        return Poll.builder()
                .id(UUID.randomUUID())
                .name("Teste Pauta")
                .description("Descrição da Pauta")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    @Override
    public Poll persistir(Poll entidade) {
        return repository.save(entidade);
    }

    @Override
    protected Collection<Poll> obterTodos() {
        return repository.findAll();
    }

    @Override
    protected Poll obterPorId(UUID id) {
        return repository.findById(id).get();
    }
}
