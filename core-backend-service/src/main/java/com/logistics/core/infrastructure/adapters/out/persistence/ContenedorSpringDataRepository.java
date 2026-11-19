package com.logistics.core.infrastructure.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContenedorSpringDataRepository extends JpaRepository<ContenedorJpaEntity, Integer> {

    @Override
    @EntityGraph(attributePaths = {"detalles", "detalles.producto"})
    List<ContenedorJpaEntity> findAll();

    @EntityGraph(attributePaths = {"detalles", "detalles.producto"})
    Optional<ContenedorJpaEntity> findByNumeroContenedorIgnoreCase(String numeroContenedor);
}