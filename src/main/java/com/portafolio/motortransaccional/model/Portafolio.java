package com.portafolio.motortransaccional.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "portafolios")
public class Portafolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", unique = true, nullable = false)
    private Long usuarioId;

    /**
     * Se añade nullable = false para asegurar la restricción e integridad en la BD
     */
    @Column(name = "balance_mxn", precision = 19, scale = 4, nullable = false)
    private BigDecimal balanceMxn = BigDecimal.ZERO;

    @Column(name = "balance_usdc", precision = 19, scale = 4, nullable = false)
    private BigDecimal balanceUsdc = BigDecimal.ZERO;
}