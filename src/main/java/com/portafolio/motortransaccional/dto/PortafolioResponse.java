package com.portafolio.motortransaccional.dto;

import java.math.BigDecimal;

// Este record define la estructura exacta del JSON que se envía como respuesta
public record PortafolioResponse(
        Long id,
        Long usuarioId,
        BigDecimal balanceMxn,
        BigDecimal balanceUsdc
) {}
