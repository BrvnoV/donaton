# 🤝 Donaton — Arquitectura de Microservicios para la Gestión de Ayuda Humanitaria

[![Java 17](https://img.shields.io/badge/Java-17-007396?style=flat&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?style=flat&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring WebFlux](https://img.shields.io/badge/Reactive-Spring%20WebFlux-green?style=flat)](https://docs.spring.io/spring-framework/reference/web/webflux.html)
[![Spring Cloud](https://img.shields.io/badge/Discovery-Eureka%20Server-blue?style=flat)](https://spring.io/projects/spring-cloud)
[![Resilience4j](https://img.shields.io/badge/Resilience-Circuit%20Breaker-red?style=flat)](https://resilience4j.readme.io/)
[![Docker](https://img.shields.io/badge/Deployment-Docker%20Compose-2496ED?style=flat&logo=docker&logoColor=white)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

**Donaton** es un sistema distribuido diseñado para la gestión y coordinación eficiente de ayuda humanitaria frente a emergencias y catástrofes. La arquitectura está construida sobre un paradigma de **microservicios reactivos** desacoplados, coordinados por un API Gateway con patrón **Backend For Frontend (BFF)** y descubrimiento dinámico de servicios.

---

## 🏗️ Arquitectura del Sistema

El ecosistema utiliza el patrón **BFF** como único punto de entrada para los clientes, abstrayendo la complejidad de la red de microservicios y gestionando la autenticación centralizada.

* **`donaton-bff`**: API Gateway y orquestador principal que canaliza el tráfico cliente.
* **`eureka-server`**: Servidor de descubrimiento dinámico de servicios (Spring Cloud Netflix Eureka).
* **`ms-campanas`**: Microservicio para la creación y seguimiento de campañas solidarias.
* **`ms-donaciones`**: Microservicio enfocado en el procesamiento reactivo de aportes y recursos.
* **`ms-voluntarios`**: Microservicio para el registro y asignación de personal voluntario.
* **`admin-panel`**: Interfaz de administración técnica del sistema.
* **`donaton-front`**: Cliente web enfocado en los usuarios finales.

---

## 🛠️ Stack Tecnológico

* **Lenguaje & Core:** Java 17 + Spring Boot 3.x
* **Programación Reactiva:** Spring WebFlux & Project Reactor
* **Descubrimiento de Servicios:** Spring Cloud Netflix Eureka
* **Resiliencia:** Resilience4j (Circuit Breaker & Retry patterns)
* **Seguridad:** JJWT (JSON Web Token) con validación a nivel de Gateway
* **Contenedorización:** Docker & Docker Compose
* **Pruebas y Cobertura:** JUnit 5, Mockito & JaCoCo (mínimo 60% de cobertura)

---

## 🔒 Seguridad y Resiliencia

* 🔐 **Validación JWT Centralizada:** Autenticación estricta en el BFF antes de delegar las peticiones a los servicios de dominio.
* ⚡ **Circuit Breaker:** Tolerancia a fallos con Resilience4j para evitar degradación en cascada si un microservicio cae.
* 🔄 **Concurrencia Reactiva:** Patrón *Scatter-Gather* para la orquestación concurrente de respuestas entre múltiples servicios sin bloquear hilos.

---

## 🚀 Despliegue e Instalación Rápida

### Requisitos Previos
* **Java 17** y **Maven 3.8+**
* **Docker** y **Docker Compose**

### Opción A: Despliegue con Docker Compose (Recomendado)
Para levantar la infraestructura completa (Servidor de descubrimiento, BFF, microservicios y bases de datos):

1. **Clonar el repositorio:**
   `git clone https://github.com/tu-usuario/donaton.git`
   `cd donaton`

2. **Iniciar los contenedores:**
   `docker-compose up -d`

3. **Verificar el Discovery Server:**
   Accede a Eureka Dashboard en: `http://localhost:8761`

---

## 👥 Colaboradores

* **Bruno Valenzuela** — *Backend & Software Architect* — [GitHub](https://github.com/tu-usuario) | [LinkedIn](https://linkedin.com/in/tu-perfil)
* **Rodrigo Garrido** — *Co-Developer*

---

## 📄 Licencia

Este proyecto está distribuido bajo la Licencia [MIT](LICENSE).
