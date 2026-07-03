package com.portafolio.motortransaccional.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CompraUsdcRequest(
        @NotNull(message = "El monto en MXN es obligatorio")
        @Positive(message = "El monto a usar debe ser mayor a cero")
        BigDecimal montoMxn,

        @NotNull(message = "El tipo de cambio es obligatorio")
        @Positive(message = "El tipo de cambio debe ser mayor a cero")
        BigDecimal tipoCambio
) {}