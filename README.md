<div align="center">

# 🤝 Donaton Platform

**Plataforma de microservicios para coordinar esfuerzos de ayuda humanitaria frente a emergencias y catástrofes**

Facilita la inscripción de voluntarios y el procesamiento eficiente de donaciones de recursos.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Coverage](https://img.shields.io/badge/coverage-≥60%25-brightgreen)](#-cobertura-de-código)
[![License](https://img.shields.io/badge/license-MIT-blue)](#-licencia)

</div>

---

## 📑 Tabla de contenidos

- [Arquitectura](#-arquitectura)
- [Tecnologías principales](#-tecnologías-principales)
- [Quick start](#-quick-start)
- [Seguridad y resiliencia](#-seguridad-y-resiliencia)
- [Cobertura de código](#-cobertura-de-código)
- [Repositorios](#-repositorios)

---

## 🏗️ Arquitectura

El sistema sigue una **arquitectura de microservicios** orquestada mediante un patrón BFF (Backend For Frontend) con descubrimiento de servicios centralizado.

| Componente | Descripción |
|---|---|
| `donaton-bff` | Backend For Frontend que actúa como API Gateway y orquestador principal |
| `ms-campanas` | Microservicio de gestión de campañas solidarias |
| `ms-donaciones` | Microservicio de procesamiento de donaciones |
| `ms-voluntarios` | Microservicio de registro de voluntarios |
| `eureka-server` | Servidor de descubrimiento de servicios (Spring Cloud Netflix Eureka) |
| `admin-panel` | Panel de administración |
| `donaton-front` | Frontend para usuarios finales |

---

## 📊 Tecnologías principales

- **Java 17**
- **Spring Boot 3.x**
- **Spring WebFlux** — programación reactiva
- **Spring Cloud Netflix Eureka** — descubrimiento de servicios
- **Resilience4j** — Circuit Breaker
- **JJWT** — autenticación JWT

---

## 🚀 Quick start

### Prerrequisitos

- Java 17
- Maven
- Eureka Server ejecutándose (puerto `8761` por defecto)

### Ejecución individual de servicios

Para ejecutar el BFF (Backend For Frontend):

```bash
cd donaton-bff
mvn clean install -DskipTests
mvn spring-boot:run
```

### Levantar todos los servicios con Docker Compose

```bash
docker-compose up -d
```

---

## 🔒 Seguridad y resiliencia

- ✅ Validación de tokens JWT antes de alcanzar los microservicios de dominio
- ✅ Circuit Breaker con **Resilience4j** para manejar caídas de servicios
- ✅ Patrones **Scatter-Gather** para orquestación reactiva concurrente

---

## 📈 Cobertura de código

El proyecto mantiene un **mínimo de 60% de cobertura** en todos los microservicios centrales, con **JaCoCo** configurado como *build breaker*.

---

## 📚 Repositorios

| Servicio | Enlace |
|---|---|
| Frontend | [donaton-front](https://github.com/organizacion-donaton/donaton-front) |
| BFF | [donaton-bff](https://github.com/organizacion-donaton/donaton-bff) |
| Microservicio Campañas | [ms-campanas](https://github.com/organizacion-donaton/ms-campanas) |
| Microservicio Donaciones | [ms-donaciones](https://github.com/organizacion-donaton/ms-donaciones) |
| Microservicio Voluntarios | [ms-voluntarios](https://github.com/organizacion-donaton/ms-voluntarios) |
| Eureka Server | [eureka-server](https://github.com/organizacion-donaton/eureka-server) |
| Panel Admin | [admin-panel](https://github.com/organizacion-donaton/admin-panel) |

---

<div align="center">

Hecho con ❤️ por el equipo **Donaton**

</div>
