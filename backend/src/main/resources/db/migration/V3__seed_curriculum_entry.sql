-- seed optionale
INSERT INTO materii_optionale(nume)
VALUES
    ('Pachet optionale 1'),
    ('Pachet optionale 2'),
    ('Pachet optionale 3'),
    ('Pachet optionale 4'),
    ('Pachet optionale 5'),
    ('Pachet optionale 6'),
    ('Pachet optionale 7')
ON DUPLICATE KEY UPDATE nume = nume;

-- seed curriculum_entry pentru specializarea Matematica-Informatica
INSERT INTO curriculum_entry (
    specializare_id,
    materie_id,
    an,
    semestru,
    tip,
    materii_optionale_id
)
SELECT
    s.id,
    m.id,
    m.an,
    m.semestru,
    'OBLIGATORIE',
    NULL
FROM specializare s
         JOIN materie m ON m.nume IN (
                                      'Algebra1','Algebra2','Geometrie1','Geometrie2','Baze de date',
                                      'Analiza complexa','Ecuatii diferentiale','Arhitectura sistemelor de calcul',
                                      'Algoritmi si programare', 'Logica matematica', 'Analiza matematica 1','Structuri de date',
                                      'Programare orientata obiect', 'Analiza matematica 2', 'Mecanica teoretica', 'Analiza numerica',
                                      'Analiza reala', 'Sisteme de operare', 'Teoria probabilitatilor', 'Limbaje formale si tehnici de compilare',
                                      'Ecuatii cu derivate paritale', 'Proiect colectiv', 'Statistica matematica', 'Inteligenta artificiala',
                                      'Tehnici de optimizare', 'Programare web'

    )
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS(
    SELECT 1 FROM curriculum_entry ce
    WHERE ce.specializare_id = s.id
      AND ce.materie_id       = m.id
);

-- 2) Optionale în Pachetul I
INSERT INTO curriculum_entry (
    specializare_id,
    materie_id,
    an,
    semestru,
    tip,
    materii_optionale_id
)
SELECT
    s.id,
    m.id,
    m.an,
    m.semestru,
    'OPTIONALA',
    og.id
FROM specializare s
         JOIN materie m ON m.nume IN (
                                      'Aplicatii ale geometriei in informatica', 'Complemente de algebra','Topologie', 'Programare in C'
    )
         JOIN materii_optionale og
              ON og.nume = 'Pachet optionale 1'
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS(
    SELECT 1 FROM curriculum_entry ce
    WHERE ce.specializare_id      = s.id
      AND ce.materie_id            = m.id
);

INSERT INTO curriculum_entry (
    specializare_id,
    materie_id,
    an,
    semestru,
    tip,
    materii_optionale_id
)
SELECT
    s.id,
    m.id,
    m.an,
    m.semestru,
    'OPTIONALA',
    og.id
FROM specializare s
         JOIN materie m ON m.nume IN (
    'Complemente de geometrie','Complemente de analiza complexa','Capitole speciale de ecuatii diferentiale','Capitole de analiza matematica'
    )
         JOIN materii_optionale og
              ON og.nume = 'Pachet optionale 2'
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS(
    SELECT 1 FROM curriculum_entry ce
    WHERE ce.specializare_id      = s.id
      AND ce.materie_id            = m.id
);

INSERT INTO curriculum_entry (
    specializare_id,
    materie_id,
    an,
    semestru,
    tip,
    materii_optionale_id
)
SELECT
    s.id,
    m.id,
    m.an,
    m.semestru,
    'OPTIONALA',
    og.id
FROM specializare s
         JOIN materie m ON m.nume IN (
        'Astronomie', 'Software matematic''Analiza functionala'
    )
         JOIN materii_optionale og
              ON og.nume = 'Pachet optionale 3'
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS(
    SELECT 1 FROM curriculum_entry ce
    WHERE ce.specializare_id      = s.id
      AND ce.materie_id            = m.id
);

INSERT INTO curriculum_entry (
    specializare_id,
    materie_id,
    an,
    semestru,
    tip,
    materii_optionale_id
)
SELECT
    s.id,
    m.id,
    m.an,
    m.semestru,
    'OPTIONALA',
    og.id
FROM specializare s
         JOIN materie m ON m.nume IN (
    'Automatizarea proceselor de business', 'Interactiune om-calculator','Principiile retelelor de calculatoare'
    )
         JOIN materii_optionale og
              ON og.nume = 'Pachet optionale 4'
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS(
    SELECT 1 FROM curriculum_entry ce
    WHERE ce.specializare_id      = s.id
      AND ce.materie_id            = m.id
);

INSERT INTO curriculum_entry (
    specializare_id,
    materie_id,
    an,
    semestru,
    tip,
    materii_optionale_id
)
SELECT
    s.id,
    m.id,
    m.an,
    m.semestru,
    'OPTIONALA',
    og.id
FROM specializare s
         JOIN materie m ON m.nume IN (
    'Practica de specialitate in informatica', 'Practica de specialitate in matematica'
    )
         JOIN materii_optionale og
              ON og.nume = 'Pachet optionale 5'
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS(
    SELECT 1 FROM curriculum_entry ce
    WHERE ce.specializare_id      = s.id
      AND ce.materie_id            = m.id
);

INSERT INTO curriculum_entry (
    specializare_id,
    materie_id,
    an,
    semestru,
    tip,
    materii_optionale_id
)
SELECT
    s.id,
    m.id,
    m.an,
    m.semestru,
    'OPTIONALA',
    og.id
FROM specializare s
         JOIN materie m ON m.nume IN (
    'Tehnici de testare software', 'Ingineria sistemelor soft', 'Grafică pe calculator'
    )
         JOIN materii_optionale og
              ON og.nume = 'Pachet optionale 6'
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS(
    SELECT 1 FROM curriculum_entry ce
    WHERE ce.specializare_id      = s.id
      AND ce.materie_id            = m.id
);

INSERT INTO curriculum_entry (
    specializare_id,
    materie_id,
    an,
    semestru,
    tip,
    materii_optionale_id
)
SELECT
    s.id,
    m.id,
    m.an,
    m.semestru,
    'OPTIONALA',
    og.id
FROM specializare s
         JOIN materie m ON m.nume IN (
    'Etica si integritate academica', 'Istoria matematicii', 'Istoria informaticii'
    )
         JOIN materii_optionale og
              ON og.nume = 'Pachet optionale 7'
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS(
    SELECT 1 FROM curriculum_entry ce
    WHERE ce.specializare_id      = s.id
      AND ce.materie_id            = m.id
);


-- 4) Facultative
INSERT INTO curriculum_entry (
    specializare_id,
    materie_id,
    an,
    semestru,
    tip,
    materii_optionale_id
)
SELECT
    s.id,
    m.id,
    m.an,
    m.semestru,
    'FACULTATIVA',
    NULL
FROM specializare s
         JOIN materie m ON m.nume IN (
                                      'Comunicare si dezvoltare profesionala',
                                      'Fundamente de educatie umanista',
                                      'Programare in C'
    )
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS(
    SELECT 1 FROM curriculum_entry ce
    WHERE ce.specializare_id      = s.id
      AND ce.materie_id            = m.id
);

-- 5) Toate materiile rămase ca OBLIGATORII
INSERT INTO curriculum_entry (
    specializare_id,
    materie_id,
    an,
    semestru,
    tip,
    materii_optionale_id
)
SELECT
    s.id,
    m.id,
    m.an,
    m.semestru,
    'OBLIGATORIE',
    NULL
FROM specializare s
         JOIN materie m
              ON s.specializare = 'Matematica-Informatica'
WHERE NOT EXISTS (
    SELECT 1
    FROM curriculum_entry ce
    WHERE ce.specializare_id      = s.id
      AND ce.materie_id            = m.id
)
  AND m.nume NOT IN (
    -- excludem cele care au fost deja marcate ca OPTIONALA
                     'Aplicatii ale geometriei in informatica',
                     'Complemente de algebra',
                     'Topologie',
                     'Programare in C',
                     'Complemente de geometrie',
                     'Complemente de analiza complexa',
                     'Capitole speciale de ecuatii diferentiale',
                     'Capitole de analiza matematica',
                     'Astronomie',
                     'Software matematic',
                     'Analiza functionala',
                     'Automatizarea proceselor de business',
                     'Interactiune om-calculator',
                     'Principiile retelelor de calculatoare',
                     'Practica de specialitate in informatica',
                     'Practica de specialitate in matematica',
                     'Tehnici de testare software',
                     'Ingineria sistemelor soft',
                     'Grafică pe calculator',
                     'Etica si integritate academica',
                     'Istoria matematicii',
                     'Istoria informaticii'
    )
  AND m.nume NOT IN (
    -- excludem şi facultativele
                     'Comunicare si dezvoltare profesionala',
                     'Fundamente de educatie umanista',
                     'Programare in C'
    );
