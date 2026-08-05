package com.portafolio.motortransaccional.service;

import com.portafolio.motortransaccional.model.Portafolio;
import com.portafolio.motortransaccional.repository.PortafolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PortafolioService {

    private final PortafolioRepository portafolioRepository;

    /**
     * Crea un nuevo portafolio en cero para un usuario.
     */
    @Transactional
    public Portafolio inicializarPortafolio(Long usuarioId) {
        Portafolio nuevoPortafolio = new Portafolio();
        nuevoPortafolio.setUsuarioId(usuarioId);
        return portafolioRepository.save(nuevoPortafolio);
    }

    /**
     * Busca y devuelve el portafolio de un usuario para operaciones de solo lectura.
     */
    @Transactional(readOnly = true)
    public Portafolio obtenerPortafolio(Long usuarioId) {
        return portafolioRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        obtenerMensaje(
                                "No portfolio found for user: " + usuarioId,
                                "No se encontró un portafolio para el usuario: " + usuarioId
                        )
                ));
    }

    /**
     * Inyecta capital en pesos mexicanos (MXN) al portafolio del usuario.
     */
    @Transactional
    public Portafolio inyectarCapital(Long usuarioId, BigDecimal monto) {
        validarMontoPositivo(monto,
                "The injection amount must be greater than zero.",
                "El monto de inyección debe ser mayor a cero.");

        Portafolio portafolio = obtenerPortafolioParaActualizacion(usuarioId);

        portafolio.setBalanceMxn(portafolio.getBalanceMxn().add(monto));

        return portafolioRepository.save(portafolio);
    }

    /**
     * Convierte una cantidad de MXN a USDC basado en un bloqueo de concurrencia ACID.
     */
    @Transactional
    public Portafolio comprarUsdc(Long usuarioId, BigDecimal montoMxn, BigDecimal tipoCambio) {
        validarMontoPositivo(montoMxn,
                "The amount must be greater than zero.",
                "El monto debe ser mayor a cero.");

        validarMontoPositivo(tipoCambio,
                "The exchange rate must be greater than zero.",
                "El tipo de cambio debe ser mayor a cero.");

        Portafolio portafolio = obtenerPortafolioParaActualizacion(usuarioId);

        if (portafolio.getBalanceMxn().compareTo(montoMxn) < 0) {
            throw new IllegalStateException(
                    obtenerMensaje("Insufficient balance to perform the purchase.", "Saldo insuficiente para realizar la compra.")
            );
        }

        portafolio.setBalanceMxn(portafolio.getBalanceMxn().subtract(montoMxn));

        BigDecimal usdcComprados = montoMxn.divide(tipoCambio, 4, RoundingMode.HALF_UP);
        portafolio.setBalanceUsdc(portafolio.getBalanceUsdc().add(usdcComprados));

        return portafolioRepository.save(portafolio);
    }

    /**
     * Restablece los balances a cero.
     */
    @Transactional
    public Portafolio reiniciarPortafolio(Long usuarioId) {
        Portafolio portafolio = obtenerPortafolioParaActualizacion(usuarioId);

        portafolio.setBalanceMxn(BigDecimal.ZERO);
        portafolio.setBalanceUsdc(BigDecimal.ZERO);

        return portafolioRepository.save(portafolio);
    }

    /**
     * Obtiene el portafolio aplicando el bloqueo pesimista en base de datos.
     */
    private Portafolio obtenerPortafolioParaActualizacion(Long usuarioId) {
        return portafolioRepository.findByUsuarioIdForUpdate(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException(
                        obtenerMensaje(
                                "No active portfolio found for user: " + usuarioId,
                                "No se encontró un portafolio activo para el usuario: " + usuarioId
                        )
                ));
    }

    /**
     * Centraliza la validacion defensiva de montos financieros.
     */
    private void validarMontoPositivo(BigDecimal monto, String msgEn, String msgEs) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(obtenerMensaje(msgEn, msgEs));
        }
    }

    /**
     * Determina dinamicamente el mensaje basado en el idioma de la petición HTTP.
     */
    private String obtenerMensaje(String msgEn, String msgEs) {
        boolean isEn = "en".equals(LocaleContextHolder.getLocale().getLanguage());
        return isEn ? msgEn : msgEs;
    }
}