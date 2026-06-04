package cl.duoc.donaton.ms.service;

import cl.duoc.donaton.ms.model.Necesidad;
import cl.duoc.donaton.ms.repository.NecesidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NecesidadService {

    @Autowired
    private NecesidadRepository necesidadRepository;

    /**
     * Obtiene el catastro completo de necesidades registradas desde MySQL.
     */
    public List<Necesidad> listarTodas() {
        return necesidadRepository.findAll();
    }

    /**
     * Registra una nueva solicitud de necesidad en el sistema.
     */
    public Necesidad guardar(Necesidad necesidad) {
        return necesidadRepository.save(necesidad);
    }
}