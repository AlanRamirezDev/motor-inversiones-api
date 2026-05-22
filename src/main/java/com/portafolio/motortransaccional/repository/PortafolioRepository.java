package com.portafolio.motortransaccional.repository;

import com.portafolio.motortransaccional.model.Portafolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortafolioRepository extends JpaRepository<Portafolio, Long> {
    Optional<Portafolio> findByUsuarioId(Long usuarioId);
}