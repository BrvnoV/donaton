package cl.duoc.donaton.bff.controller;

import cl.duoc.donaton.bff.service.BffDonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/api/bff/logistica")
@CrossOrigin(origins = "*")
public class BffLogisticaController {

    @Autowired
    private BffDonacionService bffService;

    @GetMapping
    public Mono<ResponseEntity<List>> listarEnvios() {
        return bffService.obtenerTodosLosEnvios()
                .map(envios -> ResponseEntity.ok().body(envios));
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> registrarEnvio(@RequestBody Object nuevoEnvio) {
        return bffService.crearEnvio(nuevoEnvio)
                .map(resultado -> ResponseEntity.status(201).body(resultado));
    }
}