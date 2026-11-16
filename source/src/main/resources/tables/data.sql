-- =============================================================================
-- SMARTWMS - DỮ LIỆU KHỞI TẠO BAN ĐẦU (SEED DATA SCRIPT)
-- Học phần: Phân tích & Thiết kế Yêu cầu (Mã môn: 502050 - ĐH Tôn Đức Thắng)
-- Thiết kế CSDL 3NF: Phân quyền chuẩn 3 vai trò: ROLE_STAFF, ROLE_MANAGER, ROLE_ADMIN
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. BẢNG `user`: DANH SÁCH TÀI KHOẢN THEO CHUẨN 3 VAI TRÒ (RBAC) & MAKER-CHECKER
-- Mật khẩu mặc định cho toàn bộ tài khoản demo: 123456
-- -----------------------------------------------------------------------------
INSERT INTO `user` (id, username, password_hash, full_name, role, active, created_at) VALUES
(1, 'admin', '123456', 'Quản trị viên Hệ thống', 'ROLE_ADMIN', 1, CURRENT_TIMESTAMP),
(2, 'manager_trung', '123456', 'Nguyễn Thành Trung (Thủ kho trưởng)', 'ROLE_MANAGER', 1, CURRENT_TIMESTAMP),
(3, 'manager_khang', '123456', 'Nguyễn Bảo Khang (Quản lý kho phó / Checker)', 'ROLE_MANAGER', 1, CURRENT_TIMESTAMP),
(4, 'staff_thien', '123456', 'Tôn Thanh Thiên (Thủ kho Tiếp nhận / Maker)', 'ROLE_STAFF', 1, CURRENT_TIMESTAMP),
(5, 'staff_quan', '123456', 'Thạch Hoàng Quân (Thủ kho Xuất hàng / Maker)', 'ROLE_STAFF', 1, CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------------------
-- 2. BẢNG `category`: DANH MỤC NGÀNH HÀNG PHỤ TÙNG XE MÁY YAMAHA
-- -----------------------------------------------------------------------------
INSERT INTO `category` (id, code, name, active, created_at) VALUES
(1, 'LUB', 'Dầu nhớt Yamalube & Hóa chất bảo dưỡng chính hãng', 1, CURRENT_TIMESTAMP),
(2, 'ENG', 'Động cơ & Cơ khí chính xác (Pít-tông, Bạc đạn, Xú-páp)', 1, CURRENT_TIMESTAMP),
(3, 'ELE', 'Hệ thống Điện, Đèn LED & Đánh lửa (Bugi, ECU, Bobin)', 1, CURRENT_TIMESTAMP),
(4, 'BDY', 'Dàn áo, Khung sườn & Gương chiếu hậu (Yamaha Genuine Body)', 1, CURRENT_TIMESTAMP),
(5, 'BRK', 'Hệ thống Phanh & Dẫn động (Má phanh, Đĩa phanh, Dây curoa)', 1, CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------------------
-- 3. BẢNG `warehouse_location`: SƠ ĐỒ VỊ TRÍ Ô KỆ PHÂN VÙNG 3M FLOOR MARKING
-- -----------------------------------------------------------------------------
INSERT INTO `warehouse_location` (id, location_code, zone, max_capacity, status, created_at) VALUES
(1, 'DOCK-01', 'DOCK', 10000.00, 'EMPTY', CURRENT_TIMESTAMP),
(2, 'INSP-01', 'INSP', 5000.00, 'EMPTY', CURRENT_TIMESTAMP),
(3, 'RAW-A01-01', 'RAW', 2000.00, 'EMPTY', CURRENT_TIMESTAMP),
(4, 'RAW-A01-02', 'RAW', 2000.00, 'EMPTY', CURRENT_TIMESTAMP),
(5, 'FG-B01-01', 'FG', 3000.00, 'EMPTY', CURRENT_TIMESTAMP),
(6, 'FG-B01-02', 'FG', 3000.00, 'EMPTY', CURRENT_TIMESTAMP),
(7, 'FG-B02-01', 'FG', 3000.00, 'EMPTY', CURRENT_TIMESTAMP),
(8, 'SCRAP-01', 'SCRAP', 1000.00, 'EMPTY', CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------------------
-- 4. BẢNG `product`: DANH MỤC SKU PHỤ TÙNG CHÍNH HÃNG YAMAHA
-- -----------------------------------------------------------------------------
INSERT INTO `product` (id, category_id, sku_code, name, uom, min_stock, max_stock, created_at) VALUES
(1, 1, 'YAM-LUB-001', 'Dầu nhớt Yamalube 4T 10W-40 GP 800ml (Dầu động cơ xe số)', 'Chai', 50, 500, CURRENT_TIMESTAMP),
(2, 1, 'YAM-LUB-002', 'Dầu nhớt Yamalube AT 10W-40 800ml (Dầu động cơ xe tay ga)', 'Chai', 50, 500, CURRENT_TIMESTAMP),
(3, 2, 'YAM-ENG-001', 'Bộ pít-tông & xéc-măng Exciter 155 VVA chính hãng', 'Bộ', 20, 200, CURRENT_TIMESTAMP),
(4, 5, 'YAM-BRK-001', 'Bộ má phanh đĩa trước NVX 155 ABS', 'Bộ', 30, 300, CURRENT_TIMESTAMP),
(5, 3, 'YAM-ELE-001', 'Bugi tiêu chuẩn NGK CPR8EA-9 Yamaha chính hãng', 'Cái', 100, 1000, CURRENT_TIMESTAMP),
(6, 4, 'YAM-BDY-001', 'Ốp yếm sườn phải YZF-R15 V4 Racing Blue', 'Cái', 10, 100, CURRENT_TIMESTAMP);
