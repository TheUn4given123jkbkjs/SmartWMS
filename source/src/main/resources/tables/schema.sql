-- =============================================================================
-- HỆ THỐNG QUẢN LÝ KHO HÀNG THÔNG MINH (SMARTWMS) - DATABASE DDL MASTER SCHEMA
-- Học phần: Phân tích và Thiết kế Yêu cầu (Mã môn: 502050 - ĐH Tôn Đức Thắng)
-- Sprint 3 - Task T3.1 | Maker: M1 - Nguyễn Thành Trung | Reviewer: M2 - Nguyễn Bảo Khang
-- Tiêu chuẩn: Chuẩn hóa 3NF, Khóa ngoại tường minh, Ràng buộc nghiệp vụ toàn vẹn
-- Ghi chú: Mã nguồn được chia thành 10 file riêng biệt tại thư mục ./tables/
-- =============================================================================

-- TẮT TẠM THỜI RÀNG BUỘC KHÓA NGOẠI KHI KHỞI TẠO LẠI BẢNG (NẾU CẦN RESET)
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS stock_card;
DROP TABLE IF EXISTS issue_note_item;
DROP TABLE IF EXISTS issue_note;
DROP TABLE IF EXISTS receipt_note_item;
DROP TABLE IF EXISTS receipt_note;
DROP TABLE IF EXISTS stock_batch;
DROP TABLE IF EXISTS purchase_order_item;
DROP TABLE IF EXISTS purchase_order;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS warehouse_location;
DROP TABLE IF EXISTS category;

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- BẢNG 01: category (Ngành hàng phân loại sản phẩm)
-- =============================================================================
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL,
    name VARCHAR(255) NOT NULL,
    active BIT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_category_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- BẢNG 02: warehouse_location (Vị trí ô kệ trong kho theo chuẩn 3M)
-- =============================================================================
CREATE TABLE warehouse_location (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    location_code VARCHAR(30) NOT NULL,
    zone VARCHAR(20) NOT NULL,
    max_capacity DECIMAL(12, 2) NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'EMPTY',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_location_code UNIQUE (location_code),
    CONSTRAINT chk_location_zone CHECK (zone IN ('DOCK', 'INSP', 'RAW', 'FG', 'SCRAP')),
    CONSTRAINT chk_location_status CHECK (status IN ('EMPTY', 'OCCUPIED', 'MAINTENANCE'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- BẢNG 03: user (Tài khoản người dùng & Kiểm soát thẩm quyền)
-- =============================================================================
CREATE TABLE `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    active BIT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_username UNIQUE (username),
    CONSTRAINT chk_user_role CHECK (role IN ('ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================================================
-- BẢNG 04: product (Mặt hàng lưu kho / SKU)
-- =============================================================================
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT NOT NULL,
    sku_code VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    uom VARCHAR(50) NOT NULL,
    min_stock INT NOT NULL DEFAULT 0,
    max_stock INT NOT NULL,
    manage_by_lot BIT NOT NULL DEFAULT 1,
    active BIT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uk_product_sku UNIQUE (sku_code),
    CONSTRAINT chk_product_min_stock CHECK (min_stock >= 0),
    CONSTRAINT chk_product_max_stock CHECK (max_stock > min_stock)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_product_category ON product(category_id);

-- =============================================================================
-- BẢNG 04_1: purchase_order (Đơn mua hàng từ nhà cung cấp - PO Header)
-- =============================================================================
CREATE TABLE purchase_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    po_number VARCHAR(50) NOT NULL,
    supplier_name VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'APPROVED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_po_number UNIQUE (po_number),
    CONSTRAINT chk_po_status CHECK (status IN ('APPROVED', 'PARTIALLY_RECEIVED', 'CLOSED', 'CANCELLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_po_status ON purchase_order(status);

-- =============================================================================
-- BẢNG 04_2: purchase_order_item (Chi tiết mặt hàng trên đơn mua hàng PO)
-- =============================================================================
CREATE TABLE purchase_order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    ordered_qty DECIMAL(12, 2) NOT NULL,
    received_qty DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_po_item_header FOREIGN KEY (purchase_order_id) REFERENCES purchase_order(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_po_item_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uk_po_item_product UNIQUE (purchase_order_id, product_id),
    CONSTRAINT chk_po_item_ordered_qty CHECK (ordered_qty > 0),
    CONSTRAINT chk_po_item_received_qty CHECK (received_qty >= 0 AND received_qty <= ordered_qty)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_po_item_product ON purchase_order_item(product_id);

-- =============================================================================
-- BẢNG 05: stock_batch (Lô hàng tồn kho - Thực thể trung tâm phân rã N:N)
-- =============================================================================
CREATE TABLE stock_batch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    lot_number VARCHAR(50) NOT NULL,
    exp_date DATE NULL, -- Cho phép NULL đối với hàng không quản lý lô/hạn (Quyết định D5)
    on_hand_qty INT NOT NULL DEFAULT 0,
    reserved_qty INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_batch_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_batch_location FOREIGN KEY (location_id) REFERENCES warehouse_location(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uk_batch_product_loc_lot UNIQUE (product_id, location_id, lot_number),
    CONSTRAINT chk_batch_on_hand CHECK (on_hand_qty >= 0),
    CONSTRAINT chk_batch_reserved CHECK (reserved_qty >= 0),
    CONSTRAINT chk_batch_reservation_limit CHECK (reserved_qty <= on_hand_qty),
    CONSTRAINT chk_batch_status CHECK (status IN ('AVAILABLE', 'EXPIRED', 'QUARANTINED', 'BLOCKED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_batch_product_exp ON stock_batch(product_id, exp_date);
CREATE INDEX idx_batch_location ON stock_batch(location_id);

-- =============================================================================
-- BẢNG 06: receipt_note (Phiếu nhập kho - Header)
-- =============================================================================
CREATE TABLE receipt_note (
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
    CONSTRAINT fk_receipt_maker FOREIGN KEY (maker_id) REFERENCES `user`(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_receipt_checker FOREIGN KEY (checker_id) REFERENCES `user`(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uk_receipt_number UNIQUE (receipt_number),
    CONSTRAINT chk_receipt_maker_checker CHECK (checker_id IS NULL OR checker_id <> maker_id),
    CONSTRAINT chk_receipt_status CHECK (status IN ('PENDING_APPROVAL', 'COMPLETED', 'REJECTED', 'CANCELLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_receipt_po ON receipt_note(po_number);
CREATE INDEX idx_receipt_status ON receipt_note(status);
CREATE INDEX idx_receipt_maker ON receipt_note(maker_id);

-- =============================================================================
-- BẢNG 07: receipt_note_item (Chi tiết dòng hàng nhập kho - Detail)
-- =============================================================================
CREATE TABLE receipt_note_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_id BIGINT NOT NULL,
    batch_id BIGINT NULL,
    product_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    lot_number VARCHAR(50) NOT NULL,
    exp_date DATE NULL, -- Cho phép NULL đối với hàng không quản lý lô/hạn (Quyết định D5)
    accepted_qty INT NOT NULL DEFAULT 0,
    damaged_qty INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_receipt_item_note FOREIGN KEY (receipt_id) REFERENCES receipt_note(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_receipt_item_batch FOREIGN KEY (batch_id) REFERENCES stock_batch(id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_receipt_item_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_receipt_item_location FOREIGN KEY (location_id) REFERENCES warehouse_location(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_receipt_item_accepted CHECK (accepted_qty >= 0),
    CONSTRAINT chk_receipt_item_damaged CHECK (damaged_qty >= 0),
    CONSTRAINT chk_receipt_item_total CHECK (accepted_qty + damaged_qty > 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_receipt_item_note ON receipt_note_item(receipt_id);
CREATE INDEX idx_receipt_item_batch ON receipt_note_item(batch_id);

-- =============================================================================
-- BẢNG 08: issue_note (Phiếu xuất kho - Header)
-- =============================================================================
CREATE TABLE issue_note (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_number VARCHAR(50) NOT NULL,
    issue_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dispatch_strategy VARCHAR(10) NOT NULL DEFAULT 'FEFO',
    maker_id BIGINT NOT NULL,
    checker_id BIGINT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING_APPROVAL',
    is_overridden BIT NOT NULL DEFAULT 0,
    override_reason VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_issue_maker FOREIGN KEY (maker_id) REFERENCES `user`(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_issue_checker FOREIGN KEY (checker_id) REFERENCES `user`(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT uk_issue_number UNIQUE (issue_number),
    CONSTRAINT chk_issue_maker_checker CHECK (checker_id IS NULL OR checker_id <> maker_id),
    CONSTRAINT chk_issue_strategy CHECK (dispatch_strategy IN ('FEFO', 'FIFO')),
    CONSTRAINT chk_issue_status CHECK (status IN ('PENDING_APPROVAL', 'APPROVED_READY_TO_PICK', 'COMPLETED', 'REJECTED', 'CANCELLED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_issue_status ON issue_note(status);
CREATE INDEX idx_issue_maker ON issue_note(maker_id);

-- =============================================================================
-- BẢNG 09: issue_note_item (Chi tiết phân bổ xuất kho - Detail)
-- =============================================================================
CREATE TABLE issue_note_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    issue_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    requested_qty INT NOT NULL,
    allocated_qty INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_issue_item_note FOREIGN KEY (issue_id) REFERENCES issue_note(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_issue_item_batch FOREIGN KEY (batch_id) REFERENCES stock_batch(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_issue_item_requested CHECK (requested_qty > 0),
    CONSTRAINT chk_issue_item_allocated CHECK (allocated_qty >= 0),
    CONSTRAINT chk_issue_item_allocated_limit CHECK (allocated_qty <= requested_qty)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_issue_item_note ON issue_note_item(issue_id);
CREATE INDEX idx_issue_item_batch ON issue_note_item(batch_id);

-- =============================================================================
-- BẢNG 10: stock_card (Thẻ kho điện tử - Sổ cái biến động tồn kho bất biến)
-- =============================================================================
CREATE TABLE stock_card (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    batch_id BIGINT NOT NULL,
    transaction_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reference_code VARCHAR(50) NOT NULL,
    change_type VARCHAR(20) NOT NULL,
    qty_changed INT NOT NULL,
    after_changed INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_card_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_card_batch FOREIGN KEY (batch_id) REFERENCES stock_batch(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT chk_card_change_type CHECK (change_type IN ('NHAP_PO', 'XUAT_KHO', 'KIEM_KE', 'DIEU_CHINH')),
    CONSTRAINT chk_card_qty_changed CHECK (qty_changed <> 0),
    CONSTRAINT chk_card_after_changed CHECK (after_changed >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_card_product_date ON stock_card(product_id, transaction_date);
CREATE INDEX idx_card_batch ON stock_card(batch_id);
CREATE INDEX idx_card_ref ON stock_card(reference_code);
