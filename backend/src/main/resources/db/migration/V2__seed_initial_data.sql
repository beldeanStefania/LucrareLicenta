-- Insert buildings (cladire)
INSERT INTO cladire (nume, adresa)
SELECT 'Facultatea de drept', 'Strada Avram Iancu 11'
WHERE NOT EXISTS (
    SELECT 1
    FROM cladire
    WHERE nume = 'Facultatea de drept'
      AND adresa = 'Strada Avram Iancu 11'
);

INSERT INTO cladire (nume, adresa)
SELECT 'Cladirea Centrala', 'Strada Mihail Kogalniceanu 1'
WHERE NOT EXISTS (
    SELECT 1
    FROM cladire
    WHERE nume = 'Cladirea Centrala'
      AND adresa = 'Strada Mihail Kogalniceanu 1'
);

-- (Repeat for other buildings...)

-- Insert rooms (sala)
INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '2\\I', 30, 1
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = '2\\I'
      AND cladire_id = 1
);

-- (Repeat for other room entries...)

-- Insert roles (rol)
INSERT INTO rol (name)
SELECT 'ADMIN'
WHERE NOT EXISTS (
    SELECT 1
    FROM rol
    WHERE name = 'ADMIN'
);

INSERT INTO rol (name)
SELECT 'STUDENT'
WHERE NOT EXISTS (
    SELECT 1
    FROM rol
    WHERE name = 'STUDENT'
);

INSERT INTO rol (name)
SELECT 'PROFESOR'
WHERE NOT EXISTS (
    SELECT 1
    FROM rol
    WHERE name = 'PROFESOR'
);

-- Insert a user (user)
INSERT INTO user (username, password, rol_id)
SELECT 'admin1', '$2a$12$G0OuquMudPvJljVVMUEXLeO18MYnikPuun6S3OE97AoVWGMt.oCrm', 1
WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE username = 'admin1'
);
