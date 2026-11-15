-- =============================================================================
-- BẢNG 02: warehouse_location (Vị trí ô kệ trong kho theo chuẩn 3M)
-- Module: Danh mục vị trí kho (FR01)
-- Phân vùng Zone: DOCK (Cửa tiếp nhận), INSP (Kiểm tra chất lượng), 
--                 RAW (Nguyên liệu), FG (Thành phẩm), SCRAP (Hàng phế phẩm)
-- =============================================================================

CREATE TABLE IF NOT EXISTS warehouse_location (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location_code VARCHAR(30) NOT NULL,
    zone VARCHAR(20) NOT NULL,
    max_capacity DECIMAL(12, 2) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'EMPTY',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Ràng buộc tính duy nhất của mã vị trí kho chuẩn 3M (BR-CAT-02)
    CONSTRAINT uk_location_code UNIQUE (location_code),
    
    -- Ràng buộc giá trị phân vùng hợp lệ
    CONSTRAINT chk_location_zone CHECK (zone IN ('DOCK', 'INSP', 'RAW', 'FG', 'SCRAP')),
    
    -- Ràng buộc trạng thái ô kệ (BR-CAT-03)
    CONSTRAINT chk_location_status CHECK (status IN ('EMPTY', 'OCCUPIED', 'MAINTENANCE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

