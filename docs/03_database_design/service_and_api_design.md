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
| **`IssueRequestDTO`** | `orderNumber` | `String` | `@NotBlank`, `@Size(max = 50)` | Mã đơn hàng bán hoặc cấp phát sản xuất |
| `IssueRequestDTO` | `customerName` | `String` | `@NotBlank`, `@Size(max = 255)` | Tên khách hàng hoặc bộ phận nhận hàng |
| `IssueRequestDTO` | `exportType` | `String` | `PRODUCTION` hoặc `SALE`, mặc định `SALE` | Loại xuất |
| `IssueRequestDTO` | `overrideReason` | `String` | Tối đa 255 ký tự. Khi có ngoại lệ, tối thiểu 10 ký tự sau khi bỏ khoảng trắng đầu/cuối | Lý do đổi lô hoặc thứ tự ưu tiên |
| `IssueRequestDTO` | `strategy` | `DispatchStrategy`| `@NotNull(message = "Chiến lược bắt buộc")` | Chiến lược xuất (`FEFO` hoặc `FIFO`) |
| `IssueRequestDTO` | `note` | `String` | `@Size(max = 500)` | Ghi chú xuất kho |
| `IssueRequestDTO` | `items` | `List<IssueItemRequestDTO>`| `@NotEmpty(message = "Phải có ít nhất 1 dòng")`| Danh sách mặt hàng yêu cầu xuất |
| **`IssueItemRequestDTO`** | `productId` | `Long` | `@NotNull(message = "Mã sản phẩm bắt buộc")` | ID sản phẩm cần xuất |
| `IssueItemRequestDTO` | `requestedQty` | `BigDecimal` | Lớn hơn 0, tối đa 10 chữ số nguyên và 2 chữ số thập phân | Số lượng yêu cầu xuất (`BR-OUT-01`) |
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
| `IssueNoteResponseDTO` | `exportType` | `String` | Có thể rỗng với dữ liệu cũ | Loại xuất |
| `IssueNoteResponseDTO` | `isOverridden` | `boolean` | Không rỗng | Phiếu có phân bổ ngoại lệ |
| `IssueNoteResponseDTO` | `overrideReason` | `String` | Tùy chọn | Lý do ngoại lệ |
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
| `NearExpiryAlertDTO` | `alertLevel` | `String` | Không rỗng | Mức cảnh báo: `EXPIRED` (<0 ngày), `CRITICAL` (0–7 ngày), `WARNING` (8 ngày đến ngưỡng đang dùng) |

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
  * **Mục đích.** Trả danh sách ô `EMPTY` trong vùng được yêu cầu. Danh sách chọn ô trên phiếu nhập lấy từ danh mục vị trí và lọc các ô `RAW` hoặc `FG` đang không bảo trì.

---

### 3.3. ReceiptService
*File nguồn:* [`source/src/main/java/com/wms/service/ReceiptService.java`](../../source/src/main/java/com/wms/service/ReceiptService.java)

**Tiếp nhận PO đã duyệt (D21).** Cả hai API nhập PO lấy định danh từ phiên đăng nhập. `PurchaseOrderService` kiểm tra tài khoản còn hoạt động và có `ROLE_MANAGER` trước khi lưu PO ở trạng thái `APPROVED` và ghi vết người nhập. Các thao tác ghi phiếu nhập và phiếu xuất dùng quyền Thủ kho hoặc Quản lý kho theo BR-SEC-03. Phiên bản này chưa có chức năng thay đổi role tài khoản.

* **`createReceiptNote(ReceiptNoteCreateDTO dto, Long currentUserId)`:**
  * **Mục đích:** Tiếp nhận hàng hóa và lập Phiếu nhập kho ở trạng thái chờ duyệt (`UC02`).
  * **Quy tắc & Logic xử lý:**
    * **Kiểm tra PO (`BR-INB-01`):** Đối soát `actualQty <= plannedQty`. Nếu số lượng nhập thực tế vượt quá hợp đồng PO, ném `InvalidQuantityException`.
    * **Kiểm tra hạn sử dụng (`BR-LOT-01`):** `expiryDate > CURRENT_DATE`. API lập phiếu từ chối EXP bằng hoặc trước hôm nay với `400 Bad Request`. Khi duyệt, service kiểm tra lại và trả `409 Conflict` nếu EXP bằng hoặc trước ngày duyệt. Hàng không quản lý hạn được để EXP trống.
    * **Kiểm tra vị trí kệ (BR-INB-03).** Chỉ cất hàng vào ô `RAW` hoặc `FG` đang không bảo trì. Ô đang chứa SKU khác cũng bị từ chối. Vi phạm trả `400 Bad Request`. `max_capacity` là thông tin sức chứa thiết kế được lưu khi tạo ô. Luồng nhập hiện chưa đối chiếu tải với giá trị này.
    * **Ghi dữ liệu:** Tự động sinh mã phiếu `RN-YYYYMMDD-XXXX`, gán `maker_id = currentUserId`, `status = 'PENDING_APPROVAL'`, lưu `receipt_note` và các dòng `receipt_note_item`. **Chưa tăng tồn kho thực tế ở bước này.**
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`.
* **`approveReceiptNote(Long receiptId, ReceiptApprovalDTO approvalDTO, Long currentUserId)`:**
  * **Mục đích:** Phê duyệt phiếu nhập và thực thi giao dịch nguyên tử 4 bảng (`UC03`).
  * **Quy tắc & Logic xử lý:**
    * **Maker-Checker (`BR-SEC-03`):** Bắt buộc `currentUserId != receipt.maker_id`. Nếu người lập phiếu cố tình tự phê duyệt, ném `SecurityViolationException`.
    * **Kiểm tra trạng thái:** Phiếu phải đang ở trạng thái `PENDING_APPROVAL`.
    * **Trường hợp từ chối (`approved == false`):** Cập nhật `status = 'REJECTED'`, lưu lý do từ chối vào `review_note`.
    * **Trường hợp chấp thuận (`approved == true`):**
      1. Kiểm tra lại vùng, bảo trì và SKU tại vị trí. Ô không hợp lệ trả `400 Bad Request` và phiếu tiếp tục chờ duyệt.
      2. Tạo hoặc cập nhật lô hàng và cộng lượng thực nhận vào On-hand.
      3. Cập nhật ô thành `OCCUPIED` khi có tồn.
      4. Ghi phát sinh nhập và vết kiểm toán. Trên mỗi dòng thẻ kho, `after_changed` là tổng On-hand của SKU ngay sau khi cộng lượng của lô phát sinh. Hoàn tất phiếu với trạng thái `COMPLETED`.
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
    * **Dữ liệu lưu.** Đầu phiếu lưu `order_number`, `customer_name` (khách hàng hoặc bộ phận nhận) và `export_type`. Một yêu cầu SKU có thể sinh nhiều dòng theo lô. Khi lập phiếu, mỗi dòng lưu `requested_qty = allocated_qty` bằng phần lượng lấy từ lô đó. Tổng các dòng là lượng yêu cầu đã được cấp đủ.
    * **Ngoại lệ FEFO/FIFO.** Yêu cầu lý do 10–255 ký tự sau khi bỏ khoảng trắng đầu/cuối. API phản hồi `isOverridden` và `overrideReason`. Giao diện đánh dấu phiếu ngoại lệ để Quản lý xem lý do khi duyệt phiếu.
    * **Khóa tạm giữ tồn kho:** Với mỗi dòng xuất, tăng `reserved_qty = reserved_qty + allocated_qty` trên bảng `stock_batch`. Cơ chế này ngăn chặn tuyệt đối các giao dịch khác tranh chấp xuất âm.
    * Sinh mã phiếu `IN-YYYYMMDD-XXXX`, gán `maker_id = currentUserId`, `status = 'PENDING_APPROVAL'`, lưu `issue_note` và `issue_note_item`.
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`.
* **`approveIssueNote(Long issueId, Long currentUserId)`:**
  * **Mục đích:** Quản lý phê duyệt phiếu xuất kho chuyển sang trạng thái chờ lấy hàng (`UC05`).
  * **Quy tắc & Logic xử lý:**
    * **Maker-Checker (`BR-SEC-03`):** Bắt buộc `currentUserId != issue.maker_id`. Ném `SecurityViolationException` (HTTP 403) nếu tự duyệt.
    * **Kiểm tra điều kiện lô (BR-OUT-03).** Lô quá hạn hoặc không đủ điều kiện xuất thông thường, gồm `QUARANTINED` và `BLOCKED`, làm thao tác duyệt trả `409 Conflict`. Lượng giữ chỗ được giải phóng khi từ chối hoặc hủy phiếu theo trạng thái.
    * Cập nhật `issue_note`: `status = 'APPROVED_READY_TO_PICK'`, `checker_id = currentUserId`, `updated_at = CURRENT_TIMESTAMP`.
  * **Ràng buộc giao dịch:** `@Transactional(rollbackFor = Exception.class)`.
* **`confirmDispatch(Long issueId, Long currentUserId)`:**
  * **Mục đích:** Nhân viên kho xác nhận hoàn tất bốc hàng và bàn giao thực tế khỏi kho, chính thức trừ tồn On-hand (`UC05`).
  * **Quy tắc & Logic xử lý:**
    * Kiểm tra trạng thái phiếu: Bắt buộc phải là `APPROVED_READY_TO_PICK`. Ném `ConflictException` (HTTP 409) nếu sai trạng thái.
    * **Kiểm tra điều kiện lô (BR-OUT-03).** Kiểm tra lại từng lô trước khi giao. Lô quá hạn hoặc bị khóa làm thao tác trả `409 Conflict` và giữ nguyên On-hand, Reserved và trạng thái phiếu. Hủy phiếu đã duyệt để giải phóng lượng giữ chỗ.
    * Cập nhật `stock_batch` theo từng item:
      * Trừ tồn thực tế: `on_hand_qty = on_hand_qty - allocated_qty`.
      * Giải phóng lượng giữ chỗ: `reserved_qty = reserved_qty - allocated_qty`.
    * Cập nhật ô kệ còn tồn thành `OCCUPIED`, ô hết tồn thành `EMPTY` và giữ nguyên trạng thái ô `MAINTENANCE` (`BR-CAT-03`).
    * Ghi nhật ký vào `stock_card`: `change_type = 'XUAT_KHO'`, `qty_changed = -allocated_qty`, `after_changed = SUM(on_hand_qty)` trên mọi lô cùng SKU ngay sau dòng xuất, `reference_code = issue_number` (`BR-SEC-02`).
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
    Available = SUM(max(0, on_hand_qty - reserved_qty))
    Lô đủ điều kiện: AVAILABLE, vị trí RAW hoặc FG, EXP >= hôm nay hoặc EXP NULL.
    ```
* **`getAllAvailableStock()`:**
  * **Mục đích:** Tổng hợp tồn theo SKU. `underMin` đúng khi `Available ≤ Min Stock`. `onOrder` là tổng `ordered_qty − received_qty` trên PO `APPROVED`. `suggestedOrderQty` bằng `max(0, Max Stock − (Available + onOrder))` nếu tổng không vượt Min Stock. Các trường hợp còn lại trả 0.
* **`getStockCardHistory(Long productId, LocalDate fromDate, LocalDate toDate)`:**
  * **Mục đích:** Tra cứu sổ thẻ kho của sản phẩm (`UC06`), sắp xếp theo thời gian tăng dần để phục vụ công tác đối soát, kiểm toán kho.
* **`getLowStockAlerts()`:**
  * **Mục đích:** Lọc danh sách các mặt hàng có tồn khả dụng `<= min_stock_alert` (`UC07`) để hiển thị thông báo bổ sung hàng khẩn cấp lên Dashboard.
* **`getNearExpiryAlerts(int thresholdDays)`:**
  * **Mục đích:** Lọc lô có On-hand > 0, EXP khác NULL và `EXP ≤ ngày tra cứu + thresholdDays` (UC07). Bao gồm cả lô quá hạn. API dùng một ngưỡng chung theo lần tra cứu.

---

## 4. DANH MỤC REST API & WEB CONTROLLER ENDPOINTS (ENDPOINTS CATALOG)

| Mã UC | HTTP Method | URL Route Path | Controller Handler | Tham số Đầu vào (Input Payload) | Kết quả Trả về (Output) | HTTP Status | Quyền hạn (RBAC) |
| :---: | :---: | :--- | :--- | :--- | :--- | :---: | :---: |
| **AUTH** | `GET` | `/auth/login` | `AuthController.loginView` | — | `auth/login.html` | 200 | Công khai |
| **AUTH** | `POST` | `/auth/api/login` | `AuthController.loginApi` | `LoginRequestDTO` | `UserResponseDTO` và phiên đăng nhập | 200 | Công khai |
| **AUTH** | `POST` | `/auth/logout` | `AuthController.logout` | — | Chuyển tới `/auth/login` | 302 | Kết thúc phiên hiện có |
| **FR02** | `GET` | `/api/v1/purchase-orders`, `/api/v1/purchase-orders/{poNumber}` | `PurchaseOrderController` | Mã PO khi xem chi tiết | Danh sách hoặc chi tiết PO | 200 | Công khai theo cấu hình hiện tại |
| **FR02** | `GET` | `/receipt/api/purchase-orders` | `CoreController.orders` | — | Danh sách PO workspace | 200 | Đã đăng nhập |
| **FR02** | `POST` | `/receipt/api/purchase-orders` | `CoreController.importOrder` | `ImportRequest` | `PurchaseOrderService.View` | 200, 401, 403 | `ROLE_MANAGER` |
| **FR02** | `POST` | `/api/v1/purchase-orders` | `PurchaseOrderController.importOrder` | `PurchaseOrderImportDTO` | `PurchaseOrderResponseDTO` | 201, 401, 403 | `ROLE_MANAGER` |
| **UC01** | `GET` | `/catalog/products`, `/catalog/locations` | `CatalogController` | — | Màn hình danh mục | 200 | Đã đăng nhập |
| **UC01** | `GET` | `/catalog/api/products`, `/catalog/api/locations` | `CatalogController`, `UiQueryController` | — | Danh sách SKU hoặc ô kệ | 200 | Đã đăng nhập |
| **UC01** | `POST` | `/catalog/api/products` | `CatalogController.createProductApi` | `ProductCreateDTO` | `ProductResponseDTO` | 201 | `ROLE_MANAGER`, `ROLE_ADMIN` |
| **UC01** | `POST` | `/catalog/api/locations` | `CatalogController.setupLocationApi` | `LocationSetupDTO` | Thân phản hồi rỗng | 201 | `ROLE_MANAGER`, `ROLE_ADMIN` |
| **UC01** | `PUT` | `/catalog/api/products/{id}/limits` | `CoreController.limits` | `Limits` | `ProductResponseDTO` | 200 | `ROLE_MANAGER`, `ROLE_ADMIN` |
| **UC02** | `GET` | `/receipt/create` | `ReceiptController.createReceiptView` | — | Màn hình lập phiếu | 200 | Đã đăng nhập |
| **UC02** | `GET` | `/receipt/api/notes`, `/receipt/api/notes/{id}` | `ReceiptController`, `UiQueryController` | Bộ lọc trạng thái và mã PO hoặc ID phiếu | Danh sách hoặc chi tiết phiếu | 200 | Đã đăng nhập |
| **UC02** | `POST` | `/receipt/api/notes` | `ReceiptController.createReceiptNoteApi` | `ReceiptNoteCreateDTO` | `ReceiptNoteResponseDTO` | 201 | `ROLE_STAFF` |
| **UC03** | `GET` | `/receipt/approve` | `ReceiptController.approveReceiptView` | — | Màn hình duyệt phiếu | 200 | Đã đăng nhập |
| **UC03** | `POST` | `/receipt/api/notes/{id}/approve` | `ReceiptController.approveReceiptNoteApi` | `ReceiptApprovalDTO` | Phiếu `COMPLETED` hoặc `REJECTED` | 200 | `ROLE_MANAGER`, khác người lập |
| **UC04** | `GET` | `/issue/create`, `/issue/pick` | `IssueController` | — | Màn hình lập phiếu hoặc bảng lấy hàng | 200 | Đã đăng nhập |
| **UC04** | `GET` | `/issue/api/propose-fefo` | `IssueController.proposeAllocationApi` | `productId`, `requestedQty`, `strategy` | `List<PickListProposalDTO>` | 200 | Đã đăng nhập |
| **UC04** | `POST` | `/issue/api/notes` | `IssueController.createIssueNoteApi` | `IssueRequestDTO` | `IssueNoteResponseDTO` | 201 | `ROLE_STAFF` |
| **UC05** | `GET` | `/issue/api/notes`, `/issue/api/notes/{id}` | `UiQueryController` | Trạng thái hoặc ID phiếu | Danh sách hoặc chi tiết phiếu | 200 | Đã đăng nhập |
| **UC05** | `POST` | `/issue/api/notes/{id}/approve`, `/issue/api/notes/{id}/reject` | `IssueController` | Lý do khi từ chối | `IssueNoteResponseDTO` | 200 | `ROLE_MANAGER`, khác người lập |
| **UC05** | `POST` | `/issue/api/notes/{id}/dispatch`, `/issue/api/notes/{id}/complete` | `IssueController` | ID lệnh đã duyệt | `IssueNoteResponseDTO` | 200 | `ROLE_STAFF` |
| **UC05** | `POST` | `/issue/api/notes/{id}/cancel` | `IssueController.cancel` | `Decision` gồm lý do hủy | Thân phản hồi rỗng | 204 | `ROLE_MANAGER` hoặc `ROLE_STAFF` là người lập |
| **UC06** | `GET` | `/inventory/stock-card` | `InventoryController.stockCardView` | — | Màn hình thẻ kho | 200 | Đã đăng nhập |
| **UC06** | `GET` | `/inventory/api/stock-cards` | `InventoryController.getStockCardHistoryApi` | `productId`, tùy chọn `fromDate`, `toDate` | `List<StockCardDTO>` | 200 | Đã đăng nhập |
| **UC07** | `GET` | `/inventory/alerts` | `InventoryController.alertsView` | — | Màn hình cảnh báo | 200 | Đã đăng nhập |
| **UC07** | `GET` | `/inventory/api/alerts/low-stock` | `InventoryController.getLowStockAlertsApi` | — | `List<AvailableStockDTO>` | 200 | Đã đăng nhập |
| **UC07** | `GET` | `/inventory/api/alerts/near-expiry` | `InventoryController.getNearExpiryAlertsApi` | `thresholdDays` (1–365, mặc định 30) | `List<NearExpiryAlertDTO>` | 200, 400 | Đã đăng nhập |

“Đã đăng nhập” áp dụng cho cả ba role ở workspace. Phân quyền ghi và điều kiện trạng thái chứng từ được đối chiếu ở [ma trận quyền](../00_survey_and_scope/chuong_1_gioi_thieu_va_pham_vi.md#actor-role-permission).

---

## 5. KẾT LUẬN & ĐÁNH GIÁ NGHIỆM THU CỦA REVIEWER

* **Maker (M4 - Thạch Hoàng Quân):** Đã hoàn thành 100% việc viết mã Java Interfaces thực tế tại `source/src/main/java/com/wms/service/`, hoàn thành toàn bộ các DTOs, Enums, Exceptions và lập tài liệu đặc tả chi tiết logic xử lý nghiệp vụ cho từng hàm cùng danh mục REST API endpoints.
* **Reviewer (M1 - Nguyễn Thành Trung):** Xác nhận tài liệu rõ ràng, chuẩn định dạng Markdown, không bị lỗi bảng hay lỗi KaTeX, khớp hoàn toàn với Class Diagram, Sequence Diagram, 10 bảng CSDL 3NF và Cấu trúc Package Spring Boot. **Chấp thuận nghiệm thu hoàn thành Task T3.4.**
