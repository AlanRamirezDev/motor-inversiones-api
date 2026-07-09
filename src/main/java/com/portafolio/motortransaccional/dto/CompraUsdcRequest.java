package com.portafolio.motortransaccional.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CompraUsdcRequest(
        @NotNull(message = "{compra.montoMxn.notnull}")
        @Positive(message = "{compra.montoMxn.positive}")
        BigDecimal montoMxn,

        @NotNull(message = "{compra.tipoCambio.notnull}")
        @Positive(message = "{compra.tipoCambio.positive}")
        BigDecimal tipoCambio
) {}