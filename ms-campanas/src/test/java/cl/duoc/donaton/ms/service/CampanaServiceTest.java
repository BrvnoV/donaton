package cl.duoc.donaton.ms.service;

import cl.duoc.donaton.ms.model.Campana;
import cl.duoc.donaton.ms.repository.CampanaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CampanaServiceTest {

    @Mock
    private CampanaRepository campanaRepository;

    @InjectMocks
    private CampanaService campanaService;

    @Test
    public void testListarTodas() {
        Campana campana1 = new Campana();
        campana1.setId(1L);
        campana1.setNombre("Campaña 1");
        campana1.setEstado("ACTIVE");

        Campana campana2 = new Campana();
        campana2.setId(2L);
        campana2.setNombre("Campaña 2");
        campana2.setEstado("PLANNED");

        when(campanaRepository.findAll()).thenReturn(Arrays.asList(campana1, campana2));

        List<Campana> resultado = campanaService.listarTodas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Campaña 1", resultado.get(0).getNombre());
        verify(campanaRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerPorId() {
        Campana campana = new Campana();
        campana.setId(1L);
        campana.setNombre("Campaña Test");

        when(campanaRepository.findById(1L)).thenReturn(Optional.of(campana));

        Optional<Campana> resultado = campanaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Campaña Test", resultado.get().getNombre());
        verify(campanaRepository, times(1)).findById(1L);
    }

    @Test
    public void testCrearCampana() {
        Campana input = new Campana();
        input.setNombre("Nueva Campaña");
        input.setDescripcion("Descripción");

        Campana output = new Campana();
        output.setId(10L);
        output.setNombre("Nueva Campaña");
        output.setDescripcion("Descripción");
        output.setEstado("PLANNED");

        when(campanaRepository.save(any(Campana.class))).thenReturn(output);

        Campana resultado = campanaService.crearCampana(input);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("PLANNED", resultado.getEstado());
        verify(campanaRepository, times(1)).save(any(Campana.class));
    }

    @Test
    public void testActualizarEstadoExitoso() {
        Campana campana = new Campana();
        campana.setId(1L);
        campana.setNombre("Campaña Test");
        campana.setEstado("PLANNED");

        when(campanaRepository.findById(1L)).thenReturn(Optional.of(campana));
        when(campanaRepository.save(any(Campana.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Campana resultado = campanaService.actualizarEstado(1L, "active");

        assertNotNull(resultado);
        assertEquals("ACTIVE", resultado.getEstado());
        verify(campanaRepository, times(1)).findById(1L);
        verify(campanaRepository, times(1)).save(any(Campana.class));
    }

    @Test
    public void testActualizarEstadoNoEncontrada() {
        when(campanaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            campanaService.actualizarEstado(1L, "active");
        });

        verify(campanaRepository, times(1)).findById(1L);
        verify(campanaRepository, never()).save(any(Campana.class));
    }
}
