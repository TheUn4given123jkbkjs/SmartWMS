package com.wms.dto.inventory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NearExpiryAlertDTO {
    private Long batchId;
    private String batchCode;
    private String skuCode;
    private String productName;
    private String locationCode;
    private LocalDate expiryDate;
    private Long daysRemaining;
    private BigDecimal currentQty;
    private String alertLevel;

    public NearExpiryAlertDTO() {}

    public NearExpiryAlertDTO(Long batchId, String batchCode, String skuCode, String productName, String locationCode, LocalDate expiryDate, Long daysRemaining, BigDecimal currentQty, String alertLevel) {
        this.batchId = batchId;
        this.batchCode = batchCode;
        this.skuCode = skuCode;
        this.productName = productName;
        this.locationCode = locationCode;
        this.expiryDate = expiryDate;
        this.daysRemaining = daysRemaining;
        this.currentQty = currentQty;
        this.alertLevel = alertLevel;
    }

    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }

    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }

    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Long getDaysRemaining() { return daysRemaining; }
    public void setDaysRemaining(Long daysRemaining) { this.daysRemaining = daysRemaining; }

    public BigDecimal getCurrentQty() { return currentQty; }
    public void setCurrentQty(BigDecimal currentQty) { this.currentQty = currentQty; }

    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
}

