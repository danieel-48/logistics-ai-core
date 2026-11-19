package com.logistics.core.domain.model;

import java.time.LocalDate;
import java.util.List;

public class Shipment {
    private Long id;
    private String containerNumber;
    private String shippingLine;
    private String originPort;
    private String destinationPort;
    private ShipmentStatus status;
    private LocalDate departureDate;
    private LocalDate estimatedArrivalDate;
    private String customsNotes;
    private List<ShipmentItem> items;

    public Shipment() {}

    public Shipment(Long id, String containerNumber, String shippingLine, String originPort,
                    String destinationPort, ShipmentStatus status, LocalDate departureDate,
                    LocalDate estimatedArrivalDate, String customsNotes, List<ShipmentItem> items) {
        this.id = id;
        this.containerNumber = containerNumber;
        this.shippingLine = shippingLine;
        this.originPort = originPort;
        this.destinationPort = destinationPort;
        this.status = status;
        this.departureDate = departureDate;
        this.estimatedArrivalDate = estimatedArrivalDate;
        this.customsNotes = customsNotes;
        this.items = items;
    }

    public boolean isDelayedInCustoms() {
        return ShipmentStatus.EN_INSPECCION_DIAN.equals(this.status);
    }

    // Getters explícitos: Cero errores en cualquier IDE
    public Long getId() { return id; }
    public String getContainerNumber() { return containerNumber; }
    public String getShippingLine() { return shippingLine; }
    public String getOriginPort() { return originPort; }
    public String getDestinationPort() { return destinationPort; }
    public ShipmentStatus getStatus() { return status; }
    public LocalDate getDepartureDate() { return departureDate; }
    public LocalDate getEstimatedArrivalDate() { return estimatedArrivalDate; }
    public String getCustomsNotes() { return customsNotes; }
    public List<ShipmentItem> getItems() { return items; }
}