package cl.duoc.donaton.bff.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BffServicesTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private BffDonacionService bffDonacionService;

    @InjectMocks
    private BffCampanaService bffCampanaService;

    @InjectMocks
    private BffDonacionService localBffDonacionService;

    @InjectMocks
    private BffVoluntarioService localBffVoluntarioService;

    @BeforeEach
    public void setup() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
    }

    private void mockGet(Object responseBody) {
        lenient().when(webClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        lenient().when(responseSpec.bodyToMono(any(Class.class))).thenReturn(Mono.just(responseBody));
    }

    private void mockPost(Object responseBody) {
        lenient().when(webClient.post()).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        lenient().when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        lenient().when(responseSpec.bodyToMono(any(Class.class))).thenReturn(Mono.just(responseBody));
    }

    private void mockPut(Object responseBody) {
        lenient().when(webClient.put()).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        lenient().when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        lenient().when(responseSpec.bodyToMono(any(Class.class))).thenReturn(Mono.just(responseBody));
    }

    // --- BffCampanaService Tests ---

    @Test
    public void testListarTodasLasCampanas() {
        List<Map<String, Object>> campanas = new ArrayList<>();
        Map<String, Object> c = new HashMap<>();
        c.put("id", 1);
        c.put("nombre", "Test");
        campanas.add(c);

        mockGet(campanas);

        Mono<List> resultMono = bffCampanaService.listarTodasLasCampanas();
        List result = resultMono.block();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test", ((Map) result.get(0)).get("nombre"));
    }

    @Test
    public void testObtenerCampanaPorId() {
        Map<String, Object> campana = new HashMap<>();
        campana.put("id", 1);
        campana.put("nombre", "Test");

        mockGet(campana);

        Mono<Object> resultMono = bffCampanaService.obtenerCampanaPorId(1L);
        Object result = resultMono.block();

        assertNotNull(result);
        assertEquals("Test", ((Map) result).get("nombre"));
    }

    @Test
    public void testCrearCampana() {
        Map<String, Object> campana = new HashMap<>();
        campana.put("id", 1);
        campana.put("nombre", "Test");

        mockPost(campana);

        Mono<Object> resultMono = bffCampanaService.crearCampana(campana);
        Object result = resultMono.block();

        assertNotNull(result);
        assertEquals(1, ((Map) result).get("id"));
    }

    @Test
    public void testActualizarEstadoCampana() {
        Map<String, Object> campana = new HashMap<>();
        campana.put("id", 1);
        campana.put("estado", "ACTIVE");

        mockPut(campana);

        Mono<Object> resultMono = bffCampanaService.actualizarEstadoCampana(1L, Collections.singletonMap("estado", "ACTIVE"));
        Object result = resultMono.block();

        assertNotNull(result);
        assertEquals("ACTIVE", ((Map) result).get("estado"));
    }

    @Test
    public void testFallbackListarCampanas() {
        Mono<List> resultMono = bffCampanaService.fallbackListarCampanas(new RuntimeException("Service down"));
        List result = resultMono.block();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFallbackObtenerCampana() {
        Mono<Object> resultMono = bffCampanaService.fallbackObtenerCampana(1L, new RuntimeException("Service down"));
        Object result = resultMono.block();

        assertNotNull(result);
        assertTrue(((Map) result).containsKey("error"));
    }

    @Test
    public void testFallbackCrearCampana() {
        Mono<Object> resultMono = bffCampanaService.fallbackCrearCampana(new Object(), new RuntimeException("Service down"));
        Object result = resultMono.block();

        assertNotNull(result);
        assertTrue(((Map) result).containsKey("error"));
    }

    @Test
    public void testFallbackActualizarEstado() {
        Mono<Object> resultMono = bffCampanaService.fallbackActualizarEstado(1L, new HashMap<>(), new RuntimeException("Service down"));
        Object result = resultMono.block();

        assertNotNull(result);
        assertTrue(((Map) result).containsKey("error"));
    }

    // --- BffDonacionService Tests ---

    @Test
    public void testObtenerTodasLasDonaciones() {
        List<Map<String, Object>> donaciones = new ArrayList<>();
        Map<String, Object> d = new HashMap<>();
        d.put("id", 1);
        d.put("cantidad", 50.0);
        donaciones.add(d);

        mockGet(donaciones);

        Mono<List> resultMono = localBffDonacionService.obtenerTodasLasDonaciones();
        List result = resultMono.block();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testObtenerDonacionesPorCampana() {
        List<Map<String, Object>> donaciones = new ArrayList<>();
        Map<String, Object> d = new HashMap<>();
        d.put("id", 1);
        d.put("campanaId", 10);
        donaciones.add(d);

        mockGet(donaciones);

        Mono<List> resultMono = localBffDonacionService.obtenerDonacionesPorCampana(10L);
        List result = resultMono.block();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testCrearDonacion() {
        Map<String, Object> donacion = new HashMap<>();
        donacion.put("id", 1);

        mockPost(donacion);

        Mono<Object> resultMono = localBffDonacionService.crearDonacion(donacion);
        Object result = resultMono.block();

        assertNotNull(result);
        assertEquals(1, ((Map) result).get("id"));
    }

    @Test
    public void testActualizarEstadoLogistico() {
        Map<String, Object> donacion = new HashMap<>();
        donacion.put("id", 1);

        mockPut(donacion);

        Mono<Object> resultMono = localBffDonacionService.actualizarEstadoLogistico(1L, new HashMap<>());
        Object result = resultMono.block();

        assertNotNull(result);
        assertEquals(1, ((Map) result).get("id"));
    }

    @Test
    public void testFallbackObtenerDonaciones() {
        Mono<List> resultMono = localBffDonacionService.fallbackObtenerDonaciones(new RuntimeException("error"));
        List result = resultMono.block();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- BffVoluntarioService Tests ---

    @Test
    public void testListarTodosLosVoluntarios() {
        List<Map<String, Object>> vols = new ArrayList<>();
        Map<String, Object> v = new HashMap<>();
        v.put("id", 1);
        vols.add(v);

        mockGet(vols);

        Mono<List> resultMono = localBffVoluntarioService.listarTodosLosVoluntarios();
        List result = resultMono.block();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testObtenerVoluntariosPorCampana() {
        List<Map<String, Object>> vols = new ArrayList<>();
        Map<String, Object> v = new HashMap<>();
        v.put("id", 1);
        vols.add(v);

        mockGet(vols);

        Mono<List> resultMono = localBffVoluntarioService.obtenerVoluntariosPorCampana(10L);
        List result = resultMono.block();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testRegistrarVoluntario() {
        Map<String, Object> voluntario = new HashMap<>();
        voluntario.put("id", 1);

        mockPost(voluntario);

        Mono<Object> resultMono = localBffVoluntarioService.registrarVoluntario(voluntario);
        Object result = resultMono.block();

        assertNotNull(result);
        assertEquals(1, ((Map) result).get("id"));
    }

    @Test
    public void testFallbackListarVoluntarios() {
        Mono<List> resultMono = localBffVoluntarioService.fallbackListarVoluntarios(new RuntimeException("error"));
        List result = resultMono.block();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
