package cl.duoc.donaton.bff.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper mapper = new ObjectMapper();

    private final List<String> PROTECTED_METHODS = List.of("POST", "PUT", "DELETE");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isPublicPost = request.getMethod().equalsIgnoreCase("POST") && 
            (request.getRequestURI().endsWith("/api/bff/voluntarios") || 
             request.getRequestURI().endsWith("/api/bff/donaciones"));

        if (PROTECTED_METHODS.contains(request.getMethod().toUpperCase()) && 
            !request.getRequestURI().contains("/auth/login") &&
            !isPublicPost) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                abortWith401(response, "Token JWT ausente o formato inválido");
                return;
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                abortWith401(response, "Token JWT inválido o expirado");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void abortWith401(HttpServletResponse response, String message) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", 401);
        errorDetails.put("error", "UNAUTHORIZED");
        errorDetails.put("message", message);
        response.getWriter().write(mapper.writeValueAsString(errorDetails));
    }
}
