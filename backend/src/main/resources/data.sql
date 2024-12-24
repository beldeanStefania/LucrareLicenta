
-- Inserare clădiri
INSERT INTO cladire (nume, adresa) VALUES ('Facultatea de drept', 'Strada Avram Iancu 11');
INSERT INTO cladire (nume, adresa) VALUES ('Cladirea Centrala', 'Strada Mihail Kogalniceanu 1');
INSERT INTO cladire (nume, adresa) VALUES ('Mathematica', 'Strada Ploiesti 23-25');
INSERT INTO cladire (nume, adresa) VALUES ('FSEGA', 'Strada Teodor Mihali 58-60');
INSERT INTO cladire (nume, adresa) VALUES ('NTT Data', 'Strada Teodor Mihali 58-60');

-- Inserare săli asociate cu clădirile
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('2\I', 30, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('5\I', 25, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('6\II', 40, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('7\I', 40, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('9\I', 40, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('AAM', 40, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('AVM', 40, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('MOS-S15', 40, 1);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('A303', 33, 2);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('A308', 33, 2);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('A311', 33, 2);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('A312', 33, 2);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('A313', 33, 2);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('A321', 33, 2);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('A322', 33, 2);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('A323', 33, 2);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('Multimedia', 33, 3);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('e', 33, 3);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('gamma', 33, 3);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('lambda', 33, 3);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('pi', 33, 3);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('A2', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('C310', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('C335', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('C510', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('C512', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L534', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L001', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L002', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L301', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L302', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L306', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L307', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L308', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L320', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('321', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L336', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L338', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L339', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L343', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L402', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L404', 33, 4);
INSERT INTO sala (nume, capacitate, cladire_id) VALUES ('L439', 33, 4);


-- Roluri useri
INSERT INTO rol (name) VALUES ('ADMIN');
INSERT INTO rol (name) VALUES ('STUDENT');
INSERT INTO rol (name) VALUES ('PROFESOR');

-- Useri
INSERT INTO user (username, password, rol_id) VALUES ('admin1', '$2a$12$G0OuquMudPvJljVVMUEXLeO18MYnikPuun6S3OE97AoVWGMt.oCrm', 1); -- Parola: password1

