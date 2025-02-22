-- Cladiri (buildings)
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

INSERT INTO cladire (nume, adresa)
SELECT 'Mathematica', 'Strada Ploiesti 23-25'
WHERE NOT EXISTS (
    SELECT 1
    FROM cladire
    WHERE nume = 'Mathematica'
      AND adresa = 'Strada Ploiesti 23-25'
);

INSERT INTO cladire (nume, adresa)
SELECT 'FSEGA', 'Strada Teodor Mihali 58-60'
WHERE NOT EXISTS (
    SELECT 1
    FROM cladire
    WHERE nume = 'FSEGA'
      AND adresa = 'Strada Teodor Mihali 58-60'
);

INSERT INTO cladire (nume, adresa)
SELECT 'NTT Data', 'Strada Teodor Mihali 58-60'
WHERE NOT EXISTS (
    SELECT 1
    FROM cladire
    WHERE nume = 'NTT Data'
      AND adresa = 'Strada Teodor Mihali 58-60'
);

-- Sali (rooms)
INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '2\\I', 30, 1
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = '2\\I'
      AND cladire_id = 1
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '5\\I', 25, 1
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = '5\\I'
      AND cladire_id = 1
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '6\\II', 40, 1
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = '6\\II'
      AND cladire_id = 1
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '7\\I', 40, 1
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = '7\\I'
      AND cladire_id = 1
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '9\\I', 40, 1
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = '9\\I'
      AND cladire_id = 1
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'AAM', 40, 1
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'AAM'
      AND cladire_id = 1
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'AVM', 40, 1
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'AVM'
      AND cladire_id = 1
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'MOS-S15', 40, 1
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'MOS-S15'
      AND cladire_id = 1
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A303', 33, 2
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'A303'
      AND cladire_id = 2
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A308', 33, 2
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'A308'
      AND cladire_id = 2
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A311', 33, 2
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'A311'
      AND cladire_id = 2
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A312', 33, 2
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'A312'
      AND cladire_id = 2
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A313', 33, 2
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'A313'
      AND cladire_id = 2
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A321', 33, 2
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'A321'
      AND cladire_id = 2
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A322', 33, 2
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'A322'
      AND cladire_id = 2
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A323', 33, 2
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'A323'
      AND cladire_id = 2
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'Multimedia', 33, 3
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'Multimedia'
      AND cladire_id = 3
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'e', 33, 3
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'e'
      AND cladire_id = 3
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'gamma', 33, 3
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'gamma'
      AND cladire_id = 3
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'lambda', 33, 3
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'lambda'
      AND cladire_id = 3
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'pi', 33, 3
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'pi'
      AND cladire_id = 3
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'A2', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'A2'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'C310', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'C310'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'C335', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'C335'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'C510', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'C510'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'C512', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'C512'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L534', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L534'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L001', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L001'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L002', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L002'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L301', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L301'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L302', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L302'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L306', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L306'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L307', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L307'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L308', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L308'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L320', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L320'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT '321', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = '321'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L336', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L336'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L338', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L338'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L339', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L339'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L343', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L343'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L402', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L402'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L404', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L404'
      AND cladire_id = 4
);

INSERT INTO sala (nume, capacitate, cladire_id)
SELECT 'L439', 33, 4
WHERE NOT EXISTS (
    SELECT 1
    FROM sala
    WHERE nume = 'L439'
      AND cladire_id = 4
);

-- Roluri (roles)
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

-- User (only insert if not present)
INSERT INTO user (username, password, rol_id)
SELECT 'admin1', '$2a$12$G0OuquMudPvJljVVMUEXLeO18MYnikPuun6S3OE97AoVWGMt.oCrm', 1
WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE username = 'admin1'
);
