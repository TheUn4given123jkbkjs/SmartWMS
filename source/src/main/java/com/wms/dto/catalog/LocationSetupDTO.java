package com.wms.dto.catalog;

import com.wms.util.ZoneType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

public class LocationSetupDTO {

  @NotBlank(message = "Mã vị trí ô kệ bắt buộc")
  @Pattern(
    regexp = "^[A-Z0-9-]+$",
    message = "Mã vị trí chỉ gồm chữ in hoa, số và dấu gạch nối"
  )
  @jakarta.validation.constraints.Size(min = 3, max = 20)
  private String locationCode;

  @NotNull(message = "Phân khu chức năng bắt buộc")
  private ZoneType zone;

  private BigDecimal maxCapacity;
  private BigDecimal maxWeightCapacity;

  public LocationSetupDTO() {}

  public LocationSetupDTO(
    String locationCode,
    ZoneType zone,
    BigDecimal maxCapacity
  ) {
    this.locationCode = locationCode;
    this.zone = zone;
    this.maxCapacity = maxCapacity;
    this.maxWeightCapacity = maxCapacity;
  }

  public String getLocationCode() {
    return locationCode;
  }

  public void setLocationCode(String locationCode) {
    this.locationCode = locationCode;
  }

  public ZoneType getZone() {
    return zone;
  }

  public void setZone(ZoneType zone) {
    this.zone = zone;
  }

  public BigDecimal getMaxCapacity() {
    if (maxCapacity != null) return maxCapacity;
    return maxWeightCapacity;
  }

  public void setMaxCapacity(BigDecimal maxCapacity) {
    this.maxCapacity = maxCapacity;
    this.maxWeightCapacity = maxCapacity;
  }

  public BigDecimal getMaxWeightCapacity() {
    if (maxWeightCapacity != null) return maxWeightCapacity;
    return maxCapacity;
  }

  public void setMaxWeightCapacity(BigDecimal maxWeightCapacity) {
    this.maxWeightCapacity = maxWeightCapacity;
    this.maxCapacity = maxWeightCapacity;
  }
}
