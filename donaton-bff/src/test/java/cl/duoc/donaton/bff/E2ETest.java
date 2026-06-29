package cl.duoc.donaton.bff;

import cl.duoc.donaton.bff.security.JwtUtil;
import cl.duoc.donaton.bff.service.BffDonacionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class E2ETest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private BffDonacionService bffDonacionService;

    @Test
    public void testFlujoE2E1_OperacionProtegidaConJWT() {
        // 1. Simular Login generando JWT
        String token = jwtUtil.generateToken("admin");

        // 2. Realizar PUT protegido
        String payload = "{ \"estadoLogistico\": \"ENTREGADO\" }";

        // Mockeamos la respuesta del BffDonacionService para que retorne exitosamente y obtengamos 200 OK
        Mockito.when(bffDonacionService.actualizarEstadoLogistico(anyLong(), any()))
                .thenReturn(Mono.just(Map.of("estadoLogistico", "ENTREGADO")));

        webTestClient.put()
                .uri("/api/bff/donaciones/1/estado")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void testFlujoE2E2_ResilienciaCircuitBreaker() {
        // Mockeamos el fallback de error (retornando Map con la llave "error")
        Mockito.when(bffDonacionService.crearDonacion(any()))
                .thenReturn(Mono.just(Map.of("error", "No se pudo registrar la donación. Revisa la campaña o inténtelo más tarde.")));

        // Enviar POST a donaciones con payload válido. 
        // El Fallback devolverá 400 con un mapa de error.
        String payload = "{ \"campanaId\": 9999, \"donante\": \"E2E Test\", \"tipoRecurso\": \"DINERO\", \"cantidad\": 100 }";

        webTestClient.post()
                .uri("/api/bff/donaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchange()
                .expectStatus().isBadRequest() // El fallback devuelve 400
                .expectBody()
                .jsonPath("$.error").isEqualTo("No se pudo registrar la donación. Revisa la campaña o inténtelo más tarde.");
    }

    @Test
    public void testFlujoE2E3_OrquestacionCampanaYVoluntarios() {
        // GET /api/bff/campanas/1/voluntarios usa Mono.zip
        // Independientemente de si existen o no, el Gateway los combina. 
        // Si fallan, sus respectivos fallbacks retornan listas vacías o diccionarios vacíos.
        webTestClient.get()
                .uri("/api/bff/campanas/1/voluntarios")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.campana").exists()
                .jsonPath("$.voluntarios").exists();
    }
}
