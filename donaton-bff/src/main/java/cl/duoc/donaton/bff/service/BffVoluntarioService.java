package cl.duoc.donaton.bff.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class BffVoluntarioService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final String MS_VOLUNTARIOS_URL = "http://ms-voluntarios";

    @CircuitBreaker(name = "voluntariosCB", fallbackMethod = "fallbackListarVoluntarios")
    public Mono<List> listarTodosLosVoluntarios() {
        log.info("Llamando a ms-voluntarios para listar todos los voluntarios");
        return webClientBuilder.build()
                .get()
                .uri(MS_VOLUNTARIOS_URL + "/voluntarios")
                .retrieve()
                .bodyToMono(List.class);
    }

    @CircuitBreaker(name = "voluntariosCB", fallbackMethod = "fallbackObtenerVoluntariosPorCampana")
    public Mono<List> obtenerVoluntariosPorCampana(Long campanaId) {
        log.info("Llamando a ms-voluntarios para obtener voluntarios de campaña ID: {}", campanaId);
        return webClientBuilder.build()
                .get()
                .uri(MS_VOLUNTARIOS_URL + "/voluntarios/campana/" + campanaId)
                .retrieve()
                .bodyToMono(List.class);
    }

    @CircuitBreaker(name = "voluntariosCB", fallbackMethod = "fallbackRegistrarVoluntario")
    public Mono<Object> registrarVoluntario(Object nuevoVoluntario) {
        log.info("Llamando a ms-voluntarios para registrar nuevo voluntario");
        return webClientBuilder.build()
                .post()
                .uri(MS_VOLUNTARIOS_URL + "/voluntarios")
                .bodyValue(nuevoVoluntario)
                .exchangeToMono(response -> {
                    if (response.statusCode().is4xxClientError()) {
                        return response.bodyToMono(Object.class)
                                .defaultIfEmpty(java.util.Collections.singletonMap("message", "Error de validación"));
                    }
                    return response.bodyToMono(Object.class);
                });
    }

    // Fallbacks
    public Mono<List> fallbackListarVoluntarios(Throwable t) {
        log.error("🚨 Fallback listar voluntarios invocado debido a error: {}", t.getMessage(), t);
        return Mono.just(Collections.emptyList());
    }

    public Mono<List> fallbackObtenerVoluntariosPorCampana(Long campanaId, Throwable t) {
        log.error("🚨 Fallback obtener voluntarios por campaña ID {} invocado debido a error: {}", campanaId, t.getMessage(), t);
        return Mono.just(Collections.emptyList());
    }

    public Mono<Object> fallbackRegistrarVoluntario(Object body, Throwable t) {
        log.error("🚨 Fallback registrar voluntario invocado debido a error: {}", t.getMessage(), t);
        return Mono.just(Collections.singletonMap("error", "No se pudo registrar el voluntario en la campaña"));
    }
}
