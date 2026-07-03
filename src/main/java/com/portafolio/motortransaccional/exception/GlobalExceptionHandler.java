package com.portafolio.motortransaccional.exception;

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
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        /**
         * Obtiene el primer error de validación disponible en el BindingResult
         */
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String mensajeDefault = fieldError != null ? fieldError.getDefaultMessage() : "Error de validación en los datos de entrada";

        errorResponse.put("error", mensajeDefault);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}