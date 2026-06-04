package cl.duoc.donaton.bff.controller;

import cl.duoc.donaton.bff.service.BffDonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/api/bff/logistica") // ◄ Esta es la ruta exacta que llamas en Postman
@CrossOrigin(origins = "*")
public class BffLogisticaController {

    @Autowired
    private BffDonacionService bffDonacionService; // Conecta al servicio unificado

    @GetMapping
    public Mono<ResponseEntity<Object>> listarEnvios() { // ◄ Cambiado List por Object para soportar el fallback
        return bffDonacionService.obtenerTodosLosEnvios()
                .map(envios -> ResponseEntity.ok((Object) envios)); // ◄ Casteo explícito a Object
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> registrarEnvio(@RequestBody Object nuevoEnvio) {
        return bffDonacionService.crearEnvio(nuevoEnvio)
                .map(resultado -> ResponseEntity.status(201).body(resultado));
    }
}