package com.logistics.core.application.dto;

import com.logistics.core.domain.model.Shipment;
import com.logistics.core.domain.model.ShipmentStatus;

import java.time.LocalDate;
import java.util.List;

public class ShipmentResponseDto {
    private Long id;
    private String containerNumber;
    private String shippingLine;
    private String originPort;
    private String destinationPort;
    private ShipmentStatus status;
    private String statusDescription;
    private LocalDate departureDate;
    private LocalDate estimatedArrivalDate;
    private String customsNotes;
    private boolean delayedInCustoms;
    private List<ItemDto> items;

    public ShipmentResponseDto() {}

    public ShipmentResponseDto(Long id, String containerNumber, String shippingLine, String originPort,
                               String destinationPort, ShipmentStatus status, String statusDescription,
                               LocalDate departureDate, LocalDate estimatedArrivalDate, String customsNotes,
                               boolean delayedInCustoms, List<ItemDto> items) {
        this.id = id;
        this.containerNumber = containerNumber;
        this.shippingLine = shippingLine;
        this.originPort = originPort;
        this.destinationPort = destinationPort;
        this.status = status;
        this.statusDescription = statusDescription;
        this.departureDate = departureDate;
        this.estimatedArrivalDate = estimatedArrivalDate;
        this.customsNotes = customsNotes;
        this.delayedInCustoms = delayedInCustoms;
        this.items = items;
    }

    public static class ItemDto {
        private String purchaseOrder;
        private Integer quantity;
        private String lotNumber;
        private String productSku;
        private String productName;

        public ItemDto() {}

        public ItemDto(String purchaseOrder, Integer quantity, String lotNumber, String productSku, String productName) {
            this.purchaseOrder = purchaseOrder;
            this.quantity = quantity;
            this.lotNumber = lotNumber;
            this.productSku = productSku;
            this.productName = productName;
        }

        public String getPurchaseOrder() { return purchaseOrder; }
        public Integer getQuantity() { return quantity; }
        public String getLotNumber() { return lotNumber; }
        public String getProductSku() { return productSku; }
        public String getProductName() { return productName; }
    }

    public static ShipmentResponseDto fromDomain(Shipment shipment) {
        List<ItemDto> itemDtos = (shipment.getItems() == null) ? List.of() : shipment.getItems().stream()
                .map(item -> new ItemDto(
                        item.getPurchaseOrder(),
                        item.getQuantity(),
                        item.getLotNumber(),
                        item.getProductSku(),
                        item.getProductName()
                ))
                .toList();

        return new ShipmentResponseDto(
                shipment.getId(),
                shipment.getContainerNumber(),
                shipment.getShippingLine(),
                shipment.getOriginPort(),
                shipment.getDestinationPort(),
                shipment.getStatus(),
                shipment.getStatus() != null ? shipment.getStatus().getDescription() : "",
                shipment.getDepartureDate(),
                shipment.getEstimatedArrivalDate(),
                shipment.getCustomsNotes(),
                shipment.isDelayedInCustoms(),
                itemDtos
        );
    }

    public Long getId() { return id; }
    public String getContainerNumber() { return containerNumber; }
    public String getShippingLine() { return shippingLine; }
    public String getOriginPort() { return originPort; }
    public String getDestinationPort() { return destinationPort; }
    public ShipmentStatus getStatus() { return status; }
    public String getStatusDescription() { return statusDescription; }
    public LocalDate getDepartureDate() { return departureDate; }
    public LocalDate getEstimatedArrivalDate() { return estimatedArrivalDate; }
    public String getCustomsNotes() { return customsNotes; }
    public boolean isDelayedInCustoms() { return delayedInCustoms; }
    public List<ItemDto> getItems() { return items; }
}