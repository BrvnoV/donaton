package cl.duoc.donaton.ms.service;

import cl.duoc.donaton.ms.factory.CampanaFactory;
import cl.duoc.donaton.ms.model.Campana;
import cl.duoc.donaton.ms.repository.CampanaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CampanaService {

    @Autowired
    private CampanaRepository campanaRepository;

    public List<Campana> listarTodas() {
        log.info("Buscando todas las campañas en base de datos");
        return campanaRepository.findAll();
    }

    public Optional<Campana> obtenerPorId(Long id) {
        log.info("Buscando campaña con ID: {}", id);
        return campanaRepository.findById(id);
    }

    public Campana crearCampana(Campana campanaInput) {
        log.info("Creando nueva campaña mediante el Factory: {}", campanaInput.getNombre());
        Campana nuevaCampana = CampanaFactory.crearCampanaNueva(campanaInput.getNombre(), campanaInput.getDescripcion());
        Campana guardada = campanaRepository.save(nuevaCampana);
        log.info("Nueva campaña persistida exitosamente con ID: {}", guardada.getId());
        return guardada;
    }

    public Campana actualizarEstado(Long id, String nuevoEstado) {
        log.info("Intentando actualizar el estado de campaña ID: {} a: {}", id, nuevoEstado);
        Campana campana = campanaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al actualizar campaña: no existe la campaña con ID: {}", id);
                    return new IllegalArgumentException("Campaña no encontrada");
                });
        campana.setEstado(nuevoEstado.toUpperCase());
        Campana actualizada = campanaRepository.save(campana);
        log.info("Campaña ID: {} actualizada exitosamente al estado: {}", id, actualizada.getEstado());
        return actualizada;
    }

    public Campana actualizarCampana(Long id, Campana datosActualizados) {
        log.info("Intentando actualizar los datos de la campaña ID: {}", id);
        Campana campana = campanaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al actualizar campaña: no existe la campaña con ID: {}", id);
                    return new IllegalArgumentException("Campaña no encontrada");
                });
        campana.setNombre(datosActualizados.getNombre());
        campana.setDescripcion(datosActualizados.getDescripcion());
        // Puedes agregar más campos aquí si es necesario

        Campana actualizada = campanaRepository.save(campana);
        log.info("Campaña ID: {} actualizada exitosamente", id);
        return actualizada;
    }

    public void eliminarCampana(Long id) {
        log.info("Intentando eliminar físicamente la campaña ID: {}", id);
        if (!campanaRepository.existsById(id)) {
            log.error("Error al eliminar campaña: no existe la campaña con ID: {}", id);
            throw new IllegalArgumentException("Campaña no encontrada");
        }
        campanaRepository.deleteById(id);
        log.info("Campaña ID: {} eliminada exitosamente", id);
    }
}
