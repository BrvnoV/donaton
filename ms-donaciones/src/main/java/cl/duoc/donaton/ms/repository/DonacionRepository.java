package cl.duoc.donaton.ms.repository;

import cl.duoc.donaton.ms.model.Donacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {
    
    List<Donacion> findByTipoRecurso(String tipoRecurso);
    List<Donacion> findByCampanaId(Long campanaId);
}