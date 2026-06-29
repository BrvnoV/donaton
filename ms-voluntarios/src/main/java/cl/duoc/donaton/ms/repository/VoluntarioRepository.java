package cl.duoc.donaton.ms.repository;

import cl.duoc.donaton.ms.model.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    Optional<Voluntario> findByCorreo(String correo);

    @Query("SELECT v FROM Voluntario v JOIN v.campanasIds c WHERE c = :campanaId")
    List<Voluntario> findByCampanaId(@Param("campanaId") Long campanaId);

    boolean existsByCorreo(String correo);
}
