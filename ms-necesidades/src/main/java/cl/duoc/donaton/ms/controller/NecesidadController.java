package cl.duoc.donaton.ms.controller;

import cl.duoc.donaton.ms.model.Necesidad;
import cl.duoc.donaton.ms.service.NecesidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/necesidades") // ◄ Crucial: Empalma con la URL ".uri(MS_NECESIDADES_URL + "/necesidades")" del BFF
public class NecesidadController {

    @Autowired
    private NecesidadService necesidadService;

    @GetMapping
    public ResponseEntity<List<Necesidad>> obtenerTodasLasNecesidades() {
        return ResponseEntity.ok(necesidadService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Necesidad> crearNecesidad(@RequestBody Necesidad necesidad) {
        Necesidad nuevaNecesidad = necesidadService.guardar(necesidad);
        return ResponseEntity.status(201).body(nuevaNecesidad);
    }
}