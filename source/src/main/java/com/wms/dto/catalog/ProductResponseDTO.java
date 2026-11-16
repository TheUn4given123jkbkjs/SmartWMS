package com.wms.dto.catalog;

import java.math.BigDecimal;

public class ProductResponseDTO {
    private Long id;
    private String skuCode;
    private String name;
    private String categoryName;
    private String uom;
    private Integer minStock;
    private Integer maxStock;
    private Boolean manageByLot;
    private BigDecimal minStockAlert;
    private BigDecimal maxWeightPerUnit;
    private Integer shelfLifeDays;
    private Boolean active;

    public ProductResponseDTO() {}

    public ProductResponseDTO(Long id, String skuCode, String name, String categoryName, String uom, Integer minStock, Integer maxStock, Boolean manageByLot) {
        this.id = id;
        this.skuCode = skuCode;
        this.name = name;
        this.categoryName = categoryName;
        this.uom = uom;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.manageByLot = manageByLot;
        if (minStock != null) this.minStockAlert = BigDecimal.valueOf(minStock);
        this.active = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getUom() { return uom; }
    public void setUom(String uom) { this.uom = uom; }

    public Integer getMinStock() { return minStock; }
    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
        if (minStock != null) this.minStockAlert = BigDecimal.valueOf(minStock);
    }

    public Integer getMaxStock() { return maxStock; }
    public void setMaxStock(Integer maxStock) { this.maxStock = maxStock; }

    public Boolean getManageByLot() { return manageByLot; }
    public void setManageByLot(Boolean manageByLot) { this.manageByLot = manageByLot; }

    public BigDecimal getMinStockAlert() { return minStockAlert; }
    public void setMinStockAlert(BigDecimal minStockAlert) {
        this.minStockAlert = minStockAlert;
        if (minStockAlert != null) this.minStock = minStockAlert.intValue();
    }

    public BigDecimal getMaxWeightPerUnit() { return maxWeightPerUnit; }
    public void setMaxWeightPerUnit(BigDecimal maxWeightPerUnit) { this.maxWeightPerUnit = maxWeightPerUnit; }

    public Integer getShelfLifeDays() { return shelfLifeDays; }
    public void setShelfLifeDays(Integer shelfLifeDays) { this.shelfLifeDays = shelfLifeDays; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
