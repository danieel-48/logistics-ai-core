package com.logistics.core.domain.ports.out;

import com.logistics.core.domain.model.Shipment;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepositoryPort {
    List<Shipment> findAll();
    Optional<Shipment> findByContainerNumber(String containerNumber);
}