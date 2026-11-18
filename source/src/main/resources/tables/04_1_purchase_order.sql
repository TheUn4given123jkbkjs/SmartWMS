-- =============================================================================
-- BẢNG 04_1: purchase_order (Đơn mua hàng từ nhà cung cấp - PO Header)
-- Module: Tiếp nhận hàng hóa & Đối soát đơn hàng (FR02, BR-INB-01)
-- =============================================================================

CREATE TABLE IF NOT EXISTS purchase_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    po_number VARCHAR(50) NOT NULL,
    supplier_name VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'APPROVED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Ràng buộc tính duy nhất của mã đơn mua hàng PO
    CONSTRAINT uk_po_number UNIQUE (po_number),

    -- Ràng buộc trạng thái đơn mua hàng
    CONSTRAINT chk_po_status CHECK (status IN ('APPROVED', 'PARTIALLY_RECEIVED', 'CLOSED', 'CANCELLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_po_status ON purchase_order(status);
