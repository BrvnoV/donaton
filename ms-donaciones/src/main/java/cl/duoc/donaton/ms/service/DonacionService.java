package cl.duoc.donaton.ms.service;

import cl.duoc.donaton.ms.model.Donacion;
import cl.duoc.donaton.ms.repository.DonacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DonacionService {

    @Autowired
    private DonacionRepository donacionRepository;

    /**
     * Obtiene todos los registros guardados en la tabla de MySQL.
     */
    public List<Donacion> listarTodas() {
        return donacionRepository.findAll();
    }

    /**
     * Guarda una nueva donación y procesa el @PrePersist de la fecha.
     */
    public Donacion guardar(Donacion donacion) {
        return donacionRepository.save(donacion);
    }
}