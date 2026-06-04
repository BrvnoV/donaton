package cl.duoc.donaton.bff.controller;

import cl.duoc.donaton.bff.service.BffDonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/api/bff/necesidades") // ◄ Ruta para el módulo de necesidades
@CrossOrigin(origins = "*")
public class BffNecesidadController {

    @Autowired
    private BffDonacionService bffDonacionService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listarNecesidades() {
        return bffDonacionService.obtenerTodasLasNecesidades()
                .map(necesidades -> ResponseEntity.ok((Object) necesidades));
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> registrarNecesidad(@RequestBody Object nuevaNecesidad) {
        return bffDonacionService.crearNecesidad(nuevaNecesidad)
                .map(resultado -> ResponseEntity.status(201).body(resultado));
    }
}