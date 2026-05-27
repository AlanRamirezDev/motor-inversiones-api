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

    // Inyección de dependencias
    private final PortafolioRepository portafolioRepository;

    /**
     * Crea un nuevo portafolio en cero para un usuario.
     */
    @Transactional
    public Portafolio inicializarPortafolio(Long usuarioId) {
        Portafolio nuevoPortafolio = new Portafolio();
        nuevoPortafolio.setUsuarioId(usuarioId);

        // El repositorio toma el objeto y lo guarda en la base
        return portafolioRepository.save(nuevoPortafolio);
    }
    /**
     * Busca y devuelve el portafolio de un usuario.
     */
    public Portafolio obtenerPortafolio(Long usuarioId) {
        return portafolioRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("No se encontró un portafolio para el usuario: " + usuarioId));
    }
    /**
     * Inyecta capital en pesos mexicanos (MXN) al portafolio del usuario.
     */
    @Transactional
    public Portafolio inyectarCapital(Long usuarioId, BigDecimal monto) {
        // 1. Validación de negocio: El monto debe ser positivo
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto de inyección debe ser mayor a cero.");
        }

        // Buscamos el portafolio actual
        Portafolio portafolio = obtenerPortafolio(usuarioId);

        // Sumamos el nuevo capital al balance existente de forma segura
        BigDecimal nuevoBalance = portafolio.getBalanceMxn().add(monto);
        portafolio.setBalanceMxn(nuevoBalance);

        // Guardamos los cambios en BD
        return portafolioRepository.save(portafolio);
    }
    /**
     * Convierte una cantidad de MXN a USDC basado en un tipo de cambio proporcionado.
     */
    @Transactional
    public Portafolio comprarUsdc(Long usuarioId, BigDecimal montoMxn, BigDecimal tipoCambio) {
        // Validaciones básicas
        if (montoMxn.compareTo(BigDecimal.ZERO) <= 0 || tipoCambio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto y el tipo de cambio deben ser mayores a cero.");
        }

        // Obtener el portafolio
        Portafolio portafolio = obtenerPortafolio(usuarioId);

        // Validar que tenga suficientes fondos en pesos
        if (portafolio.getBalanceMxn().compareTo(montoMxn) < 0) {
            throw new IllegalStateException("Saldo insuficiente para realizar la compra.");
        }

        // Descontar los pesos (MXN)
        BigDecimal nuevoBalanceMxn = portafolio.getBalanceMxn().subtract(montoMxn);
        portafolio.setBalanceMxn(nuevoBalanceMxn);

        // Calcular cuántos USDC se compran (montoMxn / tipoCambio)
        BigDecimal usdcComprados = montoMxn.divide(tipoCambio, 4, RoundingMode.HALF_UP);

        // Sumar los USDC al balance
        BigDecimal nuevoBalanceUsdc = portafolio.getBalanceUsdc().add(usdcComprados);
        portafolio.setBalanceUsdc(nuevoBalanceUsdc);

        // Guardar en la BD
        return portafolioRepository.save(portafolio);
    }

    @Transactional
    public Portafolio reiniciarPortafolio(Long usuarioId) {
        Portafolio portafolio = obtenerPortafolio(usuarioId);
        portafolio.setBalanceMxn(BigDecimal.ZERO);
        portafolio.setBalanceUsdc(BigDecimal.ZERO);
        return portafolioRepository.save(portafolio);
    }
}