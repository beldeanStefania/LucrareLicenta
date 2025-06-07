ALTER TABLE catalog_student_materie
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACTIV';

UPDATE catalog_student_materie
SET status = 'ACTIV'
WHERE status IS NULL;

ALTER TABLE catalog_student_materie
    ALTER COLUMN status DROP DEFAULT;
