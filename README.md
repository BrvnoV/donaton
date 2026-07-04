# README para GitHub - Plataforma Donaton

Basado en la información disponible en el codebase, aquí tienes un README estructurado siguiendo las mejores prácticas de GitHub:

---

```markdown
# Donaton Platform

Plataforma de microservicios para coordinar esfuerzos de ayuda humanitaria frente a emergencias y catástrofes. Facilita la inscripción de voluntarios y el procesamiento eficiente de donaciones de recursos.

## 🏗️ Arquitectura

El sistema sigue una arquitectura de microservicios con los siguientes componentes:

- **donaton-bff**: Backend For Frontend que actúa como API Gateway y orquestador principal
- **ms-campanas**: Microservicio de gestión de campañas solidarias
- **ms-donaciones**: Microservicio de procesamiento de donaciones
- **ms-voluntarios**: Microservicio de registro de voluntarios
- **eureka-server**: Servidor de descubrimiento de servicios (Spring Cloud Netflix Eureka)
- **admin-panel**: Panel de administración
- **donaton-front**: Frontend para usuarios finales

## 🚀 Quick Start

### Prerrequisitos
- Java 17
- Maven
- Eureka Server ejecutándose (puerto 8761 por defecto)

### Ejecución Individual de Servicios

Para ejecutar el BFF (Backend For Frontend):

```bash
cd donaton-bff
mvn clean install -DskipTests
mvn spring-boot:run
```

Para levantar los servicios con Docker Compose:
```bash
docker-compose up -d
```

## 📊 Tecnologías Principales

- **Java 17**
- **Spring Boot 3.x**
- **Spring WebFlux** (Programación Reactiva)
- **Spring Cloud Netflix Eureka** (Descubrimiento de servicios)
- **Resilience4j** (Circuit Breaker)
- **JJWT** (Autenticación JWT) [3](#0-2) 

## 🔒 Seguridad y Resiliencia

- Validación de tokens JWT antes de alcanzar microservicios de dominio
- Circuit Breaker con Resilience4j para manejar caídas de servicios
- Patrones Scatter-Gather para orquestación reactiva concurrente [4](#0-3) 

## 📈 Cobertura de Código

El proyecto mantiene un mínimo de **60% de cobertura** en todos los microservicios centrales, con JaCoCo configurado como build breaker. [5](#0-4) 

## 📚 Repositorios

- [Frontend](https://github.com/organizacion-donaton/donaton-front)
- [BFF](https://github.com/organizacion-donaton/donaton-bff)
- [Microservicio Campañas](https://github.com/organizacion-donaton/ms-campanas)
- [Microservicio Donaciones](https://github.com/organizacion-donaton/ms-donaciones)
- [Microservicio Voluntarios](https://github.com/organizacion-donaton/ms-voluntarios)
- [Eureka Server](https://github.com/organizacion-donaton/eureka-server)
- [Panel Admin](https://github.com/organizacion-donaton/admin-panel) [6](#0-5) 

