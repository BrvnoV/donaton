package cl.duoc.donaton.ms.service;

import cl.duoc.donaton.ms.model.Voluntario;
import cl.duoc.donaton.ms.repository.VoluntarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VoluntarioServiceTest {

    @Mock
    private VoluntarioRepository voluntarioRepository;

    @Mock
    private cl.duoc.donaton.ms.adapter.CampanaAdapter campanaAdapter;

    @InjectMocks
    private VoluntarioService voluntarioService;

    @Test
    public void testInscribirVoluntarioExitoso() {
        Voluntario voluntario = new Voluntario();
        voluntario.setCorreo("test@test.com");
        voluntario.setNombre("Test");

        when(campanaAdapter.esCampanaValida(1L)).thenReturn(true);
        when(voluntarioRepository.existsByCorreo("test@test.com")).thenReturn(false);
        when(voluntarioRepository.save(any(Voluntario.class))).thenReturn(voluntario);

        Voluntario resultado = voluntarioService.registrarEnCampana(voluntario, 1L);
        assertNotNull(resultado);
        assertEquals("test@test.com", resultado.getCorreo());
    }

    @Test
    public void testRegistrarVoluntario_EmailDuplicado_DebeLanzarExcepcion() {
        Voluntario voluntario = new Voluntario();
        voluntario.setCorreo("duplicado@test.com");
        voluntario.setNombre("Test");

        when(campanaAdapter.esCampanaValida(1L)).thenReturn(true);
        when(voluntarioRepository.existsByCorreo("duplicado@test.com")).thenReturn(true);

        assertThrows(cl.duoc.donaton.ms.exception.DuplicateVolunteerException.class, () -> {
            voluntarioService.registrarEnCampana(voluntario, 1L);
        });
        
        verify(voluntarioRepository, never()).save(any(Voluntario.class));
    }

    @Test
    public void testListarTodos() {
        java.util.List<Voluntario> mockList = java.util.Arrays.asList(new Voluntario(), new Voluntario());
        when(voluntarioRepository.findAll()).thenReturn(mockList);
        java.util.List<Voluntario> result = voluntarioService.listarTodos();
        assertEquals(2, result.size());
    }

    @Test
    public void testObtenerPorCampana() {
        java.util.List<Voluntario> mockList = java.util.Arrays.asList(new Voluntario());
        when(voluntarioRepository.findByCampanaId(1L)).thenReturn(mockList);
        java.util.List<Voluntario> result = voluntarioService.obtenerPorCampana(1L);
        assertEquals(1, result.size());
    }
}
