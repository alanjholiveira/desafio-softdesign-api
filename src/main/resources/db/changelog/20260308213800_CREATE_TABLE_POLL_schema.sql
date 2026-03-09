-- Drop table

-- DROP TABLE tb_poll;

CREATE TABLE tb_poll (
    id uuid NOT NULL,
    name varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    created_at timestamp NULL,
    last_update timestamp NULL,
    CONSTRAINT tb_poll_pkey PRIMARY KEY (id)
);
