package cl.duoc.donaton.ms.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Component
@Slf4j
public class CampanaAdapter {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public boolean esCampanaValida(Long campanaId) {
        log.info("Iniciando consulta externa a ms-campanas para validar campaña ID: {}", campanaId);
        try {
            Map response = webClientBuilder.build()
                    .get()
                    .uri("http://ms-campanas/campanas/" + campanaId + "/estado")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null) {
                Boolean existe = (Boolean) response.get("existe");
                Boolean valida = (Boolean) response.get("valida");
                log.info("Respuesta de validación externa recibida: existe={}, valida={}", existe, valida);
                return Boolean.TRUE.equals(existe) && Boolean.TRUE.equals(valida);
            }
        } catch (Exception e) {
            log.error("Error al validar la campaña ID: {} con ms-campanas: {}", campanaId, e.getMessage());
        }
        return false;
    }
}
