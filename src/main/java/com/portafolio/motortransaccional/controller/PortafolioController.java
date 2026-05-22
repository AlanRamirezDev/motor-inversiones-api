package com.portafolio.motortransaccional.controller;

import com.portafolio.motortransaccional.dto.InyeccionRequest;
import com.portafolio.motortransaccional.dto.PortafolioResponse;
import com.portafolio.motortransaccional.model.Portafolio;
import com.portafolio.motortransaccional.service.PortafolioService;
import com.portafolio.motortransaccional.dto.CompraUsdcRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/portafolios")
@RequiredArgsConstructor
public class PortafolioController {

    private final PortafolioService portafolioService;

    @PostMapping("/inicializar/{usuarioId}")
    public ResponseEntity<PortafolioResponse> inicializar(@PathVariable Long usuarioId) {
        Portafolio portafolio = portafolioService.inicializarPortafolio(usuarioId);
        return new ResponseEntity<>(mapearAResponse(portafolio), HttpStatus.CREATED);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<PortafolioResponse> obtener(@PathVariable Long usuarioId) {
        Portafolio portafolio = portafolioService.obtenerPortafolio(usuarioId);
        return new ResponseEntity<>(mapearAResponse(portafolio), HttpStatus.OK);
    }

    /**
     * Endpoint para inyectar capital.
     * Ejemplo de uso: POST http://localhost:8080/api/v1/portafolios/1/inyeccion
     */
    @PostMapping("/{usuarioId}/inyeccion")
    public ResponseEntity<PortafolioResponse> inyectarCapital(
            @PathVariable Long usuarioId,
            @RequestBody InyeccionRequest request) {

        Portafolio portafolioActualizado = portafolioService.inyectarCapital(usuarioId, request.monto());
        return new ResponseEntity<>(mapearAResponse(portafolioActualizado), HttpStatus.OK);
    }

    /**
     * Endpoint para comprar USDC usando el balance en MXN.
     * Ejemplo de uso: POST http://localhost:8080/api/v1/portafolios/1/comprar-usdc
     */
    @PostMapping("/{usuarioId}/comprar-usdc")
    public ResponseEntity<PortafolioResponse> comprarUsdc(
            @PathVariable Long usuarioId,
            @RequestBody CompraUsdcRequest request) {

        Portafolio portafolioActualizado = portafolioService.comprarUsdc(
                usuarioId,
                request.montoMxn(),
                request.tipoCambio()
        );

        return new ResponseEntity<>(mapearAResponse(portafolioActualizado), HttpStatus.OK);
    }

    // Método auxiliar (Mapper) para convertir la Entidad en un DTO limpio
    private PortafolioResponse mapearAResponse(Portafolio portafolio) {
        return new PortafolioResponse(
                portafolio.getId(),
                portafolio.getUsuarioId(),
                portafolio.getBalanceMxn(),
                portafolio.getBalanceUsdc()
        );
    }
}
