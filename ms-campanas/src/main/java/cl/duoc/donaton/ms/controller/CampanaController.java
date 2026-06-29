package cl.duoc.donaton.ms.controller;

import jakarta.validation.Valid;

import cl.duoc.donaton.ms.model.Campana;
import cl.duoc.donaton.ms.service.CampanaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/campanas")
@Slf4j
public class CampanaController {

    @Autowired
    private CampanaService campanaService;

    @GetMapping
    public List<Campana> listar() {
        log.info("Petición GET para listar todas las campañas");
        List<Campana> resultado = campanaService.listarTodas();
        log.info("Campañas listadas con éxito: total={}", resultado.size());
        return resultado;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campana> obtener(@PathVariable Long id) {
        log.info("Petición GET para obtener campaña ID: {}", id);
        return campanaService.obtenerPorId(id)
                .map(campana -> {
                    log.info("Campaña encontrada con ID: {}", id);
                    return ResponseEntity.ok(campana);
                })
                .orElseGet(() -> {
                    log.warn("Campaña no encontrada con ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ResponseEntity<Campana> crear(@Valid @RequestBody Campana campana) {
        log.info("Petición POST para crear campaña: {}", campana.getNombre());
        Campana nueva = campanaService.crearCampana(campana);
        log.info("Campaña creada con éxito: ID={}", nueva.getId());
        return ResponseEntity.status(201).body(nueva);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Campana> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        log.info("Petición PUT para actualizar estado de campaña ID: {} a: {}", id, nuevoEstado);
        if (nuevoEstado == null) {
            log.warn("Petición PUT errónea: estado es nulo");
            return ResponseEntity.badRequest().build();
        }
        try {
            Campana actualizada = campanaService.actualizarEstado(id, nuevoEstado);
            log.info("Estado de campaña ID: {} actualizado con éxito a: {}", id, nuevoEstado);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al intentar cambiar el estado de la campaña ID: {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<Map<String, Object>> obtenerEstado(@PathVariable Long id) {
        log.info("Petición GET para validar estado de campaña ID: {}", id);
        Optional<Campana> campanaOpt = campanaService.obtenerPorId(id);
        if (campanaOpt.isPresent()) {
            Campana campana = campanaOpt.get();
            boolean valida = !"COMPLETED".equalsIgnoreCase(campana.getEstado());
            log.info("Validación de campaña ID: {}: existe=true, estado={}, valida={}", id, campana.getEstado(), valida);
            return ResponseEntity.ok(Map.of("existe", true, "estado", campana.getEstado(), "valida", valida));
        } else {
            log.warn("Validación de campaña ID: {}: existe=false", id);
            return ResponseEntity.ok(Map.of("existe", false, "valida", false));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campana> actualizar(@PathVariable Long id, @Valid @RequestBody Campana campana) {
        log.info("Petición PUT para actualizar campaña ID: {}", id);
        try {
            Campana actualizada = campanaService.actualizarCampana(id, campana);
            return ResponseEntity.ok(actualizada);
        } catch (IllegalArgumentException e) {
            log.error("Error al actualizar la campaña ID: {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Petición DELETE para eliminar campaña ID: {}", id);
        try {
            campanaService.eliminarCampana(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error al eliminar la campaña ID: {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
