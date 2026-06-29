package cl.duoc.donaton.ms.service;

import cl.duoc.donaton.ms.model.Donacion;
import cl.duoc.donaton.ms.repository.DonacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DonacionServiceTest {

    @Mock
    private DonacionRepository donacionRepository;

    @Mock
    private cl.duoc.donaton.ms.adapter.CampanaAdapter campanaAdapter;

    @InjectMocks
    private DonacionService donacionService;

    @Test
    public void testRegistrarDonacionExitoso() {
        Donacion donacion = new Donacion();
        donacion.setCantidad(100.0);
        donacion.setCampanaId(1L);

        when(campanaAdapter.obtenerEstadoCampana(1L)).thenReturn("ACTIVE");
        when(donacionRepository.save(any(Donacion.class))).thenReturn(donacion);

        Donacion resultado = donacionService.guardar(donacion);
        assertNotNull(resultado);
        assertEquals(100.0, resultado.getCantidad());
    }

    @Test
    public void testRegistrarDonacion_MontoNegativo_DebeLanzarExcepcion() {
        Donacion donacion = new Donacion();
        donacion.setCantidad(-50.0); // Monto negativo
        donacion.setCampanaId(1L);

        assertThrows(cl.duoc.donaton.ms.exception.InvalidResourceQuantityException.class, () -> {
            donacionService.guardar(donacion);
        });
        
        verify(donacionRepository, never()).save(any(Donacion.class));
    }
}
