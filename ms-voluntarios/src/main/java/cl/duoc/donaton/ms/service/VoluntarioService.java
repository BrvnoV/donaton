package cl.duoc.donaton.ms.service;

import cl.duoc.donaton.ms.adapter.CampanaAdapter;
import cl.duoc.donaton.ms.model.Voluntario;
import cl.duoc.donaton.ms.repository.VoluntarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private CampanaAdapter campanaAdapter;

    public List<Voluntario> listarTodos() {
        log.info("Buscando todos los voluntarios en base de datos");
        return voluntarioRepository.findAll();
    }

    public Voluntario registrarEnCampana(Voluntario voluntarioInput, Long campanaId) {
        log.info("Iniciando inscripción de voluntario con correo: {} en campaña ID: {}", voluntarioInput.getCorreo(), campanaId);
        // Validar campaña usando el Adapter
        if (!campanaAdapter.esCampanaValida(campanaId)) {
            log.error("Validación de campaña fallida para inscripción: campaña ID: {} no existe o está COMPLETED", campanaId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La campaña seleccionada no existe o está completada");
        }

        // Buscar si ya existe por correo
        Optional<Voluntario> existente = voluntarioRepository.findByCorreo(voluntarioInput.getCorreo());
        if (existente.isPresent()) {
            Voluntario vol = existente.get();
            // Verificar si ya está inscrito en esta campaña específica
            if (vol.getCampanasIds().contains(campanaId)) {
                log.error("Freno de persistencia: El correo {} ya está inscrito en la campaña ID: {}", voluntarioInput.getCorreo(), campanaId);
                throw new cl.duoc.donaton.ms.exception.DuplicateVolunteerException("Este correo ya está inscrito en esta campaña");
            }
            // Si existe pero en otra campaña, agregar esta campaña
            log.info("Voluntario con correo {} ya existe, agregando campaña ID: {}", voluntarioInput.getCorreo(), campanaId);
            vol.getCampanasIds().add(campanaId);
            Voluntario actualizado = voluntarioRepository.save(vol);
            log.info("Voluntario actualizado con nueva campaña. ID: {}", actualizado.getId());
            return actualizado;
        }
        
        log.info("Registrando voluntario nuevo con correo: {}", voluntarioInput.getCorreo());
        Voluntario voluntario = new Voluntario();
        voluntario.setNombre(voluntarioInput.getNombre());
        voluntario.setContacto(voluntarioInput.getContacto());
        voluntario.setCorreo(voluntarioInput.getCorreo());
        
        voluntario.getCampanasIds().add(campanaId);
        Voluntario guardado = voluntarioRepository.save(voluntario);
        log.info("Inscripción de voluntario completada con éxito. ID: {}", guardado.getId());
        return guardado;
    }

    public List<Voluntario> obtenerPorCampana(Long campanaId) {
        log.info("Obteniendo voluntarios de campaña ID: {}", campanaId);
        return voluntarioRepository.findByCampanaId(campanaId);
    }
}
