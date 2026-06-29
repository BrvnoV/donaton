# Microservicio de Voluntarios (ms-voluntarios)

## Enfoque de Diseño Guiado por el Dominio (DDD)
Este microservicio opera dentro del **Bounded Context (Contexto Delimitado)** de "Gestión de Capital Humano".

## Matriz de Casos de Uso (CU)

### CU-002: Inscripción Obligatoria de Voluntarios
- **Actor:** Usuario Público (Candidato a Voluntario)
- **Precondiciones:** La campaña a la que se inscribe debe estar en estado `ACTIVE`.
- **Flujo Principal:** El usuario envía sus datos de contacto y un correo electrónico válido.
- **Flujo Alternativo (Unhappy Path):**
  - **1:** El usuario ingresa un formato de correo incorrecto (ej: `hola.com`). `@Email` captura el error (HTTP 400).
  - **2:** El correo ingresado ya existe en la base de datos (Unique Constraint). El sistema retorna HTTP 400/500 según el `ControllerAdvice`.
