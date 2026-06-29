# Donaton BFF (Backend For Frontend)

## Descripción
El **BFF (Backend For Frontend)** actúa como el orquestador principal y punto único de entrada (API Gateway) para las aplicaciones cliente (Frontend / Panel de Administración) en el ecosistema **Donaton**. Su propósito central es encapsular la lógica de negocio compleja, agregar y combinar respuestas de múltiples microservicios subyacentes, y proveer un contrato de API unificado, limpio y seguro.

Implementa patrones de diseño clave como:
- **API Gateway / Orquestador:** Canaliza las solicitudes a los microservicios correspondientes (Campañas, Donaciones, Voluntarios).
- **Resiliencia (Circuit Breaker):** Utiliza *Resilience4j* para manejar caídas en los microservicios subyacentes sin afectar la disponibilidad total del sistema, proveyendo respuestas de *Fallback* controladas.
- **Seguridad (JWT):** Valida los tokens de autorización antes de permitir que las peticiones críticas alcancen los microservicios de dominio.
- **Scatter-Gather (Programación Reactiva):** Utiliza *Spring WebFlux* (`Mono.zip`, `Flux`) para hacer llamados concurrentes a múltiples microservicios y combinar sus respuestas en un solo JSON estructurado para el cliente.

## Tecnologías Principales
- **Java 17**
- **Spring Boot 3.x**
- **Spring WebFlux** (Programación Reactiva)
- **Spring Cloud Netflix Eureka Client** (Descubrimiento de servicios)
- **Resilience4j** (Circuit Breaker)
- **JJWT** (Autenticación basada en JSON Web Tokens)

## Prerrequisitos
- Tener **Eureka Server** ejecutándose localmente (por defecto en el puerto `8761`).
- Los microservicios de dominio (`ms-campanas`, `ms-donaciones`, `ms-voluntarios`) deben estar corriendo para el flujo de datos completo, aunque el BFF es tolerante a sus caídas.

## Instalación y Ejecución

1. Posiciónate en la carpeta raíz del BFF:
   ```bash
   cd donaton-bff
   ```

2. Compila el proyecto y descarga las dependencias:
   ```bash
   mvn clean install -DskipTests
   ```

3. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```
   *El BFF se levantará por defecto en un puerto aleatorio o definido en su configuración, registrándose automáticamente en Eureka Server bajo el nombre `DONATON-BFF`.*

## Ejecución de Pruebas y Aseguramiento de Calidad
Este componente cuenta con una suite de pruebas End-to-End (E2E) que valida su comportamiento reactivo, la resiliencia ante fallos y la validación de tokens de seguridad.

Para ejecutar los tests automatizados:
```bash
mvn test
```
*Asegúrate de revisar la carpeta `target/site/jacoco/index.html` para validar la cobertura de código tras ejecutar los tests.*
