# MA TRẬN TRUY VẾT YÊU CẦU (REQUIREMENTS TRACEABILITY MATRIX - RTM)
<!-- 
Tài liệu khởi tạo ở Sprint 1 (Task T1.5) và hoàn thiện ở Sprint 4 (Task T4.4)
Tham chiếu tiêu chuẩn: SpecificationStandard.md (Mục 1.5)
Quy cách:
- Bảng ánh xạ xuyên suốt: Problem (P-xx) -> UR -> BR -> FR/NFR -> Use Case (UC) -> Sequence (SQ) -> Database -> Screen (SCR) -> Test Case (TC).
-->

## 1. MỤC ĐÍCH MA TRẬN TRUY VẾT
Ma trận truy vết yêu cầu (Requirements Traceability Matrix - RTM) bảo đảm tính nhất quán và tính kết nối tuyến tính xuyên suốt đồ án: từ **Vấn đề hiện trạng (`P-xx`)** $\rightarrow$ **Yêu cầu người dùng (`URxx`)** $\rightarrow$ **Quy tắc nghiệp vụ (`BRxx`)** $\rightarrow$ **Yêu cầu chức năng (`FRxx`) / Phi chức năng (`NFRxx`)** $\rightarrow$ **Mô hình UML** $\rightarrow$ **Cơ sở dữ liệu** $\rightarrow$ **Giao diện** $\rightarrow$ **Ca kiểm thử (Test Case)**.

---

## 2. MA TRẬN TRUY VẾT GIAI ĐOẠN 1: TỪ HIỆN TRẠNG ĐẾN ĐẶC TẢ YÊU CẦU (SPRINT 1)

### Bảng 1: Ánh xạ từ Vấn đề Hiện trạng (`P-xx`) sang Nhu cầu (`URxx`), Quy tắc (`BRxx`) và Yêu cầu (`FRxx` / `NFRxx`)

| Mã Vấn đề | Tên Vấn đề Hiện trạng (Chương 2) | Yêu cầu Người dùng (`URxx`) | Quy tắc Nghiệp vụ chi phối (`BRxx`) | Yêu cầu Chức năng (`FRxx`) | Yêu cầu Phi chức năng (`NFRxx`) | Ghi chú & Định hướng Triển khai |
| :---: | :--- | :---: | :--- | :---: | :---: | :--- |
| **P-01** | Dữ liệu tồn kho phân tán, không đồng bộ | `UR01`, `UR02`, `UR05` | `BR-CAT-01`, `BR-CAT-04`, `BR-INB-02`, `BR-OUT-01`, `BR-OUT-04` | `FR01.01`, `FR02.01`, `FR02.03`, `FR03.03`, `FR04.01`, `FR04.02` | `NFR-REL-01`, `NFR-REL-02`, `NFR-PER-01` | CSDL tập trung, cập nhật tồn kho real-time khi duyệt chứng từ. |
| **P-02** | Không kiểm soát tồn khả dụng, nguy cơ cấp phát thiếu hàng | `UR04`, `UR01` | `BR-OUT-01`, `BR-OUT-04` | `FR03.01`, `FR03.03`, `FR04.01` | `NFR-PER-02`, `NFR-REL-02` | Tự động giữ chỗ ($Reserved$) khi gửi phiếu xuất; chống bán âm/tranh chấp hàng. |
| **P-03** | Đứt gãy thông tin giữa Mua hàng và Kho | `UR03`, `UR02` | `BR-INV-01`, `BR-INV-02`, `BR-INB-01` | `FR02.01`, `FR04.03` | `NFR-USA-01`, `NFR-SEC-04` | Nhập kho gắn PO đã duyệt; liên thông trạng thái PO và hàng đang về (On-order). |
| **P-04** | Bố trí hàng ngẫu nhiên, thiếu mã vị trí kho | `UR01` | `BR-CAT-02`, `BR-CAT-03`, `BR-INB-03` | `FR01.03`, `FR02.02`, `FR03.02` | `NFR-USA-01`, `NFR-USA-02` | Mô hình hóa vị trí theo Zone - Shelf - Bin chuẩn mặt bằng 3M Floor Marking. |
| **P-05** | Không quản lý Lô và Hạn dùng, vi phạm FIFO/FEFO | `UR01`, `UR04` | `BR-LOT-01`, `BR-INV-03`, `BR-OUT-02`, `BR-OUT-03`, `BR-OUT-05`, `BR-OUT-06` | `FR02.02`, `FR03.02`, `FR04.03`, `FR06.01`, `FR06.02` | `NFR-PER-02`, `NFR-USA-03` | Quản lý Lot/EXP; thuật toán tự động gợi ý xuất lô hạn ngắn trước (FEFO); khóa chặn hàng quá hạn. |
| **P-06** | Kiểm kê định kỳ kéo dài, sai số lớn (10–15%) | `UR02` | `BR-STK-01`, `BR-STK-02`, `BR-STK-03` | `FR05.01`, `FR05.02`, `FR05.03`, `FR07.02` | `NFR-PER-01`, `NFR-SEC-03` | Kiểm kê điện tử; tự động tính sai lệch thừa/thiếu; cơ chế đếm lại lần 2 độc lập. |
| **P-07** | Thiếu định mức an toàn và cảnh báo tồn thấp | `UR03`, `UR04` | `BR-CAT-05`, `BR-INV-01`, `BR-INV-02` | `FR01.02`, `FR04.03` | `NFR-PER-02`, `NFR-USA-03` | Thiết lập Min/Max Stock; cảnh báo trực quan; tính toán lượng đề xuất đặt hàng bù trừ. |
| **P-08** | Chứng từ lỏng lẻo, thiếu cơ chế Maker-Checker | `UR02`, `UR05` | `BR-SEC-01`, `BR-SEC-03`, `BR-INB-02`, `BR-OUT-03`, `BR-STK-02` | `FR02.03`, `FR03.03`, `FR05.03`, `FR06.02` | `NFR-SEC-03`, `NFR-SEC-04` | Cơ chế kiểm soát 4 mắt (Maker-Checker); cấm người lập tự duyệt; cấm sửa xóa phiếu Completed. |
| **P-09** | Khó truy xuất lịch sử luân chuyển (Audit Trail) | `UR02`, `UR05`, `UR06` | `BR-SEC-01`, `BR-SEC-02` | `FR04.02` | `NFR-SEC-03`, `NFR-REL-02` | Thẻ kho điện tử; lưu vết người tạo, người duyệt, thời gian và số tồn sau giao dịch. |
| **P-10** | Báo cáo quản trị chậm trễ, cắt dán thủ công | `UR06` | `BR-SEC-02` | `FR04.01`, `FR07.01`, `FR08.01`, `FR09.01` | `NFR-PER-01`, `NFR-POR-02` | Báo cáo NXT tổng hợp tự động theo thời gian thực; widget hiệu năng kho trực quan. |

---

## 3. MA TRẬN TRUY VẾT GIAI ĐOẠN 2: TỪ YÊU CẦU ĐẾN MÔ HÌNH UML & CSDL PHÂN TÍCH (SPRINT 2)

### Bảng 2: Ánh xạ Yêu cầu chức năng (`FRxx`) sang Use Case (`UCxx`), Biểu đồ Lớp Phân tích và ERD

| Mã FR | Tên Phân hệ / Chức năng | Use Case Chi tiết | Biểu đồ Lớp Phân tích (Class Diagram) | Lược đồ ERD Phân tích (3NF Table) | Quy tắc BR Chi phối |
| :---: | :--- | :---: | :--- | :--- | :--- |
| **FR01** | Quản lý Danh mục & Vị trí kho (3M) | [`UC01` Quản lý danh mục kho](../02_uml_modeling/use_case/usecase_specification.md#uc01) | `Category`, `Product`, `WarehouseLocation`, `ZoneType`, `LocationStatus` | `category`, `product`, `warehouse_location` | `BR-CAT-01` $\rightarrow$ `05` |
| **FR02** | Nhập kho theo PO & Maker-Checker | [`UC02` Lập phiếu nhập kho](../02_uml_modeling/use_case/usecase_specification.md#uc02), [`UC03` Phê duyệt phiếu nhập kho](../02_uml_modeling/use_case/usecase_specification.md#uc03) | `PurchaseOrder`, `PurchaseOrderItem`, `ReceiptNote`, `ReceiptNoteItem`, `ReceiptStatus` | `purchase_order`, `purchase_order_item`, `receipt_note`, `receipt_note_item` | `BR-INB-01` $\rightarrow$ `04`, `BR-LOT-01`, `BR-SEC-01`, `03` |
| **FR03** | Xuất kho, Giữ chỗ & Gợi ý FEFO/FIFO | [`UC04` Lập phiếu xuất kho](../02_uml_modeling/use_case/usecase_specification.md#uc04), [`UC05` Phê duyệt phiếu xuất kho](../02_uml_modeling/use_case/usecase_specification.md#uc05) | `IssueNote`, `IssueNoteItem`, `DispatchStrategy`, `IssueStatus` | `issue_note`, `issue_note_item` | `BR-OUT-01` $\rightarrow$ `06`, `BR-SEC-01`, `03` |
| **FR04** | Tra cứu tồn khả dụng, Thẻ kho & Cảnh báo | [`UC06` Tra cứu tồn kho](../02_uml_modeling/use_case/usecase_specification.md#uc06), [`UC07` Xem cảnh báo tồn kho](../02_uml_modeling/use_case/usecase_specification.md#uc07) | `StockBatch`, `StockCard`, `ChangeType` | `stock_batch`, `stock_card` | `BR-INV-01` $\rightarrow$ `03`, `BR-LOT-01`, `BR-SEC-02` |
| **SEC** | Phân quyền & Vết kiểm toán | *Xuyên suốt toàn hệ thống* | `User`, `UserRole` | `user`, `stock_card` | `BR-SEC-01` $\rightarrow$ `03` |

---

## 4. MA TRẬN TRUY VẾT GIAI ĐOẠN 3: TỪ YÊU CẦU CHI TIẾT ĐẾN THIẾT KẾ CSDL, MÀN HÌNH & CA KIỂM THỬ (SPRINT 3 – SPRINT 5)

### Bảng 3: Ánh xạ Xuyên suốt Toàn Hệ thống (`FR` / `SEC` / `NFR` $\rightarrow$ `UC` $\rightarrow$ `SQ` $\rightarrow$ `DB 3NF` $\rightarrow$ `API / Service` $\rightarrow$ `SCR` $\rightarrow$ `54 Test Cases`)

| Mã Yêu cầu | Tên Yêu cầu Chi tiết | Use Case (`UCxx`) | Biểu đồ Tuần tự (`SQxx`) | Bảng CSDL 3NF Liên quan | Phương thức Service / REST API | Mã Màn hình (`SCRxx`) | Ca Kiểm thử Nghiệp vụ (`TC-xx`) | Trạng thái |
| :---: | :--- | :---: | :---: | :--- | :--- | :---: | :--- | :---: |
| **`SEC / AUTH`** | Xác thực, Phân quyền RBAC & Cô lập vai trò | Xuyên suốt (`UC01`–`UC07`) | Tiền điều kiện `SQ01`–`SQ03` | `user` | `AuthService.login()`, `POST /auth/api/login`, `GET /auth/api/me` | [`SCR01`](../04_ui_ux_design/screen_specifications.md) | `TC-AUTH.01-P`, `TC-AUTH.02-P`, `TC-AUTH.03-P`, `TC-AUTH.01-N` $\rightarrow$ `TC-AUTH.05-N` (8 ca) | **PASS 100%** |
| **`FR01.01`** | Khởi tạo thông tin mặt hàng mới (SKU) | [`UC01`](../02_uml_modeling/use_case/usecase_specification.md#uc01) | — | `category`, `product` | `CatalogService.createProduct()`, `POST /catalog/api/products` | [`SCR03`](../04_ui_ux_design/screen_specifications.md) | `TC-FR01.01-P`, `TC-FR01.01-N1`, `TC-FR01.01-N2` (3 ca) | **PASS 100%** |
| **`FR01.02`** | Thiết lập định mức tồn kho an toàn (Min/Max) | [`UC01`](../02_uml_modeling/use_case/usecase_specification.md#uc01) | — | `product` | `CatalogService.setupStockLimits()`, `PUT /catalog/api/products/{id}/limits` | [`SCR03`](../04_ui_ux_design/screen_specifications.md) | `TC-FR01.02-P`, `TC-FR01.02-N1`, `TC-FR01.02-N2`, `TC-FR01.02-N3` (4 ca) | **PASS 100%** |
| **`FR01.03`** | Quản lý danh mục vị trí lưu kho chuẩn 3M | [`UC01`](../02_uml_modeling/use_case/usecase_specification.md#uc01) | — | `warehouse_location` | `CatalogService.setupLocation()`, `POST /catalog/api/locations` | [`SCR03`](../04_ui_ux_design/screen_specifications.md) | `TC-FR01.03-P`, `TC-FR01.03-N1`, `TC-FR01.03-N2` (3 ca) | **PASS 100%** |
| **`FR02.01`** | Tạo phiếu nhập kho từ Đơn mua hàng (PO) | [`UC02`](../02_uml_modeling/use_case/usecase_specification.md#uc02) | [`SQ01`](../02_uml_modeling/sequence_diagram/tom_tat.md) | `purchase_order`, `purchase_order_item`, `receipt_note`, `receipt_note_item` | `ReceiptService.createReceiptNote()`, `POST /receipt/api/create` | [`SCR04`](../04_ui_ux_design/screen_specifications.md) | `TC-FR02.01-P`, `TC-FR02.01-P2`, `TC-FR02.01-N1` $\rightarrow$ `TC-FR02.01-N5` (7 ca) | **PASS 100%** |
| **`FR02.02`** | Ghi nhận Lô, Hạn sử dụng & Vị trí cất hàng | [`UC02`](../02_uml_modeling/use_case/usecase_specification.md#uc02) | [`SQ01`](../02_uml_modeling/sequence_diagram/tom_tat.md) | `receipt_note_item`, `stock_batch`, `warehouse_location` | `ReceiptService.createReceiptNote()`, `POST /receipt/api/create` | [`SCR04`](../04_ui_ux_design/screen_specifications.md) | `TC-FR02.02-P`, `TC-FR02.02-P2`, `TC-FR02.02-N1`, `TC-FR02.02-N2` (4 ca) | **PASS 100%** |
| **`FR02.03`** | Phê duyệt phiếu nhập kho (Maker-Checker) | [`UC03`](../02_uml_modeling/use_case/usecase_specification.md#uc03) | [`SQ02`](../02_uml_modeling/sequence_diagram/tom_tat.md) | `receipt_note`, `receipt_note_item`, `stock_batch`, `purchase_order_item`, `stock_card` | `ReceiptService.approveReceiptNote()`, `POST /receipt/api/{id}/approve` | [`SCR04`](../04_ui_ux_design/screen_specifications.md) | `TC-FR02.03-P`, `TC-FR02.03-N1`, `TC-FR02.03-N2` (3 ca) | **PASS 100%** |
| **`FR03.01`** | Tạo yêu cầu xuất kho & Giữ chỗ tồn khả dụng | [`UC04`](../02_uml_modeling/use_case/usecase_specification.md#uc04) | [`SQ03`](../02_uml_modeling/sequence_diagram/tom_tat.md) | `issue_note`, `issue_note_item`, `stock_batch` | `IssueService.createIssueNote()`, `POST /issue/api/create` | [`SCR05`](../04_ui_ux_design/screen_specifications.md) | `TC-FR03.01-P`, `TC-FR03.01-N1`, `TC-FR03.01-N2` (3 ca) | **PASS 100%** |
| **`FR03.02`** | Đề xuất phân bổ lô tự động theo FEFO / FIFO | [`UC04`](../02_uml_modeling/use_case/usecase_specification.md#uc04) | [`SQ03`](../02_uml_modeling/sequence_diagram/tom_tat.md) | `stock_batch`, `product`, `issue_note_item` | `IssueService.proposeFefoAllocation()`, `GET /issue/api/propose-fefo` | [`SCR05`](../04_ui_ux_design/screen_specifications.md) | `TC-FR03.02-P`, `TC-FR03.02-N1`, `TC-FR03.02-N2` (3 ca) | **PASS 100%** |
| **`FR03.03`** | Phê duyệt lệnh xuất & Xác nhận thực xuất kho | [`UC05`](../02_uml_modeling/use_case/usecase_specification.md#uc05) | — | `issue_note`, `issue_note_item`, `stock_batch`, `stock_card`, `warehouse_location` | `IssueService.approveIssueNote()`, `IssueService.confirmDispatch()` | [`SCR05`](../04_ui_ux_design/screen_specifications.md) | `TC-FR03.03-P`, `TC-FR03.03-P2`, `TC-FR03.03-N1`, `TC-FR03.03-N3` (4 ca) | **PASS 100%** |
| **`FR04.01`** | Tra cứu tồn kho đa chiều thời gian thực | [`UC06`](../02_uml_modeling/use_case/usecase_specification.md#uc06) | — | `product`, `stock_batch`, `warehouse_location` | `InventoryService.getAvailableStockSummary()`, `GET /inventory/api/available-stock` | [`SCR02`](../04_ui_ux_design/screen_specifications.md), [`SCR06`](../04_ui_ux_design/screen_specifications.md) | `TC-FR04.01-P`, `TC-FR04.01-N1` (2 ca) | **PASS 100%** |
| **`FR04.02`** | Truy xuất lịch sử biến động Thẻ kho điện tử | [`UC06`](../02_uml_modeling/use_case/usecase_specification.md#uc06) | — | `stock_card`, `product`, `stock_batch`, `user` | `InventoryService.getStockCardHistory()`, `GET /inventory/api/stock-cards` | [`SCR06`](../04_ui_ux_design/screen_specifications.md) | `TC-FR04.02-P`, `TC-FR04.02-N1`, `TC-FR04.02-N2` (3 ca) | **PASS 100%** |
| **`FR04.03`** | Phát cảnh báo tồn thấp, cận hạn & gợi ý đặt hàng | [`UC07`](../02_uml_modeling/use_case/usecase_specification.md#uc07) | — | `product`, `stock_batch`, `purchase_order`, `purchase_order_item` | `InventoryService.getLowStockAlerts()`, `InventoryService.getNearExpiryAlerts()` | [`SCR02`](../04_ui_ux_design/screen_specifications.md) | `TC-FR04.03-P1`, `TC-FR04.03-P2`, `TC-FR04.03-P3` (3 ca) | **PASS 100%** |
| **`NFR-REL / PER`** | Toàn vẹn giao dịch ACID, Chống xung đột & Hiệu năng | Xuyên suốt | `SQ01`–`SQ03` | Toàn bộ 12 bảng 3NF | `@Transactional(rollbackFor = Exception.class)` | [`SCR02`](../04_ui_ux_design/screen_specifications.md)–[`SCR06`](../04_ui_ux_design/screen_specifications.md) | `TC-INT-TX.01`, `TC-INT-TX.02`, `TC-NFR-PER.01`, `TC-NFR-PER.02` (4 ca) | **PASS 100%** |

---

## 5. ĐÁNH GIÁ ĐỘ BAO PHỦ TRUY VẾT (TRACEABILITY COVERAGE SUMMARY)

* **Truy vết xuôi (Forward Traceability = 100%):** Toàn bộ 10 vấn đề hiện trạng (`P-01` $\rightarrow$ `P-10`), 6 nhu cầu người dùng (`UR01` $\rightarrow$ `UR06`), 25 quy tắc nghiệp vụ (`BR`) và 12 yêu cầu chức năng cốt lõi Must-have (`FR01.01` $\rightarrow$ `FR04.03`) đều được ánh xạ đầy đủ tới 7 Use Case, 12 bảng CSDL 3NF, 6 màn hình giao diện (`SCR01` $\rightarrow$ `SCR06`) và 54 ca kiểm thử (`TC`).
* **Truy vết ngược (Backward Traceability = 100%):** Không tồn tại bảng CSDL dư thừa, không có màn hình hay API nào nằm ngoài phạm vi yêu cầu đặc tả SRS.
* **Tham chiếu tài liệu liên quan:**
  * Mô hình UML & ERD: [`docs/02_uml_modeling/class_erd/class_erd.md`](../02_uml_modeling/class_erd/class_erd.md)
  * Biểu đồ tuần tự: [`docs/02_uml_modeling/sequence_diagram/tom_tat.md`](../02_uml_modeling/sequence_diagram/tom_tat.md)
  * Đặc tả ca kiểm thử & báo cáo nghiệm thu: [`docs/05_testing/test_cases.md`](../05_testing/test_cases.md) và [`docs/05_testing/test_execution_report.md`](../05_testing/test_execution_report.md)
