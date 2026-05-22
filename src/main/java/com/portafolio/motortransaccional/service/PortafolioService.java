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

        // 2. Buscamos el portafolio actual
        Portafolio portafolio = obtenerPortafolio(usuarioId);

        // 3. Sumamos el nuevo capital al balance existente de forma segura
        BigDecimal nuevoBalance = portafolio.getBalanceMxn().add(monto);
        portafolio.setBalanceMxn(nuevoBalance);

        // 4. Guardamos los cambios en BD
        return portafolioRepository.save(portafolio);
    }
    /**
     * Convierte una cantidad de MXN a USDC basado en un tipo de cambio proporcionado.
     */
    @Transactional
    public Portafolio comprarUsdc(Long usuarioId, BigDecimal montoMxn, BigDecimal tipoCambio) {
        // 1. Validaciones básicas
        if (montoMxn.compareTo(BigDecimal.ZERO) <= 0 || tipoCambio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto y el tipo de cambio deben ser mayores a cero.");
        }

        // 2. Obtener el portafolio
        Portafolio portafolio = obtenerPortafolio(usuarioId);

        // 3. Validar que tenga suficientes fondos en pesos
        if (portafolio.getBalanceMxn().compareTo(montoMxn) < 0) {
            throw new IllegalStateException("Saldo insuficiente en MXN para realizar la compra.");
        }

        // 4. Descontar los pesos (MXN)
        BigDecimal nuevoBalanceMxn = portafolio.getBalanceMxn().subtract(montoMxn);
        portafolio.setBalanceMxn(nuevoBalanceMxn);

        // 5. Calcular cuántos USDC se compran (montoMxn / tipoCambio)
        BigDecimal usdcComprados = montoMxn.divide(tipoCambio, 4, RoundingMode.HALF_UP);

        // 6. Sumar los USDC al balance
        BigDecimal nuevoBalanceUsdc = portafolio.getBalanceUsdc().add(usdcComprados);
        portafolio.setBalanceUsdc(nuevoBalanceUsdc);

        // 7. Guardar en la BD
        return portafolioRepository.save(portafolio);
    }
}