package cl.duoc.donaton.bff.controller;

import cl.duoc.donaton.bff.service.BffCampanaService;
import cl.duoc.donaton.bff.service.BffVoluntarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bff/campanas")
@CrossOrigin(origins = "*")
@Slf4j
public class BffCampanaController {

    @Autowired
    private BffCampanaService bffCampanaService;

    @Autowired
    private BffVoluntarioService bffVoluntarioService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listarCampanas() {
        log.info("BFF: Solicitud para listar todas las campañas");
        return bffCampanaService.listarTodasLasCampanas()
                .map(campanas -> {
                    log.info("BFF: Campañas listadas con éxito, cantidad: {}", campanas.size());
                    return ResponseEntity.ok((Object) campanas);
                });
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Object>> obtenerCampana(@PathVariable Long id) {
        log.info("BFF: Solicitud para obtener campaña por ID: {}", id);
        return bffCampanaService.obtenerCampanaPorId(id)
                .map(campana -> {
                    log.info("BFF: Campaña con ID {} obtenida con éxito", id);
                    return ResponseEntity.ok(campana);
                });
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> crearCampana(@RequestBody Object nuevaCampana) {
        log.info("BFF: Solicitud para crear nueva campaña");
        return bffCampanaService.crearCampana(nuevaCampana)
                .map(resultado -> {
                    if (resultado instanceof Map && ((Map<?, ?>) resultado).containsKey("error")) {
                        log.error("BFF: Error al crear campaña: {}", resultado);
                        return ResponseEntity.status(400).body(resultado);
                    }
                    log.info("BFF: Campaña creada con éxito");
                    return ResponseEntity.status(201).body(resultado);
                });
    }

    @PutMapping("/{id}/estado")
    public Mono<ResponseEntity<Object>> actualizarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        log.info("BFF: Solicitud para actualizar estado de campaña ID: {} a {}", id, body.get("estado"));
        return bffCampanaService.actualizarEstadoCampana(id, body)
                .map(resultado -> {
                    log.info("BFF: Estado de campaña ID: {} actualizado con éxito", id);
                    return ResponseEntity.ok(resultado);
                });
    }

    @GetMapping("/resumen")
    public Mono<ResponseEntity<Object>> obtenerResumen() {
        log.info("BFF: Solicitud para obtener resumen de campañas");
        return bffCampanaService.obtenerResumenCampanas()
                .map(resumen -> {
                    log.info("BFF: Resumen de campañas obtenido con éxito, cantidad de campañas activas: {}", resumen.size());
                    return ResponseEntity.ok((Object) resumen);
                });
    }

    @GetMapping("/{id}/voluntarios")
    public Mono<ResponseEntity<Object>> obtenerVoluntarios(@PathVariable Long id) {
        log.info("BFF: Solicitud para obtener voluntarios de campaña ID: {}", id);
        Mono<Object> campanaMono = bffCampanaService.obtenerCampanaPorId(id);
        Mono<List> voluntariosMono = bffVoluntarioService.obtenerVoluntariosPorCampana(id);

        return Mono.zip(campanaMono, voluntariosMono, (campana, voluntarios) -> {
            log.info("BFF: Voluntarios y campaña ID: {} combinados con éxito", id);
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("campana", campana);
            result.put("voluntarios", voluntarios);
            return (Object) result;
        }).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Object>> actualizarCampana(@PathVariable Long id, @RequestBody Object body) {
        log.info("BFF: Solicitud para actualizar campaña ID: {}", id);
        return bffCampanaService.actualizarCampana(id, body)
                .map(resultado -> {
                    log.info("BFF: Campaña ID: {} actualizada con éxito", id);
                    return ResponseEntity.ok(resultado);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> eliminarCampana(@PathVariable Long id) {
        log.info("BFF: Solicitud para eliminar campaña ID: {}", id);
        return bffCampanaService.eliminarCampana(id)
                .map(resultado -> {
                    log.info("BFF: Campaña ID: {} eliminada con éxito", id);
                    return ResponseEntity.ok(resultado);
                });
    }
}
