ALTER TABLE student
    ADD COLUMN specializare_id INT;

ALTER TABLE student
    ADD CONSTRAINT fk_student_specializare
        FOREIGN KEY (specializare_id)
            REFERENCES specializare(id);
