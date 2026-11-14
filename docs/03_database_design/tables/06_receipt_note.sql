-- =============================================================================
-- BẢNG 06: receipt_note (Phiếu nhập kho - Header)
-- Module: Tiếp nhận hàng hóa & Maker-Checker (FR02)
-- =============================================================================

CREATE TABLE IF NOT EXISTS receipt_note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL,
    po_number VARCHAR(50) NOT NULL,
    receipt_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    maker_id BIGINT NOT NULL,
    checker_id BIGINT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING_APPROVAL',
    rejection_note VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Khóa ngoại tham chiếu người lập (Maker) và người duyệt (Checker)
    CONSTRAINT fk_receipt_maker FOREIGN KEY (maker_id) 
        REFERENCES `user`(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_receipt_checker FOREIGN KEY (checker_id) 
        REFERENCES `user`(id) ON DELETE RESTRICT ON UPDATE CASCADE,
        
    -- Ràng buộc tính duy nhất của mã số phiếu nhập kho
    CONSTRAINT uk_receipt_number UNIQUE (receipt_number),
    
    -- Ràng buộc Maker-Checker: Người duyệt không được trùng với người lập (BR-SEC-03)
    CONSTRAINT chk_receipt_maker_checker CHECK (checker_id IS NULL OR checker_id <> maker_id),
    
    -- Ràng buộc trạng thái phiếu nhập kho hợp lệ
    CONSTRAINT chk_receipt_status CHECK (status IN ('PENDING_APPROVAL', 'COMPLETED', 'REJECTED', 'CANCELLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_receipt_po ON receipt_note(po_number);
CREATE INDEX idx_receipt_status ON receipt_note(status);
CREATE INDEX idx_receipt_maker ON receipt_note(maker_id);
