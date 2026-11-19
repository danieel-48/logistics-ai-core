package com.logistics.core.infrastructure.adapters.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.logistics.core.domain.model.Shipment;
import com.logistics.core.domain.ports.out.ShipmentRepositoryPort;

@Component
public class ShipmentJpaAdapter implements ShipmentRepositoryPort {

    private final ContenedorSpringDataRepository repository;
    private final ShipmentPersistenceMapper mapper;

    public ShipmentJpaAdapter(ContenedorSpringDataRepository repository, ShipmentPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Shipment> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Shipment> findByContainerNumber(String containerNumber) {
        return repository.findByNumeroContenedorIgnoreCase(containerNumber)
                .map(mapper::toDomain);
    }
}