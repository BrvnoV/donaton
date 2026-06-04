package cl.duoc.donaton.bff.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
public class BffDonacionService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    // Nombre lógico del microservicio registrado en el application.yml de Rodrigo
    private final String MS_DONACIONES_URL = "http://ms-donaciones";

    /**
     * Obtiene la lista completa de donaciones desde el microservicio core.
     * El circuit breaker se llama "donacionesCB" y define un método de fallback si falla.
     */
    @CircuitBreaker(name = "donacionesCB", fallbackMethod = "fallbackObtenerDonaciones")
    public Mono<List> obtenerTodasLasDonaciones() {
        return webClientBuilder.build()
                .get()
                .uri(MS_DONACIONES_URL + "/donaciones") // Ajustar ruta según los endpoints del microservicio
                .retrieve()
                .bodyToMono(List.class);
    }

    /**
     * Enviar una nueva donación al microservicio core.
     */
    @CircuitBreaker(name = "donacionesCB", fallbackMethod = "fallbackCrearDonacion")
    public Mono<Object> crearDonacion(Object nuevaDonacion) {
        return webClientBuilder.build()
                .post()
                .uri(MS_DONACIONES_URL + "/donaciones")
                .bodyValue(nuevaDonacion)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // ==========================================
    // 🛡️ MÉTODOS DE FALLBACK (PLAN DE RESPALDO)
    // ==========================================

    /**
     * Si ms-donaciones no responde, devolvemos una lista vacía controlada en lugar de romper el BFF.
     */
    public Mono<List> fallbackObtenerDonaciones(Throwable t) {
        System.out.println("🚨 [Circuit Breaker] ms-donaciones no disponible. Motivo: " + t.getMessage());
        return Mono.just(Collections.singletonList("Servicio de donaciones temporalmente no disponible (Modo Resiliente Activo)"));
    }

    /**
     * Si falla la creación, devolvemos un objeto de error controlado indicando el estado.
     */
    public Mono<Object> fallbackCrearDonacion(Object nuevaDonacion, Throwable t) {
        System.out.println("🚨 [Circuit Breaker] Error al registrar donación. Motivo: " + t.getMessage());
        return Mono.just(Collections.singletonMap("error", "No se pudo procesar el registro de la donación en este momento. Inténtelo más tarde."));
    }
}