-- MySQL 8+ : table simple pour tests
-- V1 : création de la table de base
CREATE TABLE IF NOT EXISTS demo_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NULL,
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

-- Contrainte d'unicité sur le nom (exemple)
CREATE UNIQUE INDEX IF NOT EXISTS ux_demo_item_name ON demo_item (name);

-- Index pour requêtes temporelles
CREATE INDEX IF NOT EXISTS ix_demo_item_created_at ON demo_item (created_at);