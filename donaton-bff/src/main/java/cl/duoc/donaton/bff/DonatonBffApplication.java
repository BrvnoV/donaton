package cl.duoc.donaton.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // ◄ Conecta el BFF con el panel de Eureka para descubrir los microservicios por su nombre
public class DonatonBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonatonBffApplication.class, args);
	}

}