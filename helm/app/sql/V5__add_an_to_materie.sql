ALTER TABLE materie
    ADD COLUMN an INT NOT NULL DEFAULT 1;

UPDATE materie
SET an = CASE
             WHEN semestru IN (1,2) THEN 1
             WHEN semestru IN (3,4) THEN 2
             WHEN semestru IN (5,6) THEN 3
             ELSE 1
    END;

ALTER TABLE materie
    ALTER COLUMN an DROP DEFAULT;