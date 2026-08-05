package com.portafolio.motortransaccional.exception;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura las excepciones específicas de las reglas de negocio.
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, String>> handleBusinessExceptions(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    /**
     * Intercepta los fallos de validación de los DTOs.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {


        boolean isEn = "en".equals(LocaleContextHolder.getLocale().getLanguage());

        FieldError fieldError = ex.getBindingResult().getFieldError();

        String fallbackMsg = isEn
                ? "Validation error in input data"
                : "Error de validación en los datos de entrada";

        String mensajeDefault = fieldError != null ? fieldError.getDefaultMessage() : fallbackMsg;

        return ResponseEntity.badRequest().body(Map.of("error", mensajeDefault));
    }
}