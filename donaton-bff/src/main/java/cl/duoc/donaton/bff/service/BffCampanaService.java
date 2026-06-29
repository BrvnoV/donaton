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
import java.util.stream.Collectors;

@Service
@Slf4j
public class BffCampanaService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private BffDonacionService bffDonacionService;

    private final String MS_CAMPANAS_URL = "http://ms-campanas";

    @CircuitBreaker(name = "campanasCB", fallbackMethod = "fallbackListarCampanas")
    public Mono<List> listarTodasLasCampanas() {
        log.info("Llamando a ms-campanas para listar todas las campañas");
        return webClientBuilder.build()
                .get()
                .uri(MS_CAMPANAS_URL + "/campanas")
                .retrieve()
                .bodyToMono(List.class);
    }

    @CircuitBreaker(name = "campanasCB", fallbackMethod = "fallbackObtenerCampana")
    public Mono<Object> obtenerCampanaPorId(Long id) {
        log.info("Llamando a ms-campanas para obtener campaña con ID: {}", id);
        return webClientBuilder.build()
                .get()
                .uri(MS_CAMPANAS_URL + "/campanas/" + id)
                .retrieve()
                .bodyToMono(Object.class);
    }

    @CircuitBreaker(name = "campanasCB", fallbackMethod = "fallbackCrearCampana")
    public Mono<Object> crearCampana(Object nuevaCampana) {
        log.info("Llamando a ms-campanas para crear una campaña");
        return webClientBuilder.build()
                .post()
                .uri(MS_CAMPANAS_URL + "/campanas")
                .bodyValue(nuevaCampana)
                .retrieve()
                .bodyToMono(Object.class);
    }

    @CircuitBreaker(name = "campanasCB", fallbackMethod = "fallbackActualizarEstado")
    public Mono<Object> actualizarEstadoCampana(Long id, Map<String, String> body) {
        log.info("Llamando a ms-campanas para actualizar estado de la campaña ID: {}", id);
        return webClientBuilder.build()
                .put()
                .uri(MS_CAMPANAS_URL + "/campanas/" + id + "/estado")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Object.class);
    }

    public Mono<List<Map<String, Object>>> obtenerResumenCampanas() {
        log.info("Calculando resumen de campañas activas en BFF");
        return listarTodasLasCampanas()
                .flatMap(campanas -> {
                    List<Map<String, Object>> activeCampanas = ((List<Map<String, Object>>) campanas).stream()
                             .filter(c -> "ACTIVE".equalsIgnoreCase((String) c.get("estado")))
                            .collect(Collectors.toList());

                    if (activeCampanas.isEmpty()) {
                        log.info("No se encontraron campañas activas para el resumen");
                        return Mono.just(Collections.emptyList());
                    }

                    List<Mono<Map<String, Object>>> monos = activeCampanas.stream()
                            .map(c -> {
                                Long campanaId = ((Number) c.get("id")).longValue();
                                return bffDonacionService.obtenerDonacionesPorCampana(campanaId)
                                        .map(donacionesList -> {
                                            double total = ((List<Map<String, Object>>) donacionesList).stream()
                                                    .mapToDouble(d -> ((Number) d.get("cantidad")).doubleValue())
                                                    .sum();
                                            c.put("totalRecursos", total);
                                            return c;
                                        });
                            })
                            .collect(Collectors.toList());

                    return Mono.zip(monos, args -> {
                        List<Map<String, Object>> result = new java.util.ArrayList<>();
                        for (Object o : args) {
                            result.add((Map<String, Object>) o);
                        }
                        return result;
                    });
                });
    }

    // Fallbacks
    public Mono<List> fallbackListarCampanas(Throwable t) {
        log.error("🚨 Fallback listar campanas invocado debido a error: {}", t.getMessage(), t);
        return Mono.just(Collections.emptyList());
    }

    public Mono<Object> fallbackObtenerCampana(Long id, Throwable t) {
        log.error("🚨 Fallback obtener campaña ID {} invocado debido a error: {}", id, t.getMessage(), t);
        return Mono.just(Collections.singletonMap("error", "Campaña no disponible"));
    }

    public Mono<Object> fallbackCrearCampana(Object body, Throwable t) {
        log.error("🚨 Fallback crear campaña invocado debido a error: {}", t.getMessage(), t);
        return Mono.just(Collections.singletonMap("error", "No se pudo crear la campaña"));
    }

    public Mono<Object> fallbackActualizarEstado(Long id, Map<String, String> body, Throwable t) {
        log.error("🚨 Fallback actualizar estado campaña ID {} invocado debido a error: {}", id, t.getMessage(), t);
        return Mono.just(Collections.singletonMap("error", "No se pudo actualizar el estado de la campaña"));
    }

    @CircuitBreaker(name = "campanasCB", fallbackMethod = "fallbackActualizarCampana")
    public Mono<Object> actualizarCampana(Long id, Object body) {
        log.info("Llamando a ms-campanas para actualizar la campaña ID: {}", id);
        return webClientBuilder.build()
                .put()
                .uri(MS_CAMPANAS_URL + "/campanas/" + id)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Object.class);
    }

    @CircuitBreaker(name = "campanasCB", fallbackMethod = "fallbackEliminarCampana")
    public Mono<Object> eliminarCampana(Long id) {
        log.info("Llamando a ms-campanas para eliminar la campaña ID: {}", id);
        return webClientBuilder.build()
                .delete()
                .uri(MS_CAMPANAS_URL + "/campanas/" + id)
                .retrieve()
                .toBodilessEntity()
                .map(response -> Collections.singletonMap("message", "Campaña eliminada con éxito"));
    }

    public Mono<Object> fallbackActualizarCampana(Long id, Object body, Throwable t) {
        log.error("🚨 Fallback actualizar campaña ID {} invocado debido a error: {}", id, t.getMessage(), t);
        return Mono.just(Collections.singletonMap("error", "No se pudo actualizar la campaña"));
    }

    public Mono<Object> fallbackEliminarCampana(Long id, Throwable t) {
        log.error("🚨 Fallback eliminar campaña ID {} invocado debido a error: {}", id, t.getMessage(), t);
        return Mono.just(Collections.singletonMap("error", "No se pudo eliminar la campaña"));
    }
}
