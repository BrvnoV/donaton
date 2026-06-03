**# Arquetipo Base Backend - Donaton**  
   
Este arquetipo Maven sirve como plantilla estandarizada para la creación de nuevos microservicios de negocio dentro de la plataforma Donaton. Asegura que cada nuevo servicio cuente con la arquitectura en capas (Controller, Service, Repository, Model) y las dependencias base (Eureka Client, JPA, Actuator).  
   
**## Instrucciones de uso**  
   
Para generar un nuevo microservicio a partir de este molde, sitúese en la carpeta del proyecto y ejecute en su terminal:  
   
mvn archetype:generate -DarchetypeGroupId=cl.duoc.donaton -DarchetypeArtifactId=donaton-ms-base-archetype -DarchetypeVersion=0.0.1-SNAPSHOT  
