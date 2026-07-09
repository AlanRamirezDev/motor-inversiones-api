package com.portafolio.motortransaccional.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record InyeccionRequest(
        @NotNull(message = "{inyeccion.monto.notnull}")
        @Positive(message = "{inyeccion.monto.positive}")
        BigDecimal monto
) {}