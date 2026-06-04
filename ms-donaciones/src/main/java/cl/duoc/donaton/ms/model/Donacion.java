package cl.duoc.donaton.ms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "donaciones")
@Data // Genera automáticamente getters, setters, equals, y toString gracias a Lombok
public class Donacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "tipo_recurso")
    private String tipoRecurso; // Ejemplo: ALIMENTOS, ROPA, DINERO, MEDICAMENTOS

    @Column(nullable = false)
    private Double cantidad;

    @Column(nullable = false)
    private String donante; // Nombre de la persona o institución que dona

    @Column(name = "estado_logistico")
    private String estadoLogistico; // Ejemplo: EN_ACOPIO, EN_TRANSITO, ENTREGADO

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.estadoLogistico == null) {
            this.estadoLogistico = "EN_ACOPIO"; // Estado por defecto al registrarse
        }
    }
}