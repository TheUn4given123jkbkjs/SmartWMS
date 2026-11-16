package com.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Thực thể Chi tiết dòng hàng nhập kho Detail (Bảng receipt_note_item).
 */
@Entity
@Table(name = "receipt_note_item")
public class ReceiptNoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private ReceiptNote receiptNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    private StockBatch stockBatch;

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

    @Column(name = "accepted_qty", nullable = false)
    private Integer acceptedQty = 0;

    @Column(name = "damaged_qty", nullable = false)
    private Integer damagedQty = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ReceiptNoteItem() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ReceiptNote getReceiptNote() { return receiptNote; }
    public void setReceiptNote(ReceiptNote receiptNote) { this.receiptNote = receiptNote; }

    public StockBatch getStockBatch() { return stockBatch; }
    public void setStockBatch(StockBatch stockBatch) { this.stockBatch = stockBatch; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public WarehouseLocation getLocation() { return location; }
    public void setLocation(WarehouseLocation location) { this.location = location; }

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    public LocalDate getExpDate() { return expDate; }
    public void setExpDate(LocalDate expDate) { this.expDate = expDate; }

    public Integer getAcceptedQty() { return acceptedQty; }
    public void setAcceptedQty(Integer acceptedQty) { this.acceptedQty = acceptedQty; }

    public Integer getDamagedQty() { return damagedQty; }
    public void setDamagedQty(Integer damagedQty) { this.damagedQty = damagedQty; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
