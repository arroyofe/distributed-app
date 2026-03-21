CREATE TABLE demo_item
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(100)          NOT NULL,
    `description` VARCHAR(255)          NULL,
    created_at    datetime              NOT NULL,
    updated_at    datetime              NOT NULL,
    CONSTRAINT pk_demo_item PRIMARY KEY (id)
);

ALTER TABLE demo_item
    ADD CONSTRAINT ux_demo_item_name UNIQUE (name);

CREATE INDEX ix_demo_item_created_at ON demo_item (created_at);