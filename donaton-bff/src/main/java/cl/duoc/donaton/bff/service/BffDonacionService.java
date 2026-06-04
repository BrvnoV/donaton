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

    // URLs lógicas registradas en Eureka
    private final String MS_DONACIONES_URL = "http://ms-donaciones";
    private final String MS_LOGISTICA_URL = "http://ms-logistica";
    private final String MS_NECESIDADES_URL = "http://ms-necesidades";

    // ==========================================
    // 🍏 FLUJO: DONACIONES (Ya implementado)
    // ==========================================
    @CircuitBreaker(name = "donacionesCB", fallbackMethod = "fallbackObtenerDonaciones")
    public Mono<List> obtenerTodasLasDonaciones() {
        return webClientBuilder.build().get().uri(MS_DONACIONES_URL + "/donaciones").retrieve().bodyToMono(List.class);
    }

    @CircuitBreaker(name = "donacionesCB", fallbackMethod = "fallbackCrearDonacion")
    public Mono<Object> crearDonacion(Object nuevaDonacion) {
        return webClientBuilder.build().post().uri(MS_DONACIONES_URL + "/donaciones").bodyValue(nuevaDonacion).retrieve().bodyToMono(Object.class);
    }

    // ==========================================
    // 🚚 FLUJO: LOGÍSTICA (Nuevo)
    // ==========================================
    @CircuitBreaker(name = "logisticaCB", fallbackMethod = "fallbackObtenerEnvios")
    public Mono<List> obtenerTodosLosEnvios() {
        return webClientBuilder.build()
                .get()
                .uri(MS_LOGISTICA_URL + "/envios") // Ruta del controlador en ms-logistica
                .retrieve()
                .bodyToMono(List.class);
    }

    @CircuitBreaker(name = "logisticaCB", fallbackMethod = "fallbackCrearEnvio")
    public Mono<Object> crearEnvio(Object nuevoEnvio) {
        return webClientBuilder.build()
                .post()
                .uri(MS_LOGISTICA_URL + "/envios")
                .bodyValue(nuevoEnvio)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // ==========================================
    // 🚨 FLUJO: NECESIDADES (Nuevo)
    // ==========================================
    @CircuitBreaker(name = "necesidadesCB", fallbackMethod = "fallbackObtenerNecesidades")
    public Mono<List> obtenerTodasLasNecesidades() {
        return webClientBuilder.build()
                .get()
                .uri(MS_NECESIDADES_URL + "/necesidades") // Ruta del controlador en ms-necesidades
                .retrieve()
                .bodyToMono(List.class);
    }

    @CircuitBreaker(name = "necesidadesCB", fallbackMethod = "fallbackCrearNecesidad")
    public Mono<Object> crearNecesidad(Object nuevaNecesidad) {
        return webClientBuilder.build()
                .post()
                .uri(MS_NECESIDADES_URL + "/necesidades")
                .bodyValue(nuevaNecesidad)
                .retrieve()
                .bodyToMono(Object.class);
    }

    // ==========================================
    // 🛡️ MÉTODOS DE FALLBACK (PLANES DE RESPALDO)
    // ==========================================
    public Mono<List> fallbackObtenerDonaciones(Throwable t) {
        return Mono.just(Collections.singletonList("Servicio de donaciones no disponible (Modo Resiliente)"));
    }

    public Mono<Object> fallbackCrearDonacion(Object o, Throwable t) {
        return Mono.just(Collections.singletonMap("error", "No se pudo registrar la donación. Inténtelo más tarde."));
    }

    // Fallbacks Logística
    public Mono<List> fallbackObtenerEnvios(Throwable t) {
        System.out.println("🚨 [CB Logística] ms-logistica caído. Motivo: " + t.getMessage());
        return Mono.just(Collections.singletonList("Servicio de seguimiento logístico temporalmente fuera de línea"));
    }

    public Mono<Object> fallbackCrearEnvio(Object nuevoEnvio, Throwable t) {
        return Mono.just(Collections.singletonMap("error", "No se pudo agendar el despacho de logística en este momento."));
    }

    // Fallbacks Necesidades
    public Mono<List> fallbackObtenerNecesidades(Throwable t) {
        System.out.println("🚨 [CB Necesidades] ms-necesidades caído. Motivo: " + t.getMessage());
        return Mono.just(Collections.singletonList("Servicio de catastro de necesidades no disponible"));
    }

    public Mono<Object> fallbackCrearNecesidad(Object nuevaNecesidad, Throwable t) {
        return Mono.just(Collections.singletonMap("error", "No se pudo levantar la solicitud de necesidad. Inténtelo de nuevo."));
    }
}