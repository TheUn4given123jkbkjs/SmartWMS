-- =============================================================================
-- BẢNG 10: stock_card (Thẻ kho điện tử - Sổ cái biến động tồn kho bất biến)
-- Module: Báo cáo & Lịch sử biến động kho (FR04)
-- Quy tắc: Chỉ INSERT, không bao giờ UPDATE hoặc DELETE (Immutable Ledger)
-- =============================================================================

CREATE TABLE IF NOT EXISTS stock_card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reference_code VARCHAR(50) NOT NULL,
    change_type VARCHAR(20) NOT NULL,
    qty_changed DECIMAL(12, 2) NOT NULL,
    after_changed DECIMAL(12, 2) NOT NULL,
    performed_by BIGINT NULL,
    approved_by BIGINT NULL,
    reason VARCHAR(500) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Khóa ngoại tham chiếu Mặt hàng, Lô hàng và Người dùng thực hiện / phê duyệt (BR-SEC-02)
    CONSTRAINT fk_card_product FOREIGN KEY (product_id) 
        REFERENCES product(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_card_batch FOREIGN KEY (batch_id) 
        REFERENCES stock_batch(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_card_performed_by FOREIGN KEY (performed_by) 
        REFERENCES `user`(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_card_approved_by FOREIGN KEY (approved_by) 
        REFERENCES `user`(id) ON DELETE RESTRICT ON UPDATE CASCADE,
        
    -- Ràng buộc loại biến động kho hợp lệ
    CONSTRAINT chk_card_change_type CHECK (change_type IN ('NHAP_PO', 'XUAT_KHO', 'KIEM_KE', 'DIEU_CHINH')),
    
    -- Ràng buộc số lượng biến động khác 0 và tồn sau giao dịch không âm (BR-SEC-02)
    CONSTRAINT chk_card_qty_changed CHECK (qty_changed <> 0),
    CONSTRAINT chk_card_after_changed CHECK (after_changed >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_card_product_date ON stock_card(product_id, transaction_date);
CREATE INDEX idx_card_batch ON stock_card(batch_id);
CREATE INDEX idx_card_ref ON stock_card(reference_code);
CREATE INDEX idx_card_performed_by ON stock_card(performed_by);
CREATE INDEX idx_card_approved_by ON stock_card(approved_by);

