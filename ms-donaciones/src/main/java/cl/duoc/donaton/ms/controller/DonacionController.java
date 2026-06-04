package cl.duoc.donaton.ms.controller;

import cl.duoc.donaton.ms.model.Donacion;
import cl.duoc.donaton.ms.service.DonacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donaciones") // ◄ Esta ruta empalma exacto con la URI del WebClient en el BFF
public class DonacionController {

    @Autowired
    private DonacionService donacionService;

    @GetMapping
    public ResponseEntity<List<Donacion>> obtenerDonaciones() {
        return ResponseEntity.ok(donacionService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<Donacion> crearDonacion(@RequestBody Donacion donacion) {
        Donacion nuevaDonacion = donacionService.guardar(donacion);
        return ResponseEntity.status(201).body(nuevaDonacion);
    }
}