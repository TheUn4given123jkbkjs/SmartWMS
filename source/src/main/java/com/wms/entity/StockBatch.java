package com.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Thực thể Lô hàng tồn kho (Bảng stock_batch - Thực thể trung tâm phân rã N:N).
 */
@Entity
@Table(name = "stock_batch", uniqueConstraints = {
    @UniqueConstraint(name = "uk_batch_product_loc_lot", columnNames = {"product_id", "location_id", "lot_number"})
})
public class StockBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private WarehouseLocation location;

    @Column(name = "lot_number", nullable = false, length = 50)
    private String lotNumber;

    @Column(name = "exp_date", nullable = false)
    private LocalDate expDate;

    @Column(name = "on_hand_qty", nullable = false)
    private Integer onHandQty = 0;

    @Column(name = "reserved_qty", nullable = false)
    private Integer reservedQty = 0;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "AVAILABLE";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public StockBatch() {}

    /**
     * Kiểm tra lô hàng đã hết hạn hay chưa (D3: Hết hạn khi expDate < ngày hiện tại).
     */
    public boolean isExpired() {
        return expDate != null && expDate.isBefore(LocalDate.now());
    }

    /**
     * Tồn khả dụng (Available Qty) tính toán động = onHandQty - reservedQty
     * Tuân thủ D2: Chỉ khả dụng khi trạng thái là AVAILABLE và chưa hết hạn.
     */
    public Integer getAvailableQty() {
        if (!"AVAILABLE".equalsIgnoreCase(status) || isExpired()) {
            return 0;
        }
        return Math.max(0, (onHandQty != null ? onHandQty : 0) - (reservedQty != null ? reservedQty : 0));
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public WarehouseLocation getLocation() { return location; }
    public void setLocation(WarehouseLocation location) { this.location = location; }

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    public LocalDate getExpDate() { return expDate; }
    public void setExpDate(LocalDate expDate) { this.expDate = expDate; }

    public Integer getOnHandQty() { return onHandQty; }
    public void setOnHandQty(Integer onHandQty) { this.onHandQty = onHandQty; }

    public Integer getReservedQty() { return reservedQty; }
    public void setReservedQty(Integer reservedQty) { this.reservedQty = reservedQty; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Integer getOnHandQuantity() { return onHandQty; }
    public Integer getReservedQuantity() { return reservedQty; }
    public Integer getAvailableQuantity() { return getAvailableQty(); }

    /**
     * Tạm giữ chỗ tồn kho (BR-OUT-01).
     */
    public void reserve(Integer quantity) {
        if (quantity != null && quantity > 0) {
            this.reservedQty = (this.reservedQty != null ? this.reservedQty : 0) + quantity;
            this.updatedAt = LocalDateTime.now();
        }
    }

    /**
     * Trừ tồn kho thực tế và giải phóng giữ chỗ khi xuất kho (BR-OUT-04).
     */
    public void deductDispatched(Integer quantity) {
        if (quantity != null && quantity > 0) {
            this.onHandQty = Math.max(0, (this.onHandQty != null ? this.onHandQty : 0) - quantity);
            this.reservedQty = Math.max(0, (this.reservedQty != null ? this.reservedQty : 0) - quantity);
            this.updatedAt = LocalDateTime.now();
        }
    }
}
