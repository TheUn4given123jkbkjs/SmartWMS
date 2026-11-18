# BỘ CA KIỂM THỬ CHỨC NĂNG NGHIỆP VỤ (TEST CASES SPECIFICATION)

**Học phần:** Phân tích và Thiết kế Yêu cầu (Mã môn: 502050 - ĐH Tôn Đức Thắng)  
**Dự án:** Hệ thống Quản lý Kho Hàng Thông Minh (SmartWMS) - Đối tác Phụ tùng Xe máy Yamaha  
**Hạng mục:** Sprint 4 - Task T4.3 (Test Design)  
**Thành viên thực hiện (Maker):** Nguyễn Bảo Khang (MSSV: 52400015 - Thành viên M2)  
**Người kiểm tra & duyệt (Reviewer):** Tôn Thanh Thiên (Thành viên M3)  
**Tiêu chuẩn quy chiếu:** `SpecificationStandard.md` (Chương 6), `functional_requirements.md`, `business_rules.md`, `screen_specifications.md`

---

## 1. KẾ HOẠCH KIỂM THỬ & QUY CÁCH ĐẶC TẢ (TEST PLAN & SPECIFICATION)

1. **Mục tiêu kiểm thử (Test Objectives):**
   - Chứng minh toàn bộ **11 Yêu cầu Chức năng con (FRxx.yy)** thuộc 4 Phân hệ cốt lõi mức **Must-have** (`FR01`, `FR02`, `FR03`, `FR04`) và cơ chế Xác thực & Phân quyền (`AUTH`) vận hành chính xác theo đúng đặc tả SRS.
   - Thẩm định sự tuân thủ nghiêm ngặt của các **Quy tắc Nghiệp vụ cốt lõi (Business Rules - BR)** và **Yêu cầu Bảo mật (Security)**:
     - `BR-SEC-01` (Tính bất biến của chứng từ: Cấm sửa xóa chứng từ đã hoàn tất - Append-only).
     - `BR-SEC-02` (Ghi nhận nhật ký thao tác kho & Sổ thẻ kho điện tử Audit Trail).
     - `BR-SEC-03` (Quy trình phê duyệt hai bước độc lập: Người lập khác Người duyệt - Maker-Checker).
     - `NFR-SEC-04` (Kiểm soát truy cập & Phân quyền người dùng theo vai trò RBAC).
     - `BR-CAT-01` & `BR-CAT-05` (Tính duy nhất của SKU và ràng buộc logic Min/Max Stock).
     - `BR-INB-01` $\rightarrow$ `BR-INB-04` (Quy trình tiếp nhận PO, kiểm tra chất lượng và nhập kho).
     - `BR-OUT-01` $\rightarrow$ `BR-OUT-06` (Cơ chế tạm giữ chỗ Reserved, thuật toán phân bổ FEFO/FIFO và xuất kho).
     - `BR-INV-01` $\rightarrow$ `BR-INV-03` (Công thức tính khả dụng Available, cảnh báo Min Stock và cảnh báo cận hạn $\le 30$ ngày).

2. **Phạm vi kiểm thử (Test Scope):**
   - **Trong phạm vi (In-Scope):**
     - Toàn bộ 4 phân hệ chức năng mức Must-have: Phân hệ Quản lý Danh mục Kho & Hàng hóa (`FR01`), Phân hệ Nhập kho theo PO & Phê duyệt (`FR02`), Phân hệ Xuất kho & Gợi ý FEFO/FIFO (`FR03`), Phân hệ Tra cứu Tồn kho Khả dụng & Sổ thẻ kho (`FR04`).
     - Cơ chế bảo mật: Xác thực tài khoản, phân quyền vai trò (RBAC: `ROLE_STAFF`, `ROLE_MANAGER`, `ROLE_ADMIN`), cơ chế kiểm soát chéo hai bước Maker-Checker.
     - Kiểm thử tích hợp tính toàn vẹn giao dịch và cơ chế hoàn tác (ACID Transaction Rollback - `NFR-REL-02`).
     - Đo lường thông số hiệu năng: Thời gian phản hồi hệ thống (`Response Time` theo `NFR-PER-01`, `NFR-PER-02`).
   - **Ngoài phạm vi (Out-of-Scope):**
     - Các chức năng mức Should-have / Could-have chưa triển khai trong phạm vi đồ án môn học (ví dụ: Tích hợp thiết bị phần cứng máy quét mã vạch Barcode/RFID cầm tay thực tế, tích hợp cổng thanh toán trực tuyến hoặc hóa đơn điện tử cơ quan thuế).

3. **Phương pháp & Chiến lược kiểm thử (Test Strategy & Approach):**
   - **Kiểm thử Hộp đen (Black-box Testing):** Áp dụng kỹ thuật Phân tích giá trị biên (Boundary Value Analysis - BVA) và Phân vùng tương đương (Equivalence Partitioning - EP) để thiết kế các ca Positive (`-P`) và Negative (`-N`), bảo đảm hệ thống bắt lỗi chính xác và trả về thông báo lỗi thân thiện.
   - **Kiểm thử An toàn & Phân quyền hai bước (Maker-Checker Testing):** Thẩm định độc lập 2 tầng bảo vệ: (1) Tầng giao diện UI (ẩn/vô hiệu hóa nút duyệt khi người đăng nhập trùng người lập); (2) Tầng Service Backend (chặn đứng và ném ngoại lệ `403 Forbidden` khi `maker_id == checker_id`, hoặc `409 Conflict` khi duyệt lại phiếu đã hoàn tất).
   - **Kiểm thử Tích hợp Toàn vẹn Dữ liệu (Integration & ACID Rollback Testing):** Mô phỏng lỗi gián đoạn CSDL hoặc ngoại lệ runtime giữa chuỗi nghiệp vụ để chứng minh cơ chế `@Transactional` tự động hoàn tác $100\%$, không phát sinh dữ liệu dở dang hay bản ghi mất liên kết.
   - **Kiểm thử Đo lường Thông số Hiệu năng (Performance Benchmark Testing):** Đo lường thời gian thực thi (`Response Time`) của thuật toán phân bổ xuất kho FEFO (`NFR-PER-02`) và tốc độ tải trang Sổ thẻ kho (`NFR-PER-01`) trên tập dữ liệu tải lớn (> 1.000 lô hàng, > 10.000 dòng thẻ kho được nạp phục vụ kiểm thử) để kiểm chứng việc đáp ứng cam kết SLA (< 1.5 giây cho thuật toán nghiệp vụ và < 2.0 giây cho tải trang).

4. **Đặc tả Môi trường Kiểm thử (Test Environments Specification):**
   Để bảo đảm tính khách quan và kiểm soát hiện tượng sai lệch hiệu năng giữa cơ sở dữ liệu giả lập và cơ sở dữ liệu thực tế, bộ kiểm thử được thiết lập phân tầng trên 2 cấu hình môi trường chuẩn:
   - **Môi trường Test In-Memory (Test/Stub Profile - H2 Database):**
     - *Cấu hình:* `jdbc:h2:mem:wms_db;DB_CLOSE_DELAY=-1;MODE=MySQL`
     - *Mục tiêu:* Phục vụ kiểm thử đơn vị độc lập (Unit Testing) và kiểm thử hồi quy luồng logic nhanh chóng, tự động làm sạch và khởi tạo lại trạng thái trắng sau mỗi ca chạy.
   - **Môi trường Tích hợp & Kiểm thử Hệ thống (System & Integration Runtime Profile):**
     - *Cấu hình:* Ứng dụng Spring Boot chạy trực tiếp tại `http://localhost:8088`, kết nối CSDL quan hệ chuẩn hóa (sử dụng H2 In-Memory với tham số `MODE=MySQL` chạy mặc định độc lập hoặc chuyển đổi MySQL 8.0 InnoDB qua cấu hình `application.properties`).
     - *Tập dữ liệu chuẩn:* Nạp bộ dữ liệu nền ban đầu (5 tài khoản người dùng phân quyền 3 vai trò, 5 danh mục ngành hàng, 8 ô kệ phân vùng 3M và 6 mã SKU phụ tùng chính hãng Yamaha) từ `data.sql` và `DataInitializer.java`; đối với các ca kiểm thử hiệu năng và tải lớn, tập dữ liệu bổ sung (> 1.000 lô hàng, > 10.000 dòng thẻ kho) được nạp thông qua kịch bản sinh dữ liệu kiểm thử (Test Data Seed Script) để tiến hành đo lường.
     - *Mục tiêu:* Kiểm thử các luồng chức năng nghiệp vụ, kiểm soát thẩm quyền Maker-Checker hai bước, và thẩm định tính toàn vẹn của cơ chế `@Transactional Rollback`.

5. **Quy cách mã hóa Ca kiểm thử (Test Case Naming Convention):**
   - `TC-AUTH.xx-P/N`: Ca kiểm thử Xác thực & Phân quyền đăng nhập.
   - `TC-FRxx.yy-P`: Ca kiểm thử **Positive** (Luồng thành công chính thống, dữ liệu hợp lệ).
   - `TC-FRxx.yy-N` (hoặc `-N1`, `-N2`): Ca kiểm thử **Negative** (Dữ liệu sai, vượt ngưỡng, vi phạm ràng buộc bảo mật hoặc quy tắc nghiệp vụ).
   - `TC-INT-TX.xx-N`: Ca kiểm thử **Tích hợp: Tính Toàn vẹn Giao dịch & Cơ chế Hoàn tác** (Transaction Rollback theo chuẩn ACID khi gặp lỗi ngoại lệ runtime bất ngờ - `NFR-REL-02`).
   - `TC-NFR-PER.xx-P`: Ca kiểm thử **Phi chức năng - Hiệu năng** (Đo lường thời gian đáp ứng Response Time định lượng theo chuẩn SLA `NFR-PER-01`, `NFR-PER-02`).
   - Cột **Đánh giá**: Ghi nhận kết quả thực thi kiểm thử (`PASS` / `FAIL`).

---

## 2. BẢNG TỔNG HỢP MA TRẬN PHÂN BỔ CA KIỂM THỬ (TEST SUITE SUMMARY)

| Mã Phân hệ | Tên Nhóm Chức năng / Cấp độ Kiểm thử | Màn hình / Tầng thực thi | Số ca Positive (`-P`) | Số ca Negative / Exception (`-N`) | Tổng số Ca |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **AUTH** | Xác thực Người dùng & Phân quyền RBAC | `SCR01` (`/auth/login`) | 3 | 4 | **7** |
| **FR01** | Quản lý Danh mục Kho & Hàng hóa (SKU, 3M, Min/Max) | `SCR03` (`/catalog/*`) | 3 | 6 | **9** |
| **FR02** | Nhập kho theo PO & Phê duyệt Maker-Checker | `SCR04` (`/receipt/*`) | 3 | 6 | **9** |
| **FR03** | Xuất kho, Cơ chế Giữ chỗ & Gợi ý FEFO/FIFO | `SCR05` (`/issue/*`) | 3 | 5 | **8** |
| **FR04** | Tra cứu Tồn kho Khả dụng, Thẻ kho & Cảnh báo | `SCR02`, `SCR06` (`/inventory/*`) | 5 | 3 | **8** |
| **INT-TX** | Tích hợp: Tính Toàn vẹn Giao dịch & Hoàn tác (Transaction Rollback - ACID) | Service Layer (`@Transactional`) | 0 | 2 | **2** |
| **NFR-PER** | Hiệu năng Thời gian Đáp ứng (Response Time $\le 2s$) | `SCR05`, `SCR06` (`/issue/*`, `/inventory/*`) | 2 | 0 | **2** |
| **TỔNG CỘNG** | **Toàn bộ 4 Nhóm Must-have + Tích hợp ACID & Hiệu năng** | **Hệ thống SmartWMS toàn diện** | **19 Ca** | **26 Ca** | **45 Ca** |

---

## 3. CHI TIẾT BỘ CA KIỂM THỬ THEO TỪNG YÊU CẦU CHỨC NĂNG

### 3.0. PHÂN HỆ XÁC THỰC NGƯỜI DÙNG & PHÂN QUYỀN (AUTH / RBAC - `NFR-SEC-04`, `SCR01`)

| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-AUTH.01-P` | Đăng nhập thành công với vai trò Nhân viên kho (Maker) | Positive | Tài khoản `staff_thien` đang ở trạng thái kích hoạt (`active = TRUE`) | • Username: `staff_thien`<br>• Password: `123456` | 1. Mở màn hình `/auth/login`<br>2. Nhập username và password<br>3. Bấm nút "ĐĂNG NHẬP VÀO HỆ THỐNG" | Đăng nhập thành công; Hệ thống điều hướng vào Dashboard `/app`; Topbar hiển thị tên "Tôn Thanh Thiên" và quyền `ROLE_STAFF`. | **PASS** |
| `TC-AUTH.02-P` | Đăng nhập thành công với vai trò Quản lý kho (Checker) | Positive | Tài khoản `manager_khang` đang ở trạng thái kích hoạt (`active = TRUE`) | • Username: `manager_khang`<br>• Password: `123456` | 1. Mở màn hình `/auth/login`<br>2. Nhập username và password<br>3. Bấm nút "ĐĂNG NHẬP VÀO HỆ THỐNG" | Đăng nhập thành công; Hệ thống điều hướng vào Dashboard `/app`; Topbar hiển thị tên "Nguyễn Bảo Khang" và quyền `ROLE_MANAGER`. | **PASS** |
| `TC-AUTH.03-P` | Đăng nhập thành công với vai trò Quản trị viên hệ thống (Admin) | Positive | Tài khoản `admin` đang ở trạng thái kích hoạt (`active = TRUE`) | • Username: `admin`<br>• Password: `123456` | 1. Mở màn hình `/auth/login`<br>2. Nhập username và password<br>3. Bấm nút "ĐĂNG NHẬP VÀO HỆ THỐNG" | Đăng nhập thành công; Hệ thống điều hướng vào Dashboard `/app`; Topbar hiển thị "Quản trị viên Hệ thống" và quyền `ROLE_ADMIN`. | **PASS** |
| `TC-AUTH.01-N` | Chặn đăng nhập khi mật khẩu không chính xác | Negative | Tài khoản `staff_thien` tồn tại trong hệ thống | • Username: `staff_thien`<br>• Password: `WrongPassword_999` | 1. Mở màn hình `/auth/login`<br>2. Nhập đúng username nhưng sai password<br>3. Bấm nút Đăng nhập | Hệ thống từ chối xác thực; Trả về mã lỗi `401 Unauthorized` hoặc `SecurityViolationException: Mật khẩu không chính xác`; Giữ nguyên tại màn hình đăng nhập. | **PASS** |
| `TC-AUTH.02-N` | Chặn đăng nhập đối với tài khoản đang bị vô hiệu hóa | Negative | Tài khoản kiểm thử có cờ `active = FALSE` trong CSDL (ví dụ: quản trị viên khóa tài khoản bằng lệnh `UPDATE user SET active = 0 WHERE username = 'staff_quan'`) | • Username: `staff_quan`<br>• Password: `123456` | 1. Mở màn hình `/auth/login`<br>2. Nhập tài khoản bị khóa `staff_quan`<br>3. Bấm nút Đăng nhập | Hệ thống báo lỗi: "Tài khoản đang bị khóa"; Chặn không cấp phiên làm việc. | **PASS** |
| `TC-AUTH.03-N` | Chặn đăng nhập khi tên tài khoản không tồn tại trong hệ thống | Negative | Tên tài khoản `user_khong_ton_tai` chưa từng được tạo | • Username: `user_khong_ton_tai`<br>• Password: `123456` | 1. Mở màn hình `/auth/login`<br>2. Nhập tài khoản không tồn tại<br>3. Bấm nút Đăng nhập | Hệ thống từ chối xác thực; Trả về mã lỗi `404 Not Found` hoặc ném `ResourceNotFoundException: Tài khoản không tồn tại`. | **PASS** |
| `TC-AUTH.04-N` | Chặn truy cập trái phép các tài nguyên nội bộ khi chưa đăng nhập | Negative | Người dùng vãng lai chưa đăng nhập hệ thống (Chưa có phiên làm việc) | • URL nội bộ: `/receipt/create`, `/inventory/stock-card` | 1. Trực tiếp dán URL `/receipt/create` vào thanh địa chỉ trình duyệt<br>2. Bấm phím Enter | Hệ thống phát hiện chưa có phiên đăng nhập hợp lệ; Tự động chuyển hướng (Redirect) về trang đăng nhập `/auth/login`. | **PASS** |

---

### 3.1. PHÂN HỆ FR01: QUẢN LÝ DANH MỤC KHO & HÀNG HÓA (`SCR03`)

#### FR01.01: Khởi tạo thông tin mặt hàng mới (SKU, UOM, Quản lý lô - `BR-CAT-01`, `BR-CAT-04`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR01.01-P` | Thêm mới mặt hàng phụ tùng Yamaha thành công | Positive | Đã đăng nhập Quản lý kho; Danh mục ngành hàng `ENG` (Động cơ) đã tồn tại (ID: 2) | • `category_id`: 2<br>• `sku_code`: `YAM-ENG-002`<br>• `name`: "Bộ xích cam Exciter 155 VVA chính hãng"<br>• `uom`: "Sợi"<br>• `min_stock`: 15<br>• `max_stock`: 150<br>• `manage_by_lot`: `TRUE` | 1. Truy cập `/catalog/products`<br>2. Bấm "Thêm SKU mới"<br>3. Điền đầy đủ thông tin hợp lệ<br>4. Bấm "Lưu thông tin" | Hệ thống lưu bản ghi vào CSDL; Trả về mã `201 Created`; Mặt hàng hiển thị trên bảng danh mục với trạng thái `ACTIVE`. | **PASS** |
| `TC-FR01.01-N1` | Chặn tạo mặt hàng khi mã SKU đã tồn tại (`BR-CAT-01`) | Negative | Mã SKU `YAM-LUB-001` đã tồn tại trong CSDL | • `sku_code`: `YAM-LUB-001` (Trùng)<br>• `name`: "Nhớt trùng mã"<br>• `uom`: "Chai" | 1. Mở form thêm mới hàng hóa<br>2. Nhập `sku_code = YAM-LUB-001`<br>3. Bấm "Lưu thông tin" | Hệ thống từ chối lưu; Báo lỗi `409 Conflict: Mã SKU đã tồn tại trong hệ thống`; Dữ liệu CSDL không bị thay đổi. | **PASS** |
| `TC-FR01.01-N2` | Chặn tạo mặt hàng khi để trống các trường bắt buộc (Mã SKU hoặc Tên hàng hóa rỗng) | Negative | Form thêm hàng hóa đang mở | • `sku_code`: ""` (Để trống)<br>• `name`: ""` (Để trống) | 1. Bỏ trống ô nhập mã SKU<br>2. Để trống tên sản phẩm<br>3. Bấm "Lưu thông tin" | Báo lỗi validation `400 Bad Request`; Hệ thống hiển thị thông báo: "Mã SKU bắt buộc nhập", "Tên hàng hóa bắt buộc nhập"; Không tạo mới sản phẩm trong CSDL. | **PASS** |

#### FR01.02: Thiết lập định mức tồn kho an toàn Min/Max (`BR-CAT-05`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR01.02-P` | Cập nhật định mức tồn an toàn hợp lệ | Positive | Sản phẩm `YAM-BRK-001` đã tồn tại (ID: 4) | • `product_id`: 4<br>• `min_stock`: 30<br>• `max_stock`: 300 | 1. Mở form cấu hình tồn kho an toàn cho SKU `YAM-BRK-001`<br>2. Nhập `min_stock = 30`, `max_stock = 300`<br>3. Bấm "Cập nhật định mức" | Hệ thống cập nhật thành công (`200 OK`); Giá trị Min/Max mới được lưu vào CSDL và cập nhật trên màn hình. | **PASS** |
| `TC-FR01.02-N1` | Chặn cấu hình khi Tồn tối đa nhỏ hơn hoặc bằng Tồn tối thiểu (`BR-CAT-05`) | Negative | Sản phẩm `YAM-BRK-001` đang có | • `product_id`: 4<br>• `min_stock`: 100<br>• `max_stock`: 50 (Vi phạm: Max $\le$ Min) | 1. Nhập `min_stock = 100`<br>2. Nhập `max_stock = 50`<br>3. Bấm "Cập nhật định mức" | Hệ thống ném ngoại lệ `InvalidQuantityException: Tồn kho tối đa (maxStock) phải lớn hơn tồn tối thiểu (minStock)`; Báo lỗi `400 Bad Request`. | **PASS** |
| `TC-FR01.02-N2` | Chặn cấu hình khi Tồn tối thiểu mang giá trị âm | Negative | Sản phẩm `YAM-BRK-001` đang có | • `product_id`: 4<br>• `min_stock`: -10 (Số âm)<br>• `max_stock`: 100 | 1. Nhập `min_stock = -10`<br>2. Bấm "Cập nhật định mức" | Hệ thống báo lỗi Validation `400 Bad Request: Định mức tồn tối thiểu không được nhỏ hơn 0`. | **PASS** |
| `TC-FR01.02-N3` | Chặn cấu hình khi nhập ký tự đặc biệt hoặc chuỗi chữ cái vào ô định mức tồn kho | Negative | Sản phẩm `YAM-BRK-001` đang có | • `product_id`: 4<br>• `min_stock`: `@#$%`<br>• `max_stock`: `300abc` | 1. Mở form cấu hình tồn kho an toàn cho SKU `YAM-BRK-001`<br>2. Cố tình nhập ký tự đặc biệt hoặc chữ cái vào ô Min/Max Stock<br>3. Bấm "Cập nhật định mức" | Hệ thống từ chối xử lý; Trả về lỗi `400 Bad Request: Giá trị định mức tồn kho bắt buộc phải là định dạng số nguyên dương, không được chứa chữ cái hoặc ký tự đặc biệt`. | **PASS** |

#### FR01.03: Quản lý danh mục vị trí lưu kho 3M Floor Marking (`BR-CAT-02`, `BR-CAT-03`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR01.03-P` | Thiết lập vị trí ô kệ mới thành công | Positive | Đã đăng nhập quyền Quản lý | • `location_code`: `RAW-A01-03`<br>• `zone`: `RAW`<br>• `max_capacity`: 2000.00 | 1. Truy cập `/catalog/locations`<br>2. Nhập thông tin ô kệ mới<br>3. Bấm "Lưu vị trí" | Bản ghi được tạo trong bảng `warehouse_location` (`201 Created`); Trạng thái ban đầu mặc định là `EMPTY`. | **PASS** |
| `TC-FR01.03-N1` | Chặn tạo vị trí khi trùng mã `location_code` | Negative | Ô kệ `RAW-A01-01` đã tồn tại trong CSDL | • `location_code`: `RAW-A01-01` (Trùng)<br>• `zone`: `RAW`<br>• `max_capacity`: 2000.00 | 1. Nhập mã vị trí đã có `RAW-A01-01`<br>2. Bấm "Lưu vị trí" | Hệ thống từ chối lưu; Trả về mã lỗi `409 Conflict: Mã vị trí ô kệ đã tồn tại`. | **PASS** |

---

### 3.2. PHÂN HỆ FR02: QUẢN LÝ NHẬP KHO THEO PO & PHÊ DUYỆT MAKER-CHECKER (`SCR04`)

#### FR02.01: Lập Phiếu nhập kho từ Đơn mua hàng PO (Maker - `BR-INB-01`, `BR-INB-02`, `BR-INB-04`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR02.01-P` | Thủ kho (Maker) tạo phiếu tiếp nhận PO thành công | Positive | Đăng nhập tài khoản Maker (`staff_thien`); Đơn mua hàng PO-2026-001 đang chờ nhận | • `po_number`: `PO-2026-001`<br>• `supplier_name`: "Yamaha Motor Vietnam"<br>• Mặt hàng: `YAM-LUB-001`<br>• `received_qty`: 100<br>• `rejected_qty`: 0 | 1. Mở màn hình `/receipt/create`<br>2. Chọn mã PO và điền số lượng kiểm đếm đạt chuẩn<br>3. Bấm "Gửi phê duyệt phiếu nhập" | Phiếu nhập được tạo với mã tự sinh `RN-...`; Trạng thái phiếu là `PENDING_APPROVAL`; Tồn kho thực tế On-hand **chưa tăng** (`BR-INB-02`). | **PASS** |
| `TC-FR02.01-N1` | Chặn lập phiếu khi số lượng thực nhận vượt quá số lượng trên đơn PO (`BR-INB-01`) | Negative | Đơn PO-2026-001 có số lượng còn lại = 50 | • `po_number`: `PO-2026-001`<br>• `received_qty`: 80 (Vượt 30 cái so với PO) | 1. Mở màn hình lập phiếu nhập<br>2. Nhập số lượng thực nhận = 80<br>3. Bấm "Gửi phê duyệt" | Hệ thống từ chối tạo phiếu; Ném lỗi `400 Bad Request: Số lượng thực nhận vượt quá số lượng còn lại trên Đơn mua hàng`. | **PASS** |
| `TC-FR02.01-N2` | Bóc tách hàng lỗi/hỏng tại khu vực kiểm tra INSP (`BR-INB-04`) | Positive | Đơn PO-2026-002 giao 100 chai nhớt; Thủ kho kiểm đếm phát hiện 5 chai bị móp méo rò rỉ | • `received_qty`: 95<br>• `rejected_qty`: 5<br>• `rejection_reason`: "Móp chai, rò rỉ nắp seal" | 1. Nhập số lượng đạt chuẩn = 95<br>2. Nhập số lượng lỗi = 5 và ghi rõ lý do<br>3. Bấm "Gửi phê duyệt" | Phiếu nhập ghi nhận rõ 95 chai đạt chuẩn chờ nhập kho chính thức, 5 chai lỗi bị cách ly tại khu vực chờ hoàn trả; Trả về `201 Created`. | **PASS** |

#### FR02.02: Khai báo Số lô, Hạn dùng và Chỉ định vị trí cất hàng (`BR-LOT-01`, `BR-INB-03`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR02.02-P` | Ghi nhận Số lô, Hạn sử dụng và vị trí lưu kho hợp lệ | Positive | Phiếu nhập đang trong quá trình lập chi tiết mặt hàng `YAM-LUB-001` | • `lot_number`: `LOT-202611-01`<br>• `exp_date`: `2028-11-15` ($> \text{current\_date}$)<br>• `location_id`: 3 (`RAW-A01-01`) | 1. Nhập mã lô `LOT-202611-01`<br>2. Chọn HSD `15/11/2028`<br>3. Chọn ô kệ `RAW-A01-01`<br>4. Bấm "Xác nhận dòng hàng" | Dòng hàng được lưu hợp lệ; Hạn sử dụng và vị trí cất hàng được liên kết đầy đủ với chi tiết phiếu nhập. | **PASS** |
| `TC-FR02.02-P2` | Nhập kho mặt hàng không quản lý hạn dùng (`manage_by_lot = FALSE`) với `exp_date` NULL | Positive | Phiếu nhập chi tiết mặt hàng cơ khí `YAM-ENG-001` (`manage_by_lot = FALSE`) | • `lot_number`: để trống (hệ thống tự sinh)<br>• `exp_date`: `NULL`<br>• `location_id`: 3 (`RAW-A01-01`) | 1. Chọn sản phẩm `YAM-ENG-001`<br>2. Bỏ trống mã lô và HSD<br>3. Chọn ô kệ `RAW-A01-01`<br>4. Bấm "Xác nhận dòng hàng" | Hệ thống tự động sinh mã lô định danh (tiền tố `LOT-`), chấp nhận `exp_date` NULL, lưu chi tiết phiếu thành công và xuất kho theo FIFO (`created_at ASC`). | **PASS** |
| `TC-FR02.02-N1` | Chặn nhập hàng có Hạn sử dụng nhỏ hơn hoặc bằng ngày hiện tại (`BR-LOT-01`) | Negative | Đang lập chi tiết phiếu nhập hàng | • `lot_number`: `LOT-EXPIRED`<br>• `exp_date`: `2026-10-01` (Đã hết hạn so với 17/11/2026) | 1. Nhập ngày HSD cũ trong quá khứ<br>2. Bấm "Xác nhận dòng hàng" | Báo lỗi `400 Bad Request: Hạn sử dụng phải lớn hơn ngày hiện tại; không tiếp nhận hàng hết hạn vào kho`. | **PASS** |
| `TC-FR02.02-N2` | Chặn hoàn tất dòng nhập khi không chỉ định vị trí ô kệ lưu kho (`BR-CAT-02`) | Negative | Đang lập chi tiết phiếu nhập hàng | • `lot_number`: `LOT-NOLOC`<br>• `exp_date`: `2028-01-01`<br>• `location_id`: `NULL` | 1. Bỏ trống trường chọn Vị trí lưu kho<br>2. Bấm "Xác nhận dòng hàng" | Báo lỗi `400 Bad Request: Bắt buộc chỉ định vị trí ô kệ lưu kho cụ thể theo chuẩn 5S Seiton`. | **PASS** |

#### FR02.03: Thẩm định và Phê duyệt Nhập kho Maker-Checker (`BR-SEC-01`, `BR-SEC-02`, `BR-SEC-03`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR02.03-P` | Quản lý kho (Checker) duyệt phiếu nhập thành công (Cập nhật tồn kho & Thẻ kho) | Positive | Phiếu nhập ID: 1 do `staff_thien` (Maker) tạo; Đăng nhập bằng tài khoản `manager_khang` (Checker); Ô kệ `RAW-A01-01` đang trống | • `receipt_id`: 1<br>• `currentUserId`: 3 (`manager_khang`)<br>• `decision`: `APPROVE` | 1. Mở màn hình `/receipt/approve`<br>2. Xem xét chi tiết phiếu nhập ID: 1<br>3. Bấm nút "Phê duyệt nhập kho" | Giao dịch toàn vẹn `@Transactional` hoàn tất: (1) Trạng thái phiếu thành `APPROVED`; (2) Tồn On-hand tăng đúng số lượng; (3) Ô kệ đổi sang `OCCUPIED`; (4) Tự động sinh dòng ghi Sổ Thẻ kho; (5) Khóa chứng từ cấm sửa xóa. | **PASS** |
| `TC-FR02.03-N1` | Chặn nguyên tắc Maker-Checker: Người lập phiếu không được tự phê duyệt (`BR-SEC-03`) | Negative | Phiếu nhập ID: 1 do `staff_thien` tạo (hoặc phiếu do Quản lý `manager_khang` tự tạo đóng vai Maker) | • `receipt_id`: 1<br>• `currentUserId`: 4 (`staff_thien` - Maker gọi lệnh duyệt) hoặc `currentUserId`: 3 (`manager_khang` tự duyệt phiếu do mình lập) | **Kịch bản 1 (Kiểm soát Giao diện Web):**<br>1. Đăng nhập tài khoản Maker<br>2. Giao diện Web ẩn hoàn toàn nút Duyệt (đối với Staff) hoặc hiển thị cảnh báo đỏ và khóa nút Duyệt trên chứng từ do chính mình lập (đối với Manager).<br>**Kịch bản 2 (Chốt chặn Backend API Security):**<br>1. Người lập cố tình gửi trực tiếp lệnh duyệt ngầm qua API `POST /receipt/api/notes/1/approve`<br>2. Backend kiểm tra điều kiện `maker_id == checker_id` | Hệ thống nhận diện xung đột lợi ích Maker trùng Checker; Ném ngoại lệ `SecurityViolationException: Người lập phiếu không được phép tự phê duyệt phiếu nhập kho do chính mình tạo`; Trả về mã lỗi `403 Forbidden`. | **PASS** |
| `TC-FR02.03-N2` | Chặn phê duyệt phiếu nhập đã được phê duyệt trước đó | Negative | Phiếu nhập ID: 1 đã có trạng thái `APPROVED` | • `receipt_id`: 1<br>• Trạng thái hiện tại: `APPROVED` | 1. Quản lý kho bấm nút phê duyệt lần thứ 2 trên phiếu đã duyệt | Hệ thống báo lỗi `409 Conflict: Phiếu nhập kho đã được phê duyệt hoặc không ở trạng thái chờ duyệt`. | **PASS** |

---

### 3.3. PHÂN HỆ FR03: QUẢN LÝ XUẤT KHO, CƠ CHẾ GIỮ CHỖ & GỢI Ý FEFO/FIFO (`SCR05`)

#### FR03.01: Tạo yêu cầu xuất kho và Tự động tạm giữ chỗ Reserved (`BR-OUT-01`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR03.01-P` | Tạo lệnh xuất kho thành công và kích hoạt giữ chỗ tự động | Positive | Mặt hàng `YAM-LUB-001` đang có On-hand = 100, Reserved = 0 $\rightarrow$ Available = 100; Đăng nhập `staff_quan` | • `order_number`: `ORD-OUT-001`<br>• `customer_name`: "Đại lý Yamaha Town"<br>• `product_id`: 1<br>• `requested_qty`: 40 | 1. Mở màn hình `/issue/create`<br>2. Nhập thông tin đơn hàng và số lượng xuất = 40<br>3. Bấm "Gửi yêu cầu xuất kho" | Lệnh xuất tạo thành công (`201 Created`); Trạng thái `PENDING_APPROVAL`; Tồn `reserved_qty` tăng từ 0 lên 40; Tồn `available_qty` giảm từ 100 xuống 60; Tồn thực tế `on_hand_qty` giữ nguyên 100. | **PASS** |
| `TC-FR03.01-N1` | Chặn tạo lệnh xuất khi số lượng yêu cầu vượt quá tồn khả dụng (`BR-OUT-01`) | Negative | Mặt hàng `YAM-LUB-001` đang có Available = 60 | • `order_number`: `ORD-OUT-002`<br>• `product_id`: 1<br>• `requested_qty`: 80 (Vượt quá 60) | 1. Nhập số lượng yêu cầu xuất = 80<br>2. Bấm "Gửi yêu cầu xuất kho" | Hệ thống từ chối tạo lệnh; Ném ngoại lệ `InsufficientStockException: Tồn kho khả dụng không đủ để xuất số lượng yêu cầu. Còn thiếu: 20`; Không thay đổi số lượng giữ chỗ. | **PASS** |
| `TC-FR03.01-N2` | Tự động giải phóng lượng giữ chỗ khi lệnh xuất bị hủy bỏ | Positive | Lệnh xuất ID: 10 đang tạm giữ 40 chai nhớt ở trạng thái `PENDING_APPROVAL` | • `issue_id`: 10<br>• Thao tác: Hủy lệnh xuất | 1. Quản lý hoặc nhân viên bấm "Hủy lệnh xuất"<br>2. Nhập lý do hủy | Lệnh xuất chuyển trạng thái `CANCELLED`; Lượng giữ chỗ `reserved_qty` tự động giảm 40; Tồn `available_qty` tự động hoàn trả tăng lại 40. | **PASS** |

#### FR03.02: Thuật toán gợi ý xuất kho thông minh theo FEFO / FIFO (`BR-OUT-02`, `BR-OUT-03`, `BR-OUT-06`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR03.02-P` | Thuật toán FEFO tự động phân bổ chính xác lô có hạn dùng gần nhất ra trước | Positive | Mặt hàng `YAM-LUB-001` có 2 lô khả dụng: Lô B1 (HSD: `15/01/2027`, Tồn: 30), Lô B2 (HSD: `20/06/2027`, Tồn: 50) | • `product_id`: 1<br>• `requested_qty`: 45<br>• `strategy`: `FEFO` | 1. Gọi API hoặc bấm nút "Gợi ý phân bổ FEFO" tại màn hình `/issue/create` với số lượng 45 | Hệ thống trả về bảng Pick List gồm 2 dòng chính xác theo thứ tự FEFO: (1) Bốc toàn bộ 30 chai từ Lô B1 (Hạn gần); (2) Bốc tiếp 15 chai từ Lô B2 (Hạn xa hơn); Hiển thị rõ mã vị trí ô kệ cho từng lô. | **PASS** |
| `TC-FR03.02-N1` | Khóa chặn triệt để thuật toán không gợi ý lô hàng đã quá hạn sử dụng (`BR-OUT-03`) | Negative | Trong kho tồn tại Lô B0 có HSD `01/10/2026` (Đã hết hạn so với hiện tại) | • `product_id`: 1<br>• Lô B0 đã quá hạn | 1. Chạy thuật toán gợi ý xuất kho FEFO | Hệ thống tự động lọc bỏ Lô B0 (`exp_date <= current_date`); Tuyệt đối không xuất hiện Lô B0 trong danh sách Pick List đề xuất. | **PASS** |
| `TC-FR03.02-N2` | Bắt buộc nhập lý do ngoại lệ khi thủ kho bốc sai thứ tự lô gợi ý | Negative | Thủ kho không lấy Lô B1 theo gợi ý FEFO mà chọn lấy Lô B2 | • `selected_batch`: Lô B2<br>• `override_reason`: "" (Để trống) | 1. Chọn thủ công đổi sang lô khác<br>2. Bỏ trống lý do<br>3. Bấm xác nhận | Hệ thống cảnh báo đỏ và chặn không cho hoàn tất: "Bắt buộc nhập lý do ngoại lệ khi thay đổi lô xuất khác với chỉ dẫn FEFO". | **PASS** |

#### FR03.03: Phê duyệt và Xác nhận xuất kho thực tế (Trừ tồn kho & Ghi Thẻ kho - `BR-SEC-01`, `BR-SEC-02`, `BR-SEC-03`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR03.03-P` | Quản lý kho (Checker) duyệt xuất và trừ tồn thực tế thành công | Positive | Lệnh xuất ID: 2 do `staff_quan` lập (xuất 40 chai nhớt); Đăng nhập `manager_khang` (Checker); Ô kệ xuất hết hàng | • `issue_id`: 2<br>• `currentUserId`: 3 (`manager_khang`) | 1. Mở màn hình `/issue/create` (hoặc `/issue/pick`)<br>2. Bấm "Phê duyệt & Xuất kho" | Giao dịch toàn vẹn hoàn tất: (1) Trạng thái lệnh thành `APPROVED_READY_TO_PICK` / `COMPLETED`; (2) Tồn On-hand giảm đúng 40; (3) Giải phóng lượng giữ chỗ Reserved giảm 40; (4) Ô kệ chuyển về `EMPTY` nếu số dư bằng 0; (5) Tự động ghi dòng xuất vào Thẻ kho; (6) Khóa chứng từ. | **PASS** |
| `TC-FR03.03-N1` | Chặn người lập lệnh xuất tự phê duyệt xuất kho (`BR-SEC-03`) | Negative | Lệnh xuất do `staff_quan` tạo; Đang dùng tài khoản `staff_quan` gửi lệnh duyệt | • `issue_id`: 2<br>• `currentUserId`: 5 (`staff_quan` - Maker trùng Checker) | 1. Gửi lệnh duyệt xuất kho bằng chính tài khoản người lập | Hệ thống ném ngoại lệ `SecurityViolationException: Người lập phiếu không được phép tự phê duyệt phiếu xuất kho`; Báo lỗi `403 Forbidden`. | **PASS** |

---

### 3.4. PHÂN HỆ FR04: TRA CỨU TỒN KHO KHẢ DỤNG, THẺ KHO & CẢNH BÁO DỰ TRỮ (`SCR02`, `SCR06`)

#### FR04.01: Tra cứu tồn kho đa chiều thời gian thực (`BR-OUT-01`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR04.01-P` | Tra cứu tồn kho thời gian thực hiển thị chuẩn xác công thức $\text{Available} = \text{On-hand} - \text{Reserved}$ | Positive | Hệ thống có sẵn 6 mặt hàng; Mặt hàng `YAM-LUB-001` đang có On-hand = 100, Reserved = 40 | • URL: `/inventory/api/available-stock` | 1. Truy cập màn hình Sổ thẻ kho / Tổng quan kho<br>2. Xem dòng mặt hàng `YAM-LUB-001` | Màn hình hiển thị chính xác: On-hand = 100, Reserved = 40, Khả dụng Available = 60; Không có độ trễ số liệu. | **PASS** |
| `TC-FR04.01-N1` | Kiểm tra phản hồi khi tra cứu mã SKU không tồn tại trong CSDL | Negative | Mã SKU `YAM-FAKE-999` không tồn tại | • Từ khóa: `YAM-FAKE-999` | 1. Nhập từ khóa `YAM-FAKE-999` vào ô tìm kiếm danh mục tồn kho | Bảng trả về danh sách rỗng (0 bản ghi) kèm thông báo "Không tìm thấy mặt hàng phù hợp"; Hệ thống không bị crash. | **PASS** |

#### FR04.02: Truy xuất Thẻ kho điện tử Audit Trail (`BR-SEC-02`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR04.02-P` | Truy xuất Sổ Thẻ kho điện tử liên tục theo trật tự thời gian | Positive | Mặt hàng `YAM-LUB-001` (ID: 1) đã phát sinh giao dịch Nhập ban đầu (+100) và Xuất (-40) | • `productId`: 1<br>• `fromDate`: Đầu tháng hiện tại<br>• `toDate`: Ngày hiện tại | 1. Truy cập màn hình `/inventory/stock-card`<br>2. Chọn mặt hàng `YAM-LUB-001` và bấm "Tra cứu" | Bảng thẻ kho hiển thị đầy đủ các dòng: Ngày giờ giao dịch, Loại giao dịch (`RECEIPT`, `ISSUE`), Mã chứng từ tham chiếu (`RN-...`, `IN-...`), Số lượng thay đổi, Tồn trước và Tồn sau biến động; Tồn cuối khớp $100\%$ với số dư thực tế. | **PASS** |
| `TC-FR04.02-N1` | Chặn tra cứu thẻ kho khi khoảng thời gian không hợp lệ (`from_date > to_date`) | Negative | Form tra cứu thẻ kho đang mở | • `productId`: 1<br>• `fromDate`: `2026-12-01`<br>• `toDate`: `2026-11-01` (Vi phạm logic) | 1. Nhập ngày bắt đầu lớn hơn ngày kết thúc<br>2. Bấm "Tra cứu" | Hệ thống báo lỗi `400 Bad Request: Khoảng thời gian tra cứu không hợp lệ (Từ ngày phải nhỏ hơn hoặc bằng Đến ngày)`. | **PASS** |
| `TC-FR04.02-N2` | Kiểm tra tính bất biến của Thẻ kho điện tử (Không có nút sửa/xóa - Append-only) | Positive | Người dùng đăng nhập quyền Quản trị viên cao nhất (`ROLE_ADMIN`) | • Giao diện màn hình Thẻ kho | 1. Mở màn hình Thẻ kho điện tử `/inventory/stock-card`<br>2. Kiểm tra các phần tử giao diện | Hệ thống **tuyệt đối không cung cấp** bất kỳ nút bấm "Sửa" (Edit) hay "Xóa" (Delete) dòng thẻ kho; Đảm bảo tính toàn vẹn kiểm toán và bất biến chứng từ (`BR-SEC-01`, `BR-SEC-02`, `NFR-SEC-03`). | **PASS** |

#### FR04.03: Cảnh báo tồn an toàn và Đề xuất đặt hàng bổ sung (`BR-INV-01`, `BR-INV-02`, `BR-INV-03`)
| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR04.03-P1` | Tự động phát cảnh báo Thiếu hàng (Badge Đỏ) khi tồn khả dụng dưới Min Stock (`BR-INV-02`) | Positive | Sản phẩm `YAM-ENG-001` có Min Stock = 20; Tồn khả dụng hiện tại = 0 ($< 20$) | • URL: `/inventory/alerts` | 1. Truy cập màn hình Cảnh báo tồn kho `/inventory/alerts`<br>2. Xem danh sách "Cảnh báo thiếu hụt dưới mức Min Stock" | Sản phẩm `YAM-ENG-001` xuất hiện nổi bật với huy hiệu đỏ "DƯỚI ĐỊNH MỨC AN TOÀN" kèm số tồn hiện tại và ngưỡng Min yêu cầu. | **PASS** |
| `TC-FR04.03-P2` | Tự động phát cảnh báo Lô hàng cận hạn trong vòng 30 ngày (Badge Vàng) (`BR-INV-03`) | Positive | Trong kho có Lô `LOT-EXP-NEAR` của bugi `YAM-ELE-001` có HSD còn lại 12 ngày so với ngày hiện tại | • Ngưỡng quét: `thresholdDays = 30` | 1. Mở màn hình Cảnh báo tồn kho `/inventory/alerts`<br>2. Xem bảng "Lô hàng sắp hết hạn" | Lô `LOT-EXP-NEAR` xuất hiện với mức độ cảnh báo nguy cấp `CRITICAL` (Màu vàng cam), hiển thị chính xác số ngày còn lại là 12 ngày và vị trí ô kệ đang chứa. | **PASS** |
| `TC-FR04.03-P3` | Tự động tính toán số lượng đề xuất đặt hàng bù trừ chuẩn xác (`BR-INV-01`) | Positive | Sản phẩm `YAM-LUB-001` có: Max Stock = 500, Tồn khả dụng = 60, Đang trên đường về (On-order) = 100 | • Công thức: $\max(0, 500 - (60 + 100)) = 340$ | 1. Mở bảng đề xuất đặt hàng bổ sung | Hệ thống tính toán và hiển thị chính xác cột "Số lượng đề xuất đặt mua" là **340 Chai**, ngăn chặn nguy cơ đặt trùng đơn hàng. | **PASS** |

---

### 3.5. PHÂN HỆ KIỂM THỬ TÍCH HỢP: TÍNH TOÀN VẸN GIAO DỊCH & CƠ CHẾ HOÀN TÁC (TRANSACTION ROLLBACK - ACID - `NFR-REL-02`)

*Mục đích:* Kiểm thử tích hợp (Integration Testing) có nhiệm vụ phát hiện sự tương tác trạng thái bất thường (*unexpected state interaction*) và các ngoại lệ thời gian chạy (*run-time exceptions*). Trong hệ thống SmartWMS, các nghiệp vụ cốt lõi như Phê duyệt nhập kho (`FR02.03`) và Phê duyệt xuất kho (`FR03.03`) là các chuỗi thao tác liên chuỗi (Multi-table Pipeline) đòi hỏi tính toàn vẹn tuyệt đối (**Chuẩn ACID Transaction theo `NFR-REL-02`**) thông qua cơ chế `@Transactional`. Nếu một bước trong chuỗi nghiệp vụ thất bại, toàn bộ giao dịch phải được tự động hoàn tác (Rollback) về trạng thái xuất phát, không để lại bất kỳ dữ liệu dở dang, bản ghi mất liên kết hay số liệu tồn kho sai lệch.

| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-INT-TX.01` | Thẩm định tính toàn vẹn giao dịch và cơ chế hoàn tác (Rollback) khi Phê duyệt nhập kho gặp lỗi ghi Thẻ kho (`@Transactional Rollback` - `NFR-REL-02`) | Integration / Exception | Ứng dụng Spring Boot đang vận hành tại `http://localhost:8088`; Phiếu nhập ID: 1 đang ở trạng thái `PENDING_APPROVAL`; Ô kệ `RAW-A01-01` đang `EMPTY`; Tồn On-hand hiện tại của SKU `YAM-LUB-001` là 100 chai. Kích hoạt môi trường mô phỏng lỗi ngắt kết nối hoặc ngoại lệ runtime (`DataAccessException` / `TransactionSystemException`) tại bước ghi bản ghi Thẻ kho (`stock_card`). | • `receipt_id`: 1<br>• `currentUserId`: 3 (`manager_khang`)<br>• Lỗi mô phỏng: Thất bại tại bước ghi Sổ Thẻ kho | 1. Đăng nhập tài khoản Quản lý kho `manager_khang`<br>2. Mở màn hình `/receipt/approve`<br>3. Bấm nút "Phê duyệt nhập kho"<br>4. Hệ thống tiến hành cập nhật trạng thái phiếu $\rightarrow$ tăng tồn kho On-hand $\rightarrow$ đổi trạng thái ô kệ $\rightarrow$ gặp lỗi ngoại lệ khi ghi thẻ kho | Giao dịch được bảo toàn tính toàn vẹn thông qua cơ chế hoàn tác Rollback $100\%$:<br>(1) Trạng thái phiếu nhập giữ nguyên `PENDING_APPROVAL` (không chuyển thành `APPROVED`);<br>(2) Tồn On-hand của `YAM-LUB-001` giữ nguyên 100 chai (không bị tăng ảo lên 200);<br>(3) Trạng thái ô kệ `RAW-A01-01` giữ nguyên `EMPTY`;<br>(4) Không phát sinh dòng ghi thẻ kho dang dở;<br>(5) Hệ thống ném ngoại lệ kiểm soát được, ghi log kiểm toán và báo lỗi `500 Internal Server Error: Lỗi đồng bộ thẻ kho, giao dịch nhập kho đã được hoàn tác an toàn`. | **PASS** |
| `TC-INT-TX.02` | Thẩm định tính toàn vẹn giao dịch và cơ chế hoàn tác (Rollback) khi Phê duyệt xuất kho gặp ngoại lệ cập nhật tồn lô hàng (`NFR-REL-02`) | Integration / Exception | Ứng dụng Spring Boot đang vận hành tại `http://localhost:8088`; Lệnh xuất ID: 2 đang ở trạng thái `PENDING_APPROVAL` với lượng giữ chỗ `reserved_qty = 40`; Tồn On-hand là 100. Kích hoạt ngoại lệ khóa bản ghi hoặc xung đột truy cập đồng thời (`OptimisticLockingFailureException`) tại bước trừ số lượng trong bảng `stock_batch`. | • `issue_id`: 2<br>• `currentUserId`: 3 (`manager_khang`)<br>• Lỗi mô phỏng: Xung đột khóa dòng cập nhật lô hàng | 1. Quản lý kho bấm "Phê duyệt & Xuất kho" tại màn hình `/issue/create`<br>2. Hệ thống bắt đầu trừ tồn On-hand $\rightarrow$ giải phóng Reserved $\rightarrow$ phát sinh ngoại lệ khóa dòng tại bước trừ tồn chi tiết lô | Cơ chế `@Transactional` tự động hoàn tác (Rollback) toàn bộ dữ liệu về trạng thái trước khi bấm duyệt:<br>(1) Lệnh xuất giữ nguyên trạng thái `PENDING_APPROVAL` (không chuyển sang `COMPLETED`);<br>(2) Tồn On-hand giữ nguyên 100;<br>(3) Lượng tạm giữ `reserved_qty` giữ nguyên 40 chai (không bị giải phóng nhầm);<br>(4) Tuyệt đối không sinh dòng xuất ảo trong Sổ Thẻ kho;<br>(5) Báo lỗi an toàn: `409 Conflict: Dữ liệu lô hàng đang bị khóa bởi tiến trình khác, vui lòng thử lại`. | **PASS** |

---

### 3.6. PHÂN HỆ KIỂM THỬ HIỆU NĂNG & THỜI GIAN PHẢN HỒI (USABILITY & PERFORMANCE TESTING - `NFR-PER-01`, `NFR-PER-02`)

*Mục đích:* Đo lường các thông số hiệu năng bao gồm:
- **Response Time (Thời gian phản hồi):** Giới hạn thời gian tối đa để người dùng nhận được phản hồi từ hệ thống phần mềm.
- **Throughput (Thông lượng xử lý):** Số lượng giao dịch nghiệp vụ mà hệ thống hoàn thành trong một đơn vị thời gian.
Theo đặc tả Yêu cầu Phi chức năng:
- **`NFR-PER-02`:** Thời gian thực thi các giao dịch có tính toán nghiệp vụ (Duyệt phiếu xuất, thuật toán phân bổ FEFO/FIFO) cam kết **$< 1.5\text{ giây}$ / giao dịch**.
- **`NFR-PER-01`:** Thời gian tải trang giao diện (Page Load Time) cam kết **$< 2.0\text{ giây}$ cho trang thông thường** và **$< 5.0\text{ giây}$ cho trang Báo cáo tồn lớn**.

| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-NFR-PER.01` | Đo lường thời gian đáp ứng của Thuật toán gợi ý phân bổ lô FEFO trên tập dữ liệu lớn (Response Time $< 1.5$ giây theo `NFR-PER-02`) | Performance Benchmark | Ứng dụng Spring Boot đang chạy tại `http://localhost:8088`; Bảng `stock_batch` được nạp tập dữ liệu kiểm thử tải gồm 1.000 lô hàng với các hạn sử dụng khác nhau của 50 mã SKU; Có chỉ mục Index `idx_batch_product_exp` trên cặp `(product_id, exp_date)`. | • `requested_qty`: 500 phụ tùng<br>• Danh mục: 5 SKU khác nhau thuộc ngành `ENG`<br>• `strategy`: `FEFO` | 1. Đăng nhập tài khoản Quản lý `manager_khang`<br>2. Mở màn hình Lập lệnh xuất kho `/issue/create`<br>3. Nhập đơn hàng xuất 5 SKU với tổng 500 chiếc<br>4. Bấm "Gợi ý phân bổ FEFO" và kích hoạt bộ đo thời gian phản hồi Backend (`System.currentTimeMillis()`) | Thuật toán quét và sắp xếp danh sách lô hàng theo FEFO hoàn tất với thông số đo lường:<br>$$\mathbf{\text{Response Time}} \le \mathbf{1.20\text{ giây}} \quad (\text{Thỏa mãn cam kết } < 1.5\text{ giây theo } \mathbf{NFR-PER-02})$$<br>• Bảng Pick List hiển thị đầy đủ, chính xác tuyệt đối theo thứ tự hạn dùng gần nhất xuất trước;<br>• Trạng thái phản hồi HTTP `200 OK`, không xảy ra hiện tượng tràn bộ nhớ hay nghẽn CPU. | **PASS** |
| `TC-NFR-PER.02` | Đo lường thời gian truy xuất và kết xuất Sổ Thẻ kho điện tử với tập dữ liệu lịch sử lớn (Response Time $< 2.0$ giây theo `NFR-PER-01`) | Performance Benchmark | Ứng dụng Spring Boot đang chạy tại `http://localhost:8088`; Bảng `stock_card` được nạp tập dữ liệu kiểm thử lịch sử trên 10.000 bản ghi giao dịch; Có thiết lập chỉ mục Index `idx_card_product_date` trên cặp khóa `(product_id, transaction_date)`. | • `productId`: 1 (`YAM-LUB-001`)<br>• Phạm vi: Tra cứu biến động trong 6 tháng (khoảng 1.500 dòng giao dịch) | 1. Truy cập màn hình Sổ Thẻ kho điện tử `/inventory/stock-card`<br>2. Chọn mã hàng `YAM-LUB-001`, chọn khoảng thời gian từ ngày đầu quý đến hiện tại<br>3. Bấm "Tra cứu lịch sử thẻ kho"<br>4. Đo thời gian từ lúc bấm gửi yêu cầu đến khi bảng dữ liệu render hoàn tất trên trình duyệt | Hệ thống truy vấn cơ sở dữ liệu và render giao diện với thông số đo lường:<br>$$\mathbf{\text{Response Time}} \le \mathbf{0.85\text{ giây}} \quad (\text{Thỏa mãn cam kết } < 2.0\text{ giây theo } \mathbf{NFR-PER-01})$$<br>• Bảng thẻ kho hiển thị tức thì, phân trang mượt mà 50 dòng/trang;<br>• Dữ liệu toàn vẹn, khớp số dư lũy kế, không bị gián đoạn trải nghiệm người dùng. | **PASS** |
