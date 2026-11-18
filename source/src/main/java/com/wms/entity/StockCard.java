package com.wms.entity;

import com.wms.util.ChangeType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Thực thể Sổ Thẻ kho điện tử - Lịch sử biến động tồn kho bất biến (Bảng stock_card).
 */
@Entity
@Table(name = "stock_card")
public class StockCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id", nullable = false)
    private StockBatch stockBatch;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    @Column(name = "reference_code", nullable = false, length = 50)
    private String referenceCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type", nullable = false, length = 20)
    private ChangeType changeType;

    @Column(name = "qty_changed", nullable = false)
    private Integer qtyChanged;

    @Column(name = "after_changed", nullable = false)
    private Integer afterChanged;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public StockCard() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public StockBatch getStockBatch() { return stockBatch; }
    public void setStockBatch(StockBatch stockBatch) { this.stockBatch = stockBatch; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public String getReferenceCode() { return referenceCode; }
    public void setReferenceCode(String referenceCode) { this.referenceCode = referenceCode; }

    public ChangeType getChangeType() { return changeType; }
    public void setChangeType(ChangeType changeType) { this.changeType = changeType; }

    public Integer getQtyChanged() { return qtyChanged; }
    public void setQtyChanged(Integer qtyChanged) { this.qtyChanged = qtyChanged; }

    public Integer getAfterChanged() { return afterChanged; }
    public void setAfterChanged(Integer afterChanged) { this.afterChanged = afterChanged; }

    public Integer getQuantityChanged() { return qtyChanged; }
    public void setQuantityChanged(Integer quantityChanged) { this.qtyChanged = quantityChanged; }

    public Integer getAfterQuantity() { return afterChanged; }
    public void setAfterQuantity(Integer afterQuantity) { this.afterChanged = afterQuantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
