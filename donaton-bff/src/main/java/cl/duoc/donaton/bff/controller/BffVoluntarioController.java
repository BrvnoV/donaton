package cl.duoc.donaton.bff.controller;

import cl.duoc.donaton.bff.service.BffVoluntarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bff/voluntarios")
@CrossOrigin(origins = "*")
@Slf4j
public class BffVoluntarioController {

    @Autowired
    private BffVoluntarioService bffVoluntarioService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listarVoluntarios() {
        log.info("BFF: Solicitud para listar todos los voluntarios");
        return bffVoluntarioService.listarTodosLosVoluntarios()
                .map(voluntarios -> {
                    log.info("BFF: Voluntarios listados con éxito, cantidad: {}", voluntarios.size());
                    return ResponseEntity.ok((Object) voluntarios);
                });
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> registrarVoluntario(@RequestBody Object nuevoVoluntario) {
        log.info("BFF: Solicitud para registrar nuevo voluntario");
        return bffVoluntarioService.registrarVoluntario(nuevoVoluntario)
                .map(resultado -> {
                    log.info("BFF: Voluntario registrado con éxito");
                    return ResponseEntity.status(201).body(resultado);
                });
    }
}
