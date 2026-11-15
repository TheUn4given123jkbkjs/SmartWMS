# THIẾT KẾ LƯỢC ĐỒ QUAN HỆ CSDL (RELATIONAL SCHEMA - 3NF)
**Nhiệm vụ:** `T3.1` (Sprint 3) | **Maker:** M1 - Nguyễn Thành Trung | **Reviewer:** M2 - Nguyễn Bảo Khang  
**Tiêu chuẩn:** Chuẩn hóa 3NF, Khóa ngoại tường minh, loại bỏ triệt để dị thường dữ liệu.  
👉 *Chi tiết từng trường, kiểu dữ liệu, độ dài và giải nghĩa nghiệp vụ: xem tại [Từ điển dữ liệu (data_dictionary.md)](data_dictionary.md).*

---

## 1. CHỨNG MINH CHUẨN HÓA 3NF (NORMALIZATION)

Mô hình dữ liệu SmartWMS gồm **10 bảng** được chuẩn hóa theo quy trình 3 bước:

| Dạng chuẩn | Tiêu chí | Chứng minh trên mô hình SmartWMS |
| :---: | :--- | :--- |
| **1NF** | Thuộc tính nguyên tử, không mảng lặp, có PK xác định. | • 100% thuộc tính đơn trị (Atomic).<br>• 100% bảng có Khóa chính đơn nhân tạo ($\underline{\text{id}}$ `BIGINT AUTO_INCREMENT`). |
| **2NF** | Đạt 1NF và không có phụ thuộc hàm từng phần vào khóa. | • Vì 100% bảng dùng **Khóa chính đơn `id`**, nên về toán học **không thể tồn tại phụ thuộc từng phần**. Mọi thuộc tính không khóa đều phụ thuộc đầy đủ vào duy nhất `id`. |
| **3NF** | Đạt 2NF và không có phụ thuộc hàm bắc cầu ($X \rightarrow Y \rightarrow Z$). | • **Loại bỏ cột `available_qty`:** Tồn khả dụng $\text{Available} = \text{on\_hand\_qty} - \text{reserved\_qty}$ là giá trị phái sinh. Nếu lưu thêm sẽ gây phụ thuộc bắc cầu: $(\text{on\_hand}, \text{reserved}) \rightarrow \text{available}$ (dễ lỗi không đồng bộ). Nhóm tính động qua SQL.<br>• **Khử quan hệ N:N:** Phân rã quan hệ Nhiều - Nhiều giữa `product` và `warehouse_location` qua thực thể trung tâm `stock_batch`.<br>• **Tách Master - Detail:** Tách riêng header chứng từ (`receipt_note`, `issue_note`) và dòng chi tiết (`receipt_note_item`, `issue_note_item`). |

---

## 2. MA TRẬN KHÓA & RÀNG BUỘC TOÀN VẸN (CONSTRAINTS MATRIX)

| Bảng | Khóa chính (PK) | Khóa ngoại (FK) & Tham chiếu | Hành vi kích hoạt | Ràng buộc toàn vẹn & Quy tắc BR |
| :--- | :---: | :--- | :---: | :--- |
| **`category`** | `id` | — | — | `UNIQUE(code)` |
| **`warehouse_location`** | `id` | — | — | `UNIQUE(location_code)`, CHECK `zone IN ('DOCK','INSP','RAW','FG','SCRAP')`, CHECK `status IN ('EMPTY','OCCUPIED','MAINTENANCE')` (`BR-CAT-02`, `03`) |
| **`user`** | `id` | — | — | `UNIQUE(username)`, CHECK `role IN ('ROLE_STAFF','ROLE_MANAGER','ROLE_ADMIN')` |
| **`product`** | `id` | `category_id` $\rightarrow$ `category.id` | ON DELETE RESTRICT | `UNIQUE(sku_code)`, CHECK `min_stock >= 0`, `max_stock > min_stock` (`BR-CAT-01`, `05`) |
| **`stock_batch`** | `id` | `product_id` $\rightarrow$ `product.id`<br>`location_id` $\rightarrow$ `warehouse_location.id` | ON DELETE RESTRICT | `UNIQUE(product_id, location_id, lot_number)` (`BR-LOT-01`), CHECK `on_hand_qty >= 0`, `reserved_qty >= 0`, `reserved_qty <= on_hand_qty` (`BR-OUT-01`) |
| **`receipt_note`** | `id` | `maker_id` $\rightarrow$ `user.id`<br>`checker_id` $\rightarrow$ `user.id` | ON DELETE RESTRICT | `UNIQUE(receipt_number)`, CHECK `checker_id IS NULL OR checker_id <> maker_id` (`BR-SEC-03`), CHECK `status IN ('PENDING_APPROVAL','COMPLETED','REJECTED','CANCELLED')` |
| **`receipt_note_item`**| `id` | `receipt_id` $\rightarrow$ `receipt_note.id`<br>`batch_id` $\rightarrow$ `stock_batch.id`<br>`product_id` $\rightarrow$ `product.id`<br>`location_id` $\rightarrow$ `warehouse_location.id` | ON DELETE CASCADE (phiếu)<br>ON DELETE SET NULL (lô)<br>ON DELETE RESTRICT | CHECK `accepted_qty >= 0`, `damaged_qty >= 0`, `accepted_qty + damaged_qty > 0` (`BR-INB-01`, `04`) |
| **`issue_note`** | `id` | `maker_id` $\rightarrow$ `user.id`<br>`checker_id` $\rightarrow$ `user.id` | ON DELETE RESTRICT | `UNIQUE(issue_number)`, CHECK `checker_id IS NULL OR checker_id <> maker_id` (`BR-SEC-03`), CHECK `dispatch_strategy IN ('FEFO','FIFO')` (`BR-OUT-02`) |
| **`issue_note_item`** | `id` | `issue_id` $\rightarrow$ `issue_note.id`<br>`batch_id` $\rightarrow$ `stock_batch.id` | ON DELETE CASCADE (phiếu)<br>ON DELETE RESTRICT (lô) | CHECK `requested_qty > 0`, `allocated_qty >= 0`, `allocated_qty <= requested_qty` (`BR-OUT-01`) |
| **`stock_card`** | `id` | `product_id` $\rightarrow$ `product.id`<br>`batch_id` $\rightarrow$ `stock_batch.id` | ON DELETE RESTRICT | CHECK `change_type IN ('NHAP_PO','XUAT_KHO','KIEM_KE','DIEU_CHINH')`, CHECK `qty_changed <> 0`, `after_changed >= 0` (`BR-SEC-02`) |

---

## 3. TỔ CHỨC MÃ NGUỒN SQL DDL

Mã DDL được bóc tách thành **10 file SQL độc lập** tại thư mục [`tables/`](tables/) theo thứ tự phụ thuộc:
1. [`01_category.sql`](tables/01_category.sql) $\rightarrow$ 2. [`02_warehouse_location.sql`](tables/02_warehouse_location.sql) $\rightarrow$ 3. [`03_user.sql`](tables/03_user.sql)
4. [`04_product.sql`](tables/04_product.sql) $\rightarrow$ 5. [`05_stock_batch.sql`](tables/05_stock_batch.sql) $\rightarrow$ 6. [`06_receipt_note.sql`](tables/06_receipt_note.sql)
7. [`07_receipt_note_item.sql`](tables/07_receipt_note_item.sql) $\rightarrow$ 8. [`08_issue_note.sql`](tables/08_issue_note.sql) $\rightarrow$ 9. [`09_issue_note_item.sql`](tables/09_issue_note_item.sql)
10. [`10_stock_card.sql`](tables/10_stock_card.sql)

👉 **Kịch bản tổng hợp duy nhất:** [schema.sql](schema.sql) (đã đồng bộ tại `source/src/main/resources/schema.sql`).
