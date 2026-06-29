package cl.duoc.donaton.ms;

import cl.duoc.donaton.ms.model.Donacion;
import cl.duoc.donaton.ms.repository.DonacionRepository;
import cl.duoc.donaton.ms.service.DonacionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.context.TestConfiguration;

// 🔥 SOLUCIÓN: Habilitamos explícitamente la sobrescritura de beans para evitar el quiebre del Contexto
@SpringBootTest(properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
public class DonacionIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        public WebClient.Builder webClientBuilder() {
            return WebClient.builder();
        }
    }

    private static MockWebServer mockBackEnd;

    @Autowired
    private DonacionService donacionService;

    @Autowired
    private DonacionRepository donacionRepository;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("campanas.service.url", () -> mockBackEnd.url("/").toString().replaceAll("/$", ""));
    }

    @Test
    public void testCrearDonacion_CampanaCompleta_DebeHacerRollback() throws Exception {
        // Estado inicial de la BD
        long initialCount = donacionRepository.count();

        // Configuramos el mock para retornar un estado COMPLETED de la campaña 1
        String mockedResponse = "{ \"existe\": true, \"estado\": \"COMPLETED\" }";
        mockBackEnd.enqueue(new MockResponse()
          .setBody(mockedResponse)
          .addHeader("Content-Type", "application/json"));

        Donacion donacion = new Donacion();
        donacion.setCampanaId(1L);
        donacion.setCantidad(50.0);
        donacion.setDonante("Integration Test");
        donacion.setTipoRecurso("DINERO");

        // Act & Assert
        assertThrows(cl.duoc.donaton.ms.exception.ClosedCampaignException.class, () -> {
            donacionService.guardar(donacion);
        });

        // Verificamos el rollback: La cantidad de registros debe mantenerse intacta
        assertEquals(initialCount, donacionRepository.count());
    }
}