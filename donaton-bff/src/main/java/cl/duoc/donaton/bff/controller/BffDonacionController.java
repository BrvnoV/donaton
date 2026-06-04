package cl.duoc.donaton.bff.controller;

import cl.duoc.donaton.bff.service.BffDonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/bff/donaciones")
@CrossOrigin(origins = "*") // Permite conectar con el frontend sin problemas de CORS
public class BffDonacionController {

    @Autowired
    private BffDonacionService bffDonacionService;

    @GetMapping
    public Mono<ResponseEntity<Object>> listarDonaciones() {
        return bffDonacionService.obtenerTodasLasDonaciones()
                .map(donaciones -> ResponseEntity.ok((Object) donaciones));
    }

    @PostMapping
    public Mono<ResponseEntity<Object>> registrarDonacion(@RequestBody Object nuevaDonacion) {
        return bffDonacionService.crearDonacion(nuevaDonacion)
                .map(resultado -> ResponseEntity.status(201).body(resultado));
    }
}