package com.logistics.core.domain.ports.in;

import com.logistics.core.domain.model.Shipment;
import java.util.List;


public interface GetShipmentsUseCase {
    List<Shipment> getAllShipments();
    Shipment getShipmentByContainer(String containerNumber);
}