package cl.duoc.donaton.bff.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.jsonwebtoken.ExpiredJwtException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BffSecurityTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    @Test
    public void testValidarToken_TokenExpiradoOInvalido_DebeRechazar() {
        String tokenInvalido = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalido.signature";
        
        boolean isValid = jwtUtil.validateToken(tokenInvalido);
        
        assertFalse(isValid, "Un token corrupto o inválido debe ser rechazado y retornar false");
    }

    @Test
    public void testValidarToken_TokenValido_DebeAceptar() {
        String token = jwtUtil.generateToken("admin");
        boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid, "Un token generado válidamente debe ser aceptado");
        assertEquals("admin", jwtUtil.getUsernameFromToken(token));
    }
}
