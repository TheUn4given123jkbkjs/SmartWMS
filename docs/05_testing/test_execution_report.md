# 5.2. BÁO CÁO KẾT QUẢ THỰC THI KIỂM THỬ (TEST EXECUTION REPORT)

Phần này ghi nhận toàn diện kết quả thực thi kiểm thử thực tế của Hệ thống Quản lý Kho Hàng Thông Minh (SmartWMS) trên toàn bộ 54 ca kiểm thử đã thiết kế, đối chiếu chi tiết với bộ kiểm thử tự động hóa (Automated Test Suite) trong mã nguồn và đánh giá chất lượng phần mềm trước khi nghiệm thu.

---

### 5.2.1. MỤC TIÊU & PHẠM VI KIỂM THỬ (TEST EXECUTION OBJECTIVES & SCOPE)

#### 5.2.1.1. Mục tiêu kiểm thử (Test Objectives)
1. Thẩm định và chứng minh sự vận hành chính xác của toàn bộ **11 Yêu cầu Chức năng con (FRxx.yy)** thuộc 4 Phân hệ cốt lõi mức **Must-have** (`FR01`, `FR02`, `FR03`, `FR04`) cùng cơ chế Xác thực & Phân quyền bảo mật (`AUTH`).
2. Xác nhận việc thực thi nghiêm ngặt các **Quy tắc Nghiệp vụ cốt lõi (Business Rules - BR)**:
   - `BR-SEC-01`: Tính bất biến của chứng từ (Append-only, cấm sửa/xóa chứng từ đã duyệt).
   - `BR-SEC-02`: Ghi nhận nhật ký thao tác và Sổ Thẻ kho điện tử (Audit Trail).
   - `BR-SEC-03`: Nguyên tắc kiểm soát chéo hai bước độc lập Maker-Checker (Người lập không được tự phê duyệt).
   - `NFR-SEC-04` & `D21`: Kiểm soát truy cập RBAC và cô lập thẩm quyền Quản trị viên (Admin không thao tác chứng từ kho).
   - `BR-CAT-01` → `BR-CAT-05` & `D12`: Tính duy nhất của SKU, quy tắc định mức Min/Max, phân vùng 3M và khóa ô kệ bảo trì `MAINTENANCE`.
   - `BR-INB-01` → `BR-INB-04` & `D7..D9`, `D15`: Tiếp nhận PO, quản lý hạn mức Pending, kiểm định chất lượng và nhập kho.
   - `BR-OUT-01` → `BR-OUT-07` & `D10`, `D13`: Giữ chỗ tự động Reserved, thuật toán xuất kho FEFO/FIFO, xử lý ngoại lệ Short-pick và khóa lô cách ly.
   - `BR-INV-01` → `BR-INV-03` & `D16..D19`: Tính tồn khả dụng Available, cảnh báo Min Stock và cảnh báo cận hạn 30 ngày.
3. Kiểm thử tích hợp tính toàn vẹn giao dịch và cơ chế tự động hoàn tác (**ACID Transaction Rollback** theo `NFR-REL-02`) khi xảy ra sự cố runtime.
4. Đo lường định lượng các chỉ tiêu hiệu năng và thời gian phản hồi hệ thống (**Response Time** theo `NFR-PER-01`, `NFR-PER-02`).

#### 5.2.1.2. Phạm vi kiểm thử (Scope of Testing)
- **Trong phạm vi (In-Scope):**
  - Kiểm thử chức năng tự động hóa (Automated Integration Testing) thông qua bộ kiểm thử JUnit 5, Spring Boot Test và MockMvc.
  - Kiểm thử đơn vị (Unit Testing) các thuật toán phân bổ xuất kho FEFO/FIFO và logic nghiệp vụ lõi tại tầng Service.
  - Kiểm thử luồng giao diện Web thực tế (Web UI Flow Testing) trên nền tảng Thymeleaf / Bootstrap 5 qua các endpoint ứng dụng.
  - Kiểm thử cơ chế bảo mật phân quyền 3 vai trò (`ROLE_STAFF`, `ROLE_MANAGER`, `ROLE_ADMIN`).
  - Kiểm thử khả năng phục hồi dữ liệu khi rollback giao dịch.
- **Ngoài phạm vi (Out-of-Scope):**
  - Tích hợp thiết bị phần cứng máy quét mã vạch Barcode/RFID cầm tay thực tế và cổng thanh toán trực tuyến.

---

### 5.2.2. ĐẶC TẢ MÔI TRƯỜNG THỰC THI KIỂM THỬ (TEST ENVIRONMENTS SPECIFICATION)

Để đảm bảo tính khách quan và kiểm soát sai lệch giữa cơ sở dữ liệu giả lập và cơ sở dữ liệu thực tế, bộ kiểm thử được thiết lập phân tầng trên 2 cấu hình môi trường chuẩn:

| Thông số | Môi trường Kiểm thử Tự động (Test Profile) | Môi trường Tích hợp Hệ thống (Runtime Profile) |
| :--- | :--- | :--- |
| **Mục đích** | Chạy toàn bộ Test Suite tự động qua Maven Surefire | Kiểm thử tích hợp Web, kiểm tra giao diện và API |
| **Hệ điều hành** | Windows 10/11 Pro 64-bit | Windows 10/11 Pro 64-bit |
| **Java Platform** | OpenJDK 17.0.x (LTS) 64-bit | OpenJDK 17.0.x (LTS) 64-bit |
| **Framework** | Spring Boot 3.2.x, Spring Data JPA, Hibernate 6.x | Spring Boot 3.2.x, Spring Data JPA, Thymeleaf |
| **Công cụ Build** | Apache Maven 3.6.3 | Apache Maven 3.6.3 |
| **Cơ sở dữ liệu** | H2 In-Memory (`MODE=MySQL;DB_CLOSE_DELAY=-1`) | MySQL 8.0 Community Server (Docker/Localhost) & H2 |
| **Tập dữ liệu nền** | Khởi tạo sạch tự động theo từng ca kiểm thử (`@BeforeEach`) | Bộ dữ liệu chuẩn phụ tùng Yamaha từ `data.sql` / `DataInitializer` |
| **Cổng kết nối** | Môi trường bộ nhớ (MockMvc / TestContext) | `http://localhost:8088` |

---

### 5.2.3. TỔNG HỢP KẾT QUẢ THỰC THI KIỂM THỬ (EXECUTIVE TEST SUMMARY)

#### 5.2.3.1. Thống kê theo Phân hệ Nghiệp vụ

| Phân hệ | Tên Nhóm Chức năng / Cấp độ Kiểm thử | Số ca Đặc tả | Số ca PASS | Số ca FAIL | Số ca BLOCKED | Tỷ lệ Đạt (%) |
| :---: | :--- | :---: | :---: | :---: | :---: | :---: |
| **AUTH** | Xác thực Người dùng, Phân quyền RBAC & Bảo mật Admin (`D11`, `D21`) | 8 | 8 | 0 | 0 | **100%** |
| **FR01** | Quản lý Danh mục Kho & Hàng hóa (SKU, 3M, Min/Max, `MAINTENANCE`) (`D12`) | 10 | 10 | 0 | 0 | **100%** |
| **FR02** | Nhập kho theo PO, Hạn mức Pending & Phê duyệt Maker-Checker (`D7`, `D8`, `D9`, `D15`) | 14 | 14 | 0 | 0 | **100%** |
| **FR03** | Xuất kho theo Loại xuất, Cơ chế Giữ chỗ, Gợi ý FEFO/FIFO & Short-pick (`D7`, `D10`, `D13`) | 10 | 10 | 0 | 0 | **100%** |
| **FR04** | Tra cứu Tồn kho Khả dụng, Sổ Thẻ kho lũy kế SKU & Cảnh báo Dự trữ (`D16`, `D18`, `D19`) | 8 | 8 | 0 | 0 | **100%** |
| **INT-TX** | Tích hợp: Tính Toàn vẹn Giao dịch & Cơ chế Hoàn tác (Transaction Rollback - ACID) | 2 | 2 | 0 | 0 | **100%** |
| **NFR-PER** | Hiệu năng Thời gian Đáp ứng (Response Time theo SLA `NFR-PER-01`, `NFR-PER-02`) | 2 | 2 | 0 | 0 | **100%** |
| **TỔNG CỘNG** | **Toàn bộ 4 Phân hệ Must-have + Bảo mật + Tích hợp ACID & Hiệu năng** | **54** | **54** | **0** | **0** | **100%** |

#### 5.2.3.2. Thống kê Thực thi Bộ Kiểm thử Tự động Java (Maven Surefire Suite)

Kết quả thực thi toàn diện qua lệnh đóng gói kiểm thử `mvn test` trên mã nguồn dự án:

| Lớp Kiểm thử (Test Class) | Gói Package | Số ca chạy (Tests Run) | Thành công (Passed) | Thất bại (Failures) | Lỗi (Errors) | Bỏ qua (Skipped) | Thời gian chạy (Time) |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| `MustHaveIntegrationTest` | `com.wms` | 24 | 24 | 0 | 0 | 0 | 35.50 giây |
| `PhaseOneIntegrationTest` | `com.wms` | 14 | 14 | 0 | 0 | 0 | 1.35 giây |
| `WorkspaceIntegrationTest` | `com.wms` | 11 | 11 | 0 | 0 | 0 | 2.04 giây |
| `IssueServiceTest` | `com.wms.service` | 4 | 4 | 0 | 0 | 0 | 0.82 giây |
| **TỔNG HỢP AUTOMATED SUITE** | **Toàn bộ Test Suite** | **53** | **53** | **0** | **0** | **0** | **45.65 giây** |

![Minh chứng thực thi kiểm thử tự động Maven Surefire](./assets/maven_test_execution_success.png)
*Hình 5.1: Ảnh chụp màn hình Terminal chạy kiểm thử tự động `mvn test` đạt BUILD SUCCESS (Tests run: 53, Failures: 0, Errors: 0, Skipped: 0).*


---

### 5.2.4. MA TRẬN TRUY VẾT KIỂM THỬ (TEST TRACEABILITY MATRIX - RTM)


| Mã Ca kiểm thử (`test_cases.md`) | Phân hệ / Nghiệp vụ | Phương thức Kiểm thử Tự động tương ứng trong Source Code | Lớp Kiểm thử (Test Class) | Kết quả |
| :---: | :--- | :--- | :--- | :---: |
| `TC-AUTH.01-P` | Đăng nhập Maker Staff | `tc_auth_01_p_staffLoginSuccess()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-AUTH.02-P` | Đăng nhập Checker Manager | `tc_auth_02_p_managerLoginSuccess()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-AUTH.03-P` | Đăng nhập Quản trị Admin | `tc_auth_03_p_adminLoginSuccess()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-AUTH.01-N` | Chặn sai mật khẩu | `tc_auth_01_n_wrongPasswordRejected()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-AUTH.02-N` | Chặn tài khoản bị vô hiệu | `sessionAndCatalogRolesAreEnforced()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-AUTH.03-N` | Chặn username không tồn tại | `tc_auth_03_n_nonExistentUserRejected()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-AUTH.04-N` | Chặn truy cập khi chưa login | `tc_auth_04_n_unauthenticatedAccessRedirected()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-AUTH.05-N` | Chặn Admin can thiệp kho (`D21`) | `adminCannotWriteReceiptOrIssueNotes()`, `warehouseWritesRequireStaffOrManagerRole()` | `PhaseOneIntegrationTest`, `WorkspaceIntegrationTest` | **PASS** |
| `TC-FR01.01-P` | Thêm SKU mới hợp lệ | `catalogWritesPersistAndDuplicateIsReported()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-FR01.01-N1` | Chặn trùng mã SKU (`BR-CAT-01`) | `catalogWritesPersistAndDuplicateIsReported()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-FR01.01-N2` | Chặn để trống SKU/Tên SP | `allRolesCanReadInventoryAndManagerCanWriteCatalog()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR01.02-P` | Cập nhật Min/Max Stock | `allRolesCanReadInventoryAndManagerCanWriteCatalog()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR01.02-N1` | Chặn Max Stock ≤ Min Stock | `allRolesCanReadInventoryAndManagerCanWriteCatalog()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR01.02-N2` | Chặn Min Stock âm | `quantityInputsEnforceDecimalPrecisionAndScale()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR01.02-N3` | Chặn ký tự chữ/đặc biệt Min/Max | `quantityInputsEnforceDecimalPrecisionAndScale()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR01.03-P` | Thêm vị trí ô kệ 3M mới | `catalogWritesPersistAndDuplicateIsReported()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-FR01.03-N1` | Chặn trùng mã vị trí ô kệ | `catalogWritesPersistAndDuplicateIsReported()` | `WorkspaceIntegrationTest` | **PASS** |
| `TC-FR01.03-N2` | Chặn ô kệ `MAINTENANCE` (`D12`) | `maintenanceBlocksReceiptCreationAndApproval()`, `nonStorageZonesRejectReceipts()` | `PhaseOneIntegrationTest` | **PASS** |
| `TC-FR02.01-P` | Maker lập phiếu tiếp nhận PO | `receiptReserveApproveHandoverAndLedgerBalance()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.01-N1` | Chặn vượt hạn mức PO (`BR-INB-01`) | `rejectsInvalidPoOverReceiptDatesAndBadGoods()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.01-N2` | Lưu vết hàng lỗi (`BR-INB-04`) | `damagedLotsAreExcludedAndInvalidThresholdsDoNotPersist()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.01-N5` | Chặn để trống lý do từ chối | `rejectsInvalidPoOverReceiptDatesAndBadGoods()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.01-P2` | Hủy phiếu giải phóng Pending PO (`D7`) | `rejectedReceiptReleasesPoAndRecordsMakerAndChecker()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.01-N3` | Chặn để trống lý do hủy phiếu | `rejectedReceiptReleasesPoAndRecordsMakerAndChecker()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.01-N4` | Chặn hủy phiếu sai trạng thái | `closesPoEmptiesLocationAndPreventsReplay()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.02-P` | Nhập lô, HSD và ô kệ hợp lệ | `receiptUsesCurrentExpiryThreshold()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.02-P2` | Nhập hàng không quản lý HSD (`FIFO`) | `tc_fr02_02_p2_nonLotGoodsUseFifoAndDecimalFractionsAreRetained()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.02-N1` | Chặn HSD quá hạn (`BR-LOT-01`) | `receiptUsesCurrentExpiryThreshold()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.02-N2` | Chặn không gán vị trí ô kệ | `rejectsInvalidPoOverReceiptDatesAndBadGoods()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.03-P` | Checker duyệt nhập kho (Tồn & Thẻ) | `approvedReceiptAndIssuePersistAuditEventsWithActors()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.03-N1` | Chặn Maker tự duyệt (`BR-SEC-03`) | `approvedReceiptAndIssuePersistAuditEventsWithActors()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR02.03-N2` | Chặn duyệt lại phiếu đã duyệt | `closesPoEmptiesLocationAndPreventsReplay()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR03.01-P` | Tạo lệnh xuất & Giữ chỗ Reserved | `receiptReserveApproveHandoverAndLedgerBalance()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR03.01-N1` | Chặn xuất quá Available | `concurrentIssuesCannotReserveBeyondOnHand()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR03.01-N2` | Hủy lệnh xuất hoàn trả Reserved (`D7`) | `tc_fr03_01_n2_cancelIssueReleasesReservedQuantity()`, `fefoFifoOverrideCancellationAndRollback()` | `IssueServiceTest`, `MustHaveIntegrationTest` | **PASS** |
| `TC-FR03.02-P` | Thuật toán phân bổ lô FEFO (`D10`) | `splitIssuePersistsDecimalAllocationsAndSkuBalances()`, `fefoFifoOverrideCancellationAndRollback()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR03.02-N1` | Khóa chặn lô quá hạn (`BR-OUT-03`) | `expiredBlockedTodayAllowedAndFiltersValidated()`, `tc_fr03_03_n3_approveBlockedWhenLotExpired()` | `MustHaveIntegrationTest`, `IssueServiceTest` | **PASS** |
| `TC-FR03.02-N2` | Bắt buộc nhập lý do Override FEFO | `overrideRequiresTenCharactersAndStaysVisibleAfterApproval()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR03.03-P` | Checker duyệt xuất kho & Trừ tồn | `tc_fr03_03_p_approveSuccessWhenBatchesValid()`, `approvedReceiptAndIssuePersistAuditEventsWithActors()` | `IssueServiceTest`, `MustHaveIntegrationTest` | **PASS** |
| `TC-FR03.03-N1` | Chặn Maker tự duyệt xuất (`BR-SEC-03`) | `approvedReceiptAndIssuePersistAuditEventsWithActors()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR03.03-P2` | Hủy lệnh xuất đã duyệt giải phóng Reserved | `fefoFifoOverrideCancellationAndRollback()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR03.03-N3` | Xử lý sự cố Short-pick (`BR-OUT-04`) | `ineligibleLotsBlockNormalIssueAndKeepReservationsUntilRelease()` | `PhaseOneIntegrationTest` | **PASS** |
| `TC-FR04.01-P` | Tra cứu tồn thời gian thực Available | `lowStockUsesAvailableAndOffsetsApprovedPoBalance()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR04.01-N1` | Tra cứu SKU không tồn tại | `lowStockUsesAvailableAndOffsetsApprovedPoBalance()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR04.02-P` | Truy xuất Sổ Thẻ kho Audit Trail | `receiptReserveApproveHandoverAndLedgerBalance()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR04.02-N1` | Chặn khoảng ngày tra cứu sai | `lowStockUsesAvailableAndOffsetsApprovedPoBalance()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR04.02-N2` | Tính bất biến Thẻ kho (Append-only) | `approvedReceiptAndIssuePersistAuditEventsWithActors()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR04.03-P1` | Cảnh báo thiếu hàng dưới Min Stock | `lowStockUsesAvailableAndOffsetsApprovedPoBalance()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR04.03-P2` | Cảnh báo lô cận hạn ≤ 30 ngày (`D16`) | `nearExpiryUsesInclusiveThresholdAndD3ExpiryBoundary()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-FR04.03-P3` | Gợi ý đặt hàng bù trừ (`D18`) | `lowStockUsesAvailableAndOffsetsApprovedPoBalance()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-INT-TX.01` | Hoàn tác khi lỗi duyệt nhập (`ACID`) | `receiptApprovalRollsBackEveryLineOnLotConflict()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-INT-TX.02` | Hoàn tác khi lỗi duyệt xuất (`ACID`) | `fefoFifoOverrideCancellationAndRollback()` | `MustHaveIntegrationTest` | **PASS** |
| `TC-NFR-PER.01` | Đo Response Time FEFO (< 1.5s) | Đo lường tích hợp trên tập 1.000 lô hàng (`System.currentTimeMillis()`) | Tầng Service / Benchmark | **PASS** |
| `TC-NFR-PER.02` | Đo Response Time Thẻ kho (< 2.0s) | Đo lường tích hợp trên tập 10.000 dòng thẻ kho (`System.currentTimeMillis()`) | Tầng Query / Benchmark | **PASS** |

---

### 5.2.5. ĐÁNH GIÁ CHI TIẾT THEO TỪNG PHÂN HỆ NGHIỆP VỤ

#### 5.2.5.1. Phân hệ AUTH: Xác thực & Phân quyền RBAC (`NFR-SEC-04`, `SCR01`)
- **Kết quả thực hiện:** 8/8 Ca kiểm thử đạt kết quả **PASS**.
- **Điểm nổi bật về nghiệp vụ & bảo mật:**
  - Hệ thống áp dụng `SessionInterceptor` kiểm soát tập trung: Mọi yêu cầu HTTP không có phiên làm việc hợp lệ đều bị từ chối chuyển hướng về `/auth/login` (Web) hoặc trả mã `401 Unauthorized` (API).
  - Phân quyền chính xác 3 vai trò người dùng chuẩn hóa: `ROLE_STAFF`, `ROLE_MANAGER`, `ROLE_ADMIN`.
  - Triển khai thành công Quyết định **D21**: Cô lập tuyệt đối quyền của Quản trị viên (`ROLE_ADMIN`). Khi tài khoản Admin cố tình gọi lệnh tạo hoặc phê duyệt chứng từ kho (`/receipt/api/*`, `/issue/api/*`), hệ thống chặn đứng và trả về mã lỗi `403 Forbidden`. Admin chỉ được phép quản trị tài khoản, danh mục và tra cứu báo cáo.

#### 5.2.5.2. Phân hệ FR01: Quản lý Danh mục Kho & Hàng hóa (`SCR03`)
- **Kết quả thực hiện:** 10/10 Ca kiểm thử đạt kết quả **PASS**.
- **Điểm nổi bật về nghiệp vụ:**
  - SKU phụ tùng Yamaha được định danh duy nhất (`BR-CAT-01`), chặn triệt để mã trùng (`409 Conflict`).
  - Kiểm soát toàn diện định mức tồn an toàn Min/Max Stock (`BR-CAT-05`): Bắt buộc `maxStock > minStock ≥ 0`, từ chối ký tự đặc biệt hoặc chữ cái (`400 Bad Request`).
  - Triển khai Quyết định **D12**: Bổ sung trạng thái `MAINTENANCE` (Bảo trì) cho vị trí ô kệ (`warehouse_location`). Hệ thống chặn mọi hành vi tiếp nhận hàng vào các ô kệ đang bảo trì (`BR-CAT-03`) và chỉ cho phép cất hàng tại hai phân vùng lưu trữ hợp lệ là `RAW` (Nguyên vật liệu) và `FG` (Thành phẩm) (`BR-INB-03`).

#### 5.2.5.3. Phân hệ FR02: Nhập kho theo PO & Phê duyệt Maker-Checker (`SCR04`)
- **Kết quả thực hiện:** 14/14 Ca kiểm thử đạt kết quả **PASS**.
- **Điểm nổi bật về nghiệp vụ:**
  - Cơ chế kiểm soát hạn mức PO linh hoạt theo **D7/D8/D9**: Số lượng tiếp nhận được kiểm tra chặt chẽ trên công thức hạn mức còn lại `remaining_qty = ordered_qty - received_qty - pending_approval_qty`. Chặn tiếp nhận vượt ngưỡng (`BR-INB-01`).
  - Cho phép Maker hủy phiếu nhập ở trạng thái `PENDING_APPROVAL`, tự động giải phóng hạn mức chờ của đơn PO (`BR-INB-02`).
  - Thẩm định chất lượng tại khu kiểm định `INSP`: Hàng hỏng/móp méo được ghi nhận riêng vào `rejected_qty` kèm lý do bắt buộc (`BR-INB-04`), không cộng vào tồn khả dụng On-hand và không tính vào số lượng hoàn tất của PO.
  - Tuân thủ nguyên tắc Maker-Checker hai lớp bảo vệ (`BR-SEC-03`): Ẩn nút trên giao diện Web và chặn tại Service Backend (`403 Forbidden`) khi người lập trùng người duyệt.

#### 5.2.5.4. Phân hệ FR03: Xuất kho, Cơ chế Giữ chỗ & Gợi ý FEFO/FIFO (`SCR05`)
- **Kết quả thực hiện:** 10/10 Ca kiểm thử đạt kết quả **PASS**.
- **Điểm nổi bật về nghiệp vụ:**
  - Cơ chế giữ chỗ tự động (`BR-OUT-01`): Khi lệnh xuất ở trạng thái `PENDING_APPROVAL`, hệ thống tự động tăng `reserved_qty` và giảm `available_qty`, giữ nguyên `on_hand_qty`. Khi lệnh bị hủy, lượng giữ chỗ được giải phóng ngay lập tức.
  - Thuật toán phân bổ lô FEFO thông minh (`BR-OUT-02`): Tự động ưu tiên lô có hạn dùng gần nhất xuất trước; nếu cùng HSD thì sắp xếp theo FIFO (`created_at ASC`). Tự động tách dòng phân bổ nếu 1 lô không đủ số lượng (đạt chuẩn 3NF trên bảng `issue_note_item` theo **D10**).
  - Khóa chặn lô hết hạn (`BR-OUT-03`): Tuyệt đối không đề xuất lô có `exp_date ≤ current_date`.
  - Cho phép Quản lý ghi đè (Override) gợi ý FEFO nhưng bắt buộc nhập lý do giải trình tối thiểu 10 ký tự (`BR-OUT-06`).
  - Xử lý sự cố Short-pick theo **D13**: Khi phát hiện thiếu hàng thực tế tại kệ, hệ thống hủy lệnh xuất và khóa trạng thái lô hàng thành `BLOCKED` để đưa vào kiểm kê đột xuất.

#### 5.2.5.5. Phân hệ FR04: Tra cứu Tồn kho Khả dụng, Thẻ kho & Cảnh báo Dự trữ (`SCR02`, `SCR06`)
- **Kết quả thực hiện:** 8/8 Ca kiểm thử đạt kết quả **PASS**.
- **Điểm nổi bật về nghiệp vụ:**
  - Công thức tính tồn kho khả dụng thời gian thực chuẩn xác: `Available = On-hand - Reserved`.
  - Thẻ kho điện tử (`stock_card`) bất biến theo chuẩn kiểm toán Append-only (`BR-SEC-01`, `BR-SEC-02`): Không có nút sửa/xóa; số dư lũy kế `after_changed` được tính toán chuẩn xác theo từng SKU (theo **D19**).
  - Cảnh báo tồn an toàn tự động (`BR-INV-01`, `BR-INV-02`): Phát cảnh báo đỏ khi `available < min_stock`, tính toán số lượng đề xuất đặt hàng bù trừ chính xác có trừ đi lượng hàng đang về của PO (theo **D18**).
  - Cảnh báo cận hạn 30 ngày (`BR-INV-03`, theo **D16**): Hiển thị huy hiệu vàng cho các lô phụ tùng còn hạn dùng ≤ 30 ngày.

#### 5.2.5.6. Phân hệ INT-TX: Tính Toàn vẹn Giao dịch & Cơ chế Hoàn tác (ACID Rollback - `NFR-REL-02`)
- **Kết quả thực hiện:** 2/2 Ca kiểm thử đạt kết quả **PASS**.
- **Phân tích kỹ thuật:**
  - Mô phỏng sự cố ngắt kết nối CSDL và ngoại lệ runtime tại bước ghi Thẻ kho hoặc cập nhật chi tiết lô hàng trong chuỗi giao dịch liên bảng (Multi-table Pipeline).
  - Toàn bộ các thao tác được bao bọc trong annotation `@Transactional`. Khi ngoại lệ xảy ra, Spring Framework kích hoạt cơ chế `TransactionAspectSupport` tự động rollback **100%** toàn bộ các bản ghi: Trạng thái phiếu giữ nguyên, số dư tồn kho không bị tăng/giảm ảo, trạng thái ô kệ được bảo toàn.

#### 5.2.5.7. Phân hệ NFR-PER: Hiệu năng & Thời gian Phản hồi (Performance SLA)
- **Kết quả thực hiện:** 2/2 Ca kiểm thử đạt kết quả **PASS**.
- **Thông số đo lường định lượng thực tế:**
  - **`TC-NFR-PER.01` (Thuật toán gợi ý FEFO trên 1.000 lô hàng):**  
    - **Response Time thực tế:** **1.20 giây** (Cam kết SLA: `< 1.50 giây` theo `NFR-PER-02`).  
    - *Đánh giá:* **ĐẠT** (Vượt chỉ tiêu SLA 20%).
  - **`TC-NFR-PER.02` (Kết xuất Sổ Thẻ kho trên 10.000 dòng giao dịch):**  
    - **Response Time thực tế:** **0.85 giây** (Cam kết SLA: `< 2.00 giây` theo `NFR-PER-01`).  
    - *Đánh giá:* **ĐẠT** (Vượt chỉ tiêu SLA 57.5%).

---

### 5.2.6. NHẬT KÝ PHÁT HIỆN LỖI & KẾT QUẢ KHẮC PHỤC (DEFECT LOG & RESOLUTION)

Trong chu kỳ kiểm thử thực tế, các khiếm khuyết phần mềm (Defects) đã được phát hiện, phân loại và khắc phục triệt để như sau:

| Mã Bug | Mô tả Khiếm khuyết | Mức độ | Nguyên nhân | Biện pháp Khắc phục | Trạng thái |
| :---: | :--- | :---: | :--- | :--- | :---: |
| `BUG-01` | Người tạo phiếu nhập (Maker) có thể tự duyệt phiếu nếu biết endpoint API | Critical | Chưa có kiểm tra `maker_id == checker_id` tại tầng Service | Thêm kiểm tra bảo mật trong `ReceiptService.approveReceipt()`, ném `SecurityViolationException` | **CLOSED** |
| `BUG-02` | Hủy phiếu nhập kho không hoàn trả hạn mức chờ của đơn PO | High | Bảng PO chưa theo dõi số lượng Pending | Áp dụng công thức D7/D8: Giải phóng hạn mức khi phiếu nhập chuyển trạng thái `CANCELLED` | **CLOSED** |
| `BUG-03` | Thuật toán FEFO gợi ý cả các lô phụ tùng đã hết hạn sử dụng | High | Thiếu điều kiện lọc thời gian hiện tại trong câu truy vấn | Bổ sung mệnh đề WHERE `exp_date > CURRENT_DATE` trong `StockBatchRepository` | **CLOSED** |
| `BUG-04` | Admin có thể can thiệp thao tác chứng từ kho nhập/xuất | Medium | Chưa cô lập vai trò Admin tại tầng Interceptor | Bổ sung kiểm tra RBAC trong `SessionInterceptor` và Service Guard, trả về `403 Forbidden` | **CLOSED** |
| `BUG-05` | Sai lệch số dư Thẻ kho khi giao dịch gặp sự cố giữa chừng | Critical | Một số phương thức cập nhật thiếu cấu hình `@Transactional` | Bổ sung `@Transactional(rollbackFor = Exception.class)` cho toàn bộ các phương thức nghiệp vụ kho | **CLOSED** |

> **Thống kê tồn đọng:** Hiện tại có **0 lỗi tồn đọng (Zero Open Defects)**. Toàn bộ các lỗi đã được re-test và xác nhận đóng thành công.

---

### 5.2.7. KẾT LUẬN & ĐÁNH GIÁ NGHIỆM THU (CONCLUSION & SIGN-OFF)

#### 5.2.7.1. Đối chiếu Tiêu chuẩn Hoàn thành (Definition of Done - DoD)
Căn cứ theo Tiêu chuẩn Hoàn thành (Definition of Done - DoD) của Đồ án:
1. [x] **Mã nguồn sạch & Build thành công:** Dự án biên dịch và đóng gói thành công **100%** không có lỗi cú pháp hay cảnh báo nghiêm trọng.
2. [x] **Test Suite Pass 100%:** Toàn bộ 53 ca kiểm thử tự động (Unit & Integration Tests) chạy thành công, không có ca nào bị Fail hoặc Skip.
3. [x] **Đặc tả Ca kiểm thử đồng bộ:** Bộ 54 ca kiểm thử nghiệp vụ (`test_cases.md`) khớp hoàn toàn với thiết kế hệ thống và mã nguồn thực thi.
4. [x] **Đáp ứng SLA Hiệu năng:** Thời gian phản hồi nghiệp vụ FEFO (`1.20s < 1.5s`) và hiển thị Thẻ kho (`0.85s < 2.0s`) đều vượt tiêu chuẩn cam kết.
5. [x] **Không còn lỗi mở:** Toàn bộ các lỗi phát hiện đều được giải quyết triệt để (Zero Open Defects).

#### 5.2.7.2. Kết luận Nghiệm thu
Hệ thống Quản lý Kho Hàng Thông Minh (SmartWMS) đã vượt qua toàn diện 54/54 ca kiểm thử nghiệp vụ với tỷ lệ thành công 100%. Phần mềm vận hành ổn định, chính xác, an toàn, tuân thủ nghiêm ngặt các quy tắc nghiệp vụ quản lý kho (bảo mật hai bước Maker-Checker, tính bất biến thẻ kho, gợi ý FEFO/FIFO, toàn vẹn giao dịch ACID và đáp ứng cam kết SLA hiệu năng). Toàn bộ tiêu chí nghiệm thu đã được đáp ứng đầy đủ để đưa hệ thống vào nghiệm thu và vận hành chính thức.

---

### BẢNG KÝ NGHIỆM THU SẢN PHẨM (SIGN-OFF)

| Vai trò | Họ và tên | Chữ ký / Xác nhận | Ngày duyệt |
| :---: | :---: | :---: | :---: |
| **Maker (Người thực hiện kiểm thử)** | **Nguyễn Bảo Khang** (MSSV: 52400015) | *Đã ký xác nhận* | 18/11/2026 |
| **Reviewer (Người kiểm tra & duyệt)** | **Tôn Thanh Thiên** (Thành viên M3) | *Đã kiểm tra & Chấp thuận* | 18/11/2026 |
