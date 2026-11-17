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

  @Column(length = 20)
  private String shelf;

  public String getShelf() {
    return shelf;
  }

  public void setShelf(String value) {
    shelf = value;
  }

  @Column(length = 20)
  private String aisle;

  public String getAisle() {
    return aisle;
  }

  public void setAisle(String value) {
    aisle = value;
  }

  @Column(length = 20)
  private String rack;

  public String getRack() {
    return rack;
  }

  public void setRack(String value) {
    rack = value;
  }

  @Column(length = 20)
  private String bin;

  public String getBin() {
    return bin;
  }

  public void setBin(String value) {
    bin = value;
  }

  public WarehouseLocation() {}

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
