package com.portafolio.motortransaccional.controller;

import com.portafolio.motortransaccional.dto.InyeccionRequest;
import com.portafolio.motortransaccional.dto.PortafolioResponse;
import com.portafolio.motortransaccional.model.Portafolio;
import com.portafolio.motortransaccional.service.PortafolioService;
import com.portafolio.motortransaccional.dto.CompraUsdcRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/portafolios")
@RequiredArgsConstructor
public class PortafolioController {

    private final PortafolioService portafolioService;

    /**
     * Inicializa un portafolio para un usuario específico.
     */
    @PostMapping("/inicializar/{usuarioId}")
    public ResponseEntity<PortafolioResponse> inicializar(@PathVariable Long usuarioId) {
        Portafolio portafolio = portafolioService.inicializarPortafolio(usuarioId);
        return new ResponseEntity<>(PortafolioResponse.from(portafolio), HttpStatus.CREATED);
    }

    /**
     * Obtiene los balances actuales del portafolio del usuario.
     */
    @GetMapping("/{usuarioId}")
    public ResponseEntity<PortafolioResponse> obtener(@PathVariable Long usuarioId) {
        Portafolio portafolio = portafolioService.obtenerPortafolio(usuarioId);
        return new ResponseEntity<>(PortafolioResponse.from(portafolio), HttpStatus.OK);
    }

    /**
     * Modifica el balance sumando capital de fondos.
     * PUT para la actualización de un recurso existente.
     */
    @PutMapping("/{usuarioId}/inyeccion")
    public ResponseEntity<PortafolioResponse> inyectarCapital(
            @PathVariable Long usuarioId,
            @Valid @RequestBody InyeccionRequest request) {

        Portafolio portafolioActualizado = portafolioService.inyectarCapital(usuarioId, request.monto());
        return new ResponseEntity<>(PortafolioResponse.from(portafolioActualizado), HttpStatus.OK);
    }

    /**
     * Modifica los balances ejecutando el intercambio de MXN a USDC.
     * PUT para mutación de recursos financieros.
     */
    @PutMapping("/{usuarioId}/comprar-usdc")
    public ResponseEntity<PortafolioResponse> comprarUsdc(
            @PathVariable Long usuarioId,
            @Valid @RequestBody CompraUsdcRequest request) {

        Portafolio portafolioActualizado = portafolioService.comprarUsdc(
                usuarioId,
                request.montoMxn(),
                request.tipoCambio()
        );

        return new ResponseEntity<>(PortafolioResponse.from(portafolioActualizado), HttpStatus.OK);
    }

    /**
     * Restablece los balances del portafolio a valores iniciales (Cero).
     * PUT para asegurar comportamiento idempotente en reinicios de estado.
     */
    @PutMapping("/{usuarioId}/reiniciar")
    public ResponseEntity<PortafolioResponse> reiniciarPortafolio(@PathVariable Long usuarioId) {
        Portafolio portafolioActualizado = portafolioService.reiniciarPortafolio(usuarioId);
        return new ResponseEntity<>(PortafolioResponse.from(portafolioActualizado), HttpStatus.OK);
    }
}
