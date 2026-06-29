package cl.duoc.donaton.bff.controller;

import cl.duoc.donaton.bff.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bff/auth")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        log.info("Intento de inicio de sesión para el usuario: {}", username);

        // Credenciales hardcodeadas requeridas por el enunciado
        if ("admin".equals(username) && "admin123".equals(password)) {
            log.info("Inicio de sesión exitoso para el usuario: {}", username);
            String token = jwtUtil.generateToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return Mono.just(ResponseEntity.ok(response));
        } else {
            log.warn("Inicio de sesión fallido para el usuario: {}", username);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Credenciales incorrectas");
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response));
        }
    }
}
