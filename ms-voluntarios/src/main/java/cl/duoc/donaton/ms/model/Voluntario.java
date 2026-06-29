package cl.duoc.donaton.ms.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "voluntarios")
@Data
public class Voluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String contacto;

    @Column(nullable = false, unique = true)
    @jakarta.validation.constraints.NotBlank(message = "El correo es obligatorio")
    @jakarta.validation.constraints.Email(message = "El formato de correo es inválido")
    private String correo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "campanas_voluntarios", joinColumns = @JoinColumn(name = "voluntario_id"))
    @Column(name = "campana_id")
    private Set<Long> campanasIds = new HashSet<>();
}
