# BÁO CÁO THIẾT KẾ LƯỢC ĐỒ QUAN HỆ CSDL (RELATIONAL SCHEMA - 3NF)
**Học phần:** Phân tích và Thiết kế Yêu cầu (Mã môn: 502050 - ĐH Tôn Đức Thắng)  
**Nhiệm vụ:** `T3.1` (Sprint 3) | **Maker:** M1 - Nguyễn Thành Trung | **Reviewer:** M2 - Nguyễn Bảo Khang  
**Phạm vi:** 4 Phân hệ Must-have (`FR01` – `FR04`) & Kiểm soát bảo mật Maker-Checker (`FR-SEC`)  
**Tài liệu tham chiếu:** [Class Diagram & ERD](../../02_uml_modeling/class_erd/class_erd.md) | [Đặc tả Use Case](../../02_uml_modeling/use_case/usecase_specification.md)

---

## 1. BIỂU THỨC LƯỢC ĐỒ QUAN HỆ (FORMAL RELATIONAL SCHEMA)

Toàn bộ 10 thực thể trong mô hình CSDL SmartWMS được biểu diễn hình thức qua 10 quan hệ đạt chuẩn 3NF:

1. **`category`** ($\underline{\text{id}}$, $\text{code}$, $\text{name}$, $\text{active}$, $\text{created\_at}$, $\text{updated\_at}$)
2. **`warehouse_location`** ($\underline{\text{id}}$, $\text{location\_code}$, $\text{zone}$, $\text{max\_capacity}$, $\text{status}$, $\text{created\_at}$, $\text{updated\_at}$)
3. **`user`** ($\underline{\text{id}}$, $\text{username}$, $\text{password\_hash}$, $\text{full\_name}$, $\text{role}$, $\text{active}$, $\text{created\_at}$, $\text{updated\_at}$)
4. **`product`** ($\underline{\text{id}}$, $\text{sku\_code}$, $\text{name}$, $\text{uom}$, $\text{min\_stock}$, $\text{max\_stock}$, $\text{manage\_by\_lot}$, $\text{active}$, $\text{created\_at}$, $\text{updated\_at}$, $\text{category\_id}^*$)
5. **`stock_batch`** ($\underline{\text{id}}$, $\text{lot\_number}$, $\text{exp\_date}$, $\text{on\_hand\_qty}$, $\text{reserved\_qty}$, $\text{status}$, $\text{created\_at}$, $\text{updated\_at}$, $\text{product\_id}^*$, $\text{location\_id}^*$)
6. **`receipt_note`** ($\underline{\text{id}}$, $\text{receipt\_number}$, $\text{po\_number}$, $\text{receipt\_date}$, $\text{status}$, $\text{rejection\_note}$, $\text{created\_at}$, $\text{updated\_at}$, $\text{maker\_id}^*$, $\text{checker\_id}^*$)
7. **`receipt_note_item`** ($\underline{\text{id}}$, $\text{lot\_number}$, $\text{exp\_date}$, $\text{accepted\_qty}$, $\text{damaged\_qty}$, $\text{created\_at}$, $\text{receipt\_id}^*$, $\text{product\_id}^*$, $\text{location\_id}^*$, $\text{batch\_id}^*$)
8. **`issue_note`** ($\underline{\text{id}}$, $\text{issue\_number}$, $\text{issue\_date}$, $\text{dispatch\_strategy}$, $\text{status}$, $\text{is\_overridden}$, $\text{override\_reason}$, $\text{created\_at}$, $\text{updated\_at}$, $\text{maker\_id}^*$, $\text{checker\_id}^*$)
9. **`issue_note_item`** ($\underline{\text{id}}$, $\text{requested\_qty}$, $\text{allocated\_qty}$, $\text{created\_at}$, $\text{issue\_id}^*$, $\text{batch\_id}^*$)
10. **`stock_card`** ($\underline{\text{id}}$, $\text{transaction\_date}$, $\text{reference\_code}$, $\text{change\_type}$, $\text{qty\_changed}$, $\text{after\_changed}$, $\text{created\_at}$, $\text{product\_id}^*$, $\text{batch\_id}^*$)

*Quy ước ký hiệu:*  
* Khóa chính (Primary Key - PK): Được **gạch chân** ($\underline{\text{id}}$).  
* Khóa ngoại (Foreign Key - FK): Được đánh dấu bằng dấu sao ($\text{fk\_id}^*$).

---

## 2. CHỨNG MINH VÀ GIẢI TRÌNH CHUẨN HÓA 3NF (NORMALIZATION PROOF)

Lược đồ cơ sở dữ liệu được thiết kế tuân thủ nghiêm ngặt lý thuyết chuẩn hóa để loại bỏ triệt để các dị thường thêm/xóa/sửa (Insertion, Deletion, Update Anomalies):

### 2.1. Đạt Chuẩn 1NF (First Normal Form)
* **Tính nguyên tử của dữ liệu (Atomicity):** Tất cả các thuộc tính đều chứa giá trị đơn nguyên tử, không có thuộc tính đa trị (Multi-valued Attributes) hay thuộc tính phức hợp (Composite Attributes).
  - Vị trí kho được chuẩn hóa thành chuỗi đơn `location_code` (Ví dụ: `RAW-A-01`) thay vì mảng.
  - Phân vùng kho được chuẩn hóa qua trường `zone` (chỉ nhận 1 giá trị trong tập ENUM quy định).
* **Khóa chính đơn nhân tạo:** 100% các bảng đều sử dụng khóa chính đơn nhân tạo ($\underline{\text{id}}$ `BIGINT AUTO_INCREMENT`), đảm bảo mọi bản ghi đều có định danh duy nhất xác định.

### 2.2. Đạt Chuẩn 2NF (Second Normal Form)
* **Điều kiện:** Đạt 1NF và mọi thuộc tính không khóa đều phụ thuộc hàm đầy đủ (Fully Functionally Dependent) vào khóa chính.
* **Chứng minh:** Vì tất cả 10 bảng đều sử dụng **Khóa chính đơn (`id`)**, không có bảng nào sử dụng khóa phức hợp nhiều cột (Composite Primary Key). Do đó, về mặt toán học, **không thể tồn tại phụ thuộc hàm từng phần (Partial Dependency)**. Mọi thuộc tính không khóa đều phụ thuộc trực tiếp vào duy nhất khóa `id`.

### 2.3. Đạt Chuẩn 3NF (Third Normal Form)
* **Điều kiện:** Đạt 2NF và không có phụ thuộc hàm bắc cầu (Transitive Dependency) giữa các thuộc tính không khóa ($X \rightarrow Y$ và $Y \rightarrow Z$ với $Z$ không thuộc khóa).
* **Các quyết định thiết kế then chốt để đạt 3NF:**
  1. **Loại bỏ triệt để cột dư thừa `available_qty`:**
     - Trong nghiệp vụ, lượng tồn khả dụng được tính theo công thức: $\text{Available} = \text{on\_hand\_qty} - \text{reserved\_qty}$.
     - Nếu lưu thêm cột `available_qty` vào bảng `stock_batch`, sẽ phát sinh phụ thuộc hàm bắc cầu: $(\text{on\_hand\_qty}, \text{reserved\_qty}) \longrightarrow \text{available\_qty}$. Khi đó, mỗi lần cập nhật số lượng giữ chỗ hoặc tồn kho, hệ thống phải cập nhật đồng thời nhiều cột, rất dễ phát sinh **dị thường cập nhật (Update Anomaly)** dẫn đến số liệu bất nhất.
     - **Giải pháp 3NF:** Bảng `stock_batch` chỉ lưu 2 thuộc tính độc lập là `on_hand_qty` (tồn thực tế) và `reserved_qty` (tồn giữ chỗ). Thuộc tính `available_qty` là giá trị phái sinh tính toán động trên RAM hoặc thông qua câu lệnh SQL:
       $$\text{SELECT } (\text{on\_hand\_qty} - \text{reserved\_qty}) \text{ AS available\_qty FROM stock\_batch}$$
  2. **Phân rã Quan hệ Nhiều - Nhiều (N:N) giữa `product` và `warehouse_location`:**
     - Một sản phẩm có thể lưu ở nhiều vị trí kho, và một vị trí kho có thể chứa nhiều sản phẩm khác nhau.
     - Thay vì lưu các mảng ID gây vi phạm 1NF hoặc bảng kết hợp nghèo nàn, nhóm phân rã thành thực thể trung tâm **`stock_batch`** chứa thông tin quản lý Lô (`lot_number`), Hạn dùng (`exp_date`), Vị trí cất (`location_id`) và Sản phẩm (`product_id`).
  3. **Tách rời Master - Detail cho Chứng từ nhập/xuất:**
     - Các thông tin chung của phiếu (mã phiếu, ngày tạo, người lập `maker_id`, người duyệt `checker_id`) nằm tại `receipt_note` và `issue_note`.
     - Các thông tin chi tiết từng mặt hàng và số lượng bốc dỡ/tiếp nhận được phân rã về `receipt_note_item` và `issue_note_item`, tránh lặp lại thông tin chung của chứng từ trên từng dòng hàng.

---

## 3. MA TRẬN KHÓA CHÍNH, KHÓA NGOẠI & HÀNH ĐỘNG KÍCH HOẠT (PK-FK MATRIX)

| Bảng chứa khóa ngoại | Khóa ngoại (FK) | Bảng tham chiếu (Target) | Khóa chính (PK) | ON DELETE | ON UPDATE | Ý nghĩa nghiệp vụ |
| :--- | :--- | :--- | :--- | :---: | :---: | :--- |
| **`product`** | `category_id` | `category` | `id` | `RESTRICT` | `CASCADE` | Không cho xóa ngành hàng nếu đang có sản phẩm thuộc ngành đó. |
| **`stock_batch`** | `product_id` | `product` | `id` | `RESTRICT` | `CASCADE` | Không cho xóa sản phẩm nếu đang có lô tồn kho thực tế. |
| **`stock_batch`** | `location_id` | `warehouse_location` | `id` | `RESTRICT` | `CASCADE` | Không cho xóa vị trí ô kệ nếu đang chứa lô hàng. |
| **`receipt_note`** | `maker_id` | `user` | `id` | `RESTRICT` | `CASCADE` | Bảo toàn thông tin người lập phiếu để kiểm toán. |
| **`receipt_note`** | `checker_id` | `user` | `id` | `RESTRICT` | `CASCADE` | Bảo toàn thông tin người phê duyệt chứng từ. |
| **`receipt_note_item`** | `receipt_id` | `receipt_note` | `id` | `CASCADE` | `CASCADE` | Quan hệ Composition: Xóa phiếu nhập (ở bản nháp) thì tự xóa các dòng chi tiết. |
| **`receipt_note_item`** | `batch_id` | `stock_batch` | `id` | `SET NULL` | `CASCADE` | Cho phép null khi lập phiếu, gán lô khi nhập kho. |
| **`receipt_note_item`** | `product_id` | `product` | `id` | `RESTRICT` | `CASCADE` | Ràng buộc mặt hàng nhập kho. |
| **`receipt_note_item`** | `location_id` | `warehouse_location` | `id` | `RESTRICT` | `CASCADE` | Ràng buộc vị trí cất hàng chỉ định. |
| **`issue_note`** | `maker_id` | `user` | `id` | `RESTRICT` | `CASCADE` | Bảo toàn thông tin người tạo lệnh xuất kho. |
| **`issue_note`** | `checker_id` | `user` | `id` | `RESTRICT` | `CASCADE` | Bảo toàn thông tin người duyệt xuất kho. |
| **`issue_note_item`** | `issue_id` | `issue_note` | `id` | `CASCADE` | `CASCADE` | Quan hệ Composition: Xóa phiếu xuất thì tự xóa các dòng phân bổ. |
| **`issue_note_item`** | `batch_id` | `stock_batch` | `id` | `RESTRICT` | `CASCADE` | Chỉ định chính xác lô hàng cần lấy theo FEFO/FIFO. |
| **`stock_card`** | `product_id` | `product` | `id` | `RESTRICT` | `CASCADE` | Sổ cái Thẻ kho bất biến gắn với sản phẩm. |
| **`stock_card`** | `batch_id` | `stock_batch` | `id` | `RESTRICT` | `CASCADE` | Sổ cái Thẻ kho bất biến gắn với từng lô. |

---

## 4. BẢNG RÀNG BUỘC TOÀN VẸN NGHIỆP VỤ (BUSINESS INTEGRITY CONSTRAINTS)

| Bảng | Tên ràng buộc | Cú pháp biểu thức (Constraint Expression) | Quy tắc BR chi phối |
| :--- | :--- | :--- | :---: |
| **`category`** | `uk_category_code` | `UNIQUE(code)` | — |
| **`warehouse_location`** | `uk_location_code` | `UNIQUE(location_code)` | `BR-CAT-02` |
| **`warehouse_location`** | `chk_location_zone` | `zone IN ('DOCK', 'INSP', 'RAW', 'FG', 'SCRAP')` | `BR-CAT-02` |
| **`warehouse_location`** | `chk_location_status`| `status IN ('EMPTY', 'OCCUPIED', 'MAINTENANCE')` | `BR-CAT-03` |
| **`user`** | `uk_user_username` | `UNIQUE(username)` | — |
| **`user`** | `chk_user_role` | `role IN ('ROLE_STAFF', 'ROLE_MANAGER', 'ROLE_ADMIN')` | `BR-SEC-03` |
| **`product`** | `uk_product_sku` | `UNIQUE(sku_code)` | `BR-CAT-01` |
| **`product`** | `chk_product_min_stock` | `min_stock >= 0` | `BR-CAT-05` |
| **`product`** | `chk_product_max_stock` | `max_stock > min_stock` | `BR-CAT-05` |
| **`stock_batch`** | `uk_batch_product_loc_lot` | `UNIQUE(product_id, location_id, lot_number)` | `BR-LOT-01` |
| **`stock_batch`** | `chk_batch_on_hand` | `on_hand_qty >= 0` | `BR-OUT-01` |
| **`stock_batch`** | `chk_batch_reserved` | `reserved_qty >= 0` | `BR-OUT-01` |
| **`stock_batch`** | `chk_batch_reservation_limit` | `reserved_qty <= on_hand_qty` | `BR-OUT-01` |
| **`stock_batch`** | `chk_batch_status` | `status IN ('AVAILABLE', 'EXPIRED', 'QUARANTINED', 'BLOCKED')`| `BR-OUT-03` |
| **`receipt_note`** | `uk_receipt_number` | `UNIQUE(receipt_number)` | — |
| **`receipt_note`** | `chk_receipt_maker_checker` | `checker_id IS NULL OR checker_id <> maker_id` | `BR-SEC-03` |
| **`receipt_note`** | `chk_receipt_status` | `status IN ('PENDING_APPROVAL', 'COMPLETED', 'REJECTED', 'CANCELLED')` | `BR-SEC-01` |
| **`receipt_note_item`**| `chk_receipt_item_accepted` | `accepted_qty >= 0` | `BR-INB-01` |
| **`receipt_note_item`**| `chk_receipt_item_damaged` | `damaged_qty >= 0` | `BR-INB-04` |
| **`receipt_note_item`**| `chk_receipt_item_total` | `accepted_qty + damaged_qty > 0` | — |
| **`issue_note`** | `uk_issue_number` | `UNIQUE(issue_number)` | — |
| **`issue_note`** | `chk_issue_maker_checker` | `checker_id IS NULL OR checker_id <> maker_id` | `BR-SEC-03` |
| **`issue_note`** | `chk_issue_strategy` | `dispatch_strategy IN ('FEFO', 'FIFO')` | `BR-OUT-02` |
| **`issue_note_item`** | `chk_issue_item_requested` | `requested_qty > 0` | `BR-OUT-01` |
| **`issue_note_item`** | `chk_issue_item_allocated` | `allocated_qty >= 0` | `BR-OUT-01` |
| **`issue_note_item`** | `chk_issue_item_allocated_limit`| `allocated_qty <= requested_qty` | `BR-OUT-01` |
| **`stock_card`** | `chk_card_change_type` | `change_type IN ('NHAP_PO', 'XUAT_KHO', 'KIEM_KE', 'DIEU_CHINH')` | `BR-SEC-02` |
| **`stock_card`** | `chk_card_qty_changed` | `qty_changed <> 0` | `BR-SEC-02` |
| **`stock_card`** | `chk_card_after_changed` | `after_changed >= 0` | `BR-SEC-02` |

---

## 5. TỔ CHỨC MÃ NGUỒN SQL DDL (SEPARATED TABLE FILES)

Để phục vụ quản lý mã nguồn dạng module hóa theo chuẩn thiết kế, toàn bộ mã SQL DDL được bóc tách độc lập thành **10 file SQL riêng biệt** tại thư mục [`tables/`](tables/):

| Thứ tự nạp | Tên File SQL | Tên Bảng tương ứng | Trách nhiệm lưu trữ dữ liệu |
| :---: | :--- | :--- | :--- |
| **01** | [`01_category.sql`](tables/01_category.sql) | `category` | Ngành hàng phân loại sản phẩm. |
| **02** | [`02_warehouse_location.sql`](tables/02_warehouse_location.sql) | `warehouse_location` | Mặt bằng vị trí ô kệ chuẩn 3M (Zone, Capacity, Status). |
| **03** | [`03_user.sql`](tables/03_user.sql) | `user` | Tài khoản người dùng, vai trò phân quyền. |
| **04** | [`04_product.sql`](tables/04_product.sql) | `product` | Mặt hàng SKU, đơn vị tính, định mức Min/Max Stock. |
| **05** | [`05_stock_batch.sql`](tables/05_stock_batch.sql) | `stock_batch` | Thực thể trung tâm quản lý Lô, Hạn dùng, On-hand, Reserved. |
| **06** | [`06_receipt_note.sql`](tables/06_receipt_note.sql) | `receipt_note` | Header phiếu nhập kho kèm ràng buộc Maker-Checker. |
| **07** | [`07_receipt_note_item.sql`](tables/07_receipt_note_item.sql) | `receipt_note_item` | Dòng hàng chi tiết nhập kho & phân loại hàng hỏng (Composition). |
| **08** | [`08_issue_note.sql`](tables/08_issue_note.sql) | `issue_note` | Header phiếu xuất kho, chiến lược FEFO/FIFO, cờ ghi đè lô. |
| **09** | [`09_issue_note_item.sql`](tables/09_issue_note_item.sql) | `issue_note_item` | Dòng phân bổ xuất kho & danh sách Pick list (Composition). |
| **10** | [`10_stock_card.sql`](tables/10_stock_card.sql) | `stock_card` | Sổ cái Thẻ kho điện tử bất biến ghi vết lịch sử xuất nhập. |

*File kịch bản tổng hợp duy nhất phục vụ khởi tạo nhanh toàn bộ CSDL:*  
👉 **[schema.sql](schema.sql)** (đã được đồng bộ sang mã nguồn Spring Boot tại `source/src/main/resources/schema.sql`).
