CREATE TABLE ws_message_queue (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  sender_username VARCHAR(100) NOT NULL,
                                  receiver_username VARCHAR(100) NOT NULL,
                                  subject VARCHAR(255) NOT NULL,
                                  body TEXT NOT NULL,
                                  delivered BOOLEAN NOT NULL DEFAULT FALSE,
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  delivered_at TIMESTAMP NULL
);
