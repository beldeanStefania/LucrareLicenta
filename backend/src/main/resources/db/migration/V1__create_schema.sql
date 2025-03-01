-- Create table for buildings (cladire)
CREATE TABLE IF NOT EXISTS cladire (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(255) NOT NULL,
    adresa VARCHAR(255) NOT NULL,
    UNIQUE KEY uk_cladire (nume, adresa)
);

-- Create table for rooms (sala)
CREATE TABLE IF NOT EXISTS sala (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(255) NOT NULL,
    capacitate INT,
    cladire_id BIGINT,
    CONSTRAINT fk_sala_cladire FOREIGN KEY (cladire_id) REFERENCES cladire(id),
    UNIQUE KEY uk_sala (nume, cladire_id)
);

-- Create table for roles (rol)
CREATE TABLE IF NOT EXISTS rol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create table for users (user)
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol_id BIGINT,
    CONSTRAINT fk_user_rol FOREIGN KEY (rol_id) REFERENCES rol(id)
);
