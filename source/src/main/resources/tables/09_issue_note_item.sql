-- =============================================================================
-- BẢNG 09: issue_note_item (Chi tiết phân bổ xuất kho - Detail)
-- Module: Quản lý xuất kho & Pick list theo ô kệ (FR03)
-- Quan hệ: Composition với issue_note (ON DELETE CASCADE)
-- =============================================================================

CREATE TABLE IF NOT EXISTS issue_note_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    requested_qty DECIMAL(12, 2) NOT NULL,
    allocated_qty DECIMAL(12, 2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Khóa ngoại tham chiếu Phiếu xuất kho và Lô hàng
    CONSTRAINT fk_issue_item_note FOREIGN KEY (issue_id) 
        REFERENCES issue_note(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_issue_item_batch FOREIGN KEY (batch_id) 
        REFERENCES stock_batch(id) ON DELETE RESTRICT ON UPDATE CASCADE,
        
    -- Ràng buộc số lượng yêu cầu phải lớn hơn 0 và lượng phân bổ không vượt lượng yêu cầu
    CONSTRAINT chk_issue_item_requested CHECK (requested_qty > 0),
    CONSTRAINT chk_issue_item_allocated CHECK (allocated_qty >= 0),
    CONSTRAINT chk_issue_item_allocated_limit CHECK (allocated_qty <= requested_qty)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_issue_item_note ON issue_note_item(issue_id);
CREATE INDEX idx_issue_item_batch ON issue_note_item(batch_id);

