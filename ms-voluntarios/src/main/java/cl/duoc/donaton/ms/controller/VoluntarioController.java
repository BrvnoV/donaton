package cl.duoc.donaton.ms.controller;

import jakarta.validation.Valid;

import cl.duoc.donaton.ms.model.Voluntario;
import cl.duoc.donaton.ms.service.VoluntarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/voluntarios")
@Slf4j
public class VoluntarioController {

    @Autowired
    private VoluntarioService voluntarioService;

    @GetMapping
    public List<Voluntario> listar() {
        log.info("Petición GET para listar todos los voluntarios");
        List<Voluntario> resultado = voluntarioService.listarTodos();
        log.info("Voluntarios listados con éxito, total={}", resultado.size());
        return resultado;
    }

    @PostMapping
    public ResponseEntity<Voluntario> registrar(@RequestBody Map<String, Object> payload) {
        String nombre = (String) payload.get("nombre");
        String contacto = (String) payload.get("contacto");
        String correo = (String) payload.get("correo");
        Number campanaIdNum = (Number) payload.get("campanaId");

        log.info("Petición POST para registrar voluntario con correo: {}, campaña ID: {}", correo, campanaIdNum);

        if (nombre == null || contacto == null || correo == null || campanaIdNum == null) {
            log.warn("Petición POST errónea: Faltan campos requeridos en el payload");
            return ResponseEntity.badRequest().build();
        }

        Voluntario voluntario = new Voluntario();
        voluntario.setNombre(nombre);
        voluntario.setContacto(contacto);
        voluntario.setCorreo(correo);

        try {
            Voluntario guardado = voluntarioService.registrarEnCampana(voluntario, campanaIdNum.longValue());
            log.info("Voluntario registrado y asociado con éxito: ID={}", guardado.getId());
            return ResponseEntity.status(201).body(guardado);
        } catch (Exception e) {
            log.error("Fallo al registrar el voluntario con correo: {} en campaña: {}: {}", correo, campanaIdNum, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/campana/{campanaId}")
    public List<Voluntario> obtenerPorCampana(@PathVariable Long campanaId) {
        log.info("Petición GET para obtener voluntarios de campaña ID: {}", campanaId);
        List<Voluntario> resultado = voluntarioService.obtenerPorCampana(campanaId);
        log.info("Voluntarios de campaña ID: {} listados con éxito, total={}", campanaId, resultado.size());
        return resultado;
    }
}
