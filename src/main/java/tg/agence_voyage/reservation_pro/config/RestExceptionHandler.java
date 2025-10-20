package tg.agence_voyage.reservation_pro.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Validation Error");
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        errors.put("details", fieldErrors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.NOT_FOUND.value());
        errors.put("error", "Entity Not Found");
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Bad Request");
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errors.put("error", "Internal Server Error");
        errors.put("message", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Exception personnalisée pour les erreurs 404
    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    // Nouvelle exception personnalisée pour les erreurs 400
    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }
}