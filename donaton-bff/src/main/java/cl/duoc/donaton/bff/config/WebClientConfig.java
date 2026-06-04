package cl.duoc.donaton.bff.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced // ◄ Permite resolver los nombres lógicos de los microservicios automáticamente
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}