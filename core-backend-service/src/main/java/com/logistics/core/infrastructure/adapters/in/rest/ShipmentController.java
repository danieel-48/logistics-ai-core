package com.logistics.core.infrastructure.adapters.in.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.logistics.core.application.dto.ShipmentResponseDto;
import com.logistics.core.domain.ports.in.GetShipmentsUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/shipments")
@Tag(name = "Shipments & Containers", description = "Gestión de contenedores marítimos e inventario en tránsito")
public class ShipmentController {

    private final GetShipmentsUseCase getShipmentsUseCase;

    public ShipmentController(GetShipmentsUseCase getShipmentsUseCase) {
        this.getShipmentsUseCase = getShipmentsUseCase;
    }

    @GetMapping
    @Operation(summary = "Listar todos los contenedores y su manifiesto de carga")
    public ResponseEntity<List<ShipmentResponseDto>> getAllShipments() {
        List<ShipmentResponseDto> shipments = getShipmentsUseCase.getAllShipments().stream()
                .map(ShipmentResponseDto::fromDomain)
                .toList();
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/{containerNumber}")
    @Operation(summary = "Consultar el detalle de un contenedor por su número de serie")
    public ResponseEntity<ShipmentResponseDto> getShipmentByContainer(@PathVariable String containerNumber) {
        ShipmentResponseDto shipment = ShipmentResponseDto.fromDomain(
                getShipmentsUseCase.getShipmentByContainer(containerNumber)
        );
        return ResponseEntity.ok(shipment);
    }
}