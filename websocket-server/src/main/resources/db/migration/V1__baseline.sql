-- ============================================
-- V1 : Initialización de la base de datos
-- ============================================

-- TABLA: demo_item
CREATE TABLE IF NOT EXISTS demo_item
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    created_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE UNIQUE INDEX ux_demo_item_name ON demo_item (name);
CREATE INDEX ix_demo_item_created_at ON demo_item (created_at);


-- TABLA: items
CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NULL
);


-- TABLA: usuarios
CREATE TABLE IF NOT EXISTS users
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    username      VARCHAR(100)          NOT NULL,
    password_hash VARCHAR(255)          NOT NULL,
    email         VARCHAR(255)          NOT NULL,
    role          VARCHAR(100)          NOT NULL,
    created_at    DATETIME(6)           NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at    DATETIME(6)           NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE INDEX ix_users_created_at ON users (created_at);


-- TABLE de perfiles de las personas
CREATE TABLE IF NOT EXISTS profiles
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    email      VARCHAR(200),
    FOREIGN KEY (user_id) REFERENCES users (id)
);


-- INSERT demo items
INSERT INTO demo_item (name, description)
VALUES ('Alpha', 'Premier élément'),
       ('Bravo', 'Deuxième élément'),
       ('Charlie', 'Troisième élément')
ON DUPLICATE KEY UPDATE description = VALUES(description);

-- INSERT  items
INSERT INTO items (name, description)
VALUES ('Cuaderno Seilles 50 paginas', 'Cuaderno especial para el Liceo Francés'),
       ('Laptop sencillo', 'pantalla 15 pulgadas 8 GB RAM 500GB disco duro'),
       ('Laptop profesional', 'pantalla 19 pulgadas 32 GB RAM 2TB disco duro')
ON DUPLICATE KEY UPDATE description = VALUES(description);


-- INSERT usuarios
INSERT INTO users (username, password_hash, email, role)
VALUES ('admin',
        '{bcrypt}$2a$10$S3y1mBuUZTG1eJQZ9.oLYO4j1ozkzko9cKxA4Z./FoJlbbL0/Y8He',
        'admin@example.com',
        'ROLE_ADMIN'),

       ('user1',
        '{bcrypt}$2a$10$kNqlKIav31h2mOoOi3ALhebWiBotnTAce7tN0IYiR.sZAt5uXdViG',
        'user1@example.com',
        'ROLE_DEV'),

       ('user2',
        '{bcrypt}$2a$10$oPN67.SCyFltSgkZiC19e.ANWSfKULqvovxVTKc.Gwk62SVxGM8L6',
        'user2@example.com',
        'ROLE_USER'),

       ('user3',
        '{bcrypt}$2a$10$s6swRBNFujdbADmZJq/dne4dcSv2OQTMKFXI7mCBMu8FfNg/80iOS',
        'user3@example.com',
        'ROLE_USER');