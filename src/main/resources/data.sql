
-- Inserare clădiri
INSERT INTO cladire (nume, adresa) VALUES ('Cladirea A', 'Strada A nr. 1');
INSERT INTO cladire (nume, adresa) VALUES ('Cladirea B', 'Strada B nr. 2');

-- Inserare săli asociate cu clădirile
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('Sala 101', 30, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('Sala 102', 25, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('Sala 201', 40, 2);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('Sala 202', 35, 2);


INSERT INTO rol (name) VALUES ('ADMIN');
INSERT INTO rol (name) VALUES ('STUDENT');
INSERT INTO rol (name) VALUES ('PROFESOR');


INSERT INTO user (username, password, rol_id) VALUES ('admin1', '$2a$12$G0OuquMudPvJljVVMUEXLeO18MYnikPuun6S3OE97AoVWGMt.oCrm', 1); -- Parola: password1
INSERT INTO user (username, password, rol_id) VALUES ('admin2', '$2a$12$Nl4J9x8rB937FG5JWUHfWObvWRocJiOpjB/NonXhcVFNWXuWOUlrW', 1); -- Parola: password2
INSERT INTO user (username, password, rol_id) VALUES ('student1', '$2a$12$aI2UVqdL0qeUivcvckx.Se.kHyR3MllOBaS5JFNgkyIc6IUfJc8S2', 2); -- Parola: password1
-- INSERT INTO student(nume, prenume, grupa,an) VALUES ('numeeee1', 'prenumeee', 'mare', 3);
-- INSERT INTO user (username, password, rol_id) VALUES ('student2', '$2a$12$wY6AnN5oyKYsPM.Wx0MW4eqxlh58gX8gmbHGT40PEWUTPvq1N/zsu', 2); -- Parola: password1