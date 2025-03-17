-- Insert buildings (cladire)
INSERT INTO cladire (nume, adresa)
SELECT 'Facultatea de drept', 'Strada Avram Iancu 11'
WHERE NOT EXISTS (
    SELECT 1 FROM cladire WHERE nume = 'Facultatea de drept' AND adresa = 'Strada Avram Iancu 11'
);

INSERT INTO cladire (nume, adresa)
SELECT 'Cladirea Centrala', 'Strada Mihail Kogalniceanu 1'
WHERE NOT EXISTS (
    SELECT 1 FROM cladire WHERE nume = 'Cladirea Centrala' AND adresa = 'Strada Mihail Kogalniceanu 1'
);

INSERT INTO cladire (nume, adresa)
SELECT 'Mathematica', 'Strada Ploiesti 23-25'
WHERE NOT EXISTS (
    SELECT 1 FROM cladire WHERE nume = 'Mathematica' AND adresa = 'Strada Ploiesti 23-25'
);

INSERT INTO cladire (nume, adresa)
SELECT 'FSEGA', 'Strada Teodor Mihali 58-60'
WHERE NOT EXISTS (
    SELECT 1 FROM cladire WHERE nume = 'FSEGA' AND adresa = 'Strada Teodor Mihali 58-60'
);

INSERT INTO cladire (nume, adresa)
SELECT 'NTT Data', 'Strada Teodor Mihali 58-60'
WHERE NOT EXISTS (
    SELECT 1 FROM cladire WHERE nume = 'NTT Data' AND adresa = 'Strada Teodor Mihali 58-60'
);


-- (Repeat for other buildings...)

-- Insert rooms (sala)
-- Insert rooms (sala)
INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '2\\I', 30, (SELECT id FROM cladire WHERE nume = 'Cladirea Centrala')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = '2\\I' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Cladirea Centrala')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '5\\I', 25, (SELECT id FROM cladire WHERE nume = 'Cladirea Centrala')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = '5\\I' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Cladirea Centrala')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '6\\II', 40, (SELECT id FROM cladire WHERE nume = 'Cladirea Centrala')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = '6\\II' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Cladirea Centrala')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '7\\I', 40, (SELECT id FROM cladire WHERE nume = 'Cladirea Centrala')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = '7\\I' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Cladirea Centrala')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '9\\I', 40, (SELECT id FROM cladire WHERE nume = 'Cladirea Centrala')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = '9\\I' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Cladirea Centrala')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A303', 33, (SELECT id FROM cladire WHERE nume = 'Facultatea de drept')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = 'A303' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Facultatea de drept')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A308', 33, (SELECT id FROM cladire WHERE nume = 'Facultatea de drept')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = 'A308' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Facultatea de drept')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A311', 33, (SELECT id FROM cladire WHERE nume = 'Facultatea de drept')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = 'A311' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Facultatea de drept')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A312', 33, (SELECT id FROM cladire WHERE nume = 'Facultatea de drept')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = 'A312' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Facultatea de drept')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A313', 33, (SELECT id FROM cladire WHERE nume = 'Facultatea de drept')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = 'A313' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Facultatea de drept')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'Multimedia', 33, (SELECT id FROM cladire WHERE nume = 'Mathematica')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = 'Multimedia' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Mathematica')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'gamma', 33, (SELECT id FROM cladire WHERE nume = 'Mathematica')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = 'gamma' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'Mathematica')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A2', 33, (SELECT id FROM cladire WHERE nume = 'FSEGA')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = 'A2' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'FSEGA')
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'C310', 33, (SELECT id FROM cladire WHERE nume = 'FSEGA')
WHERE NOT EXISTS (
    SELECT 1 FROM sala WHERE nume = 'C310' AND cladire_id = (SELECT id FROM cladire WHERE nume = 'FSEGA')
);


-- Insert subjects (materii)
INSERT INTO materie (nume, semestru, cod)
SELECT 'OOP', 1, 'OOP101'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'OOP'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'LFTC', 1, 'LFTC102'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'LFTC'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'Mecanica', 1, 'MEC103'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Mecanica'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'Algebra1', 1, 'ALG101'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Algebra1'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'Algebra2', 2, 'ALG102'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Algebra2'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'Logica matematica', 1, 'LOG104'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Logica matematica'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'Geometrie1', 1, 'GEO101'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Geometrie1'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'Geometrie2', 2, 'GEO102'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Geometrie2'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'Analiza numerica', 2, 'AN105'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Analiza numerica'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'Analiza complexa', 3, 'AC106'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Analiza complexa'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'Analiza reala1', 3, 'AR107'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Analiza reala1'
);

INSERT INTO materie (nume, semestru, cod)
SELECT 'Analiza reala2', 4, 'AR108'
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Analiza reala2'
);


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
