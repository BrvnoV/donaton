CREATE TABLE IF NOT EXISTS donaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campana_id BIGINT NOT NULL,
    tipo_recurso VARCHAR(255) NOT NULL,
    cantidad DOUBLE NOT NULL,
    donante VARCHAR(255) NOT NULL,
    estado_logistico VARCHAR(50),
    fecha_registro TIMESTAMP
);
