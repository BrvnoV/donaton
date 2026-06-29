package cl.duoc.donaton.ms.service;

import cl.duoc.donaton.ms.adapter.CampanaAdapter;
import cl.duoc.donaton.ms.model.Donacion;
import cl.duoc.donaton.ms.repository.DonacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DonacionService {

    @Autowired
    private DonacionRepository donacionRepository;

    @Autowired
    private CampanaAdapter campanaAdapter;

    /**
     * Obtiene todos los registros guardados en la tabla de MySQL.
     */
    public List<Donacion> listarTodas() {
        log.info("Buscando todas las donaciones en base de datos");
        return donacionRepository.findAll();
    }

    /**
     * Guarda una nueva donación y procesa el @PrePersist de la fecha.
     */
    public Donacion guardar(Donacion donacion) {
        log.info("Procesando registro de donación para campaña ID: {}", donacion.getCampanaId());
        
        if (donacion.getCantidad() != null && donacion.getCantidad() <= 0) {
            throw new cl.duoc.donaton.ms.exception.InvalidResourceQuantityException("La cantidad de la donación debe ser mayor a cero");
        }
        
        if (donacion.getCampanaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de campaña no puede ser nulo");
        }
        
        String estado = campanaAdapter.obtenerEstadoCampana(donacion.getCampanaId());
        if ("COMPLETED".equalsIgnoreCase(estado) || "CANCELLED".equalsIgnoreCase(estado)) {
            log.error("Campaña {} terminada. Freno por campaña finalizada.", estado);
            throw new cl.duoc.donaton.ms.exception.ClosedCampaignException("La campaña seleccionada se encuentra " + estado);
        } else if ("UNKNOWN".equalsIgnoreCase(estado)) {
            log.error("Validación de campaña fallida: Campaña ID: {} no existe", donacion.getCampanaId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La campaña seleccionada no existe");
        }
        
        Donacion guardada = donacionRepository.save(donacion);
        log.info("Donación guardada con éxito con ID: {} para campaña ID: {}", guardada.getId(), guardada.getCampanaId());
        return guardada;
    }

    /**
     * Obtiene donaciones de una campaña específica.
     */
    public List<Donacion> listarPorCampana(Long campanaId) {
        log.info("Buscando donaciones para campaña ID: {}", campanaId);
        return donacionRepository.findByCampanaId(campanaId);
    }

    /**
     * Actualiza el estado logístico de una donación.
     */
    public Optional<Donacion> actualizarEstadoLogistico(Long id, String nuevoEstado) {
        log.info("Actualizando estado logístico de donación ID: {} a: {}", id, nuevoEstado);
        return donacionRepository.findById(id).map(donacion -> {
            donacion.setEstadoLogistico(nuevoEstado.toUpperCase());
            Donacion actualizada = donacionRepository.save(donacion);
            log.info("Estado logístico de donación ID: {} actualizado a: {}", id, actualizada.getEstadoLogistico());
            return actualizada;
        });
    }
}