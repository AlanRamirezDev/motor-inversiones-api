package com.portafolio.motortransaccional.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record InyeccionRequest(
        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto de inyección debe ser mayor a cero")
        BigDecimal monto
) {}