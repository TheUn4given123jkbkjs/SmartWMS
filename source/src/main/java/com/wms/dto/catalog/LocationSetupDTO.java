package com.wms.dto.catalog;

import com.wms.util.ZoneType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public class LocationSetupDTO {
    @NotBlank(message = "Mã vị trí ô kệ bắt buộc")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Mã vị trí chỉ gồm chữ in hoa, số và dấu gạch nối")
    private String locationCode;

    @NotNull(message = "Phân khu chức năng bắt buộc")
    private ZoneType zone;

    @NotBlank(message = "Dãy (Aisle) bắt buộc")
    private String aisle;

    @NotBlank(message = "Kệ (Rack) bắt buộc")
    private String rack;

    @NotBlank(message = "Tầng (Shelf) bắt buộc")
    private String shelf;

    @NotBlank(message = "Ô (Bin) bắt buộc")
    private String bin;

    @NotNull(message = "Tải trọng tối đa bắt buộc")
    @Min(value = 1, message = "Tải trọng phải lớn hơn 0")
    private BigDecimal maxWeightCapacity;

    public LocationSetupDTO() {}

    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

    public ZoneType getZone() { return zone; }
    public void setZone(ZoneType zone) { this.zone = zone; }

    public String getAisle() { return aisle; }
    public void setAisle(String aisle) { this.aisle = aisle; }

    public String getRack() { return rack; }
    public void setRack(String rack) { this.rack = rack; }

    public String getShelf() { return shelf; }
    public void setShelf(String shelf) { this.shelf = shelf; }

    public String getBin() { return bin; }
    public void setBin(String bin) { this.bin = bin; }

    public BigDecimal getMaxWeightCapacity() { return maxWeightCapacity; }
    public void setMaxWeightCapacity(BigDecimal maxWeightCapacity) { this.maxWeightCapacity = maxWeightCapacity; }
}

