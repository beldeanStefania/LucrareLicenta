INSERT INTO specializare(specializare)
SELECT * FROM (SELECT 'Informatica') AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM specializare WHERE specializare = 'Informatica'
);

INSERT INTO specializare(specializare)
SELECT * FROM (SELECT 'Matematica') AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM specializare WHERE specializare = 'Matematica'
);

INSERT INTO specializare(specializare)
SELECT * FROM (SELECT 'Matematica-Informatica') AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM specializare WHERE specializare = 'Matematica-Informatica'
);
