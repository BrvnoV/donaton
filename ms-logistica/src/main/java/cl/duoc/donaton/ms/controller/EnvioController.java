package cl.duoc.donaton.ms.controller;

import cl.duoc.donaton.ms.model.Envio;
import cl.duoc.donaton.ms.service.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/envios") // ◄ Crucial: Empalma con la URL ".uri(MS_LOGISTICA_URL + "/envios")" del BFF
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @GetMapping
    public ResponseEntity<List<Envio>> obtenerTodosLosEnvios() {
        return ResponseEntity.ok(envioService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Envio> crearEnvio(@RequestBody Envio envio) {
        Envio nuevoEnvio = envioService.guardar(envio);
        return ResponseEntity.status(201).body(nuevoEnvio);
    }
}