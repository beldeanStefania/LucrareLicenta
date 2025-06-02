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
                                      email VARCHAR(255) NOT NULL UNIQUE,
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
                                       nume VARCHAR(255) NOT NULL,
                                       semestru INT NOT NULL,
                                       an INT NOT NULL,
                                       cod VARCHAR(255) NOT NULL,
                                       credite INT NOT NULL,
                                       CONSTRAINT uk_materie UNIQUE (nume)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS specializare (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            specializare VARCHAR(255) NOT NULL,
                                            CONSTRAINT uk_specializare UNIQUE (specializare)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS student (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       cod VARCHAR(255),
                                       nume VARCHAR(255),
                                       prenume VARCHAR(255),
                                       an INT,
                                       grupa VARCHAR(255),
                                       user_id INT NOT NULL,
                                       specializare_id INT,
                                       CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES `user`(id),
                                       CONSTRAINT fk_student_specializare FOREIGN KEY (specializare_id) REFERENCES specializare(id)
                                       ON DELETE SET NULL
                                       ON UPDATE CASCADE,
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
                                                       status VARCHAR(20) NOT NULL,
                                                       student_id INT NOT NULL,
                                                       materie_id INT NOT NULL,
                                                       CONSTRAINT fk_catalog_student_materie_student FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
                                                           ON UPDATE CASCADE,
                                                       CONSTRAINT fk_catalog_student_materie_materie FOREIGN KEY (materie_id) REFERENCES materie(id) ON DELETE CASCADE
                                                           ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS materii_optionale (
                                                 id   INT AUTO_INCREMENT PRIMARY KEY,
                                                 nume VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `contract` (
                                          `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          `student_cod` VARCHAR(255) NOT NULL,
                                          `an_contract` INT NOT NULL,
                                          `data_generare` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `contract_materii` (
                                                  `contract_id` BIGINT NOT NULL,
                                                  `materie_cod` VARCHAR(255) NOT NULL,
                                                  CONSTRAINT `fk_contract_materii_contract`
                                                      FOREIGN KEY (`contract_id`) REFERENCES `contract`(`id`)
                                                          ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS curriculum_entry (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                specializare_id INT NOT NULL,
                                                materie_id    INT NOT NULL,
                                                an            INT NOT NULL,
                                                semestru      INT NOT NULL,
                                                tip           VARCHAR(255),
                                                materii_optionale_id INT NULL,

                                                CONSTRAINT fk_curriculum_entry_specializare
                                                    FOREIGN KEY (specializare_id) REFERENCES specializare(id),

                                                CONSTRAINT fk_curriculum_entry_materie
                                                    FOREIGN KEY (materie_id)    REFERENCES materie(id),

                                                CONSTRAINT fk_curriculum_entry_optionale
                                                    FOREIGN KEY (materii_optionale_id) REFERENCES materii_optionale(id)
                                            ON DELETE RESTRICT ON UPDATE CASCADE

) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `todo_item` (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           title VARCHAR(255) NOT NULL,
                                           description TEXT NULL,
                                           deadline DATE NOT NULL,
                                           done TINYINT(1) NOT NULL DEFAULT 0,
                                           student_id INT NOT NULL,
                                           CONSTRAINT fk_todo_student FOREIGN KEY (student_id) REFERENCES student(id)
) ENGINE=InnoDB;