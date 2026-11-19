package com.logistics.core.domain.model;

public class ShipmentItem {
    private Long id;
    private String purchaseOrder;
    private Integer quantity;
    private String lotNumber;
    private String productSku;
    private String productName;

    public ShipmentItem() {}

    public ShipmentItem(Long id, String purchaseOrder, Integer quantity, String lotNumber,
                        String productSku, String productName) {
        this.id = id;
        this.purchaseOrder = purchaseOrder;
        this.quantity = quantity;
        this.lotNumber = lotNumber;
        this.productSku = productSku;
        this.productName = productName;
    }

    public Long getId() { return id; }
    public String getPurchaseOrder() { return purchaseOrder; }
    public Integer getQuantity() { return quantity; }
    public String getLotNumber() { return lotNumber; }
    public String getProductSku() { return productSku; }
    public String getProductName() { return productName; }
}