-- Crear la base de datos
CREATE
DATABASE IF NOT EXISTS barberia;
USE
barberia;

-- Tabla Barbero
CREATE TABLE barbero
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nombre   VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    telefono VARCHAR(15),
    email    VARCHAR(100)
);

-- Tabla Cliente
CREATE TABLE cliente
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    nombre   VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    telefono VARCHAR(15),
    email    VARCHAR(100)
);

-- Tabla Servicio
CREATE TABLE servicio
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50)    NOT NULL,
    descripcion VARCHAR(255),
    precio      DECIMAL(10, 2) NOT NULL,
    duracion    INT            NOT NULL -- duración en minutos
);

-- Tabla Cita
CREATE TABLE cita
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    barbero_id  INT      NOT NULL,
    cliente_id  INT      NOT NULL,
    servicio_id INT      NOT NULL,
    fecha_hora  DATETIME NOT NULL,
    estado      VARCHAR(20) DEFAULT 'pendiente',
    notas       VARCHAR(255),
    CONSTRAINT fk_barbero FOREIGN KEY (barbero_id) REFERENCES barbero (id),
    CONSTRAINT fk_cliente FOREIGN KEY (cliente_id) REFERENCES cliente (id),
    CONSTRAINT fk_servicio FOREIGN KEY (servicio_id) REFERENCES servicio (id)
);
