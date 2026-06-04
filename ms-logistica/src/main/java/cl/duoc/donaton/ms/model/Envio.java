package cl.duoc.donaton.ms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "envios_logistica")
@Data
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "donacion_id")
    private Long donacionId; // Relación lógica con la donación que se está despachando

    @Column(nullable = false)
    private String destino; // Comuna, albergue o centro de acopio de destino

    @Column(nullable = false)
    private String transportista; // Nombre de la empresa, institución o voluntario

    @Column(name = "estado_envio")
    private String estadoEnvio; // Ejemplo: PREPARANDO, EN_RUTA, ENTREGADO

    @Column(name = "fecha_despacho")
    private LocalDateTime fechaDespacho;

    @PrePersist
    protected void onCreate() {
        this.fechaDespacho = LocalDateTime.now();
        if (this.estadoEnvio == null) {
            this.estadoEnvio = "PREPARANDO";
        }
    }
}