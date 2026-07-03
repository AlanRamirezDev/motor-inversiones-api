package com.portafolio.motortransaccional.dto;

import com.portafolio.motortransaccional.model.Portafolio;
import java.math.BigDecimal;

public record PortafolioResponse(
        Long id,
        Long usuarioId,
        BigDecimal balanceMxn,
        BigDecimal balanceUsdc
) {
    /**
     * Construye un DTO de respuesta a partir de la entidad de persistencia.
     */
    public static PortafolioResponse from(Portafolio portafolio) {
        return new PortafolioResponse(
                portafolio.getId(),
                portafolio.getUsuarioId(),
                portafolio.getBalanceMxn(),
                portafolio.getBalanceUsdc()
        );
    }
}