package com.wms.entity;

import com.wms.util.LocationStatus;
import com.wms.util.ZoneType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Thực thể Vị trí ô kệ kho bãi theo chuẩn kẻ vạch 3M Floor Marking (Bảng warehouse_location).
 */
@Entity
@Table(
  name = "warehouse_location",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_location_code",
      columnNames = { "location_code" }
    ),
  }
)
public class WarehouseLocation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "location_code", nullable = false, length = 30)
  private String locationCode;

  @Enumerated(EnumType.STRING)
  @Column(name = "zone", nullable = false, length = 20)
  private ZoneType zone;

  @Column(name = "max_capacity", precision = 12, scale = 2)
  private BigDecimal maxCapacity;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private LocationStatus status = LocationStatus.EMPTY;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public WarehouseLocation() {}

  /**
   * Phân rã tự động tọa độ [Dãy, Kệ, Tầng, Ô] từ mã vị trí chuẩn 3M (location_code).
   * Hỗ trợ định dạng:
   * - ZONE-AISLE-RACK-SHELF-BIN (VD: RAW-A-01-02-01)
   * - ZONE-AISLE-RACK-SHELF (VD: RAW-A-01-02)
   * - ZONE-AISLERACK-SHELF (VD: RAW-A01-02)
   * - ZONE-BIN (VD: DOCK-01, INSP-01, SCRAP-01)
   */
  @Transient
  private String[] parseCoordinates() {
    if (locationCode == null || locationCode.isBlank()) {
      return new String[] { "—", "—", "—", "—" };
    }
    String[] parts = locationCode.trim().split("-");
    if (parts.length >= 5) {
      return new String[] { parts[1], parts[2], parts[3], parts[4] };
    }
    if (parts.length == 4) {
      return new String[] { parts[1], parts[2], parts[3], "01" };
    }
    if (parts.length == 3) {
      String mid = parts[1];
      java.util.regex.Matcher m = java.util.regex.Pattern
        .compile("^([A-Z]+)(\\d+)$")
        .matcher(mid);
      if (m.matches()) {
        return new String[] { m.group(1), m.group(2), parts[2], "01" };
      }
      return new String[] { mid, "01", parts[2], "01" };
    }
    if (parts.length == 2) {
      return new String[] { "Sàn", "01", "01", parts[1] };
    }
    return new String[] { "—", "—", "01", "—" };
  }

  @Transient
  public String getAisle() {
    return parseCoordinates()[0];
  }

  @Transient
  public String getRack() {
    return parseCoordinates()[1];
  }

  @Transient
  public String getShelf() {
    return parseCoordinates()[2];
  }

  @Transient
  public String getBin() {
    return parseCoordinates()[3];
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    return maxCapacity;
  }

  public void setMaxCapacity(BigDecimal maxCapacity) {
    this.maxCapacity = maxCapacity;
  }

  public LocationStatus getStatus() {
    return status;
  }

  public void setStatus(LocationStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
}
