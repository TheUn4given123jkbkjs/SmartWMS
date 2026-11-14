# BÁO CÁO THIẾT KẾ CLASS DIAGRAM & ERD PHÂN TÍCH NGHIỆP VỤ

> **Nhiệm vụ:** `T2.3` (Sprint 2) | **Maker:** M1 - Nguyễn Thành Trung | **Reviewer:** M2 - Nguyễn Bảo Khang  
> **Phạm vi:** 4 Phân hệ Must-have (`FR01` – `FR04`) & Xác thực người dùng (`Auth`)

---

## 1. BIỂU ĐỒ LỚP PHÂN TÍCH (CLASS DIAGRAM)

![Biểu đồ Lớp Phân tích Nghiệp vụ SmartWMS](ClassDiagram.jpg)

### 1.1. Danh mục 9 Lớp Thực thể (`<<Entity>>`)

| Lớp Thực thể | Ý nghĩa nghiệp vụ | Thuộc tính chính | Phương thức nghiệp vụ |
| :--- | :--- | :--- | :--- |
| **`Category`** | Ngành hàng phân loại | `code` (Unique), `name`, `active` | `activate()`, `deactivate()` |
| **`Product`** | Mặt hàng lưu kho (SKU) | `skuCode` (Unique - BR-CAT-01), `uom`, `minStock`, `maxStock`, `manageByLot` | `calculateTotalAvailable()`, `isUnderMinStock()` |
| **`WarehouseLocation`** | Vị trí ô kệ chuẩn 3M | `locationCode` (Unique - BR-CAT-02), `zone`, `maxCapacity`, `status` | `updateStatus()`, `isScrapZone()` |
| **`StockBatch`** | Lô hàng tồn kho (Trung tâm) | `lotNumber`, `expDate`, `onHandQuantity`, `reservedQuantity`, `status` | `getAvailableQuantity()`, `reserve()`, `deductDispatched()`, `isExpired()` |
| **`StockCard`** | Thẻ kho ghi biến động | `transactionDate`, `referenceCode`, `changeType`, `quantityChanged`, `afterQuantity` | `recordTransaction()` |
| **`ReceiptNote`** | Phiếu nhập kho (Header) | `receiptNumber` (Unique), `poNumber`, `receiptDate`, `makerId`, `checkerId`, `status` | `submitForApproval()`, `approve()` |
| **`ReceiptNoteItem`** | Chi tiết dòng nhập kho | `acceptedQuantity`, `damagedQuantity`, `lotNumber`, `expDate` | `getTotalQuantity()`, `validateExpiry()` |
| **`IssueNote`** | Phiếu xuất kho (Header) | `issueNumber` (Unique), `dispatchStrategy`, `makerId`, `checkerId`, `status`, `isOverridden`, `overrideReason` | `reserveInventory()`, `confirmDispatch()` |
| **`IssueNoteItem`** | Chi tiết phân bổ xuất kho | `requestedQuantity`, `allocatedQuantity` | `validateLotNotExpired()` |
| **`User`** | Tài khoản & Maker-Checker | `username` (Unique), `passwordHash`, `fullName`, `role`, `active` | `login()`, `hasRole()` |

### 1.2. Danh mục 5 Lớp Điều khiển Tiền khai báo (`<<Controller>>`)
*Phục vụ kết nối trực tiếp với Sequence Diagram (T2.4) và Component Spring Boot (T3.3):*

| Controller | Phân hệ tương ứng | Chức năng chính |
| :--- | :---: | :--- |
| **`AuthController`** | Xác thực / Security | Đăng nhập (`login`), Đăng xuất (`logout`), Lấy thông tin user hiện tại (`getCurrentUser`). |
| **`CatalogController`** | Phân hệ 1 (FR01) | Quản lý mặt hàng (`createProduct`) và vị trí ô kệ (`updateLocation`). |
| **`ReceiptController`** | Phân hệ 2 (FR02) | Tạo phiếu nhập theo PO (`createReceiptNote`), Duyệt phiếu nhập (`approveReceiptNote`). |
| **`IssueController`** | Phân hệ 3 (FR03) | Tạo phiếu xuất giữ chỗ (`createIssueNote`), Xác nhận thực xuất (`confirmDispatch`). |
| **`InventoryController`** | Phân hệ 4 (FR04) | Tra cứu tồn kho khả dụng (`getAvailableInventory`), Cảnh báo cận hạn (`getNearExpiryAlerts`). |

### 1.3. Bảng Quan hệ Cốt lõi
- **Aggregation (`o--`):** `Category` $\rightarrow$ `Product` (`1`..`0..*`), `WarehouseLocation` $\rightarrow$ `StockBatch` (`1`..`0..*`).
- **Composition (`*--`):** `ReceiptNote` $\rightarrow$ `ReceiptNoteItem` (`1`..`1..*`), `IssueNote` $\rightarrow$ `IssueNoteItem` (`1`..`1..*`).
- **Association (`--`):** `Product` $\leftrightarrow$ `StockBatch` (tracks), `StockBatch` $\leftrightarrow$ `StockCard` (logs), `StockBatch` $\leftrightarrow$ `Items` (cập nhật tồn lô), `User` $\leftrightarrow$ `ReceiptNote`/`IssueNote` (`creates / approves` - Maker-Checker).

---

## 2. LƯỢC ĐỒ THỰC THỂ - LIÊN KẾT (ERD - CHUẨN 3NF)

![Lược đồ Thực thể - Liên kết ERD SmartWMS](ERD.jpg)

### 2.1. Chuẩn hóa 3NF & Khử Quan hệ N-N
- **1NF & 2NF:** 100% bảng có Khóa chính đơn nhân tạo (`id bigint(10) PK AUTO_INCREMENT`), thuộc tính nguyên tử, không có thuộc tính đa trị.
- **3NF:** Không có phụ thuộc bắc cầu; **không lưu cột dư thừa tồn khả dụng** (tính toán thời gian thực: `available = on_hand_qty - reserved_qty`).
- **Khử quan hệ N-N:** Quan hệ Nhiều - Nhiều giữa `product` và `warehouse_location` được phân rã triệt để qua bảng trung gian **`stock_batch`**.

### 2.2. Danh mục 10 Bảng Cơ sở Dữ liệu

| Tên Bảng | Khóa chính (PK) | Khóa ngoại (FK) | Ràng buộc chính |
| :--- | :--- | :--- | :--- |
| **`category`** | `id bigint(10)` | — | `code` UNIQUE, `active bit DEFAULT 1` |
| **`product`** | `id bigint(10)` | `category_id` $\rightarrow$ `category.id` | `sku_code` UNIQUE (BR-CAT-01), `min_stock >= 0`, `manage_by_lot bit` |
| **`warehouse_location`** | `id bigint(10)` | — | `location_code` UNIQUE (BR-CAT-02), `zone` (DOCK, INSP, RAW, FG, SCRAP) |
| **`stock_batch`** | `id bigint(10)` | `product_id`, `location_id` | `lot_number`, `exp_date`, `on_hand_qty >= 0`, `reserved_qty >= 0` |
| **`stock_card`** | `id bigint(10)` | `product_id`, `batch_id` | `qty_changed > 0`, `change_type`, `transaction_date` |
| **`receipt_note`** | `id bigint(10)` | `maker_id` $\rightarrow$ `user.id` | `receipt_number` UNIQUE, `po_number`, `checker_id != maker_id` (BR-SEC-03) |
| **`receipt_note_item`** | `id bigint(10)` | `receipt_id`, `batch_id` | `accepted_qty >= 0`, `damged_qty >= 0`, `exp_date > CURRENT_DATE` |
| **`issue_note`** | `id bigint(10)` | `maker_id` $\rightarrow$ `user.id` | `issue_number` UNIQUE, `dispatch_strategy` (FEFO/FIFO), `checker_id != maker_id` |
| **`issue_note_item`** | `id bigint(10)` | `issue_id`, `batch_id` | `requested_qty > 0`, `allocated_qty >= 0` |
| **`user`** | `id bigint(10)` | — | `username` UNIQUE, `passwordhash` NOT NULL, `active bit DEFAULT 1` |

### 2.3. Danh mục 11 Mối quan hệ Chân quạ (Crow's Foot 1:N Non-Identifying)
1. `category` (1) $\rightarrow$ `product` (N) [`category_id`]
2. `product` (1) $\rightarrow$ `stock_batch` (N) [`product_id`]
3. `warehouse_location` (1) $\rightarrow$ `stock_batch` (N) [`location_id`]
4. `product` (1) $\rightarrow$ `stock_card` (N) [`product_id`]
5. `stock_batch` (1) $\rightarrow$ `stock_card` (N) [`batch_id`]
6. `receipt_note` (1) $\rightarrow$ `receipt_note_item` (N) [`receipt_id`]
7. `stock_batch` (1) $\rightarrow$ `receipt_note_item` (N) [`batch_id`]
8. `issue_note` (1) $\rightarrow$ `issue_note_item` (N) [`issue_id`]
9. `stock_batch` (1) $\rightarrow$ `issue_note_item` (N) [`batch_id`]
10. `user` (1) $\rightarrow$ `receipt_note` (N) [`maker_id`]
11. `user` (1) $\rightarrow$ `issue_note` (N) [`maker_id`]

---

## 3. ĐỐI SOÁT QUY TẮC NGHIỆP VỤ (BUSINESS RULES TRACEABILITY)

| Quy tắc | Nội dung | Thể hiện trên Class & ERD |
| :--- | :--- | :--- |
| **BR-CAT-01** | SKU duy nhất | `Product.skuCode` / `product.sku_code` UNIQUE |
| **BR-CAT-02** | Mã vị trí 3M | `WarehouseLocation.locationCode` / `warehouse_location.zone` (DOCK, INSP, RAW, FG, SCRAP) |
| **BR-CAT-03** | Trạng thái ô kệ | `WarehouseLocation.status` / `warehouse_location.status` (EMPTY/OCCUPIED) |
| **BR-CAT-04** | ĐVT cơ sở duy nhất | `Product.uom` / `product.uom` NOT NULL |
| **BR-CAT-05** | Định mức Min/Max | `Product.minStock`, `maxStock` / `product.min_stock`, `max_stock` |
| **BR-LOT-01** | Lô & Hạn sử dụng | `StockBatch.lotNumber`, `expDate` / `stock_batch.lot_number`, `exp_date` |
| **BR-INB-01** | Nhập gắn đơn PO | `ReceiptNote.poNumber` / `receipt_note.po_number` |
| **BR-INB-02** | Tăng tồn khi duyệt | `StockBatch.onHandQuantity` tăng khi `ReceiptNote.status = 'COMPLETED'` |
| **BR-INB-04** | Phân loại hàng hỏng | `ReceiptNoteItem.damagedQuantity` / `receipt_note_item.damged_qty` |
| **BR-OUT-01** | Giữ chỗ tồn khi lập lệnh | `StockBatch.reserve()` tăng `reservedQuantity`, trừ tồn khả dụng |
| **BR-OUT-02** | Gợi ý FEFO/FIFO | `IssueNote.dispatchStrategy` / `issue_note.dispatch_strategy` |
| **BR-OUT-03** | Cấm xuất quá hạn | `StockBatch.isExpired()` chặn nếu `CurrentDate > expDate` |
| **BR-OUT-04** | Trừ tồn khi xuất | `StockBatch.deductDispatched()` trừ `onHandQuantity` & `reservedQuantity` |
| **BR-OUT-06** | Ghi nhận Lô thực xuất | `IssueNoteItem` liên kết bắt buộc với `StockBatch` (`batch_id`) |
| **BR-SEC-01** | Cấm sửa chứng từ duyệt | `ReceiptNote`/`IssueNote` khóa bản ghi khi COMPLETED / DISPATCHED |
| **BR-SEC-02** | Nhật ký biến động | `StockCard` ghi nhận `transactionDate`, `referenceCode`, `qty_changed` |
| **BR-SEC-03** | Maker-Checker | `User` liên kết `creates / approves`, ràng buộc `checker_id != maker_id` |

---

## 4. GIẢI TRÌNH QUYẾT ĐỊNH THIẾT KẾ (DESIGN RATIONALE)

Phần này làm rõ căn cứ nghiệp vụ từ tài liệu SRS và quy tắc kinh doanh (Business Rules) để giải trình cho việc lựa chọn các thực thể, bản chất các mối liên kết và lượng số (multiplicity) trên sơ đồ:

### 4.1. Căn cứ Lựa chọn các Thực thể Nghiệp vụ (SRS Entity Extraction)
Dựa trên tài liệu khảo sát hiện trạng (Chương 2) và đặc tả yêu cầu chức năng (FR01 – FR04), nhóm quyết định mô hình hóa hệ thống thành **9 thực thể nghiệp vụ (`<<Entity>>`)** tương ứng với các phân hệ cốt lõi:
- **`Category`, `Product`, `WarehouseLocation`:** Phục vụ phân hệ quản lý danh mục (FR01) và tổ chức mặt bằng kho theo chuẩn 3M Floor Marking.
- **`StockBatch`, `StockCard`:** Phục vụ phân hệ quản lý tồn kho theo hạn dùng (FR04). Trong đó, `StockBatch` là trung tâm theo dõi số lượng tồn/giữ chỗ theo từng lô, còn `StockCard` là sổ cái bất biến lưu vết biến động.
- **`ReceiptNote`, `ReceiptNoteItem`:** Phục vụ phân hệ nhập kho theo PO (FR02) theo mô hình Header – Detail.
- **`IssueNote`, `IssueNoteItem`:** Phục vụ phân hệ xuất kho, giữ chỗ tồn kho và phân bổ lô theo FEFO/FIFO (FR03) theo mô hình Header – Detail.
- **`User`:** Phục vụ xác thực đăng nhập và kiểm soát thẩm quyền 2 bước độc lập (Maker-Checker).

### 4.2. Giải trình Bản chất các Mối quan hệ & Lượng số (Aggregation vs. Composition)
1. **Liên kết giữa `Category` và `Product` là Aggregation (`o--`) với lượng số `1` - `0..*`:**
   - *Ngữ nghĩa:* Một danh mục (`Category`) có thể có không hoặc nhiều mặt hàng (`Product`).
   - *Lý do chọn Aggregation:* Khi xóa hoặc ngừng áp dụng một danh mục ngành hàng, các mặt hàng bên trong **không bị xóa mất** khỏi hệ thống mà chỉ cần chuyển sang ngành hàng khác. Sản phẩm có tính định danh độc lập, vòng đời không phụ thuộc sinh mệnh vào danh mục.
2. **Liên kết giữa `WarehouseLocation` và `StockBatch` là Aggregation (`o--`) với lượng số `1` - `0..*`:**
   - *Ngữ nghĩa:* Một vị trí ô kệ (`WarehouseLocation`) có thể đang trống hoặc chứa không đến nhiều lô hàng (`StockBatch`).
   - *Lý do chọn Aggregation:* Vị trí ô kệ là cấu trúc không gian vật lý của nhà kho. Khi ô kệ được dọn trống (tồn kho $= 0$) hoặc khi bảo trì, các lô hàng được di chuyển sang vị trí khác chứ **không bị tiêu hủy theo ô kệ**.
3. **Liên kết giữa `ReceiptNote` và `ReceiptNoteItem` là Composition (`*--`) với lượng số `1` - `1..*`:**
   - *Ngữ nghĩa:* Một phiếu nhập kho (`ReceiptNote`) bắt buộc phải sở hữu từ 1 đến nhiều dòng hàng nhập thực tế (`ReceiptNoteItem`).
   - *Lý do chọn Composition:* Dòng hàng nhập (`ReceiptNoteItem`) chỉ tồn tại trong ngữ cảnh của phiếu nhập đó. Nếu phiếu nhập ở trạng thái bản nháp (`DRAFT`) bị xóa hoặc hủy bỏ, toàn bộ các dòng hàng chi tiết thuộc phiếu đó **bắt buộc phải bị xóa theo (Cascade Delete)**, không thể để dòng chi tiết tồn tại bơ vơ.
4. **Liên kết giữa `IssueNote` và `IssueNoteItem` là Composition (`*--`) với lượng số `1` - `1..*`:**
   - *Ngữ nghĩa:* Một phiếu xuất kho (`IssueNote`) sở hữu từ 1 đến nhiều dòng phân bổ xuất (`IssueNoteItem`).
   - *Lý do chọn Composition:* Vòng đời của dòng phân bổ gắn chặt với phiếu xuất. Khi phiếu xuất bị hủy, các dòng phân bổ bị hủy theo và hệ thống kích hoạt hoàn trả lượng tồn giữ chỗ (`reservedQuantity`) về lại cho các lô hàng.
5. **Thực thể Trung tâm `StockBatch` (Lớp Phân rã Quan hệ N-N):**
   - *Ngữ nghĩa:* Một sản phẩm có thể nằm ở nhiều vị trí kho, và một vị trí kho có thể chứa nhiều sản phẩm (Quan hệ Nhiều - Nhiều N:N).
   - *Giải pháp thiết kế:* Nhóm đưa thực thể `StockBatch` vào giữa làm thực thể trung gian phân rã thành 2 quan hệ Một - Nhiều (`Product 1 - N StockBatch` và `WarehouseLocation 1 - N StockBatch`), đồng thời đóng vai trò lưu trữ các thuộc tính quản lý theo lô: số lô (`lotNumber`), hạn dùng (`expDate`), tồn thực tế (`onHandQuantity`) và tồn giữ chỗ (`reservedQuantity`).
6. **Liên kết giữa `User` và các Chứng từ (`ReceiptNote`, `IssueNote`):**
   - *Ngữ nghĩa:* Quan hệ Association với nhãn `creates / approves`.
   - *Giải pháp thiết kế:* Để hiện thực hóa nguyên tắc **Maker - Checker (BR-SEC-03)**, trên chứng từ tách bạch 2 vai trò: người lập phiếu (`maker_id`) và người phê duyệt (`checker_id`). Cả 2 đều tham chiếu về thực thể `User` kèm ràng buộc bắt buộc `checker_id != maker_id` (người duyệt phải độc lập với người lập).

### 4.3. Giải trình việc Tiền khai báo 5 Lớp Controller (`<<Controller>>`)
- Thay vì để các hàm xử lý luồng nghiệp vụ nằm rải rác trong các thực thể Entity (vi phạm nguyên tắc Single Responsibility), nhóm tiền khai báo 5 lớp điều khiển đại diện cho các điểm tiếp nhận yêu cầu (REST Endpoints).
- Điều này tạo nên **tiền đề kiến trúc liền mạch** cho:
  1. **Biểu đồ Tuần tự (Sequence Diagram - Task T2.4):** Các Lifelines sẽ nhận thông điệp từ Boundary (Giao diện) gửi trực tiếp vào Controller trước khi xử lý nghiệp vụ xuống Entity/Database.
  2. **Thiết kế Component Spring Boot (Sprint 3 - Task T3.3):** Mỗi Controller trên sơ đồ sẽ ánh xạ tương ứng 1-1 với một lớp `@RestController` trong mã nguồn ứng dụng Java.

