package cl.duoc.donaton.ms.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

@Component
@Slf4j
public class CampanaAdapter {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${campanas.service.url:http://ms-campanas}")
    private String campanasServiceUrl;

    public String obtenerEstadoCampana(Long campanaId) {
        log.info("Iniciando consulta externa a ms-campanas para validar campaña ID: {}", campanaId);
        try {
            Map response = webClientBuilder.build()
                    .get()
                    .uri(campanasServiceUrl + "/campanas/" + campanaId + "/estado")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && Boolean.TRUE.equals(response.get("existe"))) {
                String estado = (String) response.get("estado");
                log.info("Respuesta de validación externa recibida: existe=true, estado={}", estado);
                return estado;
            }
        } catch (Exception e) {
            log.error("Error al validar la campaña ID: {} con ms-campanas: {}", campanaId, e.getMessage());
        }
        return "UNKNOWN";
    }
}
