
-- Inserare clădiri
INSERT INTO cladire (nume, adresa) VALUES ('Cladirea A', 'Strada A nr. 1');
INSERT INTO cladire (nume, adresa) VALUES ('Cladirea B', 'Strada B nr. 2');

-- Inserare săli asociate cu clădirile
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('Sala 101', 30, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('Sala 102', 25, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('Sala 201', 40, 2);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('Sala 202', 35, 2);

-- Adăugarea rolurilor
-- INSERT INTO rol (name) VALUES ('ADMIN');
-- INSERT INTO rol (name) VALUES ('STUDENT');
-- INSERT INTO rol (name) VALUES ('PROFESOR');

-- Adăugarea utilizatorilor (parolele trebuie criptate)
-- INSERT INTO user (username, password, rol_id) VALUES ('admin1', '$2a$10$DOW4rRQzHpV.Dc./yIxTve/3JixzCIFXGHZdK1t7u3E56RHFg19eW', 1); -- Parola: password1
-- INSERT INTO user (username, password, rol_id) VALUES ('admin2', '$2a$10$7qDWOR9i2Vo9pVO7x2YTOO2a0zMPKNy3sxW5vvCxyKOrr0kxth.nq', 1); -- Parola: password2
