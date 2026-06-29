# Microservicio de Donaciones (ms-donaciones)

## Enfoque de Diseño Guiado por el Dominio (DDD)
Este microservicio opera dentro del **Bounded Context (Contexto Delimitado)** de "Logística y Acopio de Recursos".

## Matriz de Casos de Uso (CU)

### CU-001: Registrar Donación Anónima
- **Actor:** Usuario Público (Donante Anónimo)
- **Precondiciones:** La campaña a la que se dona debe estar en estado `ACTIVE`.
- **Flujo Principal:** El usuario ingresa la cantidad, tipo de recurso y nombre (o anónimo) y envía la donación.
- **Flujo Alternativo (Unhappy Path):**
  - **1:** El usuario ingresa una cantidad negativa o nula. `@Min(value = 1)` intercepta la solicitud y retorna HTTP 400.
