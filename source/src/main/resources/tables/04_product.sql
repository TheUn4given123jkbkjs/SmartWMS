-- =============================================================================
-- BẢNG 04: product (Mặt hàng lưu kho / SKU)
-- Module: Quản lý hàng hóa (FR01)
-- =============================================================================

CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    sku_code VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    uom VARCHAR(50) NOT NULL,
    min_stock DECIMAL(12, 2) NOT NULL DEFAULT 0,
    max_stock DECIMAL(12, 2) NOT NULL,
    manage_by_lot BIT NOT NULL DEFAULT 1,
    active BIT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Khóa ngoại tham chiếu ngành hàng
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) 
        REFERENCES category(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    
    -- Ràng buộc tính duy nhất của mã SKU (BR-CAT-01)
    CONSTRAINT uk_product_sku UNIQUE (sku_code),
    
    -- Ràng buộc logic định mức tồn kho an toàn (BR-CAT-05)
    CONSTRAINT chk_product_min_stock CHECK (min_stock >= 0),
    CONSTRAINT chk_product_max_stock CHECK (max_stock > min_stock)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_product_category ON product(category_id);

