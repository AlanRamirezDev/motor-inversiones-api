package com.portafolio.motortransaccional.service;

import com.portafolio.motortransaccional.model.Portafolio;
import com.portafolio.motortransaccional.repository.PortafolioRepository;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un portafolio para el usuario: " + usuarioId));
    }

    /**
     * Inyecta capital en pesos mexicanos (MXN) al portafolio del usuario.
     */
    @Transactional
    public Portafolio inyectarCapital(Long usuarioId, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto de inyección debe ser mayor a cero.");
        }

        /**
         * Congela la fila durante la escritura
         */
        Portafolio portafolio = portafolioRepository.findByUsuarioIdForUpdate(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un portafolio activo para el usuario: " + usuarioId));

        BigDecimal nuevoBalance = portafolio.getBalanceMxn().add(monto);
        portafolio.setBalanceMxn(nuevoBalance);

        return portafolioRepository.save(portafolio);
    }

    /**
     * Convierte una cantidad de MXN a USDC basado en un bloqueo de concurrencia ACID.
     */
    @Transactional
    public Portafolio comprarUsdc(Long usuarioId, BigDecimal montoMxn, BigDecimal tipoCambio) {
        if (montoMxn.compareTo(BigDecimal.ZERO) <= 0 || tipoCambio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto y el tipo de cambio deben ser mayores a cero.");
        }

        Portafolio portafolio = portafolioRepository.findByUsuarioIdForUpdate(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un portafolio activo para el usuario: " + usuarioId));

        if (portafolio.getBalanceMxn().compareTo(montoMxn) < 0) {
            throw new IllegalStateException("Saldo insuficiente para realizar la compra.");
        }

        BigDecimal nuevoBalanceMxn = portafolio.getBalanceMxn().subtract(montoMxn);
        portafolio.setBalanceMxn(nuevoBalanceMxn);

        BigDecimal usdcComprados = montoMxn.divide(tipoCambio, 4, RoundingMode.HALF_UP);
        BigDecimal nuevoBalanceUsdc = portafolio.getBalanceUsdc().add(usdcComprados);
        portafolio.setBalanceUsdc(nuevoBalanceUsdc);

        return portafolioRepository.save(portafolio);
    }

    /**
     * Restablece los balances a cero.
     */
    @Transactional
    public Portafolio reiniciarPortafolio(Long usuarioId) {
        Portafolio portafolio = portafolioRepository.findByUsuarioIdForUpdate(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un portafolio activo para el usuario: " + usuarioId));

        portafolio.setBalanceMxn(BigDecimal.ZERO);
        portafolio.setBalanceUsdc(BigDecimal.ZERO);

        return portafolioRepository.save(portafolio);
    }
}