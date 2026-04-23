-- Criação da tabela de auditoria de votos (VoteAudit)
CREATE TABLE vote_audit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vote_id BIGINT NOT NULL,
    associate_id BIGINT NOT NULL,
    action_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    details TEXT,

    -- Definição de Foreign Keys
    CONSTRAINT fk_vote_audit_vote
        FOREIGN KEY (vote_id)
        REFERENCES vote(id)
        ON DELETE CASCADE, -- Se o voto for deletado, o histórico também deve ser deletado
    CONSTRAINT fk_vote_audit_associate
        FOREIGN KEY (associate_id)
        REFERENCES associate(id)
        ON DELETE RESTRICT -- Não permite deletar associado se houver histórico
);

-- Nota: Assumindo que as tabelas 'vote' e 'associate' já existem e possuem as colunas 'id'.
