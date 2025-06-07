-- Informatica - OBLIGATORII
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, m.an, m.semestru, 'OBLIGATORIE'
FROM specializare s, materie m
WHERE s.specializare = 'Informatica'
  AND m.nume IN ('OOP', 'LFTC', 'Algebra1', 'Algebra2')
  AND NOT EXISTS (
    SELECT 1 FROM curriculum_entry ce WHERE ce.specializare_id = s.id AND ce.materie_id = m.id
);

-- Informatica - OPTIONALE
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, m.an, m.semestru, 'OPTIONALA'
FROM specializare s, materie m
WHERE s.specializare = 'Informatica'
  AND m.nume IN ('Geometrie1', 'Geometrie2')
  AND NOT EXISTS (
    SELECT 1 FROM curriculum_entry ce WHERE ce.specializare_id = s.id AND ce.materie_id = m.id
);

-- Matematica - OBLIGATORII
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, m.an, m.semestru, 'OBLIGATORIE'
FROM specializare s, materie m
WHERE s.specializare = 'Matematica'
  AND m.nume IN ('Algebra1', 'Algebra2', 'Geometrie1', 'Geometrie2')
  AND NOT EXISTS (
    SELECT 1 FROM curriculum_entry ce WHERE ce.specializare_id = s.id AND ce.materie_id = m.id
);

-- Matematica - FACULTATIVE
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, m.an, m.semestru, 'FACULTATIVA'
FROM specializare s, materie m
WHERE s.specializare = 'Matematica'
  AND m.nume IN ('Logica matematica')
  AND NOT EXISTS (
    SELECT 1 FROM curriculum_entry ce WHERE ce.specializare_id = s.id AND ce.materie_id = m.id
);

-- Matematica-Informatica - OBLIGATORII
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, m.an, m.semestru, 'OBLIGATORIE'
FROM specializare s, materie m
WHERE s.specializare = 'Matematica-Informatica'
  AND m.nume IN ('LFTC', 'Algebra1', 'Geometrie1')
  AND NOT EXISTS (
    SELECT 1 FROM curriculum_entry ce WHERE ce.specializare_id = s.id AND ce.materie_id = m.id
);

-- Matematica-Informatica - OPTIONALE
INSERT INTO curriculum_entry (specializare_id, materie_id, an, semestru, tip)
SELECT s.id, m.id, m.an, m.semestru, 'OPTIONALA'
FROM specializare s, materie m
WHERE s.specializare = 'Matematica-Informatica'
  AND m.nume IN ('OOP', 'Analiza numerica')
  AND NOT EXISTS (
    SELECT 1 FROM curriculum_entry ce WHERE ce.specializare_id = s.id AND ce.materie_id = m.id
);
