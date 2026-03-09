-- Drop table

-- DROP TABLE tb_session;

CREATE TABLE tb_session (
    id uuid NOT NULL,
    poll_id uuid NULL,
    status varchar(255) NULL,
    expiration timestamp NOT NULL,
    created_at timestamp NULL,
    last_update timestamp NULL,
    CONSTRAINT tb_session_pkey PRIMARY KEY (id),
    CONSTRAINT fk_poll_id FOREIGN KEY (poll_id) REFERENCES tb_poll(id)
);
