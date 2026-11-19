package com.logistics.core.domain.model;

public enum ShipmentStatus {
    EN_TRANSITO_MARITIMO("En travesía marítima"),
    EN_INSPECCION_DIAN("En inspección aduanera y sanitaria"),
    NACIONALIZADO_EN_BODEGA("Levante aprobado y en bodega");
    private final String description;
    ShipmentStatus(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
