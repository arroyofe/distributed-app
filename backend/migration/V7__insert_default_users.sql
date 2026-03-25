-- Insert default users (admin, dev, users)
-- Passwords are BCrypt encoded

INSERT INTO users (username, password_hash, email, role)
VALUES
    ('admin',
     '$2a$10$9UO6mD2bvjOjTnDBJoX4Ce1Xz5.HFqL5EqslugIGkmYoQ8dFkhVpu',
     'admin@example.com',
     'ADMIN'),

    ('user1',
     '$2a$10$wLfQN0CkFTH6tSgUiVX3WehbkYJ7MjZhc1aSaNneWjmJBBRmBKxge',
     'user1@example.com',
     'DEV'),

    ('user2',
     '$2a$10$0XiFh38p9c09zemZM6YtY.rUUX2pSXx6UDuW9L5NPH52G7ry6BWRO',
     'user2@example.com',
     'USER'),

    ('user3',
     '$2a$10$N6fPRgpP7ORxeP0/w7PkWeZMCT31Z9ZaYH/vhuwH06/P1QCeCeOJ6',
     'user3@example.com',
     'USER');