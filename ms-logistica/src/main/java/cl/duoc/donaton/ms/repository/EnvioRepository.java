package cl.duoc.donaton.ms.repository;

import cl.duoc.donaton.ms.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    
    // Método personalizado automático para buscar envíos según su estado logístico
    List<Envio> findByEstadoEnvio(String estadoEnvio);
}