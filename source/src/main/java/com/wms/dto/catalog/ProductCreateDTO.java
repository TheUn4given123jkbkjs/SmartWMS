package com.wms.dto.catalog;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductCreateDTO {
    @NotBlank(message = "Mã SKU bắt buộc nhập")
    @Size(max = 50, message = "Mã SKU tối đa 50 ký tự")
    private String skuCode;

    @NotBlank(message = "Tên hàng hóa bắt buộc nhập")
    @Size(max = 255, message = "Tên hàng hóa tối đa 255 ký tự")
    private String name;

    @NotNull(message = "Phải chọn ngành hàng")
    private Long categoryId;

    @NotBlank(message = "Đơn vị tính bắt buộc")
    private String uom;

    @NotNull(message = "Ngưỡng cảnh báo tồn tối thiểu bắt buộc")
    @Min(value = 0, message = "Ngưỡng tồn không được âm")
    private BigDecimal minStockAlert;

    @NotNull(message = "Trọng lượng mỗi đơn vị bắt buộc")
    @Min(value = 0, message = "Trọng lượng không được âm")
    private BigDecimal maxWeightPerUnit;

    @Min(value = 0, message = "Thời hạn bảo quản không được âm")
    private Integer shelfLifeDays;

    public ProductCreateDTO() {}

    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getUom() { return uom; }
    public void setUom(String uom) { this.uom = uom; }

    public BigDecimal getMinStockAlert() { return minStockAlert; }
    public void setMinStockAlert(BigDecimal minStockAlert) { this.minStockAlert = minStockAlert; }

    public BigDecimal getMaxWeightPerUnit() { return maxWeightPerUnit; }
    public void setMaxWeightPerUnit(BigDecimal maxWeightPerUnit) { this.maxWeightPerUnit = maxWeightPerUnit; }

    public Integer getShelfLifeDays() { return shelfLifeDays; }
    public void setShelfLifeDays(Integer shelfLifeDays) { this.shelfLifeDays = shelfLifeDays; }
}

