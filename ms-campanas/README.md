# Microservicio de Campañas (ms-campanas)

## Enfoque de Diseño Guiado por el Dominio (DDD)
Este microservicio opera dentro del **Bounded Context (Contexto Delimitado)** de "Gestión Estratégica de Campañas". Su responsabilidad exclusiva es el ciclo de vida de una campaña solidaria.

## Matriz de Casos de Uso (CU)

### CU-003: Modificación CRUD de Campañas
- **Actor:** Administrador
- **Precondiciones:** El administrador debe estar autenticado en el API Gateway y proveer el token válido.
- **Flujo Principal:** El administrador registra, edita o elimina una campaña, proporcionando nombre, descripción y fechas consistentes.
- **Flujo Alternativo (Unhappy Path):**
  - **1:** El usuario envía fechas donde `fechaFin` es anterior a `fechaInicio`. El sistema lanza una excepción validada por `@AssertTrue` retornando HTTP 400.
  - **2:** El usuario deja el nombre en blanco. `@NotBlank` captura el error y retorna `MethodArgumentNotValidException` (HTTP 400).
