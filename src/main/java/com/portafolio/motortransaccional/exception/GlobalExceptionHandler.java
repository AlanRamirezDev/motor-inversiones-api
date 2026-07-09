package com.portafolio.motortransaccional.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura las excepciones específicas de las reglas de negocio.
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> handleBusinessExceptions(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Intercepta los fallos de validación
     * @param request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errorResponse = new HashMap<>();

        /**
         * Capturar idioma
         */
        boolean isEn = "en".equals(request.getHeader("Accept-Language"));

        /**
         * Obtiene el primer error de validación disponible en el BindingResult.
         */
        FieldError fieldError = ex.getBindingResult().getFieldError();

        String fallbackMsg = isEn
                ? "Validation error in input data"
                : "Error de validación en los datos de entrada";

        String mensajeDefault = fieldError != null ? fieldError.getDefaultMessage() : fallbackMsg;

        errorResponse.put("error", mensajeDefault);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}