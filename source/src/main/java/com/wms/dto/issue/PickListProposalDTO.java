package com.wms.dto.issue;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PickListProposalDTO {
    private Long productId;
    private String skuCode;
    private String productName;
    private Long batchId;
    private String batchCode;
    private String locationCode;
    private LocalDate expiryDate;
    private BigDecimal allocatedQty;

    public PickListProposalDTO() {}

    public PickListProposalDTO(Long productId, String skuCode, String productName, Long batchId, String batchCode, String locationCode, LocalDate expiryDate, BigDecimal allocatedQty) {
        this.productId = productId;
        this.skuCode = skuCode;
        this.productName = productName;
        this.batchId = batchId;
        this.batchCode = batchCode;
        this.locationCode = locationCode;
        this.expiryDate = expiryDate;
        this.allocatedQty = allocatedQty;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getBatchId() { return batchId; }
    public void setBatchId(Long batchId) { this.batchId = batchId; }

    public String getBatchCode() { return batchCode; }
    public void setBatchCode(String batchCode) { this.batchCode = batchCode; }

    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public BigDecimal getAllocatedQty() { return allocatedQty; }
    public void setAllocatedQty(BigDecimal allocatedQty) { this.allocatedQty = allocatedQty; }
}

