INSERT INTO demo_item (name, description) VALUES
    ('Alpha',   'Premier élément'),
    ('Bravo',   'Deuxième élément'),
    ('Charlie', 'Troisième élément')
ON DUPLICATE KEY UPDATE
    description = VALUES(description);