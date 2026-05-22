package com.portafolio.motortransaccional.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "portafolios")
public class Portafolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", unique = true, nullable = false)
    private Long usuarioId;

    @Column(name = "balance_mxn", precision = 19, scale = 4)
    private BigDecimal balanceMxn = BigDecimal.ZERO;

    @Column(name = "balance_usdc", precision = 19, scale = 4)
    private BigDecimal balanceUsdc = BigDecimal.ZERO;
}