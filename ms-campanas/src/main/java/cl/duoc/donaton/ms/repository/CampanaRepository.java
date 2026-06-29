package cl.duoc.donaton.ms.repository;

import cl.duoc.donaton.ms.model.Campana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampanaRepository extends JpaRepository<Campana, Long> {
}
