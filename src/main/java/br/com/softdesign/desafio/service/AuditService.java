package br.com.softdesign.desafio.service;

import br.com.softdesign.desafio.model.VoteAudit;
import br.com.softdesign.desafio.model.VoteAuditEvent;
import br.com.softdesign.desafio.repository.VoteAuditRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço de Auditoria que agora atua como um Listener de Mensagens.
 * Ele consome eventos de votação e persiste o registro de auditoria de forma assíncrona.
 */
@Service
public class AuditService {

    private final VoteAuditRepository voteAuditRepository;

    public AuditService(VoteAuditRepository voteAuditRepository) {
        this.voteAuditRepository = voteAuditRepository;
    }

    /**
     * Método Listener que escuta a fila de eventos de votação.
     * Quando um evento chega, ele processa e salva o registro de auditoria.
     *
     * @param event O evento de votação recebido via RabbitMQ.
     */
    @RabbitListener(queues = "${spring.rabbitmq.queue.vote-audit}")
    @Transactional
    public void handleVoteAuditEvent(VoteAuditEvent event) {
        // 1. Mapeamento do Evento para a Entidade de Auditoria
        VoteAudit audit = new VoteAudit();
        audit.setVoteId(event.getVoteId());
        audit.setAssociateId(event.getAssociateId());
        audit.setPollId(event.getPollId());
        audit.setVotedValue(event.getVotedValue());
        audit.setIpAddress(event.getIpAddress());
        audit.setTimestamp(event.getEventTimestamp());

        // 2. Persistência
        voteAuditRepository.save(audit);
        System.out.println("AUDIT SUCCESS: Audit record saved for Vote ID: " + event.getVoteId());
    }
}
