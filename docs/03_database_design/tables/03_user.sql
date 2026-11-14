-- =============================================================================
-- BẢNG 03: user (Tài khoản người dùng & Kiểm soát thẩm quyền)
-- Module: Xác thực & Maker-Checker (FR-SEC)
-- =============================================================================

CREATE TABLE IF NOT EXISTS `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    active BIT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Ràng buộc tính duy nhất của tên đăng nhập
    CONSTRAINT uk_user_username UNIQUE (username),
    
    -- Ràng buộc vai trò hợp lệ
    CONSTRAINT chk_user_role CHECK (role IN ('ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

