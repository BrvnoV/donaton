package cl.duoc.donaton.bff.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewaySecurityIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testPeticionProtegidaSinJWT_DebeRetornar401() {
        // La ruta PUT /api/bff/campanas/{id}/estado está protegida y exige un token JWT válido.
        String jsonPayload = "{ \"estado\": \"COMPLETED\" }";

        webTestClient.put()
                .uri("/api/bff/campanas/1/estado")
                .header("Content-Type", "application/json")
                .bodyValue(jsonPayload)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody()
                .jsonPath("$.error").isEqualTo("UNAUTHORIZED")
                .jsonPath("$.message").isEqualTo("Token JWT ausente o formato inválido");
    }
}
