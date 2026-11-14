-- =============================================================================
-- BẢNG 05: stock_batch (Lô hàng tồn kho - Thực thể trung tâm phân rã N:N)
-- Module: Quản lý tồn kho theo lô & hạn dùng (FR04)
-- Ghi chú: Không lưu cột dư thừa available_qty (Chuẩn hóa 3NF)
--          Available = on_hand_qty - reserved_qty (tính toán thời gian thực)
-- =============================================================================

CREATE TABLE IF NOT EXISTS stock_batch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    lot_number VARCHAR(50) NOT NULL,
    exp_date DATE NOT NULL,
    on_hand_qty INT NOT NULL DEFAULT 0,
    reserved_qty INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Khóa ngoại tham chiếu Mặt hàng và Vị trí ô kệ
    CONSTRAINT fk_batch_product FOREIGN KEY (product_id) 
        REFERENCES product(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_batch_location FOREIGN KEY (location_id) 
        REFERENCES warehouse_location(id) ON DELETE RESTRICT ON UPDATE CASCADE,
        
    -- Mỗi lô hàng của một sản phẩm tại một vị trí ô kệ là duy nhất
    CONSTRAINT uk_batch_product_loc_lot UNIQUE (product_id, location_id, lot_number),
    
    -- Ràng buộc chống số lượng âm
    CONSTRAINT chk_batch_on_hand CHECK (on_hand_qty >= 0),
    CONSTRAINT chk_batch_reserved CHECK (reserved_qty >= 0),
    
    -- Ràng buộc giữ chỗ không được vượt quá tồn kho thực tế (chống xuất âm / over-reservation)
    CONSTRAINT chk_batch_reservation_limit CHECK (reserved_qty <= on_hand_qty),
    
    -- Ràng buộc trạng thái lô hàng
    CONSTRAINT chk_batch_status CHECK (status IN ('AVAILABLE', 'EXPIRED', 'QUARANTINED', 'BLOCKED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_batch_product_exp ON stock_batch(product_id, exp_date);
CREATE INDEX idx_batch_location ON stock_batch(location_id);
