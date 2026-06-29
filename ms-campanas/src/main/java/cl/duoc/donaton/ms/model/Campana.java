package cl.duoc.donaton.ms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "campanas")
@Data
public class Campana {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotBlank(message = "El nombre de la campaña es obligatorio")
    private String nombre;

    @Column(nullable = false)
    @jakarta.validation.constraints.NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @Column(nullable = false)
    private String estado; // PLANNED, ACTIVE, COMPLETED, CANCELLED

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_inicio")
    @jakarta.validation.constraints.FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @jakarta.validation.constraints.AssertTrue(message = "La fecha de fin debe ser posterior a la fecha de inicio")
    private boolean isFechaValida() {
        if (fechaInicio == null || fechaFin == null) return true; // validado por @NotNull si aplica
        return fechaFin.isAfter(fechaInicio);
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = "PLANNED";
        }
    }
}
