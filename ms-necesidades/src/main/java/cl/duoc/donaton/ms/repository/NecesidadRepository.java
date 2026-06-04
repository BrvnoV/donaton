package cl.duoc.donaton.ms.repository;

import cl.duoc.donaton.ms.model.Necesidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NecesidadRepository extends JpaRepository<Necesidad, Long> {
    
    // Método personalizado para buscar necesidades urgentes por prioridad
    List<Necesidad> findByPrioridad(String prioridad);
}