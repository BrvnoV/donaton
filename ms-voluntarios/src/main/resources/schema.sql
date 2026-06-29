CREATE TABLE IF NOT EXISTS voluntarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    contacto VARCHAR(255) NOT NULL,
    correo VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS campanas_voluntarios (
    voluntario_id BIGINT NOT NULL,
    campana_id BIGINT NOT NULL,
    FOREIGN KEY (voluntario_id) REFERENCES voluntarios(id) ON DELETE CASCADE
);
