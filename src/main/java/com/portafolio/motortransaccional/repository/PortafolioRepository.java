package com.portafolio.motortransaccional.repository;

import com.portafolio.motortransaccional.model.Portafolio;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortafolioRepository extends JpaRepository<Portafolio, Long> {

    /**
     * Operaciones de solo lectura (Health Checks, Dashboard GET)
     */
    Optional<Portafolio> findByUsuarioId(Long usuarioId);

    /**
     * Bloqueo base de datos hasta que la transacción HTTP finalice.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Portafolio p WHERE p.usuarioId = :usuarioId")
    Optional<Portafolio> findByUsuarioIdForUpdate(@Param("usuarioId") Long usuarioId);
}