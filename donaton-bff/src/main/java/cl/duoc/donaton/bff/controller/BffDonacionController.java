package cl.duoc.donaton.bff.controller;

import cl.duoc.donaton.bff.service.BffDonacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/bff/donaciones")
@CrossOrigin(origins = "*") // Permite conectar con el frontend sin problemas de CORS
@Slf4j
public class BffDonacionController {

    @Autowired
    private BffDonacionService bffDonacionService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listarDonaciones() {
        log.info("BFF: Solicitud para listar todas las donaciones");
        return bffDonacionService.obtenerTodasLasDonaciones()
                .map(donaciones -> {
                    log.info("BFF: Donaciones listadas con éxito, cantidad: {}", donaciones.size());
                    return ResponseEntity.ok((Object) donaciones);
                });
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> registrarDonacion(@RequestBody Object nuevaDonacion) {
        log.info("BFF: Solicitud para registrar nueva donación");
        return bffDonacionService.crearDonacion(nuevaDonacion)
                .map(resultado -> {
                    if (resultado instanceof Map && ((Map<?, ?>) resultado).containsKey("error")) {
                        log.warn("BFF: Error al crear donación: {}", ((Map<?, ?>) resultado).get("error"));
                        return ResponseEntity.status(400).body(resultado);
                    }
                    log.info("BFF: Donación creada con éxito");
                    return ResponseEntity.status(201).body(resultado);
                });
    }

    @PutMapping("/{id}/estado")
    public Mono<ResponseEntity<Object>> actualizarEstadoLogistico(@PathVariable Long id, @RequestBody Map<String, String> body) {
        log.info("BFF: Solicitud para actualizar estado logístico de donación ID: {} a {}", id, body.get("estadoLogistico"));
        return bffDonacionService.actualizarEstadoLogistico(id, body)
                .map(resultado -> {
                    if (resultado instanceof Map && ((Map<?, ?>) resultado).containsKey("error")) {
                        log.warn("BFF: Error al actualizar estado logístico de donación ID {}: {}", id, ((Map<?, ?>) resultado).get("error"));
                        return ResponseEntity.status(400).body(resultado);
                    }
                    log.info("BFF: Estado logístico de donación ID {} actualizado con éxito", id);
                    return ResponseEntity.ok(resultado);
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Object>> eliminarDonacion(@PathVariable Long id) {
        log.info("BFF: Solicitud para eliminar donación ID: {}", id);
        return bffDonacionService.eliminarDonacion(id)
                .map(resultado -> {
                    log.info("BFF: Donación ID {} eliminada con éxito", id);
                    return ResponseEntity.ok(resultado);
                });
    }
}