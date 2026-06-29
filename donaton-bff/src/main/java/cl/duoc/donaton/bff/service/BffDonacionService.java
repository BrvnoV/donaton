package cl.duoc.donaton.bff.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BffDonacionService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final String MS_DONACIONES_URL = "http://ms-donaciones";

    @CircuitBreaker(name = "donacionesCB", fallbackMethod = "fallbackObtenerDonaciones")
    public Mono<List> obtenerTodasLasDonaciones() {
        log.info("Llamando a ms-donaciones para obtener todas las donaciones");
        return webClientBuilder.build()
                .get()
                .uri(MS_DONACIONES_URL + "/donaciones")
                .retrieve()
                .bodyToMono(List.class);
    }

    @CircuitBreaker(name = "donacionesCB", fallbackMethod = "fallbackObtenerDonacionesPorCampana")
    public Mono<List> obtenerDonacionesPorCampana(Long campanaId) {
        log.info("Llamando a ms-donaciones para obtener donaciones de campaña ID: {}", campanaId);
        return webClientBuilder.build()
                .get()
                .uri(MS_DONACIONES_URL + "/donaciones/campana/" + campanaId)
                .retrieve()
                .bodyToMono(List.class);
    }

    @CircuitBreaker(name = "donacionesCB", fallbackMethod = "fallbackCrearDonacion")
    public Mono<Object> crearDonacion(Object nuevaDonacion) {
        log.info("Llamando a ms-donaciones para registrar nueva donación");
        return webClientBuilder.build()
                .post()
                .uri(MS_DONACIONES_URL + "/donaciones")
                .bodyValue(nuevaDonacion)
                .retrieve()
                .bodyToMono(Object.class);
    }

    @CircuitBreaker(name = "donacionesCB", fallbackMethod = "fallbackActualizarEstadoLogico")
    public Mono<Object> actualizarEstadoLogistico(Long id, Map<String, String> body) {
        log.info("Llamando a ms-donaciones para actualizar estado logístico de donación ID: {}", id);
        return webClientBuilder.build()
                .put()
                .uri(MS_DONACIONES_URL + "/donaciones/" + id + "/estado")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // Fallbacks
    public Mono<List> fallbackObtenerDonaciones(Throwable t) {
        log.error("🚨 Fallback obtener donaciones invocado debido a error: {}", t.getMessage(), t);
        return Mono.just(Collections.emptyList());
    }

    public Mono<List> fallbackObtenerDonacionesPorCampana(Long campanaId, Throwable t) {
        log.error("🚨 Fallback obtener donaciones por campaña ID {} invocado debido a error: {}", campanaId, t.getMessage(), t);
        return Mono.just(Collections.emptyList());
    }

    public Mono<Object> fallbackCrearDonacion(Object o, Throwable t) {
        log.error("🚨 Fallback crear donación invocado debido a error: {}", t.getMessage(), t);
        return Mono.just(Collections.singletonMap("error", "No se pudo registrar la donación. Revisa la campaña o inténtelo más tarde."));
    }

    public Mono<Object> fallbackActualizarEstadoLogico(Long id, Map<String, String> body, Throwable t) {
        log.error("🚨 Fallback actualizar estado logístico donación ID {} invocado debido a error: {}", id, t.getMessage(), t);
        return Mono.just(Collections.singletonMap("error", "No se pudo actualizar el estado logístico de la donación."));
    }
}