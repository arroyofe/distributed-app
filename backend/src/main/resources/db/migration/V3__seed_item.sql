-- V2 : données de démo (idempotent grâce à ON DUPLICATE KEY UPDATE)
INSERT INTO demo_item (name, description) VALUES
                                              ('Alpha',   'Premier élément'),
                                              ('Bravo',   'Deuxième élément'),
                                              ('Charlie', 'Troisième élément')
ON DUPLICATE KEY UPDATE
    description = VALUES(description);