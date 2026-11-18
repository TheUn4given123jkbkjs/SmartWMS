# TỪ ĐIỂN DỮ LIỆU CƠ SỞ DỮ LIỆU (DATA DICTIONARY)
**Học phần:** Phân tích và Thiết kế Yêu cầu (Mã môn: 502050 - ĐH Tôn Đức Thắng)  
**Nhiệm vụ:** `T3.2` (Sprint 3) | **Maker:** M2 - Nguyễn Bảo Khang | **Reviewer:** M3 - Tôn Thanh Thiên  
**Phạm vi:** 12 Bảng (10 Bảng nghiệp vụ kho lõi + 2 Bảng tham chiếu Đơn mua hàng PO) thuộc các phân hệ Must-have (`FR01` – `FR04`) & Xác thực phân quyền  
**Tiêu chuẩn cấu trúc:** Biểu mẫu chuẩn 8 cột theo `SpecificationStandard.md` (Mục 3.2).

---

## 1. BẢNG: `category` (Ngành hàng phân loại sản phẩm)
*Mô tả: Lưu trữ danh mục ngành hàng nhằm phân nhóm mặt hàng phục vụ quản lý và thống kê danh mục.*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh ngành hàng |
| `code` | VARCHAR | 30 | NO | **UK** | None | UNIQUE, Not empty | Mã ngành hàng duy nhất |
| `name` | VARCHAR | 255 | NO | - | None | Not empty | Tên ngành hàng phân loại |
| `active` | BIT | 1 | NO | - | 1 | `IN (0, 1)` | Trạng thái hoạt động (1=Active, 0=Inactive) |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm tạo bản ghi |
| `updated_at` | TIMESTAMP | - | YES | - | CURRENT_TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | Thời điểm cập nhật gần nhất |

---

## 2. BẢNG: `warehouse_location` (Vị trí ô kệ chuẩn 3M)
*Mô tả: Quản lý không gian lưu trữ vật lý của nhà kho theo chuẩn 3M Floor Marking (Zone - Shelf - Bin).*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh vị trí ô kệ |
| `location_code` | VARCHAR | 30 | NO | **UK** | None | UNIQUE, Not empty (`BR-CAT-02`) | Mã vị trí chuẩn 3M (VD: `RAW-A-01`) |
| `zone` | VARCHAR | 20 | NO | - | None | `IN ('DOCK','INSP','RAW','FG','SCRAP')` | Phân vùng chức năng trong kho |
| `max_capacity` | DECIMAL | (12, 2)| YES | - | NULL | `max_capacity >= 0` | Sức chứa hoặc tải trọng tối đa (kg/pallet) |
| `status` | VARCHAR | 20 | NO | - | 'EMPTY' | `IN ('EMPTY','OCCUPIED','MAINTENANCE')` (`BR-CAT-03`) | Trạng thái ô kệ lưu trữ |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm khởi tạo vị trí |
| `updated_at` | TIMESTAMP | - | YES | - | CURRENT_TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | Thời điểm cập nhật trạng thái ô kệ |

---

## 3. BẢNG: `user` (Tài khoản người dùng & Maker-Checker)
*Mô tả: Quản lý danh sách nhân sự, vai trò phân quyền và kiểm soát thẩm quyền độc lập 4 mắt.*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh người dùng |
| `username` | VARCHAR | 50 | NO | **UK** | None | UNIQUE, Not empty | Tên tài khoản đăng nhập |
| `password_hash` | VARCHAR | 255 | NO | - | None | Not empty (BCrypt hash) | Mật khẩu băm an toàn |
| `full_name` | VARCHAR | 100 | NO | - | None | Not empty | Họ và tên đầy đủ của nhân viên |
| `role` | VARCHAR | 30 | NO | - | None | `IN ('ROLE_STAFF','ROLE_MANAGER','ROLE_ADMIN')` | Vai trò phân quyền hệ thống |
| `active` | BIT | 1 | NO | - | 1 | `IN (0, 1)` | Trạng thái tài khoản (1=Khả dụng, 0=Khóa) |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm tạo tài khoản |
| `updated_at` | TIMESTAMP | - | YES | - | CURRENT_TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | Thời điểm cập nhật tài khoản |

---

## 4. BẢNG: `product` (Mặt hàng lưu kho / SKU)
*Mô tả: Quản lý thông tin hàng hóa, đơn vị tính cơ sở và ngưỡng tồn kho an toàn Min/Max.*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh mặt hàng |
| `category_id` | BIGINT | - | NO | **FK** | None | REFERENCES `category(id)` RESTRICT | Khóa ngoại ngành hàng |
| `sku_code` | VARCHAR | 50 | NO | **UK** | None | UNIQUE, Not empty (`BR-CAT-01`) | Mã SKU định danh duy nhất toàn hệ thống |
| `name` | VARCHAR | 255 | NO | - | None | Not empty | Tên thương mại của sản phẩm |
| `uom` | VARCHAR | 50 | NO | - | None | Not empty (`BR-CAT-04`) | Đơn vị tính cơ sở duy nhất |
| `min_stock` | INT | - | NO | - | 0 | `min_stock >= 0` (`BR-CAT-05`) | Định mức tồn tối thiểu an toàn |
| `max_stock` | INT | - | NO | - | None | `max_stock > min_stock` (`BR-CAT-05`) | Định mức tồn tối đa cho phép |
| `manage_by_lot` | BIT | 1 | NO | - | 1 | `IN (0, 1)` | Cờ quản lý theo hạn dùng/lô |
| `active` | BIT | 1 | NO | - | 1 | `IN (0, 1)` | Trạng thái kinh doanh sản phẩm |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm khởi tạo mặt hàng |
| `updated_at` | TIMESTAMP | - | YES | - | CURRENT_TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | Thời điểm chỉnh sửa thông tin |

---

## 5. BẢNG: `stock_batch` (Lô hàng tồn kho - Thực thể trung tâm phân rã N:N)
*Mô tả: Theo dõi tồn kho thực tế và tạm giữ chỗ theo từng Lô, Hạn dùng và Vị trí ô kệ cụ thể. Không lưu cột dư thừa available_qty để đạt 3NF.*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh lô tồn kho |
| `product_id` | BIGINT | - | NO | **FK** | None | REFERENCES `product(id)` RESTRICT | Khóa ngoại sản phẩm |
| `location_id` | BIGINT | - | NO | **FK** | None | REFERENCES `warehouse_location(id)` RESTRICT | Khóa ngoại vị trí ô kệ chứa lô |
| `lot_number` | VARCHAR | 50 | NO | - | None | Not empty (`BR-LOT-01`). Hệ thống tự sinh mã nếu để trống khi `manage_by_lot = FALSE` | Số lô sản xuất từ nhà cung cấp (hoặc mã hệ thống tự sinh) |
| `exp_date` | DATE | - | YES | - | None | `exp_date > CURRENT_DATE` nếu `manage_by_lot = 1`; NULL nếu `manage_by_lot = 0` (`BR-LOT-01`) | Ngày hết hạn của lô hàng (NULL nếu không quản lý hạn dùng) |
| `on_hand_qty` | INT | - | NO | - | 0 | `on_hand_qty >= 0` (`BR-OUT-01`) | Số lượng tồn kho thực tế hiện vật |
| `reserved_qty` | INT | - | NO | - | 0 | `0 <= reserved_qty <= on_hand_qty` | Số lượng đang tạm giữ chỗ cho các lệnh xuất |
| `status` | VARCHAR | 20 | NO | - | 'AVAILABLE' | `IN ('AVAILABLE','EXPIRED','QUARANTINED','BLOCKED')` | Trạng thái chất lượng của lô |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm nhập lô vào hệ thống |
| `updated_at` | TIMESTAMP | - | YES | - | CURRENT_TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | Thời điểm cập nhật số dư |

---

## 6. BẢNG: `receipt_note` (Phiếu nhập kho - Header)
*Mô tả: Quản lý thông tin chung của chứng từ tiếp nhận hàng hóa theo đơn mua hàng PO kèm kiểm soát Maker-Checker.*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh phiếu nhập |
| `receipt_number` | VARCHAR | 50 | NO | **UK** | None | UNIQUE, Not empty | Mã chứng từ nhập (VD: `RN-20261104-001`) |
| `po_number` | VARCHAR | 50 | NO | - | None | Not empty (`BR-INB-01`) | Mã đơn mua hàng PO tham chiếu |
| `receipt_date` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm tiếp nhận chuyến hàng |
| `maker_id` | BIGINT | - | NO | **FK** | None | REFERENCES `user(id)` RESTRICT | Khóa ngoại Nhân viên lập phiếu (Maker) |
| `checker_id` | BIGINT | - | YES | **FK** | NULL | `checker_id <> maker_id` (`BR-SEC-03`) | Khóa ngoại Quản lý phê duyệt (Checker) |
| `status` | VARCHAR | 30 | NO | - | 'PENDING_APPROVAL' | `IN ('PENDING_APPROVAL','COMPLETED','REJECTED','CANCELLED')` | Trạng thái phiếu (COMPLETED: khóa cấm sửa) |
| `rejection_note` | VARCHAR | 255 | YES | - | NULL | Tối thiểu 10 ký tự nếu bị từ chối | Lý do từ chối phê duyệt từ Checker |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm tạo phiếu |
| `updated_at` | TIMESTAMP | - | YES | - | CURRENT_TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | Thời điểm duyệt / từ chối |

---

## 7. BẢNG: `receipt_note_item` (Chi tiết dòng nhập kho - Detail)
*Mô tả: Chi tiết các mặt hàng thực nhận, số lô, hạn dùng, vị trí cất và số lượng đạt chuẩn/hỏng (Composition).*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh dòng hàng nhập |
| `receipt_id` | BIGINT | - | NO | **FK** | None | REFERENCES `receipt_note(id)` CASCADE | Khóa ngoại liên kết Header phiếu nhập |
| `batch_id` | BIGINT | - | YES | **FK** | NULL | REFERENCES `stock_batch(id)` SET NULL | Khóa ngoại lô hàng sau khi duyệt nhập |
| `product_id` | BIGINT | - | NO | **FK** | None | REFERENCES `product(id)` RESTRICT | Khóa ngoại mặt hàng tiếp nhận |
| `location_id` | BIGINT | - | NO | **FK** | None | REFERENCES `warehouse_location(id)` RESTRICT | Khóa ngoại vị trí ô kệ cất hàng (`BR-INB-03`) |
| `lot_number` | VARCHAR | 50 | NO | - | None | Not empty (`BR-LOT-01`). Hệ thống tự sinh mã nếu để trống khi `manage_by_lot = FALSE` | Số lô trên nhãn bao bì (hoặc mã tự sinh) |
| `exp_date` | DATE | - | YES | - | None | `exp_date > CURRENT_DATE` nếu `manage_by_lot = 1`; NULL nếu `manage_by_lot = 0` (`BR-LOT-01`) | Hạn sử dụng của mặt hàng (NULL nếu không quản lý hạn dùng) |
| `accepted_qty` | INT | - | NO | - | 0 | `accepted_qty >= 0` (`BR-INB-01`) | Số lượng đạt chuẩn tiếp nhận vào kho |
| `damaged_qty` | INT | - | NO | - | 0 | `damaged_qty >= 0` (`BR-INB-04`) | Số lượng vỡ hỏng/lỗi từ chối tiếp nhận |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm tạo dòng chi tiết |

---

## 8. BẢNG: `issue_note` (Phiếu xuất kho - Header)
*Mô tả: Quản lý lệnh xuất kho, chiến lược phân bổ FEFO/FIFO, cờ ghi đè lô ngoại lệ và kiểm soát Maker-Checker.*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh phiếu xuất |
| `issue_number` | VARCHAR | 50 | NO | **UK** | None | UNIQUE, Not empty | Mã chứng từ xuất (VD: `IN-20261104-001`) |
| `issue_date` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm tạo lệnh xuất kho |
| `dispatch_strategy` | VARCHAR | 10 | NO | - | 'FEFO' | `IN ('FEFO','FIFO')` (`BR-OUT-02`) | Chiến lược xuất kho ưu tiên |
| `maker_id` | BIGINT | - | NO | **FK** | None | REFERENCES `user(id)` RESTRICT | Khóa ngoại Nhân viên lập lệnh xuất (Maker) |
| `checker_id` | BIGINT | - | YES | **FK** | NULL | `checker_id <> maker_id` (`BR-SEC-03`) | Khóa ngoại Quản lý phê duyệt (Checker) |
| `status` | VARCHAR | 30 | NO | - | 'PENDING_APPROVAL' | `IN ('PENDING_APPROVAL','APPROVED_READY_TO_PICK','COMPLETED','REJECTED','CANCELLED')` | Trạng thái vòng đời lệnh xuất kho |
| `is_overridden` | BIT | 1 | NO | - | 0 | `IN (0, 1)` | Cờ ghi đè lô ngoại lệ (Luồng 4a) |
| `override_reason` | VARCHAR | 255 | YES | - | NULL | Bắt buộc khi `is_overridden = 1` | Lý do giải trình chọn lô khác (kẹt lối đi...) |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm tạo lệnh xuất |
| `updated_at` | TIMESTAMP | - | YES | - | CURRENT_TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | Thời điểm duyệt / xác nhận xuất |

---

## 9. BẢNG: `issue_note_item` (Chi tiết phân bổ xuất kho - Detail)
*Mô tả: Danh sách chỉ dẫn lấy hàng (Pick list) phân bổ số lượng cần bốc dỡ theo từng lô và vị trí ô kệ.*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh dòng phân bổ xuất |
| `issue_id` | BIGINT | - | NO | **FK** | None | REFERENCES `issue_note(id)` CASCADE | Khóa ngoại liên kết Header phiếu xuất |
| `batch_id` | BIGINT | - | NO | **FK** | None | REFERENCES `stock_batch(id)` RESTRICT | Khóa ngoại chỉ định lô hàng cần bốc (`BR-OUT-06`) |
| `requested_qty` | INT | - | NO | - | None | `requested_qty > 0` (`BR-OUT-01`) | Số lượng yêu cầu xuất của mặt hàng |
| `allocated_qty` | INT | - | NO | - | 0 | `0 <= allocated_qty <= requested_qty` | Số lượng thực tế phân bổ từ lô này |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm phân bổ dòng hàng |

---

## 10. BẢNG: `stock_card` (Thẻ kho điện tử - Sổ cái biến động bất biến)
*Mô tả: Sổ nhật ký ghi vết bất biến (Immutable Ledger) mọi biến động tăng/giảm tồn kho thực tế phục vụ kiểm toán.*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh dòng Thẻ kho |
| `product_id` | BIGINT | - | NO | **FK** | None | REFERENCES `product(id)` RESTRICT | Khóa ngoại sản phẩm biến động |
| `batch_id` | BIGINT | - | NO | **FK** | None | REFERENCES `stock_batch(id)` RESTRICT | Khóa ngoại lô hàng biến động |
| `transaction_date` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm giao dịch phát sinh ghi sổ |
| `reference_code` | VARCHAR | 50 | NO | - | None | Not empty | Mã chứng từ đối chiếu (`RN-xxx` hoặc `IN-xxx`) |
| `change_type` | VARCHAR | 20 | NO | - | None | `IN ('NHAP_PO','XUAT_KHO','KIEM_KE','DIEU_CHINH')` | Loại hình giao dịch phát sinh |
| `qty_changed` | INT | - | NO | - | None | `qty_changed <> 0` | Lượng biến động (+ khi nhập, - khi xuất) |
| `after_changed` | INT | - | NO | - | None | `after_changed >= 0` (`BR-SEC-02`) | Tồn kho thực tế lũy kế sau giao dịch |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm ghi nhận dòng Thẻ kho |

---

## 11. BẢNG: `purchase_order` (Đơn mua hàng từ nhà cung cấp - PO Header)
*Mô tả: Lưu trữ thông tin tham chiếu đơn mua hàng (PO) đã được phê duyệt từ hệ thống Mua hàng/ERP bên ngoài, làm căn cứ đối soát số lượng và chặn nhận thừa khi nhập kho (`BR-INB-01`).*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh đơn mua hàng |
| `po_number` | VARCHAR | 50 | NO | **UK** | None | UNIQUE, Not empty (`BR-INB-01`) | Mã đơn mua hàng duy nhất (VD: `PO-202611-001`) |
| `supplier_name` | VARCHAR | 255 | NO | - | None | Not empty | Tên nhà cung cấp phụ tùng |
| `status` | VARCHAR | 30 | NO | - | 'APPROVED' | `IN ('APPROVED','PARTIALLY_RECEIVED','CLOSED','CANCELLED')` | Trạng thái tiếp nhận đơn hàng |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm đồng bộ/khởi tạo PO |
| `updated_at` | TIMESTAMP | - | YES | - | CURRENT_TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | Thời điểm cập nhật trạng thái |

---

## 12. BẢNG: `purchase_order_item` (Chi tiết dòng đơn mua hàng - PO Detail)
*Mô tả: Chi tiết số lượng đặt mua và số lượng đã giao lũy kế của từng mặt hàng trên đơn PO, phục vụ tính toán số lượng còn lại chưa giao và hàng đang về (`BR-INV-01`).*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh dòng hàng PO |
| `purchase_order_id`| BIGINT | - | NO | **FK** | None | REFERENCES `purchase_order(id)` CASCADE | Khóa ngoại liên kết đơn mua hàng |
| `product_id` | BIGINT | - | NO | **FK** | None | REFERENCES `product(id)` RESTRICT | Khóa ngoại mặt hàng đặt mua |
| `ordered_qty` | DECIMAL | (12, 2)| NO | - | None | `ordered_qty > 0` | Số lượng đặt mua theo hợp đồng/PO |
| `received_qty` | DECIMAL | (12, 2)| NO | - | 0.00 | `0 <= received_qty <= ordered_qty` | Số lượng thực tế đã nhập kho lũy kế |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm tạo dòng chi tiết PO |
| `updated_at` | TIMESTAMP | - | YES | - | CURRENT_TIMESTAMP | ON UPDATE CURRENT_TIMESTAMP | Thời điểm cập nhật số lượng đã giao |
