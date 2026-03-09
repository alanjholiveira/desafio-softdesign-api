-- Drop table

-- DROP TABLE tb_associate;

CREATE TABLE tb_associate (
     id uuid NOT NULL,
     name varchar(255) NOT NULL,
     tax_id varchar(20) NOT NULL,
     status varchar(50) NOT NULL,
     created_at timestamp NULL,
     last_update timestamp NULL,
     CONSTRAINT tb_associate_pkey PRIMARY KEY (id),
     CONSTRAINT uk_tax_id_unique UNIQUE (tax_id)
);
