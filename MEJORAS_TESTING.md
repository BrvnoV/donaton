# Mejoras y Refactorizaciones Derivadas del Testing

Durante la implementación de la suite de pruebas unitarias, de integración y End-to-End, se detectaron fallos arquitectónicos y omisiones que impedían la correcta ejecución de los tests. A continuación se documentan las correcciones.

## 1. Refactorización de Adaptadores (ms-donaciones / ms-voluntarios)

**Bug Detectado:** 
Al intentar simular el comportamiento de `ms-campanas` con `MockWebServer` en las pruebas de integración (`DonacionIntegrationTest`), fue imposible interceptar la llamada porque la URI del servicio destino estaba quemada directamente en el código (`"http://ms-campanas/campanas/..."`). Esto violaba el principio de inversión de dependencias y la capacidad de testabilidad de Spring.

**Lugar Crítico:** 
- `ms-donaciones/src/main/java/cl/duoc/donaton/ms/adapter/CampanaAdapter.java`
- `ms-voluntarios/src/main/java/cl/duoc/donaton/ms/adapter/CampanaAdapter.java`

**Código Corregido (Java):**
Se externalizó la configuración de la URL mediante `@Value`, permitiendo su sobreescritura dinámica (DynamicPropertyRegistry) en el entorno de pruebas.

```java
@Component
public class CampanaAdapter {
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${campanas.service.url:http://ms-campanas}")
    private String campanasServiceUrl;

    public String obtenerEstadoCampana(Long campanaId) {
        // ...
        Map response = webClientBuilder.build()
                .get()
                .uri(campanasServiceUrl + "/campanas/" + campanaId + "/estado")
                // ...
    }
}
```

## 2. Mocking Incompleto en Servicios 

**Bug Detectado:** 
En los tests unitarios existentes (`VoluntarioServiceTest` y `DonacionServiceTest`), la inyección de la dependencia `CampanaAdapter` no estaba siendo mockeada correctamente, lo que provocaba `NullPointerException` al intentar validar el estado de la campaña en flujos de éxito.

**Lugar Crítico:** 
- `ms-voluntarios/src/test/java/cl/duoc/donaton/ms/service/VoluntarioServiceTest.java`
- `ms-donaciones/src/test/java/cl/duoc/donaton/ms/service/DonacionServiceTest.java`

**Código Corregido (Java):**
Se agregó el Mock para el adaptador y se interceptó su respuesta antes de las llamadas a repositorio.

```java
    @Mock
    private cl.duoc.donaton.ms.adapter.CampanaAdapter campanaAdapter;

    @Test
    public void testInscribirVoluntarioExitoso() {
        when(campanaAdapter.esCampanaValida(1L)).thenReturn(true);
        // ... assertions
    }
```

## 3. Pruebas Unitarias de Excepciones Mal Enrutadas

**Bug Detectado:**
La prueba que validaba la duplicidad de correo en `VoluntarioServiceTest` y la de montos negativos en `DonacionServiceTest` esperaban que el `Repository` fallara arrojando un `DataIntegrityViolationException` o `ConstraintViolationException`. Sin embargo, la lógica de validación debe ocurrir a nivel de la capa de Servicio ANTES de llegar al repositorio para considerarse una excepción de negocio pura.

**Lugar Crítico:** 
- Pruebas Unitarias.

**Código Corregido (Java):**
Se ajustaron los tests para validar las excepciones de negocio (`DuplicateVolunteerException` y `InvalidResourceQuantityException`) verificando con Mockito (`verify(repo, never()).save(...)`) que la transacción de persistencia jamás se inicia.
