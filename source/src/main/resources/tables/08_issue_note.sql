-- =============================================================================
-- BẢNG 08: issue_note (Phiếu xuất kho - Header)
-- Module: Quản lý xuất kho & Phân bổ lô FEFO/FIFO (FR03)
-- =============================================================================

CREATE TABLE IF NOT EXISTS issue_note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_number VARCHAR(50) NOT NULL,
    issue_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dispatch_strategy VARCHAR(10) NOT NULL DEFAULT 'FEFO',
    maker_id BIGINT NOT NULL,
    checker_id BIGINT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING_APPROVAL',
    is_overridden BIT NOT NULL DEFAULT 0,
    override_reason VARCHAR(255) NULL,
    order_number VARCHAR(50) NULL,
    customer_name VARCHAR(255) NULL,
    note VARCHAR(500) NULL,
    export_type VARCHAR(20) NULL DEFAULT 'SALE',
    approved_at DATETIME(6) NULL,
    completed_at DATETIME(6) NULL,
    confirmed_by BIGINT NULL,
    decision_reason VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Khóa ngoại tham chiếu người lập (Maker) và người duyệt (Checker)
    CONSTRAINT fk_issue_maker FOREIGN KEY (maker_id) 
        REFERENCES `user`(id) ON DELETE RESTRICT,
    CONSTRAINT fk_issue_checker FOREIGN KEY (checker_id) 
        REFERENCES `user`(id) ON DELETE RESTRICT,
        
    CONSTRAINT fk_issue_confirmed_by FOREIGN KEY (confirmed_by)
        REFERENCES `user`(id) ON DELETE RESTRICT,

    -- Ràng buộc tính duy nhất của số phiếu xuất kho
    CONSTRAINT uk_issue_number UNIQUE (issue_number),
    
    -- Ràng buộc Maker-Checker: Người duyệt không được trùng người lập (BR-SEC-03)
    CONSTRAINT chk_issue_maker_checker CHECK (checker_id IS NULL OR checker_id <> maker_id),
    
    -- Ràng buộc chiến lược xuất kho hợp lệ (BR-OUT-02)
    CONSTRAINT chk_issue_strategy CHECK (dispatch_strategy IN ('FEFO', 'FIFO')),
    
    -- Ràng buộc trạng thái phiếu xuất kho
    CONSTRAINT chk_issue_status CHECK (status IN ('PENDING_APPROVAL', 'APPROVED_READY_TO_PICK', 'COMPLETED', 'REJECTED', 'CANCELLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_issue_status ON issue_note(status);
CREATE INDEX idx_issue_maker ON issue_note(maker_id);

