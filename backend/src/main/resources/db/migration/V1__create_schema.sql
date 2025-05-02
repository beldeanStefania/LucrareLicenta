CREATE DATABASE IF NOT EXISTS orar CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE orar;

CREATE TABLE IF NOT EXISTS rol (
                                   id INT AUTO_INCREMENT PRIMARY KEY,
                                   name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `user` (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      username VARCHAR(255) NOT NULL UNIQUE,
                                      password VARCHAR(255) NOT NULL,
                                      rol_id INT NOT NULL,
                                      CONSTRAINT fk_user_rol FOREIGN KEY (rol_id) REFERENCES rol(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS cladire (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       nume VARCHAR(255) NOT NULL,
                                       adresa VARCHAR(255) NOT NULL,
                                       UNIQUE KEY uk_cladire (nume, adresa)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS sala (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    nume VARCHAR(255) NOT NULL,
                                    capacitate INT,
                                    cladire_id INT NOT NULL,
                                    UNIQUE KEY uk_sala (nume, cladire_id),
                                    CONSTRAINT fk_sala_cladire FOREIGN KEY (cladire_id) REFERENCES cladire(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS materie (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       nume VARCHAR(255),
                                       semestru INT,
                                       cod VARCHAR(255),
                                       credite INT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS student (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       cod VARCHAR(255),
                                       nume VARCHAR(255),
                                       prenume VARCHAR(255),
                                       an INT,
                                       grupa VARCHAR(255),
                                       user_id INT NOT NULL,
                                       CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES `user`(id),
                                       UNIQUE KEY uk_student_user (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS profesor (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        nume VARCHAR(255),
                                        prenume VARCHAR(255),
                                        user_id INT NOT NULL,
                                        CONSTRAINT fk_profesor_user FOREIGN KEY (user_id) REFERENCES `user`(id),
                                        UNIQUE KEY uk_profesor_user (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS repartizare_prof (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                tip VARCHAR(255),
                                                profesor_id INT NOT NULL,
                                                materie_id INT NOT NULL,
                                                CONSTRAINT fk_repartizare_prof_profesor FOREIGN KEY (profesor_id) REFERENCES profesor(id),
                                                CONSTRAINT fk_repartizare_prof_materie FOREIGN KEY (materie_id) REFERENCES materie(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS orar (
                                    id INT AUTO_INCREMENT PRIMARY KEY,
                                    formatia VARCHAR(255),
                                    ora_inceput INT NOT NULL,
                                    ora_sfarsit INT NOT NULL,
                                    grupa VARCHAR(255),
                                    zi VARCHAR(255),
                                    frecventa VARCHAR(255),
                                    sala_id INT NOT NULL,
                                    repartizare_prof_id INT NOT NULL,
                                    CONSTRAINT fk_orar_sala FOREIGN KEY (sala_id) REFERENCES sala(id),
                                    CONSTRAINT fk_orar_repartizare_prof FOREIGN KEY (repartizare_prof_id) REFERENCES repartizare_prof(id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS catalog_student_materie (
                                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                                       nota DOUBLE,
                                                       semestru INT,
                                                       student_id INT NOT NULL,
                                                       materie_id INT NOT NULL,
                                                       CONSTRAINT fk_catalog_student_materie_student FOREIGN KEY (student_id) REFERENCES student(id),
                                                       CONSTRAINT fk_catalog_student_materie_materie FOREIGN KEY (materie_id) REFERENCES materie(id)
) ENGINE=InnoDB;

-- 1. Creăm mai întâi specializarea, căci curriculum_entry o va referenția
CREATE TABLE IF NOT EXISTS specializare (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            nume VARCHAR(255) NOT NULL,
                                            UNIQUE KEY uk_specializare (nume)
) ENGINE=InnoDB;

-- 2. Apoi curriculum_entry, cu coloanele FK și constrângerile aferente
CREATE TABLE IF NOT EXISTS curriculum_entry (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                specializare_id INT NOT NULL,
                                                materie_id    INT NOT NULL,
                                                an            INT NOT NULL,
                                                semestru      INT NOT NULL,
                                                tip           VARCHAR(255),

                                                CONSTRAINT fk_curriculum_entry_specializare
                                                    FOREIGN KEY (specializare_id) REFERENCES specializare(id),

                                                CONSTRAINT fk_curriculum_entry_materie
                                                    FOREIGN KEY (materie_id)    REFERENCES materie(id)
) ENGINE=InnoDB;
