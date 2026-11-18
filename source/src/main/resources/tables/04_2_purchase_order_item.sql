-- =============================================================================
-- BẢNG 04_2: purchase_order_item (Chi tiết mặt hàng trên đơn mua hàng PO)
-- Module: Tiếp nhận hàng hóa & Đối soát đơn hàng (FR02, BR-INB-01, BR-INV-01)
-- =============================================================================

CREATE TABLE IF NOT EXISTS purchase_order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    ordered_qty DECIMAL(12, 2) NOT NULL,
    received_qty DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Khóa ngoại liên kết Header đơn mua hàng và Mặt hàng SKU
    CONSTRAINT fk_po_item_header FOREIGN KEY (purchase_order_id)
        REFERENCES purchase_order(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_po_item_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE RESTRICT ON UPDATE CASCADE,

    -- Ràng buộc một SKU chỉ xuất hiện 1 lần trên 1 PO
    CONSTRAINT uk_po_item_product UNIQUE (purchase_order_id, product_id),

    -- Ràng buộc số lượng hợp lệ: Số nhận không vượt quá số đặt
    CONSTRAINT chk_po_item_ordered_qty CHECK (ordered_qty > 0),
    CONSTRAINT chk_po_item_received_qty CHECK (received_qty >= 0 AND received_qty <= ordered_qty)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_po_item_product ON purchase_order_item(product_id);
