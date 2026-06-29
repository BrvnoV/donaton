package cl.duoc.donaton.ms.controller;

import jakarta.validation.Valid;

import cl.duoc.donaton.ms.model.Donacion;
import cl.duoc.donaton.ms.service.DonacionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/donaciones") // ◄ Esta ruta empalma exacto con la URI del WebClient en el BFF
@Slf4j
public class DonacionController {

    @Autowired
    private DonacionService donacionService;

    @GetMapping
    public ResponseEntity<List<Donacion>> obtenerDonaciones() {
        log.info("Petición GET para listar todas las donaciones");
        List<Donacion> donaciones = donacionService.listarTodas();
        log.info("Donaciones obtenidas exitosamente, total={}", donaciones.size());
        return ResponseEntity.ok(donaciones);
    }

    @PostMapping
    public ResponseEntity<Donacion> crearDonacion(@Valid @RequestBody Donacion donacion) {
        log.info("Petición POST para registrar donación por el donante: {}, campaña ID: {}", donacion.getDonante(), donacion.getCampanaId());
        try {
            Donacion nuevaDonacion = donacionService.guardar(donacion);
            log.info("Donación registrada con éxito: ID={}", nuevaDonacion.getId());
            return ResponseEntity.status(201).body(nuevaDonacion);
        } catch (Exception e) {
            log.error("Fallo al registrar la donación: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/campana/{campanaId}")
    public ResponseEntity<List<Donacion>> obtenerPorCampana(@PathVariable Long campanaId) {
        log.info("Petición GET para listar donaciones de campaña ID: {}", campanaId);
        List<Donacion> donaciones = donacionService.listarPorCampana(campanaId);
        log.info("Donaciones de campaña ID: {} obtenidas exitosamente, total={}", campanaId, donaciones.size());
        return ResponseEntity.ok(donaciones);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Donacion> actualizarEstadoLogistico(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estadoLogistico");
        log.info("Petición PUT para actualizar estado logístico de donación ID: {} a: {}", id, nuevoEstado);
        if (nuevoEstado == null) {
            log.warn("Petición PUT errónea: estadoLogistico es nulo");
            return ResponseEntity.badRequest().build();
        }
        return donacionService.actualizarEstadoLogistico(id, nuevoEstado)
                .map(d -> {
                    log.info("Estado logístico de donación ID: {} actualizado exitosamente a: {}", id, nuevoEstado);
                    return ResponseEntity.ok(d);
                })
                .orElseGet(() -> {
                    log.error("No se encontró la donación ID: {} para actualizar su estado logístico", id);
                    return ResponseEntity.notFound().build();
                });
    }
}