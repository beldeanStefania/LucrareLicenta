-- =========================================
--  INFORMATICA – ANUL 2
-- =========================================
-- Obligatorii
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, 2, m.semestru, 'OBLIGATORIE'
FROM specializare s
         JOIN materie m ON m.nume IN ('Algebra2','Geometrie2','Analiza numerica')
WHERE s.specializare = 'Informatica'
  AND NOT EXISTS (
    SELECT 1
    FROM curriculum_entry ce
    WHERE ce.specializare_id = s.id
      AND ce.materie_id       = m.id
      AND ce.an               = 2
);

-- Opționale
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, 2, m.semestru, 'OPTIONALA'
FROM specializare s
         JOIN materie m ON m.nume IN ('Mecanica','Logica matematica')
WHERE s.specializare = 'Informatica'
  AND NOT EXISTS (
    SELECT 1
    FROM curriculum_entry ce
    WHERE ce.specializare_id = s.id
      AND ce.materie_id       = m.id
      AND ce.an               = 2
);

-- =========================================
--  INFORMATICA – ANUL 3
-- =========================================
-- Obligatorii
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, 3, m.semestru, 'OBLIGATORIE'
FROM specializare s
         JOIN materie m ON m.nume IN ('Analiza complexa','Analiza reala1')
WHERE s.specializare = 'Informatica'
  AND NOT EXISTS (
    SELECT 1
    FROM curriculum_entry ce
    WHERE ce.specializare_id = s.id
      AND ce.materie_id       = m.id
      AND ce.an               = 3
);

-- =========================================
--  MATEMATICA – ANUL 2
-- =========================================
-- Obligatorii
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, 2, m.semestru, 'OBLIGATORIE'
FROM specializare s
         JOIN materie m ON m.nume IN ('Algebra2','Geometrie2','Analiza numerica')
WHERE s.specializare = 'Matematica'
  AND NOT EXISTS (
    SELECT 1
    FROM curriculum_entry ce
    WHERE ce.specializare_id = s.id
      AND ce.materie_id       = m.id
      AND ce.an               = 2
);

-- Opționale
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, 2, m.semestru, 'OPTIONALA'
FROM specializare s
         JOIN materie m ON m.nume IN ('Logica matematica','Mecanica')
WHERE s.specializare = 'Matematica'
  AND NOT EXISTS (
    SELECT 1
    FROM curriculum_entry ce
    WHERE ce.specializare_id = s.id
      AND ce.materie_id       = m.id
      AND ce.an               = 2
);

-- =========================================
--  MATEMATICA – ANUL 3
-- =========================================
-- Obligatorii
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, 3, m.semestru, 'OBLIGATORIE'
FROM specializare s
         JOIN materie m ON m.nume IN ('Analiza complexa','Analiza reala1')
WHERE s.specializare = 'Matematica'
  AND NOT EXISTS (
    SELECT 1
    FROM curriculum_entry ce
    WHERE ce.specializare_id = s.id
      AND ce.materie_id       = m.id
      AND ce.an               = 3
);

-- =========================================
--  MATEMATICA-INFORMATICA – ANUL 2
-- =========================================
-- Obligatorii
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, 2, m.semestru, 'OBLIGATORIE'
FROM specializare s
         JOIN materie m ON m.nume IN ('Algebra2','Geometrie2','Analiza numerica')
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS (
    SELECT 1
    FROM curriculum_entry ce
    WHERE ce.specializare_id = s.id
      AND ce.materie_id       = m.id
      AND ce.an               = 2
);

-- Opționale
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, 2, m.semestru, 'OPTIONALA'
FROM specializare s
         JOIN materie m ON m.nume IN ('OOP','Mecanica')
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS (
    SELECT 1
    FROM curriculum_entry ce
    WHERE ce.specializare_id = s.id
      AND ce.materie_id       = m.id
      AND ce.an               = 2
);

-- =========================================
--  MATEMATICA-INFORMATICA – ANUL 3
-- =========================================
-- Obligatorii
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, 3, m.semestru, 'OBLIGATORIE'
FROM specializare s
         JOIN materie m ON m.nume IN ('Analiza complexa','Analiza reala1')
WHERE s.specializare = 'Matematica-Informatica'
  AND NOT EXISTS (
    SELECT 1
    FROM curriculum_entry ce
    WHERE ce.specializare_id = s.id
      AND ce.materie_id       = m.id
      AND ce.an               = 3
);
