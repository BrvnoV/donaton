package cl.duoc.donaton.ms.service;

import cl.duoc.donaton.ms.model.Envio;
import cl.duoc.donaton.ms.repository.EnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;

    /**
     * Obtiene todos los despachos y seguimientos logísticos de la base de datos.
     */
    public List<Envio> listarTodos() {
        return envioRepository.findAll();
    }

    /**
     * Registra un nuevo despacho logístico en el sistema.
     */
    public Envio guardar(Envio envio) {
        return envioRepository.save(envio);
    }
}