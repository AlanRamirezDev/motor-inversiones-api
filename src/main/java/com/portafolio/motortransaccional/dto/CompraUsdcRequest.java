package com.portafolio.motortransaccional.dto;

import java.math.BigDecimal;

public record CompraUsdcRequest(
        BigDecimal montoMxn,
        BigDecimal tipoCambio
) {}