-- Drop table

-- DROP TABLE tb_vote;

CREATE TABLE tb_vote (
    id uuid NOT NULL,
    associate_id uuid NULL,
    session_id uuid NULL,
    vote_type varchar(20) NULL,
    created_at timestamp NULL,
    CONSTRAINT tb_vote_pkey PRIMARY KEY (id),
    CONSTRAINT fk_associate_id FOREIGN KEY (associate_id) REFERENCES tb_associate(id),
    CONSTRAINT fk_session_id FOREIGN KEY (session_id) REFERENCES tb_session(id)
);
