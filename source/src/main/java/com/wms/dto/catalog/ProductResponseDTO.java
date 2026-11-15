package com.wms.dto.catalog;

import java.math.BigDecimal;

public class ProductResponseDTO {
    private Long id;
    private String skuCode;
    private String name;
    private String categoryName;
    private String uom;
    private BigDecimal minStockAlert;
    private BigDecimal maxWeightPerUnit;
    private Integer shelfLifeDays;
    private Boolean active;

    public ProductResponseDTO() {}

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

    public BigDecimal getMinStockAlert() { return minStockAlert; }
    public void setMinStockAlert(BigDecimal minStockAlert) { this.minStockAlert = minStockAlert; }

    public BigDecimal getMaxWeightPerUnit() { return maxWeightPerUnit; }
    public void setMaxWeightPerUnit(BigDecimal maxWeightPerUnit) { this.maxWeightPerUnit = maxWeightPerUnit; }

    public Integer getShelfLifeDays() { return shelfLifeDays; }
    public void setShelfLifeDays(Integer shelfLifeDays) { this.shelfLifeDays = shelfLifeDays; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}

