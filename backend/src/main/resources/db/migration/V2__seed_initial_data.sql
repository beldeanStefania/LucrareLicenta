-- cladiri
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

-- sali
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


-- (materii)
-- an 1
-- sem 1
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Algoritmi si programare', 1, 1, 'AP102', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Algoritmi si programare'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Algebra1', 1, 1, 'ALG101', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Algebra1'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Geometrie1', 1, 1,  'GEO101', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Geometrie1'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Logica matematica', 1, 1, 'LOG104', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Logica matematica'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Analiza matematica 1', 1, 1, 'AM100', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Analiza matematica 1'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Comunicare si dezvoltare profesionala', 1, 1, 'CDP124', 3
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Comunicare si dezvoltare profesionala'
);

-- sem 2
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Algebra 2', 2, 1, 'ALG102', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Algebra 2'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Structuri de date', 2, 1, 'SD102', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Structuri de date'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Geometrie2', 2, 1,  'GEO102', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Geometrie2'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Programare orientata obiect', 2, 1, 'OOP101', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Programare orientata obiect'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Analiza matematica 2',2, 1, 'AM201', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Analiza matematica 2'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Fundamente de educatie umanista',2, 1, 'FEU201', 3
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Fundamente de educatie umanista'
);

-- an 2
-- sem 3
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Baze de date',3, 2, 'BD201', 5
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Baze de date'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Analiza complexa', 3, 2, 'AC106', 5
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Analiza complexa'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Ecuatii diferentiale', 3, 2, 'EDF106', 5
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Ecuatii diferentiale'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Arhitectura sistemelor de calcul', 3, 2, 'ASC106', 4
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Arhitectura sistemelor de calcul'
);

-- optionale 1
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Aplicatii ale geometriei in informatica', 3, 2, 'AGI176', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Aplicatii ale geometriei in informatica'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Complemente de algebra', 3, 2, 'CA167', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Complemente de algebra'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Topologie', 3, 2, 'TOP111', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Topologie'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Programare in C', 3, 2, 'PC126', 3
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Programare in C'
);

-- sem 4
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Mecanica teoretica', 4, 2, 'MEC103', 5
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Mecanica teoretica'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Analiza numerica', 4, 2, 'AN105', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Analiza numerica'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Analiza reala', 4, 2, 'AR107', 5
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Analiza reala1'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Sisteme de operare', 4, 2, 'SO108', 4
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Sisteme de operare'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Teoria probabilitatilor', 4 ,2, 'TO108', 4
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Teoria probabilitatilor'
);

-- optionale 2
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Complemente de geometrie', 4 ,2, 'CG108', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Complemente de geometrie'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Complemente de analiza complexa', 4 ,2, 'CA108', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Complemente de analiza complexa'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Capitole speciale de ecuatii diferentiale', 4 ,2, 'CSED178', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Capitole speciale de ecuatii diferentiale'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Capitole de analiza matematica', 4 ,2, 'CAM178', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Capitole de analiza matematica'
);

-- an 3
-- sem 5
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Limbaje formale si tehnici de compilare', 5, 3, 'LFTC102', 3
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Limbaje formale si tehnici de compilare'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Ecuatii cu derivate paritale', 5, 3, 'EDP133', 4
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Ecuatii cu derivate paritale'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Proiect colectiv', 5, 3, 'PC133', 7
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Proiect colectiv'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Statistica matematica', 5, 3, 'SM133', 4
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Statistica matematica'
);

-- optionale 3
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Astronomie', 5, 3, 'AS133', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Astronomie'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Software matematic', 5, 3, 'SM163', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Software matematic'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Analiza functionala', 5, 3, 'AF113', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Analiza functionala'
);

-- optionale 4
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Automatizarea proceselor de business', 5, 3, 'APB113', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Automaticarea proceselor de business'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Interactiune om-calculator', 5, 3, 'IOC113', 7
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Interactiune om-calculator'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Principiile retelelor de calculatoare', 5, 3, 'PRC113', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Principiile retelelor de calculatoare'
);

-- optionale 5
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Practica de specialitate in informatica', 5, 3, 'PSF113', 4
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Practica de specialitate in informatica'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Practica de specialitate in matematica', 5, 3, 'PSM116', 4
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Practica de specialitate in matematica'
);

-- sem 6
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Inteligenta artificiala', 6, 3, 'IA116', 5
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Inteligenta artificiala'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Tehnici de optimizare', 6, 3, 'TO567', 5
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Tehnici de optimizare'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Programare web', 6, 3, 'PW198', 5
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Programare web'
);

-- optionale 6
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Tehnici de testare software', 6, 3, 'PDM116', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Tehnici de testare software'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Ingineria sistemelor soft', 6, 3, 'ISS116', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Ingineria sistemelor soft'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Grafică pe calculator', 6, 3, 'TTS116', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Grafică pe calculator'
);

-- optionale 7
INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Etica si integritate academica', 6, 3, 'EIA116', 4
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Etica si integritate academica'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Istoria matematicii', 6, 3, 'IM116', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Istoria matematicii'
);

INSERT INTO materie (nume, semestru, an, cod, credite)
SELECT 'Istoria informaticii', 6, 3, 'II116', 6
WHERE NOT EXISTS (
    SELECT 1 FROM materie WHERE nume = 'Istoria informaticii'
);

-- specializare
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


-- Insert rol
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

-- Insert a user
INSERT INTO user (username, password, email, rol_id)
SELECT
    'admin1',
    '$2a$12$G0OuquMudPvJljVVMUEXLeO18MYnikPuun6S3OE97AoVWGMt.oCrm',
    'admin@yahoo.com',
    (SELECT id FROM rol WHERE name = 'ADMIN')
WHERE NOT EXISTS (
    SELECT 1
    FROM user
    WHERE username = 'admin1'
);
