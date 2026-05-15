CREATE TABLE ws_user (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(100) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         enabled BOOLEAN NOT NULL DEFAULT TRUE,
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
