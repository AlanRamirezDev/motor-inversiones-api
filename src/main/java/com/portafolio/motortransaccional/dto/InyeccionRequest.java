package com.portafolio.motortransaccional.dto;

import java.math.BigDecimal;

// Este record define la estructura del JSON que el usuario enviará para depositar
public record InyeccionRequest(
        BigDecimal monto
) {}
