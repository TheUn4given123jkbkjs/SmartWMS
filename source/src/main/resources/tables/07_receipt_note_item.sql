-- =============================================================================
-- BẢNG 07: receipt_note_item (Chi tiết dòng hàng nhập kho - Detail)
-- Module: Tiếp nhận hàng hóa & Phân loại hàng hỏng (FR02)
-- Quan hệ: Composition với receipt_note (ON DELETE CASCADE)
-- =============================================================================

CREATE TABLE IF NOT EXISTS receipt_note_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_id BIGINT NOT NULL,
    batch_id BIGINT NULL,
    product_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    lot_number VARCHAR(50) NOT NULL,
    exp_date DATE NULL, -- Cho phép NULL đối với hàng không quản lý lô/hạn (Quyết định D5)
    accepted_qty DECIMAL(12, 2) NOT NULL DEFAULT 0,
    damaged_qty DECIMAL(12, 2) NOT NULL DEFAULT 0,
    rejection_reason VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Khóa ngoại tham chiếu Phiếu nhập, Lô hàng, Sản phẩm, Vị trí ô kệ
    CONSTRAINT fk_receipt_item_note FOREIGN KEY (receipt_id) 
        REFERENCES receipt_note(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_receipt_item_batch FOREIGN KEY (batch_id) 
        REFERENCES stock_batch(id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_receipt_item_product FOREIGN KEY (product_id) 
        REFERENCES product(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_receipt_item_location FOREIGN KEY (location_id) 
        REFERENCES warehouse_location(id) ON DELETE RESTRICT ON UPDATE CASCADE,
        
    -- Ràng buộc số lượng không âm và tổng số lượng phải lớn hơn 0
    CONSTRAINT chk_receipt_item_accepted CHECK (accepted_qty >= 0),
    CONSTRAINT chk_receipt_item_damaged CHECK (damaged_qty >= 0),
    CONSTRAINT chk_receipt_item_total CHECK (accepted_qty + damaged_qty > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_receipt_item_note ON receipt_note_item(receipt_id);
CREATE INDEX idx_receipt_item_batch ON receipt_note_item(batch_id);

