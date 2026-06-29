package cl.duoc.donaton.ms.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GatewaySecurityFilter extends OncePerRequestFilter {

    private static final String SECRET_HEADER = "X-Gateway-Secret";
    private static final String EXPECTED_SECRET = "DonatonGatewaySecret-777";
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().contains("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        String secret = request.getHeader(SECRET_HEADER);
        if (secret == null || !EXPECTED_SECRET.equals(secret)) {
            response.setStatus(403);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("status", 403);
            errorDetails.put("error", "FORBIDDEN");
            errorDetails.put("message", "Acceso denegado. La solicitud no proviene del API Gateway (BFF).");
            response.getWriter().write(mapper.writeValueAsString(errorDetails));
            return;
        }

        filterChain.doFilter(request, response);
    }
}
