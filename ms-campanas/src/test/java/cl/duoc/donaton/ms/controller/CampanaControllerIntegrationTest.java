package cl.duoc.donaton.ms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CampanaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCrearCampana_PayloadInvalido_DebeRetornar400() throws Exception {
        // Un payload JSON con nombre vacío o nulo para forzar @Valid a fallar.
        // También enviamos una metaRecaudacion negativa.
        String invalidPayload = "{ \"nombre\": \"\", \"descripcion\": \"Falta meta y nombre valido\", \"metaRecaudacion\": -100 }";

        // El header X-Gateway-Secret es obligatorio debido a GatewaySecurityFilter
        mockMvc.perform(post("/campanas")
                .header("X-Gateway-Secret", "DonatonGatewaySecret-777")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }
}
