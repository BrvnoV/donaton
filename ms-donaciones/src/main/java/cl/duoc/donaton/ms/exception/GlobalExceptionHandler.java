package cl.duoc.donaton.ms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidResourceQuantityException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidResourceQuantity(InvalidResourceQuantityException ex) {
        return buildErrorResponse(ex.getMessage(), "BAD_REQUEST_BUSINESS", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClosedCampaignException.class)
    public ResponseEntity<Map<String, Object>> handleClosedCampaign(ClosedCampaignException ex) {
        return buildErrorResponse(ex.getMessage(), "BAD_REQUEST_BUSINESS", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse(ex.getMessage(), "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, String error, HttpStatus status) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_INSTANT));
        errorBody.put("status", status.value());
        errorBody.put("error", error);
        errorBody.put("message", message);
        return new ResponseEntity<>(errorBody, status);
    }
}
