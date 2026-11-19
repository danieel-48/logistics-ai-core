package com.logistics.core.application.usecases;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.logistics.core.domain.model.Shipment;
import com.logistics.core.domain.ports.in.GetShipmentsUseCase;
import com.logistics.core.domain.ports.out.ShipmentRepositoryPort;

@Service
@Transactional(readOnly = true)
public class GetShipmentsService implements GetShipmentsUseCase {

    private final ShipmentRepositoryPort shipmentRepositoryPort;

    public GetShipmentsService(ShipmentRepositoryPort shipmentRepositoryPort) {
        this.shipmentRepositoryPort = shipmentRepositoryPort;
    }

    @Override
    public List<Shipment> getAllShipments() {
        return shipmentRepositoryPort.findAll();
    }

    @Override
    public Shipment getShipmentByContainer(String containerNumber) {
        return shipmentRepositoryPort.findByContainerNumber(containerNumber)
                .orElseThrow(() -> new IllegalArgumentException("Contenedor no encontrado: " + containerNumber));
    }
}