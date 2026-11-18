# ĐẶC TẢ CHỮ KÝ HÀM SERVICE & REST API / CONTROLLER ENDPOINTS

**Học phần:** Phân tích và Thiết kế Yêu cầu (Mã môn: 502050 - ĐH Tôn Đức Thắng)  
**Dự án:** Hệ thống Quản lý Kho Hàng Thông Minh (SmartWMS)  
**Mã Task:** `T3.4`  
**Maker (Người lập):** M4 – Thạch Hoàng Quân  
**Reviewer (Người kiểm duyệt):** M1 – Nguyễn Thành Trung  
**Phiên bản:** 1.0 (Sprint 3 - Phase: Interface Design & API Specification)  

---

## MỤC LỤC TỔNG QUAN

1. [Quy chuẩn Thiết kế Giao tiếp Tầng (Architecture Communication Rules)](#1-quy-chuẩn-thiết-kế-giao-tiếp-tầng-architecture-communication-rules)
2. [Đặc tả Cấu trúc Data Transfer Objects (DTO Specification)](#2-đặc-tả-cấu-trúc-data-transfer-objects-dto-specification)
   - [2.1. Phân hệ Xác thực & Người dùng (com.wms.dto.auth)](#21-phân-hệ-xác-thực--người-dùng-comwmsdtoauth)
   - [2.2. Phân hệ Danh mục Hàng hóa & Ô Kệ (com.wms.dto.catalog)](#22-phân-hệ-danh-mục-hàng-hóa--ô-kệ-comwmsdtocatalog)
   - [2.3. Phân hệ Nhập kho (com.wms.dto.receipt)](#23-phân-hệ-nhập-kho-comwmsdtoreceipt)
   - [2.4. Phân hệ Xuất kho (com.wms.dto.issue)](#24-phân-hệ-xuất-kho-comwmsdtoissue)
   - [2.5. Phân hệ Thẻ kho & Cảnh báo Tồn (com.wms.dto.inventory)](#25-phân-hệ-thẻ-kho--cảnh-báo-tồn-comwmsdtoinventory)
3. [Đặc tả Chi tiết Hành vi & Nghiệp vụ các Hàm Service (Service Methods Behavioral Specification)](#3-đặc-tả-chi-tiết-hành-vi--nghiệp-vụ-các-hàm-service-service-methods-behavioral-specification)
   - [3.1. AuthService (source/src/main/java/com/wms/service/AuthService.java)](#31-authservice)
   - [3.2. CatalogService (source/src/main/java/com/wms/service/CatalogService.java)](#32-catalogservice)
   - [3.3. ReceiptService (source/src/main/java/com/wms/service/ReceiptService.java)](#33-receiptservice)
   - [3.4. IssueService (source/src/main/java/com/wms/service/IssueService.java)](#34-issueservice)
   - [3.5. InventoryService (source/src/main/java/com/wms/service/InventoryService.java)](#35-inventoryservice)
4. [Danh mục REST API & Web Controller Endpoints (Endpoints Catalog)](#4-danh-mục-rest-api--web-controller-endpoints-endpoints-catalog)
5. [Kết luận & Đánh giá Nghiệm thu của Reviewer](#5-kết-luận--đánh-giá-nghiệm-thu-của-reviewer)

---

## 1. QUY CHUẨN THIẾT KẾ GIAO TIẾP TẦNG (ARCHITECTURE COMMUNICATION RULES)

1. **Nguyên tắc DTO First:** Tầng Controller và Giao diện Web (Thymeleaf/JSON) trao đổi với tầng Service **100% qua DTO**. Tuyệt đối không để lộ các thực thể JPA (`@Entity`) trực tiếp ra ngoài API nhằm tránh lỗi rò rỉ dữ liệu, vòng lặp tuần tự hóa JSON và phụ thuộc ORM.
2. **Quy chuẩn Validation:** Tất cả Request DTO bắt buộc gắn chú thích kiểm tra tính hợp lệ dữ liệu (Jakarta Bean Validation: `@NotNull`, `@NotBlank`, `@Min`, `@Size`, `@Pattern`) để chặn dữ liệu rác ngay tại Controller bằng `@Valid`.
3. **Quy chuẩn Quản lý Giao dịch (`@Transactional`):**
   * Các phương thức chỉ đọc (truy vấn danh sách, tra cứu tồn, báo cáo): Đánh dấu `@Transactional(readOnly = true)`.
   * Các phương thức ghi/sửa dữ liệu (lập phiếu, duyệt phiếu, cân bằng tồn): Đánh dấu `@Transactional(rollbackFor = Exception.class)`. Nếu có lỗi nghiệp vụ ném ra (`BusinessException`), toàn bộ thao tác trong CSDL lập tức bị hủy bỏ (Rollback).
4. **Quy chuẩn Mã Phản hồi HTTP:**
   * `200 OK`: Thực thi thành công yêu cầu GET hoặc cập nhật dữ liệu.
   * `201 Created`: Tạo mới thành công bản ghi (phiếu nhập, phiếu xuất, mã sản phẩm).
   * `400 Bad Request`: Dữ liệu đầu vào sai định dạng hoặc vi phạm quy tắc nghiệp vụ (`BR-INB-01`, `BR-LOT-01`, `BR-INB-03`, `BR-OUT-01`).
   * `403 Forbidden`: Vi phạm quyền truy cập hoặc vi phạm quy tắc Maker-Checker (`BR-SEC-03`).
   * `404 Not Found`: Không tìm thấy mã sản phẩm, mã phiếu hoặc mã vị trí ô kệ.

---

## 2. ĐẶC TẢ CẤU TRÚC DATA TRANSFER OBJECTS (DTO SPECIFICATION)

### 2.1. Phân hệ Xác thực & Người dùng (`com.wms.dto.auth`)

| Tên DTO | Tên thuộc tính | Kiểu dữ liệu | Ràng buộc Validation | Ý nghĩa nghiệp vụ |
| :--- | :--- | :--- | :--- | :--- |
| **`LoginRequestDTO`** | `username` | `String` | `@NotBlank(message = "Tên đăng nhập không được trống")` | Tên tài khoản đăng nhập |
| `LoginRequestDTO` | `password` | `String` | `@NotBlank(message = "Mật khẩu không được trống")` | Mật khẩu xác thực tài khoản |
| **`UserResponseDTO`** | `id` | `Long` | Không rỗng | Khóa chính tài khoản người dùng |
| `UserResponseDTO` | `username` | `String` | Không rỗng | Tên đăng nhập |
| `UserResponseDTO` | `fullName` | `String` | Không rỗng | Họ và tên đầy đủ |
| `UserResponseDTO` | `email` | `String` | Không rỗng | Địa chỉ thư điện tử |
| `UserResponseDTO` | `role` | `UserRole` | Không rỗng | Vai trò (`ROLE_STAFF`, `ROLE_MANAGER`, `ROLE_ADMIN`) |
| `UserResponseDTO` | `active` | `Boolean` | Không rỗng | Trạng thái kích hoạt tài khoản |

---

### 2.2. Phân hệ Danh mục Hàng hóa & Ô Kệ (`com.wms.dto.catalog`)

| Tên DTO | Tên thuộc tính | Kiểu dữ liệu | Ràng buộc Validation | Ý nghĩa nghiệp vụ |
| :--- | :--- | :--- | :--- | :--- |
| **`ProductCreateDTO`** | `skuCode` | `String` | `@NotBlank`, `@Size(max = 50)` | Mã sản phẩm duy nhất trong toàn hệ thống |
| `ProductCreateDTO` | `name` | `String` | `@NotBlank`, `@Size(max = 255)` | Tên hàng hóa chi tiết |
| `ProductCreateDTO` | `categoryId` | `Long` | `@NotNull(message = "Phải chọn ngành hàng")` | Khóa ngoại tham chiếu bảng `category` |
| `ProductCreateDTO` | `uom` | `String` | `@NotBlank(message = "Đơn vị tính bắt buộc")` | Đơn vị tính cơ bản (Cái, Thùng, Kg) |
| `ProductCreateDTO` | `minStockAlert`| `BigDecimal`| `@NotNull`, `@Min(0)` | Ngưỡng cảnh báo tồn tối thiểu |
| `ProductCreateDTO` | `maxWeightPerUnit`| `BigDecimal`| `@NotNull`, `@Min(0)` | Trọng lượng đơn vị hàng hóa (kg) |
| `ProductCreateDTO` | `shelfLifeDays`| `Integer` | `@Min(0)` | Thời hạn bảo quản tiêu chuẩn (ngày) |
| **`ProductResponseDTO`**| `id` | `Long` | Không rỗng | Khóa chính bản ghi sản phẩm |
| `ProductResponseDTO` | `skuCode` | `String` | Không rỗng | Mã SKU sản phẩm |
| `ProductResponseDTO` | `name` | `String` | Không rỗng | Tên hàng hóa |
| `ProductResponseDTO` | `categoryName` | `String` | Không rỗng | Tên ngành hàng liên kết |
| `ProductResponseDTO` | `uom` | `String` | Không rỗng | Đơn vị tính |
| `ProductResponseDTO` | `minStockAlert` | `BigDecimal` | Không rỗng | Ngưỡng tồn tối thiểu an toàn |
| `ProductResponseDTO` | `maxWeightPerUnit`| `BigDecimal` | Không rỗng | Trọng lượng đơn vị hàng hóa |
| `ProductResponseDTO` | `shelfLifeDays`| `Integer` | Tùy chọn | Số ngày bảo quản chuẩn |
| `ProductResponseDTO` | `active` | `Boolean` | Không rỗng | Trạng thái kinh doanh / lưu kho |
| **`LocationSetupDTO`** | `locationCode`| `String` | `@NotBlank`, `@Pattern(regexp = "^[A-Z0-9-]+$")` | Mã vị trí ô kệ (DOCK-01, RAW-A-01...) |
| `LocationSetupDTO` | `zone` | `ZoneType` | `@NotNull(message = "Phân khu bắt buộc")` | Phân khu (`DOCK`, `INSP`, `RAW`, `FG`, `SCRAP`) |
| `LocationSetupDTO` | `aisle` | `String` | `@NotBlank(message = "Dãy bắt buộc")` | Tọa độ dãy kệ |
| `LocationSetupDTO` | `rack` | `String` | `@NotBlank(message = "Kệ bắt buộc")` | Tọa độ khung kệ |
| `LocationSetupDTO` | `shelf` | `String` | `@NotBlank(message = "Tầng bắt buộc")` | Tọa độ tầng kệ |
| `LocationSetupDTO` | `bin` | `String` | `@NotBlank(message = "Ô bắt buộc")` | Tọa độ ô kệ |
| `LocationSetupDTO` | `maxWeightCapacity` | `BigDecimal` | `@NotNull`, `@Min(1)` | Tải trọng tối đa cho phép của ô kệ (kg) |

---

### 2.3. Phân hệ Nhập kho (`com.wms.dto.receipt`)

| Tên DTO | Tên thuộc tính | Kiểu dữ liệu | Ràng buộc Validation | Ý nghĩa nghiệp vụ |
| :--- | :--- | :--- | :--- | :--- |
| **`ReceiptNoteCreateDTO`** | `poNumber` | `String` | `@NotBlank(message = "Số PO bắt buộc")` | Mã đơn đặt hàng mua (PO) |
| `ReceiptNoteCreateDTO` | `supplierName` | `String` | `@NotBlank(message = "Tên NCC bắt buộc")` | Tên nhà cung cấp giao hàng |
| `ReceiptNoteCreateDTO` | `note` | `String` | `@Size(max = 500)` | Ghi chú bổ sung khi lập phiếu |
| `ReceiptNoteCreateDTO` | `items` | `List<ReceiptItemDTO>` | `@NotEmpty(message = "Phải có ít nhất 1 mặt hàng")` | Danh sách các mặt hàng nhập kho |
| **`ReceiptItemDTO`** | `productId` | `Long` | `@NotNull(message = "Mã sản phẩm bắt buộc")` | Khóa ngoại mặt hàng SKU |
| `ReceiptItemDTO` | `locationId` | `Long` | `@NotNull(message = "Vị trí ô kệ bắt buộc")` | Vị trí ô kệ tiếp nhận hàng |
| `ReceiptItemDTO` | `batchCode` | `String` | `@NotBlank(message = "Số lô bắt buộc")` | Mã số lô sản xuất |
| `ReceiptItemDTO` | `manufactureDate` | `LocalDate` | `@PastOrPresent` | Ngày sản xuất của lô hàng |
| `ReceiptItemDTO` | `expiryDate` | `LocalDate` | `@Future(message = "Hạn dùng phải ở tương lai")` | Ngày hết hạn sử dụng (`BR-LOT-01`) |
| `ReceiptItemDTO` | `plannedQty` | `BigDecimal` | `@NotNull`, `@Min(1)` | Số lượng theo hợp đồng PO |
| `ReceiptItemDTO` | `actualQty` | `BigDecimal` | `@NotNull`, `@Min(1)` | Số lượng kiểm đếm thực nhập (`BR-INB-01`) |
| `ReceiptItemDTO` | `unitPrice` | `BigDecimal` | `@NotNull`, `@Min(0)` | Đơn giá nhập hàng hóa |
| `ReceiptItemDTO` | `note` | `String` | `@Size(max = 255)` | Ghi chú dòng mặt hàng |
| **`ReceiptApprovalDTO`** | `approved` | `Boolean` | `@NotNull(message = "Quyết định duyệt bắt buộc")`| `true`: Phê duyệt, `false`: Từ chối |
| `ReceiptApprovalDTO` | `reviewNote` | `String` | `@Size(max = 500)` | Ý kiến đánh giá hoặc lý do từ chối |
| **`ReceiptNoteResponseDTO`** | `id` | `Long` | Không rỗng | Khóa chính phiếu nhập kho |
| `ReceiptNoteResponseDTO` | `receiptCode` | `String` | Không rỗng | Mã phiếu nhập tự sinh (`RN-YYYYMMDD-XXXX`) |
| `ReceiptNoteResponseDTO` | `poNumber` | `String` | Không rỗng | Số đơn PO đối soát |
| `ReceiptNoteResponseDTO` | `supplierName` | `String` | Không rỗng | Tên nhà cung cấp |
| `ReceiptNoteResponseDTO` | `makerName` | `String` | Không rỗng | Họ tên người lập phiếu |
| `ReceiptNoteResponseDTO` | `checkerName` | `String` | Tùy chọn | Họ tên người duyệt phiếu |
| `ReceiptNoteResponseDTO` | `status` | `ReceiptStatus` | Không rỗng | Trạng thái phiếu (`PENDING_APPROVAL`, `APPROVED`...) |
| `ReceiptNoteResponseDTO` | `note` | `String` | Tùy chọn | Ghi chú lập phiếu |
| `ReceiptNoteResponseDTO` | `createdAt` | `LocalDateTime` | Không rỗng | Thời điểm lập phiếu |
| `ReceiptNoteResponseDTO` | `approvedAt` | `LocalDateTime` | Tùy chọn | Thời điểm phê duyệt |
| `ReceiptNoteResponseDTO` | `items` | `List<ReceiptItemDTO>` | Không rỗng | Danh sách chi tiết các mặt hàng trong phiếu |

---

### 2.4. Phân hệ Xuất kho (`com.wms.dto.issue`)

| Tên DTO | Tên thuộc tính | Kiểu dữ liệu | Ràng buộc Validation | Ý nghĩa nghiệp vụ |
| :--- | :--- | :--- | :--- | :--- |
| **`IssueRequestDTO`** | `orderNumber` | `String` | `@NotBlank(message = "Mã lệnh xuất bắt buộc")` | Mã đơn hàng bán hoặc cấp phát sản xuất |
| `IssueRequestDTO` | `customerName` | `String` | `@NotBlank(message = "Tên khách hàng bắt buộc")`| Tên khách hàng / bộ phận nhận hàng |
| `IssueRequestDTO` | `strategy` | `DispatchStrategy`| `@NotNull(message = "Chiến lược bắt buộc")` | Chiến lược xuất (`FEFO` hoặc `FIFO`) |
| `IssueRequestDTO` | `note` | `String` | `@Size(max = 500)` | Ghi chú xuất kho |
| `IssueRequestDTO` | `items` | `List<IssueItemRequestDTO>`| `@NotEmpty(message = "Phải có ít nhất 1 dòng")`| Danh sách mặt hàng yêu cầu xuất |
| **`IssueItemRequestDTO`** | `productId` | `Long` | `@NotNull(message = "Mã sản phẩm bắt buộc")` | ID sản phẩm cần xuất |
| `IssueItemRequestDTO` | `requestedQty` | `BigDecimal` | `@NotNull`, `@Min(1)` | Số lượng yêu cầu xuất (`BR-OUT-01`) |
| `IssueItemRequestDTO` | `selectedBatchId`| `Long` | Tùy chọn | ID lô chọn thủ công (nếu có ngoại lệ) |
| **`PickListProposalDTO`** | `productId` | `Long` | Không rỗng | ID sản phẩm SKU |
| `PickListProposalDTO` | `skuCode` | `String` | Không rỗng | Mã SKU sản phẩm |
| `PickListProposalDTO` | `productName` | `String` | Không rỗng | Tên hàng hóa |
| `PickListProposalDTO` | `batchId` | `Long` | Không rỗng | Khóa chính lô hàng được phân bổ |
| `PickListProposalDTO` | `batchCode` | `String` | Không rỗng | Số lô sản xuất |
| `PickListProposalDTO` | `locationCode`| `String` | Không rỗng | Tọa độ ô kệ cần đến lấy hàng |
| `PickListProposalDTO` | `expiryDate` | `LocalDate` | Không rỗng | Ngày hết hạn của lô hàng |
| `PickListProposalDTO` | `allocatedQty`| `BigDecimal` | Không rỗng | Số lượng đề xuất bốc tại ô kệ này |
| **`IssueNoteResponseDTO`** | `id` | `Long` | Không rỗng | Khóa chính phiếu xuất kho |
| `IssueNoteResponseDTO` | `issueCode` | `String` | Không rỗng | Mã phiếu xuất tự sinh (`IN-YYYYMMDD-XXXX`) |
| `IssueNoteResponseDTO` | `orderNumber` | `String` | Không rỗng | Số đơn hàng xuất |
| `IssueNoteResponseDTO` | `customerName` | `String` | Không rỗng | Tên đơn vị nhận hàng |
| `IssueNoteResponseDTO` | `strategy` | `DispatchStrategy`| Không rỗng | Chiến lược xuất áp dụng (`FEFO` / `FIFO`) |
| `IssueNoteResponseDTO` | `makerName` | `String` | Không rỗng | Họ tên người lập phiếu xuất |
| `IssueNoteResponseDTO` | `checkerName` | `String` | Tùy chọn | Họ tên người duyệt phiếu xuất |
| `IssueNoteResponseDTO` | `status` | `IssueStatus` | Không rỗng | Trạng thái phiếu xuất |
| `IssueNoteResponseDTO` | `note` | `String` | Tùy chọn | Ghi chú phiếu |
| `IssueNoteResponseDTO` | `createdAt` | `LocalDateTime` | Không rỗng | Thời điểm lập phiếu xuất |
| `IssueNoteResponseDTO` | `approvedAt` | `LocalDateTime` | Tùy chọn | Thời điểm duyệt hoàn tất |
| `IssueNoteResponseDTO` | `pickList` | `List<PickListProposalDTO>`| Không rỗng | Bảng danh mục vị trí và số lượng bốc hàng |

---

### 2.5. Phân hệ Thẻ kho & Cảnh báo Tồn (`com.wms.dto.inventory`)

| Tên DTO | Tên thuộc tính | Kiểu dữ liệu | Ràng buộc Validation | Ý nghĩa nghiệp vụ |
| :--- | :--- | :--- | :--- | :--- |
| **`StockCardDTO`** | `id` | `Long` | Không rỗng | Khóa chính dòng nhật ký thẻ kho |
| `StockCardDTO` | `transactionTime` | `LocalDateTime` | Không rỗng | Thời điểm phát sinh giao dịch biến động |
| `StockCardDTO` | `changeType` | `ChangeType` | Không rỗng | Loại biến động (`NHAP_PO`, `XUAT_KHO`, `KIEM_KE`...) |
| `StockCardDTO` | `referenceCode` | `String` | Không rỗng | Mã chứng từ gốc (`RN-...`, `IN-...`) |
| `StockCardDTO` | `skuCode` | `String` | Không rỗng | Mã SKU sản phẩm |
| `StockCardDTO` | `productName` | `String` | Không rỗng | Tên mặt hàng phát sinh |
| `StockCardDTO` | `batchCode` | `String` | Không rỗng | Số lô sản xuất liên quan |
| `StockCardDTO` | `locationCode` | `String` | Không rỗng | Vị trí ô kệ phát sinh biến động |
| `StockCardDTO` | `beforeQty` | `BigDecimal` | Không rỗng | Tồn kho sổ sách trước giao dịch |
| `StockCardDTO` | `changeQty` | `BigDecimal` | Không rỗng | Số lượng thay đổi (dương nhập, âm xuất) |
| `StockCardDTO` | `afterQty` | `BigDecimal` | Không rỗng | Tồn kho sổ sách sau giao dịch |
| `StockCardDTO` | `performedByName` | `String` | Không rỗng | Người thực hiện ghi nhận giao dịch |
| `StockCardDTO` | `reason` | `String` | Tùy chọn | Lý do hoặc diễn giải biến động |
| **`AvailableStockDTO`** | `productId` | `Long` | Không rỗng | Khóa chính mặt hàng SKU |
| `AvailableStockDTO` | `skuCode` | `String` | Không rỗng | Mã SKU sản phẩm |
| `AvailableStockDTO` | `productName` | `String` | Không rỗng | Tên mặt hàng |
| `AvailableStockDTO` | `uom` | `String` | Không rỗng | Đơn vị tính cơ bản |
| `AvailableStockDTO` | `totalOnHandQty` | `BigDecimal` | Không rỗng | Tổng tồn kho thực tế trong kho: `sum(on_hand)` |
| `AvailableStockDTO` | `totalReservedQty`| `BigDecimal` | Không rỗng | Tổng tồn đang bị tạm giữ: `sum(reserved)` |
| `AvailableStockDTO` | `availableQty` | `BigDecimal` | Không rỗng | Tồn kho khả dụng thực tế: $\sum (\text{on\_hand} - \text{reserved})$ của các lô AVAILABLE còn hạn |
| `AvailableStockDTO` | `minStockAlert` | `BigDecimal` | Không rỗng | Định mức tồn an toàn tối thiểu |
| `AvailableStockDTO` | `underMin` | `Boolean` | Không rỗng | Cờ cảnh báo đỏ nếu `availableQty <= minStockAlert` |
| **`NearExpiryAlertDTO`**| `batchId` | `Long` | Không rỗng | Khóa chính lô hàng cận hạn |
| `NearExpiryAlertDTO` | `batchCode` | `String` | Không rỗng | Số lô sản xuất |
| `NearExpiryAlertDTO` | `skuCode` | `String` | Không rỗng | Mã SKU sản phẩm |
| `NearExpiryAlertDTO` | `productName` | `String` | Không rỗng | Tên mặt hàng |
| `NearExpiryAlertDTO` | `locationCode` | `String` | Không rỗng | Vị trí ô kệ đang chứa lô hàng |
| `NearExpiryAlertDTO` | `expiryDate` | `LocalDate` | Không rỗng | Ngày hết hạn của lô hàng |
| `NearExpiryAlertDTO` | `daysRemaining` | `Long` | Không rỗng | Số ngày còn lại đến thời điểm hết hạn |
| `NearExpiryAlertDTO` | `currentQty` | `BigDecimal` | Không rỗng | Số lượng tồn kho hiện tại của lô cận hạn |
| `NearExpiryAlertDTO` | `alertLevel` | `String` | Không rỗng | Mức cảnh báo: `WARNING` (<=30 ngày), `CRITICAL` (<=7 ngày), `EXPIRED` (<=0 ngày) |

---

## 3. ĐẶC TẢ CHI TIẾT HÀNH VI & NGHIỆP VỤ CÁC HÀM SERVICE (SERVICE METHODS BEHAVIORAL SPECIFICATION)

Toàn bộ 5 Service Interfaces đã được triển khai mã nguồn Java thực tế tại thư mục `source/src/main/java/com/wms/service/`. Dưới đây là đặc tả chi tiết logic nghiệp vụ, kiểm tra ràng buộc và hành vi tương tác CSDL của từng hàm:

### 3.1. AuthService
*File nguồn:* [`source/src/main/java/com/wms/service/AuthService.java`](../../source/src/main/java/com/wms/service/AuthService.java)

* **`login(LoginRequestDTO request)`:**
  * **Mục đích:** Xác thực người dùng và khởi tạo phiên làm việc trong hệ thống.
  * **Quy tắc & Logic xử lý:**
    * Tìm kiếm bản ghi trong bảng `user` theo `username`. Ném `ResourceNotFoundException` nếu không tìm thấy.
    * Kiểm tra trạng thái tài khoản: Nếu `active == false`, ném `SecurityViolationException("Tài khoản đã bị khóa")`.
    * Đối soát mật khẩu qua hàm băm BCrypt. Nếu sai, ném `SecurityViolationException("Mật khẩu không chính xác")`.
  * **Kết quả:** Trả về `UserResponseDTO` chứa ID, họ tên, email và quyền hạn (`role`) để lưu vào Session Context.
* **`getCurrentUser(Long userId)`:**
  * **Mục đích:** Truy xuất thông tin tài khoản đang thực hiện thao tác trong phiên làm việc.
  * **Logic xử lý:** Đọc thông tin từ bảng `user` theo `userId` và ánh xạ sang `UserResponseDTO`.

---

### 3.2. CatalogService
*File nguồn:* [`source/src/main/java/com/wms/service/CatalogService.java`](../../source/src/main/java/com/wms/service/CatalogService.java)

* **`createProduct(ProductCreateDTO dto)`:**
  * **Mục đích:** Thêm mới một mặt hàng SKU vào danh mục quản lý kho (`UC01`).
  * **Quy tắc & Logic xử lý:**
    * Kiểm tra tính duy nhất của mã SKU (`uk_product_sku`). Nếu đã tồn tại, ném `BusinessException("Mã SKU đã tồn tại")`.
    * Kiểm tra sự tồn tại của `categoryId`. Ném `ResourceNotFoundException` nếu không hợp lệ.
    * Tạo và lưu thực thể `Product` vào CSDL với trạng thái `active = true`.
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`.
* **`getProductById(Long productId)`:**
  * **Mục đích:** Tra cứu thông tin chi tiết một sản phẩm theo ID. Ném `ResourceNotFoundException` nếu không tìm thấy.
* **`getAllProducts()`:**
  * **Mục đích:** Lấy toàn bộ danh sách sản phẩm phục vụ bảng tra cứu và dropdown box trên giao diện Web.
* **`setupLocation(LocationSetupDTO dto)`:**
  * **Mục đích:** Cấu hình khởi tạo vị trí ô kệ lưu kho mới (Dãy, Kệ, Tầng, Ô).
  * **Quy tắc & Logic xử lý:**
    * Kiểm tra duy nhất `location_code`. Ném `BusinessException` nếu trùng mã.
    * Lưu thực thể `WarehouseLocation` với `current_weight = 0`, `status = 'EMPTY'`, `active = true`.
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`.
* **`getAvailableLocationsByZone(ZoneType zone)`:**
  * **Mục đích:** Lọc danh sách ô kệ còn chỗ (`status == 'EMPTY'` hoặc `current_weight < max_weight_capacity`) thuộc phân khu chức năng quy định (`RAW`, `FG`, `DOCK`...) để gợi ý xếp hàng.

---

### 3.3. ReceiptService
*File nguồn:* [`source/src/main/java/com/wms/service/ReceiptService.java`](../../source/src/main/java/com/wms/service/ReceiptService.java)

* **`createReceiptNote(ReceiptNoteCreateDTO dto, Long currentUserId)`:**
  * **Mục đích:** Tiếp nhận hàng hóa và lập Phiếu nhập kho ở trạng thái chờ duyệt (`UC02`).
  * **Quy tắc & Logic xử lý:**
    * **Kiểm tra PO (`BR-INB-01`):** Đối soát `actualQty <= plannedQty`. Nếu số lượng nhập thực tế vượt quá hợp đồng PO, ném `InvalidQuantityException`.
    * **Kiểm tra hạn sử dụng (`BR-LOT-01`):** `expiryDate > CURRENT_DATE`. Nếu hàng đã hết hạn hoặc cận hạn dưới ngưỡng quy định, ném `ExpiredLotException`.
    * **Kiểm tra vị trí kệ (`BR-INB-03`):** Vị trí chọn phải thuộc phân khu phù hợp với ngành hàng và không bị vượt tải trọng thiết kế `max_weight_capacity`. Nếu vi phạm, ném `InvalidLocationException`.
    * **Ghi dữ liệu:** Tự động sinh mã phiếu `RN-YYYYMMDD-XXXX`, gán `maker_id = currentUserId`, `status = 'PENDING_APPROVAL'`, lưu `receipt_note` và các dòng `receipt_note_item`. **Chưa tăng tồn kho thực tế ở bước này.**
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`.
* **`approveReceiptNote(Long receiptId, ReceiptApprovalDTO approvalDTO, Long currentUserId)`:**
  * **Mục đích:** Phê duyệt phiếu nhập và thực thi giao dịch nguyên tử 4 bảng (`UC03`).
  * **Quy tắc & Logic xử lý:**
    * **Maker-Checker (`BR-SEC-03`):** Bắt buộc `currentUserId != receipt.maker_id`. Nếu người lập phiếu cố tình tự phê duyệt, ném `SecurityViolationException`.
    * **Kiểm tra trạng thái:** Phiếu phải đang ở trạng thái `PENDING_APPROVAL`.
    * **Trường hợp từ chối (`approved == false`):** Cập nhật `status = 'REJECTED'`, lưu lý do từ chối vào `review_note`.
    * **Trường hợp chấp thuận (`approved == true`):**
      1. Cập nhật `receipt_note`: `status = 'APPROVED'`, `checker_id = currentUserId`, `approved_at = CURRENT_TIMESTAMP`.
      2. Tăng tồn kho thực tế trong `stock_batch`: Tạo mới bản ghi lô hàng (hoặc cộng dồn nếu cùng lô và vị trí) với `on_hand_qty = on_hand_qty + actual_qty`, `reserved_qty = 0`.
      3. Cập nhật tải trọng `warehouse_location`: `current_weight = current_weight + (actual_qty * product.max_weight_per_unit)`, chuyển trạng thái kệ sang `OCCUPIED`.
      4. Ghi nhận nhật ký sổ `stock_card`: `change_type = 'NHAP_PO'`, `before_qty`, `change_qty = +actual_qty`, `after_qty`, `reference_code = receipt_code`.
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`. Đảm bảo tính toàn vẹn tuyệt đối (ACID).
* **`getReceiptNotes(String status, String poNumber)`:**
  * **Mục đích:** Tra cứu danh sách phiếu nhập kho theo bộ lọc trạng thái và mã PO.
* **`getReceiptNoteById(Long receiptId)`:**
  * **Mục đích:** Xem chi tiết phiếu nhập kho và các dòng mặt hàng phục vụ in phiếu nhập.

---

### 3.4. IssueService
*File nguồn:* [`source/src/main/java/com/wms/service/IssueService.java`](../../source/src/main/java/com/wms/service/IssueService.java)

* **`proposeAllocation(Long productId, BigDecimal requestedQty, DispatchStrategy strategy)`:**
  * **Mục đích:** Gợi ý danh sách bốc hàng tối ưu theo chiến lược xuất kho (`UC04`).
  * **Quy tắc & Logic xử lý:**
    * Quét các lô của sản phẩm có `status = 'AVAILABLE'`, `Available = on_hand_qty - reserved_qty > 0` và `(expiry_date IS NULL OR expiry_date >= CURRENT_DATE)`.
    * Kiểm tra tổng tồn khả dụng: Nếu `sum(Available) < requestedQty`, ném `InsufficientStockException` (`BR-OUT-01`).
    * Sắp xếp danh sách lô:
      * Nếu `strategy == FEFO`: Sắp xếp theo thứ tự `expiry_date ASC, received_date ASC`.
      * Nếu `strategy == FIFO`: Sắp xếp theo thứ tự `received_date ASC, id ASC`.
    * Trừ dần số lượng qua từng lô để tạo danh sách đề xuất lấy hàng `PickListProposalDTO` (mã lô, ô kệ, số lượng bốc).
* **`createIssueNote(IssueRequestDTO dto, Long currentUserId)`:**
  * **Mục đích:** Tạo phiếu xuất kho và tạm khóa giữ chỗ tồn kho khả dụng (`UC04`).
  * **Quy tắc & Logic xử lý:**
    * Kiểm tra tồn khả dụng của từng lô được chỉ định. Nếu không đủ, ném `InsufficientStockException`.
    * **Khóa tạm giữ tồn kho:** Với mỗi dòng xuất, tăng `reserved_qty = reserved_qty + allocated_qty` trên bảng `stock_batch`. Cơ chế này ngăn chặn tuyệt đối các giao dịch khác tranh chấp xuất âm.
    * Sinh mã phiếu `IN-YYYYMMDD-XXXX`, gán `maker_id = currentUserId`, `status = 'PENDING_APPROVAL'`, lưu `issue_note` và `issue_note_item`.
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`.
* **`approveIssueNote(Long issueId, Long currentUserId)`:**
  * **Mục đích:** Quản lý phê duyệt phiếu xuất kho chuyển sang trạng thái chờ lấy hàng (`UC05`).
  * **Quy tắc & Logic xử lý:**
    * **Maker-Checker (`BR-SEC-03`):** Bắt buộc `currentUserId != issue.maker_id`. Ném `SecurityViolationException` (HTTP 403) nếu tự duyệt.
    * **Kiểm tra hạn dùng (Chốt chặn 1 - Quyết định D4 / `BR-OUT-03`):** Hệ thống duyệt qua toàn bộ item của phiếu xuất; nếu phát hiện có lô hàng bị quá hạn (`batch.isExpired()`), lập tức ném `ExpiredLotException` (HTTP 409 Conflict) để chặn duyệt, yêu cầu Quản lý bấm Từ chối (Reject) phiếu để giải phóng hàng giữ chỗ và yêu cầu lập phiếu mới.
    * Cập nhật `issue_note`: `status = 'APPROVED_READY_TO_PICK'`, `checker_id = currentUserId`, `updated_at = CURRENT_TIMESTAMP`.
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`.
* **`confirmDispatch(Long issueId, Long currentUserId)`:**
  * **Mục đích:** Nhân viên kho xác nhận hoàn tất bốc hàng và bàn giao thực tế khỏi kho, chính thức trừ tồn On-hand (`UC05`).
  * **Quy tắc & Logic xử lý:**
    * Kiểm tra trạng thái phiếu: Bắt buộc phải là `APPROVED_READY_TO_PICK`. Ném `ConflictException` (HTTP 409) nếu sai trạng thái.
    * **Kiểm tra hạn dùng (Chốt chặn 2 - Quyết định D4 / `BR-OUT-03`):** Kiểm tra lại một lần nữa hạn sử dụng của từng lô hàng trước khi bốc hàng ra khỏi kho; nếu có lô hết hạn ném `ExpiredLotException` (HTTP 409 Conflict).
    * Cập nhật `stock_batch` theo từng item:
      * Trừ tồn thực tế: `on_hand_qty = on_hand_qty - allocated_qty`.
      * Giải phóng lượng giữ chỗ: `reserved_qty = reserved_qty - allocated_qty`.
    * Cập nhật `warehouse_location`: Nếu vị trí không còn tồn lô hàng nào thì tự động chuyển trạng thái về `EMPTY` (`BR-CAT-03`).
    * Ghi nhật ký vào `stock_card`: `change_type = 'XUAT_KHO'`, `qty_changed = -allocated_qty`, `after_changed = batch.on_hand_qty`, `reference_code = issue_number` (`BR-SEC-02`).
    * Cập nhật `issue_note`: `status = 'COMPLETED'`, `updated_at = CURRENT_TIMESTAMP`.
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`.
* **`cancelIssueNote(Long issueId, String reason, Long currentUserId)`:**
  * **Mục đích:** Hủy phiếu xuất kho và giải phóng hoàn toàn số lượng đã tạm giữ.
  * **Logic xử lý:** Giảm `reserved_qty = reserved_qty - allocated_qty` trên bảng `stock_batch`, chuyển trạng thái phiếu sang `CANCELLED`.
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`.
* **`getIssueNotes(String status)` / `getIssueNoteById(Long issueId)`:**
  * **Mục đích:** Tra cứu danh sách và lấy chi tiết phiếu xuất phục vụ in danh sách bốc hàng Pick List.

---

### 3.5. InventoryService
*File nguồn:* [`source/src/main/java/com/wms/service/InventoryService.java`](../../source/src/main/java/com/wms/service/InventoryService.java)

* **`calculateAvailableQuantity(Long productId)`:**
  * **Mục đích:** Tính toán động tồn kho khả dụng chuẩn 3NF:
    ```text
    Available = SUM(on_hand_qty - reserved_qty)  (với các lô có status = 'AVAILABLE' và expiry_date >= CURRENT_DATE hoặc không quản lý lô)
    ```
* **`getAllAvailableStock()`:**
  * **Mục đích:** Tổng hợp tồn kho toàn bộ mặt hàng SKU; tự động so sánh tồn khả dụng với `min_stock_alert` để gán cờ cảnh báo đỏ (`underMin = true`).
* **`getStockCardHistory(Long productId, LocalDate fromDate, LocalDate toDate)`:**
  * **Mục đích:** Tra cứu sổ thẻ kho của sản phẩm (`UC06`), sắp xếp theo thời gian tăng dần để phục vụ công tác đối soát, kiểm toán kho.
* **`getLowStockAlerts()`:**
  * **Mục đích:** Lọc danh sách các mặt hàng có tồn khả dụng `<= min_stock_alert` (`UC07`) để hiển thị thông báo bổ sung hàng khẩn cấp lên Dashboard.
* **`getNearExpiryAlerts(int thresholdDays)`:**
  * **Mục đích:** Lọc danh sách các lô hàng có `CURRENT_DATE <= expiry_date <= CURRENT_DATE + thresholdDays` (`UC07`) để ưu tiên xuất sớm hoặc xử lý cách ly.

---

## 4. DANH MỤC REST API & WEB CONTROLLER ENDPOINTS (ENDPOINTS CATALOG)

| Mã UC | HTTP Method | URL Route Path | Controller Handler | Tham số Đầu vào (Input Payload) | Kết quả Trả về (Output) | HTTP Status | Quyền hạn (RBAC) |
| :---: | :---: | :--- | :--- | :--- | :--- | :---: | :---: |
| **AUTH** | `GET` | `/login` | `AuthController.showLoginForm` | - | `auth/login.html` | `200 OK` | Public |
| **AUTH** | `POST` | `/api/v1/auth/login` | `AuthController.login` | `@RequestBody @Valid LoginRequestDTO` | `UserResponseDTO` (JSON) | `200 OK` | Public |
| **AUTH** | `POST` | `/logout` | `AuthController.logout` | - | Redirect `/login` | `302 Found` | Đã đăng nhập |
| **UC01** | `GET` | `/catalog/products` | `CatalogController.listProducts` | - | `catalog/list.html` | `200 OK` | Staff, Manager |
| **UC01** | `POST` | `/api/v1/catalog/products` | `CatalogController.createProduct` | `@RequestBody @Valid ProductCreateDTO` | `ProductResponseDTO` (JSON) | `201 Created`| Manager, Admin |
| **UC01** | `GET` | `/api/v1/catalog/products/{id}` | `CatalogController.getProduct` | `@PathVariable Long id` | `ProductResponseDTO` (JSON) | `200 OK` | Staff, Manager |
| **UC01** | `POST` | `/api/v1/catalog/locations` | `CatalogController.setupLocation` | `@RequestBody @Valid LocationSetupDTO` | Thông báo thành công | `201 Created`| Manager, Admin |
| **UC02** | `GET` | `/receipt/create` | `ReceiptController.showCreateForm` | - | `receipt/create.html` | `200 OK` | **ROLE_STAFF** |
| **UC02** | `POST` | `/api/v1/receipts` | `ReceiptController.createReceipt` | `@RequestBody @Valid ReceiptNoteCreateDTO` | `ReceiptNoteResponseDTO` (JSON) | `201 Created`| **ROLE_STAFF** |
| **UC02** | `GET` | `/receipt/list` | `ReceiptController.listReceipts` | `@RequestParam(required = false) String status` | `receipt/list.html` | `200 OK` | Staff, Manager |
| **UC03** | `GET` | `/receipt/{id}/approve` | `ReceiptController.showApproveView` | `@PathVariable Long id` | `receipt/approve.html` | `200 OK` | **ROLE_MANAGER** |
| **UC03** | `POST` | `/api/v1/receipts/{id}/approve` | `ReceiptController.approveReceipt` | `@PathVariable Long id`, `@RequestBody @Valid ReceiptApprovalDTO` | `ReceiptNoteResponseDTO` (JSON) | `200 OK` | **ROLE_MANAGER** |
| **UC04** | `GET` | `/issue/create` | `IssueController.showCreateForm` | - | `issue/create.html` | `200 OK` | **ROLE_STAFF** |
| **UC04** | `POST` | `/api/v1/issues/propose-allocation` | `IssueController.proposeAllocation` | `@RequestParam Long productId`, `@RequestParam BigDecimal requestedQty`, `@RequestParam DispatchStrategy strategy` | `List<PickListProposalDTO>` (JSON) | `200 OK` | **ROLE_STAFF** |
| **UC04** | `POST` | `/api/v1/issues` | `IssueController.createIssue` | `@RequestBody @Valid IssueRequestDTO` | `IssueNoteResponseDTO` (JSON) | `201 Created`| **ROLE_STAFF** |
| **UC05** | `POST` | `/api/v1/issues/{id}/approve` | `IssueController.approveIssue` | `@PathVariable Long id` | `IssueNoteResponseDTO` (JSON) | `200 OK` | **ROLE_MANAGER** |
| **UC05** | `POST` | `/api/v1/issues/{id}/dispatch` | `IssueController.confirmDispatchApi` | `@PathVariable Long id` | `IssueNoteResponseDTO` (JSON) | `200 OK` | **ROLE_STAFF** |
| **UC05** | `POST` | `/api/v1/issues/{id}/cancel` | `IssueController.cancelIssue` | `@PathVariable Long id`, `@RequestParam String reason` | Thông báo hủy thành công | `200 OK` | **ROLE_MANAGER** |
| **UC06** | `GET` | `/inventory/stock-cards` | `InventoryController.viewStockCards` | `@RequestParam(required = false) Long productId` | `inventory/stock_card.html` | `200 OK` | Staff, Manager |
| **UC06** | `GET` | `/api/v1/inventory/stock-cards` | `InventoryController.getStockCards` | `@RequestParam Long productId`, `@RequestParam(required = false) LocalDate fromDate`, `@RequestParam(required = false) LocalDate toDate` | `List<StockCardDTO>` (JSON) | `200 OK` | Staff, Manager |
| **UC07** | `GET` | `/inventory/alerts` | `InventoryController.viewAlerts` | - | `inventory/alerts.html` | `200 OK` | Staff, Manager |
| **UC07** | `GET` | `/api/v1/inventory/alerts/low-stock` | `InventoryController.getLowStock` | - | `List<AvailableStockDTO>` (JSON) | `200 OK` | Staff, Manager |
| **UC07** | `GET` | `/api/v1/inventory/alerts/near-expiry` | `InventoryController.getNearExpiry` | `@RequestParam(defaultValue = "30") int days` | `List<NearExpiryAlertDTO>` (JSON) | `200 OK` | Staff, Manager |

---

## 5. KẾT LUẬN & ĐÁNH GIÁ NGHIỆM THU CỦA REVIEWER

* **Maker (M4 - Thạch Hoàng Quân):** Đã hoàn thành 100% việc viết mã Java Interfaces thực tế tại `source/src/main/java/com/wms/service/`, hoàn thành toàn bộ các DTOs, Enums, Exceptions và lập tài liệu đặc tả chi tiết logic xử lý nghiệp vụ cho từng hàm cùng danh mục REST API endpoints.
* **Reviewer (M1 - Nguyễn Thành Trung):** Xác nhận tài liệu rõ ràng, chuẩn định dạng Markdown, không bị lỗi bảng hay lỗi KaTeX, khớp hoàn toàn với Class Diagram, Sequence Diagram, 10 bảng CSDL 3NF và Cấu trúc Package Spring Boot. **Chấp thuận nghiệm thu hoàn thành Task T3.4.**
