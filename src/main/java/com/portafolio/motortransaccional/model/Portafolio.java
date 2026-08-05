package com.portafolio.motortransaccional.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "portafolios")
public class Portafolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "usuario_id", unique = true, nullable = false)
    private Long usuarioId;

    @Setter
    @Column(name = "balance_mxn", precision = 19, scale = 4, nullable = false)
    private BigDecimal balanceMxn = BigDecimal.ZERO;

    @Setter
    @Column(name = "balance_usdc", precision = 19, scale = 4, nullable = false)
    private BigDecimal balanceUsdc = BigDecimal.ZERO;

    // Preparar la entidad para encapsular su propia lógica de negocio.

    public void adicionarMxn(BigDecimal monto) {
        this.balanceMxn = this.balanceMxn.add(monto);
    }

    public void deducirMxn(BigDecimal monto) {
        this.balanceMxn = this.balanceMxn.subtract(monto);
    }

    public void adicionarUsdc(BigDecimal monto) {
        this.balanceUsdc = this.balanceUsdc.add(monto);
    }

    public void reiniciarBalances() {
        this.balanceMxn = BigDecimal.ZERO;
        this.balanceUsdc = BigDecimal.ZERO;
    }
}