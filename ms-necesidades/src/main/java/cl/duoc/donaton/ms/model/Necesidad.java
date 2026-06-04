package cl.duoc.donaton.ms.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "necesidades_solicitadas")
@Data
public class Necesidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion; // Ej: "Alimentos no perecibles", "Artículos de aseo"

    @Column(nullable = false)
    private Integer cantidadRequerida;

    @Column(name = "cantidad_cubierta")
    private Integer cantidadCubierta; // Cuánto de esa necesidad ya se ha suplido con donaciones

    @Column(nullable = false)
    private String prioridad; // Ej: ALTA, MEDIA, BAJA

    @Column(nullable = false)
    private String institucionSolicitante; // Ej: "Albergue Central", "Municipalidad"

    @PrePersist
    protected void onCreate() {
        if (this.cantidadCubierta == null) {
            this.cantidadCubierta = 0;
        }
    }
}