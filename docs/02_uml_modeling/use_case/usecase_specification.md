# TRƯỜNG ĐẠI HỌC TÔN ĐỨC THẮNG
## KHOA CÔNG NGHỆ THÔNG TIN
### BỘ MÔN CÔNG NGHỆ PHẦN MỀM

---

# TÀI LIỆU ĐẶC TẢ CHI TIẾT USE CASE (USE CASE SPECIFICATIONS)
### Đề tài: Hệ thống Quản lý Kho Hàng Thông Minh (SmartWMS)
*Học phần: Phân tích và Thiết kế Yêu cầu (Mã môn: 502050)*  


---

## 1. TỔNG QUAN DANH MỤC 7 USE CASE CỐT LÕI (CORE USE CASES)

Tài liệu này đặc tả chi tiết 7 Ca sử dụng cốt lõi (Core Use Cases - `UC01` đến `UC07`) thuộc phạm vi bắt buộc cài đặt và kiểm thử Java Demo (**Must-have**). Danh mục 7 Use Case này được trích xuất trực tiếp từ 3 sơ đồ Use Case phân hệ:
1. **Sơ đồ Nhập - Xuất kho:** [TQ1.jpg (Nhập - Xuất kho)](Usecase_TongQuat/TQ1.jpg)
2. **Sơ đồ Quản lý & Thẩm định (Maker-Checker):** [TQ2.jpg (Quản trị & Phê duyệt)](Usecase_TongQuat/TQ2.jpg)
3. **Sơ đồ Cảnh báo & Báo cáo tồn kho:** [TQ3.jpg (Cảnh báo & Báo cáo)](Usecase_TongQuat/TQ3.jpg)

---

### Bảng 1.1: Danh mục 7 Use Case cốt lõi

| Mã UC | Tên Use Case | Tác nhân chính | Tác nhân phụ | Nhóm FR liên ứng | Quy tắc nghiệp vụ (BR) chi phối | Sơ đồ Use Case nguồn |
|:---:|:---|:---|:---|:---:|:---|:---:|
| **UC01** | Quản lý danh mục kho (Manage Warehouse Catalog) | Quản lý kho | Không có | `FR01.01`<br>`FR01.02`<br>`FR01.03` | `BR-CAT-01`, `BR-CAT-02`, `BR-CAT-03`, `BR-CAT-04`, `BR-CAT-05` | [TQ2.jpg](Usecase_TongQuat/TQ2.jpg) |
| **UC02** | Lập phiếu nhập kho (Create Goods Receipt Note) | Nhân viên kho | Nhân viên Mua hàng, Nhà cung cấp | `FR02.01`<br>`FR02.02` | `BR-LOT-01`, `BR-INB-01`, `BR-INB-02`, `BR-INB-03`, `BR-INB-04`, `BR-SEC-02` | [TQ1.jpg](Usecase_TongQuat/TQ1.jpg) |
| **UC03** | Phê duyệt phiếu nhập kho (Approve Goods Receipt Note) | Quản lý kho | Không có | `FR02.03` | `BR-CAT-03`, `BR-INB-02`, `BR-SEC-01`, `BR-SEC-02`, `BR-SEC-03` | [TQ2.jpg](Usecase_TongQuat/TQ2.jpg) |
| **UC04** | Lập phiếu xuất kho (Create Goods Issue Note) | Nhân viên kho | BP. Sản xuất / Bán hàng | `FR03.01`<br>`FR03.02` | `BR-OUT-01`, `BR-OUT-02`, `BR-OUT-03`, `BR-OUT-06` | [TQ1.jpg](Usecase_TongQuat/TQ1.jpg) |
| **UC05** | Phê duyệt phiếu xuất kho (Approve Goods Issue Note) | Quản lý kho, Nhân viên kho | BP. Sản xuất / Bán hàng | `FR03.03` | `BR-CAT-03`, `BR-OUT-01`, `BR-OUT-04`, `BR-OUT-06`, `BR-SEC-01`, `BR-SEC-02`, `BR-SEC-03` | [TQ2.jpg](Usecase_TongQuat/TQ2.jpg)<br>[TQ1.jpg](Usecase_TongQuat/TQ1.jpg) |
| **UC06** | Tra cứu tồn kho (Query Inventory) | Nhân viên kho, Quản lý kho | Kế toán kho & Ban Giám đốc | `FR04.01`<br>`FR04.02` | `BR-CAT-03`, `BR-LOT-01`, `BR-SEC-02` | [TQ3.jpg](Usecase_TongQuat/TQ3.jpg) |
| **UC07** | Xem cảnh báo tồn kho (View Inventory Alerts) | Nhân viên Mua hàng, Quản lý kho | Bộ định thời (Scheduler) | `FR04.03` | `BR-CAT-05`, `BR-INV-01`, `BR-INV-02`, `BR-INV-03` | [TQ3.jpg](Usecase_TongQuat/TQ3.jpg) |

---

## 2. ĐẶC TẢ CHI TIẾT 7 USE CASE CỐT LÕI (MUST-HAVE)

---

### UC01: QUẢN LÝ DANH MỤC KHO (MANAGE WAREHOUSE CATALOG)

#### 1. Bảng Thông tin Metadata Use Case
| Thuộc tính | Chi tiết đặc tả |
| :--- | :--- |
| **Mã Use Case** | `UC01` |
| **Tên Use Case** | Quản lý danh mục kho (Manage Warehouse Catalog) |
| **Tác nhân chính** | Quản lý kho |
| **Tác nhân phụ** | Không có |
| **Mô tả tóm tắt (Brief Description)** | Cho phép Quản lý kho thêm mới các mặt hàng, cài đặt mức tồn kho tối thiểu - tối đa và thiết lập sơ đồ vị trí các ô kệ trong kho để phục vụ việc sắp xếp, quản lý hàng hóa. |
| **Điều kiện tiên quyết (Pre-conditions)** | 1. Quản lý kho đã đăng nhập vào hệ thống với tài khoản quản lý.<br>2. Sơ đồ phân vùng kho trên thực địa (khu vực tiếp nhận, kiểm tra, chứa hàng, hàng phế phẩm...) đã được định hình sẵn. |
| **Điều kiện được thiết lập (Post-conditions)** | 1. Mặt hàng mới được tạo thành công kèm theo định mức tồn an toàn (tối thiểu - tối đa).<br>2. Các vị trí ô kệ mới được bổ sung vào sơ đồ kho ở trạng thái trống, sẵn sàng để xếp hàng. |
| **Quy tắc nghiệp vụ chi phối** | • [`BR-CAT-01`](../../01_srs/business_rules.md)<br>• [`BR-CAT-02`](../../01_srs/business_rules.md)<br>• [`BR-CAT-03`](../../01_srs/business_rules.md)<br>• [`BR-CAT-04`](../../01_srs/business_rules.md)<br>• [`BR-CAT-05`](../../01_srs/business_rules.md) |
| **Yêu cầu chức năng liên ứng** | • [`FR01.01`](../../01_srs/functional_requirements.md)<br>• [`FR01.02`](../../01_srs/functional_requirements.md)<br>• [`FR01.03`](../../01_srs/functional_requirements.md) |

#### 2. Bảng Luồng sự kiện chính (Basic Flow / Main Flow)
| Bước | Hành động của Tác nhân | Phản hồi của Hệ thống |
|:---:|:---|:---|
| 1 | Quản lý kho chọn phân hệ **"Quản lý danh mục"** trên thanh điều hướng và chọn **"Danh mục Mặt hàng"**. | Hệ thống hiển thị bảng danh sách các mặt hàng hiện hữu kèm nút chức năng **"Thêm mới SKU"** và bộ lọc tìm kiếm. |
| 2 | Quản lý kho bấm nút **"Thêm mới SKU"**. | Hệ thống hiển thị form nhập liệu mặt hàng mới gồm các trường: Mã SKU, Tên mặt hàng, Đơn vị tính cơ sở (ĐVT), Nhóm hàng, Cờ quản lý theo lô (`manage_by_lot`), Ngưỡng tồn tối thiểu (Min Stock), Ngưỡng tồn tối đa (Max Stock). |
| 3 | Quản lý kho nhập các thông tin mặt hàng và bấm **"Lưu dữ liệu"**. | Hệ thống thực hiện kiểm tra hợp lệ dữ liệu (Validation Rules):<br>1. Kiểm tra mã SKU là duy nhất trên toàn hệ thống (`BR-CAT-01`).<br>2. Kiểm tra chỉ có duy nhất 1 ĐVT cơ sở được gán (`BR-CAT-04`).<br>3. Kiểm tra logic định mức: $\text{Min Stock} \ge 0$ và $\text{Max Stock} > \text{Min Stock}$ (`BR-CAT-05`). |
| 4 | | Hệ thống lưu thông tin vào thực thể Mặt hàng (`Product`) với trạng thái `ACTIVE`, trả về mã `201 Created` và hiển thị thông báo "Khởi tạo mặt hàng mới thành công". |
| 5 | Quản lý kho chuyển sang tab **"Danh mục Vị trí kho"** và bấm **"Thêm mới Vị trí"**. | Hệ thống hiển thị form thiết lập vị trí ô kệ: Phân vùng (Zone - chọn từ danh mục: `DOCK`, `INSP`, `RAW`, `FG`, `SCRAP`), Kệ (Shelf), Ngăn/Ô (Bin), và Mã vị trí tổng hợp (`location_code`). |
| 6 | Quản lý kho nhập thông tin vị trí ô kệ và bấm **"Lưu vị trí"**. | Hệ thống kiểm tra tính duy nhất của mã `location_code` trên toàn bộ mặt bằng kho (`BR-CAT-02`). |
| 7 | | Hệ thống ghi nhận bản ghi mới vào thực thể Vị trí kho (`WarehouseLocation`) với trạng thái mặc định là Trống (`EMPTY`, `BR-CAT-03`), trả về mã `201 Created` và hiển thị vị trí mới trên sơ đồ trực quan. |

#### 3. Luồng phụ / Rẽ nhánh (Alternative Flows)
* **Luồng 2a – Cập nhật định mức Min/Max Stock cho SKU hiện hữu:**
  * Tại bước 1, Quản lý kho tìm kiếm một SKU đã có, bấm **"Cấu hình định mức"**.
  * Hệ thống mở modal chỉnh sửa Min Stock và Max Stock.
  * Quản lý kho cập nhật số liệu mới và bấm **"Xác nhận"**.
  * Hệ thống kiểm tra $\text{Min} \ge 0$ và $\text{Max} > \text{Min}$ (`BR-CAT-05`), cập nhật vào CSDL, phản hồi mã `200 OK` và kích hoạt tự động tính toán lại chỉ số cảnh báo tồn kho.
* **Luồng 5a – Import hàng loạt danh mục vị trí kho từ file mẫu Excel (Batch Putaway Setup):**
  * Tại bước 5, Quản lý kho bấm nút **"Import Excel"**.
  * Hệ thống cung cấp file mẫu (.xlsx) định dạng chuẩn Phân vùng 3M (`Zone - Shelf - Bin - LocationCode`).
  * Quản lý kho upload file dữ liệu vị trí đã điền.
  * Hệ thống đọc từng dòng, validate tính hợp lệ và tính duy nhất của từng mã vị trí (`BR-CAT-02`), khởi tạo đồng loạt trạng thái `EMPTY` (`BR-CAT-03`), và trả về báo cáo kết quả import (số dòng thành công, danh sách dòng lỗi nếu có).

#### 4. Luồng ngoại lệ (Exception Flows)
* **Luồng 3e1 – Trùng mã SKU hàng hóa (`409 Conflict`):**
  * Tại bước 3, mã SKU Quản lý kho nhập đã tồn tại trong CSDL.
  * Hệ thống phát hiện vi phạm quy tắc `BR-CAT-01`, ngăn chặn lưu dữ liệu, trả về mã phản hồi `409 Conflict` và hiển thị thông báo lỗi: *"Mã SKU [xxx] đã tồn tại trên hệ thống. Vui lòng kiểm tra lại danh mục."*
  * Hệ thống giữ nguyên dữ liệu đã nhập trên form để Quản lý kho sửa đổi.
* **Luồng 3e2 – Vi phạm logic định mức tồn kho an toàn (`400 Bad Request`):**
  * Tại bước 3, Quản lý kho nhập $\text{Min Stock} < 0$ hoặc $\text{Max Stock} \le \text{Min Stock}$.
  * Hệ thống phát hiện vi phạm `BR-CAT-05`, chặn ghi nhận và trả về mã lỗi `400 Bad Request` kèm thông báo: *"Định mức dự trữ không hợp lệ: Tồn tối đa (Max Stock) bắt buộc phải lớn hơn Tồn tối thiểu (Min Stock) và Min Stock ≥ 0."*
* **Luồng 6e1 – Trùng mã vị trí lưu kho (`409 Conflict`):**
  * Tại bước 6, mã `location_code` nhập vào đã được sử dụng tại một phân vùng khác.
  * Hệ thống chặn lưu, trả về mã `409 Conflict` và thông báo: *"Mã vị trí [xxx] đã tồn tại trong phân vùng kho theo chuẩn BR-CAT-02."*

---

### UC02: LẬP PHIẾU NHẬP KHO (CREATE GOODS RECEIPT NOTE)

#### 1. Bảng Thông tin Metadata Use Case
| Thuộc tính | Chi tiết đặc tả |
| :--- | :--- |
| **Mã Use Case** | `UC02` |
| **Tên Use Case** | Lập phiếu nhập kho (Create Goods Receipt Note) |
| **Tác nhân chính** | Nhân viên kho |
| **Tác nhân phụ** | Nhân viên Mua hàng, Nhà cung cấp |
| **Mô tả tóm tắt (Brief Description)** | Cho phép Nhân viên kho tiếp nhận hàng từ nhà cung cấp theo đơn mua hàng, kiểm tra số lượng và chất lượng thực tế, ghi nhận số lô, hạn dùng và xếp hàng vào từng vị trí ô kệ trước khi gửi quản lý duyệt. |
| **Điều kiện tiên quyết (Pre-conditions)** | 1. Nhân viên kho đã đăng nhập vào hệ thống.<br>2. Đơn mua hàng (PO) đã được phê duyệt từ trước và nhà cung cấp đã giao hàng đến cửa kho. |
| **Điều kiện được thiết lập (Post-conditions)** | 1. Một phiếu nhập kho mới được tạo ở trạng thái **Chờ duyệt** (chưa làm tăng số lượng hàng trong kho).<br>2. Thông tin số lô, hạn dùng và vị trí ô kệ dự kiến xếp hàng được lưu lại đầy đủ.<br>3. Thông báo chờ duyệt được tự động gửi đến Quản lý kho. |
| **Quy tắc nghiệp vụ chi phối** | • [`BR-LOT-01`](../../01_srs/business_rules.md)<br>• [`BR-INB-01`](../../01_srs/business_rules.md)<br>• [`BR-INB-02`](../../01_srs/business_rules.md)<br>• [`BR-INB-03`](../../01_srs/business_rules.md)<br>• [`BR-INB-04`](../../01_srs/business_rules.md)<br>• [`BR-SEC-02`](../../01_srs/business_rules.md) |
| **Yêu cầu chức năng liên ứng** | • [`FR02.01`](../../01_srs/functional_requirements.md)<br>• [`FR02.02`](../../01_srs/functional_requirements.md) |

#### 2. Bảng Luồng sự kiện chính (Basic Flow / Main Flow)
| Bước | Hành động của Tác nhân | Phản hồi của Hệ thống |
|:---:|:---|:---|
| 1 | Nhân viên kho chọn chức năng **"Quản lý Nhập kho"** và bấm **"Tạo phiếu nhập từ PO"**. | Hệ thống hiển thị danh sách các PO hợp lệ đang ở trạng thái `APPROVED` kèm công cụ tra cứu theo mã PO, Nhà cung cấp hoặc Ngày giao. |
| 2 | Nhân viên kho chọn mã PO tương ứng với chuyến hàng đang bàn giao. | Hệ thống trích xuất thông tin PO và hiển thị danh mục các dòng mặt hàng cần giao, bao gồm: Mã SKU, Tên hàng, ĐVT, Số lượng đặt mua, Số lượng đã giao lũy kế, và Số lượng còn lại chưa giao ($\text{Quantity Remaining}$). |
| 3 | Nhân viên kho tiến hành kiểm đếm số lượng thực tế tại khu vực kiểm tra (`INSP`) và nhập **Số lượng thực nhận đạt chuẩn** (`quantity_received`) cho từng dòng hàng. | Hệ thống kiểm tra: $\text{Quantity Received} \le \text{Quantity Remaining}$ theo quy định từ chối nhận hàng giao thừa (`BR-INB-01`). |
| 4 | Đối với từng dòng hàng có cờ quản lý theo hạn dùng (`manage_by_lot = TRUE`), Nhân viên kho nhập thông tin lô: Số lô (`lot_number`), Ngày sản xuất (`mfg_date` – tùy chọn), Hạn sử dụng (`exp_date`), và chọn một Vị trí cất hàng cụ thể (`location_id` thuộc phân vùng `RAW` hoặc `FG`). | Hệ thống kiểm tra các ràng buộc logic:<br>1. Số lô và Hạn dùng không được để trống (`BR-LOT-01`).<br>2. Hạn dùng phải lớn hơn ngày hiện tại ($\text{EXP} > \text{Current Date}$).<br>3. Vị trí cất hàng bắt buộc phải được chỉ định và hợp lệ (`BR-INB-03`). |
| 5 | Nhân viên kho kiểm tra lại toàn bộ thông tin đối chiếu và bấm **"Gửi phê duyệt phiếu nhập"**. | Hệ thống khởi tạo một giao dịch CSDL:<br>1. Lưu bản ghi Phiếu nhập kho (`ReceiptNote`) với trạng thái ban đầu là `PENDING_APPROVAL`.<br>2. Lưu chi tiết các dòng nhập vào `ReceiptNoteItem` kèm thông tin Lô và Vị trí tạm gán.<br>3. Ghi nhận `maker_id` là ID của Nhân viên kho đang đăng nhập và ghi nhận thời điểm tạo (`BR-SEC-02`).<br>4. Tuyệt đối **không** làm thay đổi số dư tồn kho thực tế (*On-hand*) của bất kỳ mặt hàng nào (`BR-INB-02`). |
| 6 | | Hệ thống phản hồi mã `201 Created`, cấp Mã số phiếu nhập kho (VD: `RN-20261104-001`), hiển thị thông báo lập phiếu thành công và đẩy thông báo chờ duyệt đến tài khoản Quản lý kho. |

#### 3. Luồng phụ / Rẽ nhánh (Alternative Flows)
* **Luồng 3a – Phát hiện hàng hư hỏng, rách bao bì, lỗi quy cách tại khu vực kiểm tra (Seiri):**
  * Tại bước 3, Nhân viên kho phát hiện một phần hàng hóa bị vỡ hỏng hoặc không đạt tiêu chuẩn tiếp nhận tại khu vực `INSP`.
  * Nhân viên kho nhập số lượng hàng hỏng vào ô **"Số lượng từ chối"** (`rejected_quantity`) và bắt buộc nhập **"Lý do từ chối"** (`rejection_reason` – VD: rách bao bì, cận hạn sử dụng, sai tem nhãn).
  * Hệ thống chỉ đưa phần số lượng đạt chuẩn (`quantity_received`) vào danh sách nhập kho. Phần hàng hỏng bị loại bỏ hoàn toàn khỏi phiếu nhập kho, hệ thống xuất Biên bản sự cố bàn giao giao nhận (`Rejection Report`) để bên giao vận nhận lại hàng lỗi (`BR-INB-04`). Lượng hàng này tuyệt đối không được ghi nhận vào kho.
* **Luồng 3b – Nhà cung cấp giao hàng làm nhiều đợt (Partial Delivery):**
  * Tại bước 3, số lượng giao đợt này nhỏ hơn số lượng còn lại của PO.
  * Nhân viên kho nhập đúng số lượng thực giao. Hệ thống ghi nhận số lượng này vào phiếu nhập, cập nhật số dư lũy kế đã giao của PO và giữ trạng thái PO là `PARTIALLY_RECEIVED` để tiếp tục đối chiếu cho các đợt giao tiếp theo (`BR-INB-01`).

#### 4. Luồng ngoại lệ (Exception Flows)
* **Luồng 3e1 – Số lượng thực nhận vượt quá số lượng còn lại trên PO (`400 Bad Request`):**
  * Tại bước 3, Nhân viên kho nhập số lượng nhận lớn hơn $\text{Quantity Remaining}$ của PO.
  * Hệ thống phát hiện vi phạm nguyên tắc từ chối hàng giao thừa (`BR-INB-01`), chặn lưu dữ liệu và trả về mã lỗi `400 Bad Request`: *"Số lượng thực nhận vượt quá số lượng còn lại trên Đơn mua hàng PO. Doanh nghiệp từ chối tiếp nhận phần giao thừa theo BR-INB-01."*
* **Luồng 4e1 – Hạn sử dụng của lô hàng không hợp lệ (`400 Bad Request`):**
  * Tại bước 4, Nhân viên kho nhập $\text{EXP Date} \le \text{Current Date}$.
  * Hệ thống phát hiện vi phạm `BR-LOT-01`, chặn lập phiếu và báo lỗi: *"Hạn sử dụng của lô hàng phải lớn hơn ngày hiện tại. Hệ thống từ chối tiếp nhận hàng hết hạn vào kho."*
* **Luồng 4e2 – Bỏ trống vị trí cất hàng (`400 Bad Request`):**
  * Tại bước 4, Nhân viên kho không chỉ định mã vị trí lưu kho cho dòng hàng.
  * Hệ thống chặn hành động theo nguyên tắc Seiton (`BR-INB-03`) và thông báo: *"Hàng hóa khi nhập kho bắt buộc phải được chỉ định một vị trí lưu trữ xác định."*

---

### UC03: PHÊ DUYỆT PHIẾU NHẬP KHO (APPROVE GOODS RECEIPT NOTE)

#### 1. Bảng Thông tin Metadata Use Case
| Thuộc tính | Chi tiết đặc tả |
| :--- | :--- |
| **Mã Use Case** | `UC03` |
| **Tên Use Case** | Phê duyệt phiếu nhập kho (Approve Goods Receipt Note) |
| **Tác nhân chính** | Quản lý kho |
| **Tác nhân phụ** | Không có |
| **Mô tả tóm tắt (Brief Description)** | Cho phép Quản lý kho kiểm tra lại thông tin phiếu nhập so với đơn mua hàng và biên bản giao nhận thực tế, sau đó quyết định duyệt hoặc từ chối để chính thức ghi nhận số lượng hàng nhập vào sổ kho. |
| **Điều kiện tiên quyết (Pre-conditions)** | 1. Quản lý kho đã đăng nhập vào hệ thống.<br>2. Có phiếu nhập kho đang ở trạng thái **Chờ duyệt** do nhân viên kho lập (người duyệt không phải là người đã lập phiếu). |
| **Điều kiện được thiết lập (Post-conditions)** | 1. Phiếu nhập chuyển sang trạng thái **Hoàn tất** và bị khóa vĩnh viễn (không thể sửa hoặc xóa).<br>2. Số lượng hàng thực tế trong kho chính thức tăng lên tương ứng.<br>3. Các ô kệ chứa hàng chuyển sang trạng thái đang có hàng.<br>4. Hệ thống tự động ghi sổ thẻ kho điện tử để lưu vết lịch sử nhập hàng. |
| **Quy tắc nghiệp vụ chi phối** | • [`BR-CAT-03`](../../01_srs/business_rules.md)<br>• [`BR-INB-02`](../../01_srs/business_rules.md)<br>• [`BR-SEC-01`](../../01_srs/business_rules.md)<br>• [`BR-SEC-02`](../../01_srs/business_rules.md)<br>• [`BR-SEC-03`](../../01_srs/business_rules.md) |
| **Yêu cầu chức năng liên ứng** | • [`FR02.03`](../../01_srs/functional_requirements.md) |

#### 2. Bảng Luồng sự kiện chính (Basic Flow / Main Flow)
| Bước | Hành động của Tác nhân | Phản hồi của Hệ thống |
|:---:|:---|:---|
| 1 | Quản lý kho truy cập danh mục **"Phê duyệt chứng từ"** và chọn tab **"Phiếu nhập chờ duyệt"**. | Hệ thống hiển thị danh sách các phiếu nhập đang có trạng thái `PENDING_APPROVAL` kèm thông tin tóm tắt: Mã phiếu, Ngày lập, Mã PO, Nhà cung cấp, Tên Nhân viên kho lập phiếu (Maker). |
| 2 | Quản lý kho bấm chọn một phiếu nhập cụ thể để thẩm định. | Hệ thống hiển thị màn hình chi tiết chứng từ đối chiếu 2 cột: Cột Đơn mua hàng (PO) và Cột Thực tế nhập kho (chi tiết từng SKU, số lượng nhận, số lượng từ chối, Số lô, Hạn dùng, Vị trí cất hàng chỉ định). |
| 3 | Quản lý kho kiểm tra tính hợp lệ về chứng từ, đối chiếu số liệu và nhấn nút **"Phê duyệt"** (Approve). | Hệ thống kích hoạt bước kiểm tra quyền hạn độc lập Maker-Checker: kiểm tra danh tính người duyệt hiện tại (`checker_id`) có trùng với người lập phiếu (`maker_id`) hay không (`BR-SEC-03`). |
| 4 | | Khi kiểm tra hợp lệ (`checker_id != maker_id`), hệ thống thực thi một Giao dịch nguyên tử (Atomic Database Transaction):<br>1. Cập nhật trạng thái phiếu nhập sang `COMPLETED`.<br>2. Tăng số lượng tồn kho thực tế (*On-hand*) tương ứng tại bảng `StockBatch` (theo SKU, Số lô, EXP, Vị trí).<br>3. Tự động chuyển trạng thái vị trí cất hàng sang `OCCUPIED` nếu trước đó là `EMPTY` (`BR-CAT-03`).<br>4. Ghi nhận 01 dòng bút toán vào Thẻ kho điện tử (`StockCard`): Số chứng từ, Loại giao dịch "NHẬP_PO", Số lượng tăng, Tồn sau giao dịch (`BR-SEC-02`).<br>5. Ghi nhận Audit Log: `checker_id`, thao tác `APPROVE_RECEIPT`, thời điểm hoàn tất.<br>6. Khóa vĩnh viễn phiếu nhập, cấm mọi hành vi chỉnh sửa hoặc xóa bỏ (`BR-SEC-01`).<br>7. Cập nhật số lượng đã nhận trên PO gốc; nếu PO đã giao đủ 100%, tự động chuyển trạng thái PO sang `CLOSED`. |
| 5 | | Hệ thống phản hồi mã `200 OK`, hiển thị thông báo "Phê duyệt phiếu nhập kho thành công. Số dư tồn kho đã được ghi sổ", và gửi thông báo hoàn tất đến Nhân viên kho lập phiếu. |

#### 3. Luồng phụ / Rẽ nhánh (Alternative Flows)
* **Luồng 3a – Quản lý kho từ chối phê duyệt phiếu nhập (Reject Receipt Note):**
  * Tại bước 3, Quản lý kho phát hiện sai lệch nghiêm trọng (sai quy cách, vị trí lưu kho không phù hợp hoặc nghi vấn chứng từ), nhấn nút **"Từ chối"** (Reject).
  * Hệ thống hiển thị hộp thoại bắt buộc nhập **"Lý do từ chối phê duyệt"** (`rejection_note`, tối thiểu 10 ký tự).
  * Quản lý kho nhập lý do và bấm xác nhận từ chối.
  * Hệ thống chuyển trạng thái phiếu nhập sang `REJECTED`, ghi log Checker và thời điểm từ chối. Số dư tồn kho On-hand tuyệt đối không thay đổi. Hệ thống gửi thông báo kèm lý do về tài khoản Nhân viên kho (Maker) để kiểm tra hiện trường hoặc hủy phiếu lập lại.

#### 4. Luồng ngoại lệ (Exception Flows)
* **Luồng 3e1 – Vi phạm nguyên tắc Maker-Checker / Tự phê duyệt chứng từ (`403 Forbidden`):**
  * Tại bước 3, tài khoản Quản lý kho đang đăng nhập chính là người đã tạo ra phiếu nhập đó (`checker_id == maker_id`).
  * Hệ thống ngăn chặn lập tức, từ chối thực thi giao dịch, trả về mã lỗi `403 Forbidden` và hiển thị cảnh báo vi phạm bảo mật: *"Cảnh báo kiểm soát nội bộ: Người lập chứng từ không được phép tự phê duyệt phiếu nhập của chính mình theo quy tắc Maker-Checker (BR-SEC-03)."*
* **Luồng 4e1 – Xung đột trạng thái chứng từ / Đã xử lý trước đó (`409 Conflict`):**
  * Tại bước 4, trong lúc Quản lý kho đang xem xét, một Quản lý kho khác đã phê duyệt hoặc từ chối phiếu nhập này trong phiên làm việc song song.
  * Hệ thống phát hiện trạng thái chứng từ trong CSDL không còn là `PENDING_APPROVAL`, tự động rollback toàn bộ giao dịch, trả về mã lỗi `409 Conflict` và thông báo: *"Chứng từ đã được xử lý bởi người dùng khác. Vui lòng làm mới danh sách."*

---

### UC04: LẬP PHIẾU XUẤT KHO (CREATE GOODS ISSUE NOTE)

#### 1. Bảng Thông tin Metadata Use Case
| Thuộc tính | Chi tiết đặc tả |
| :--- | :--- |
| **Mã Use Case** | `UC04` |
| **Tên Use Case** | Lập phiếu xuất kho (Create Goods Issue Note) |
| **Tác nhân chính** | Nhân viên kho |
| **Tác nhân phụ** | BP. Sản xuất / Bán hàng |
| **Mô tả tóm tắt (Brief Description)** | Cho phép Nhân viên kho tạo phiếu xuất hàng theo yêu cầu của bộ phận sản xuất hoặc bán hàng. Hệ thống sẽ tự động kiểm tra lượng hàng còn trong kho, gợi ý các lô hàng ưu tiên xuất trước và vị trí lấy hàng cụ thể, đồng thời tạm giữ số lượng này để tránh xuất trùng. |
| **Điều kiện tiên quyết (Pre-conditions)** | 1. Nhân viên kho đã đăng nhập vào hệ thống.<br>2. Nhận được yêu cầu xuất kho từ bộ phận sản xuất hoặc bộ phận bán hàng.<br>3. Hàng hóa yêu cầu vẫn còn tồn trong kho và chưa bị người khác giữ chỗ hết. |
| **Điều kiện được thiết lập (Post-conditions)** | 1. Phiếu xuất kho mới được tạo ở trạng thái **Chờ duyệt** (chưa trừ hàng thực tế trong kho).<br>2. Số lượng hàng cần xuất được hệ thống **tạm giữ chỗ** để các đơn khác không lấy mất.<br>3. Danh sách chỉ dẫn lấy hàng (Pick list) hiển thị cụ thể số lô và vị trí ô kệ cần đến lấy được tạo ra. |
| **Quy tắc nghiệp vụ chi phối** | • [`BR-OUT-01`](../../01_srs/business_rules.md)<br>• [`BR-OUT-02`](../../01_srs/business_rules.md)<br>• [`BR-OUT-03`](../../01_srs/business_rules.md)<br>• [`BR-OUT-06`](../../01_srs/business_rules.md) |
| **Yêu cầu chức năng liên ứng** | • [`FR03.01`](../../01_srs/functional_requirements.md)<br>• [`FR03.02`](../../01_srs/functional_requirements.md) |

#### 2. Bảng Luồng sự kiện chính (Basic Flow / Main Flow)
| Bước | Hành động của Tác nhân | Phản hồi của Hệ thống |
|:---:|:---|:---|
| 1 | Nhân viên kho chọn chức năng **"Quản lý Xuất kho"** và bấm **"Tạo phiếu xuất mới"**. | Hệ thống hiển thị form nhập liệu yêu cầu xuất: Loại xuất kho (Xuất sản xuất, Xuất bán hàng), Bộ phận nhận hàng, Ngày hẹn giao hàng, Ghi chú. |
| 2 | Nhân viên kho thêm các dòng mặt hàng cần xuất: chọn Mã SKU và nhập **Số lượng yêu cầu xuất** (`requested_quantity`). | Hệ thống truy vấn tồn kho thời gian thực và kiểm tra điều kiện xuất kho:<br>$$\text{Requested Quantity} \le \text{Available Quantity}$$<br>Trong đó $\text{Available} = \text{On-hand} - \text{Reserved} - \text{Expired}$ (`BR-OUT-01`). |
| 3 | | Hệ thống tự động kích hoạt thuật toán đề xuất xuất lô thông minh:<br>1. Lọc bỏ toàn bộ các lô hàng đã hết hạn ($\text{EXP Date} \le \text{Current Date}$, `BR-OUT-03`).<br>2. Đối với hàng quản lý hạn dùng: Sắp xếp các lô còn hạn theo thứ tự hạn dùng gần nhất lên trước (**FEFO** – `BR-OUT-02`).<br>3. Đối với hàng không quản lý hạn: Sắp xếp theo ngày nhập kho cũ nhất lên trước (**FIFO** – `BR-OUT-02`).<br>4. Tự động phân bổ số lượng cần xuất vào từng lô và gán Vị trí ô kệ cụ thể cần lấy hàng (`BR-OUT-06`). |
| 4 | | Hệ thống hiển thị Bảng chỉ dẫn lấy hàng dự kiến (Pick list proposal) gồm: SKU, Tên hàng, Số lô được phân bổ, Hạn sử dụng, Vị trí ô kệ (Khu vực - Kệ - Ô) và Số lượng cần bốc dỡ tại từng vị trí. |
| 5 | Nhân viên kho xem lại danh sách chỉ dẫn lấy hàng và bấm **"Gửi phê duyệt phiếu xuất"**. | Hệ thống thực thi giao dịch kích hoạt cơ chế Tạm giữ chỗ (*Reserve*):<br>1. Cộng `requested_quantity` vào trường `reserved_quantity` của mặt hàng/lô hàng.<br>2. Trừ trực tiếp khỏi `available_quantity` của mặt hàng.<br>3. Giữ nguyên số lượng tồn kho vật lý `on_hand_quantity` không đổi (`BR-OUT-01`).<br>4. Tạo bản ghi Phiếu xuất kho (`IssueNote`) với trạng thái `PENDING_APPROVAL`, lưu `maker_id` và thời điểm lập. |
| 6 | | Hệ thống phản hồi mã `201 Created`, cấp Số phiếu xuất kho, thông báo "Tạo phiếu xuất kho và tạm giữ chỗ thành công", và gửi thông báo chờ duyệt đến Quản lý kho. |

#### 3. Luồng phụ / Rẽ nhánh (Alternative Flows)
* **Luồng 4a – Nhân viên kho thay đổi lô hàng khác thứ tự ưu tiên gợi ý của thuật toán (Override FEFO/FIFO):**
  * Tại bước 4, vì trở ngại vật lý tại kho (lối đi ô kệ của lô hạn ngắn đang bảo trì hoặc pallet bị cản trở), Nhân viên kho chủ động chọn lấy từ một lô hàng khác.
  * Hệ thống phát hiện việc chọn sai trật tự ưu tiên gợi ý của FEFO/FIFO (`BR-OUT-02`).
  * Hệ thống hiển thị hộp thoại bắt buộc nhập **"Lý do ngoại lệ chọn lô khác"** (`override_reason`, tối thiểu 10 ký tự).
  * Nhân viên kho nhập lý do giải trình hợp lệ và xác nhận.
  * Hệ thống ghi nhận cờ cảnh báo ngoại lệ (`is_overridden = TRUE`) cùng lý do giải trình lên phiếu xuất kho để Quản lý kho xem xét đặc biệt khi phê duyệt.

#### 4. Luồng ngoại lệ (Exception Flows)
* **Luồng 2e1 – Số lượng yêu cầu xuất vượt quá Tồn kho khả dụng (`400 Bad Request`):**
  * Tại bước 2, Nhân viên kho nhập số lượng xuất lớn hơn lượng tồn `Available` hiện có của mặt hàng.
  * Hệ thống phát hiện vi phạm quy tắc chống bán âm `BR-OUT-01`, chặn lập phiếu và trả về mã lỗi `400 Bad Request`: *"Số lượng yêu cầu xuất [X] vượt quá số lượng tồn kho khả dụng hiện có [Y]. Không thể tạo phiếu xuất."*
* **Luồng 3e1 – Cảnh báo chặn lô hàng đã quá hạn sử dụng (`400 Bad Request`):**
  * Tại bước 3, Nhân viên kho cố tình chỉ định lấy hàng từ một lô đã hết hạn ($\text{EXP Date} \le \text{Current Date}$).
  * Hệ thống khóa chặn lập tức theo quy định `BR-OUT-03`, từ chối thao tác và thông báo: *"Nghiêm cấm xuất kho đối với lô hàng đã hết hạn sử dụng theo quy tắc BR-OUT-03. Lô hàng này đã được đưa vào diện cách ly chờ tiêu hủy."*

---

### UC05: PHÊ DUYỆT PHIẾU XUẤT KHO (APPROVE GOODS ISSUE NOTE)

#### 1. Bảng Thông tin Metadata Use Case
| Thuộc tính | Chi tiết đặc tả |
| :--- | :--- |
| **Mã Use Case** | `UC05` |
| **Tên Use Case** | Phê duyệt phiếu xuất kho (Approve Goods Issue Note) |
| **Tác nhân chính** | Quản lý kho, Nhân viên kho |
| **Tác nhân phụ** | BP. Sản xuất / Bán hàng |
| **Mô tả tóm tắt (Brief Description)** | Cho phép Quản lý kho phê duyệt phiếu xuất hàng, sau đó Nhân viên kho đi lấy hàng tại các ô kệ, bàn giao cho người nhận và bấm xác nhận hoàn thành để hệ thống chính thức trừ hàng khỏi kho. |
| **Điều kiện tiên quyết (Pre-conditions)** | 1. Quản lý kho đã đăng nhập vào hệ thống.<br>2. Phiếu xuất kho đang ở trạng thái **Chờ duyệt** (người duyệt không trùng với người lập phiếu). |
| **Điều kiện được thiết lập (Post-conditions)** | 1. Phiếu xuất kho được duyệt và chuyển sang trạng thái **Hoàn tất** sau khi nhân viên giao hàng xong.<br>2. Hàng hóa chính thức được trừ khỏi kho thực tế và giải phóng lượng hàng tạm giữ chỗ.<br>3. Các ô kệ đã lấy hết hàng tự động chuyển về trạng thái trống.<br>4. Thẻ kho điện tử được ghi nhận bút toán xuất hàng và chứng từ bị khóa cấm sửa xóa. |
| **Quy tắc nghiệp vụ chi phối** | • [`BR-CAT-03`](../../01_srs/business_rules.md)<br>• [`BR-OUT-01`](../../01_srs/business_rules.md)<br>• [`BR-OUT-04`](../../01_srs/business_rules.md)<br>• [`BR-OUT-06`](../../01_srs/business_rules.md)<br>• [`BR-SEC-01`](../../01_srs/business_rules.md)<br>• [`BR-SEC-02`](../../01_srs/business_rules.md)<br>• [`BR-SEC-03`](../../01_srs/business_rules.md) |
| **Yêu cầu chức năng liên ứng** | • [`FR03.03`](../../01_srs/functional_requirements.md) |

#### 2. Bảng Luồng sự kiện chính (Basic Flow / Main Flow)
| Bước | Hành động của Tác nhân | Phản hồi của Hệ thống |
|:---:|:---|:---|
| 1 | Quản lý kho truy cập danh mục **"Phê duyệt chứng từ"** và chọn tab **"Phiếu xuất chờ duyệt"**. | Hệ thống hiển thị danh sách các phiếu xuất đang có trạng thái `PENDING_APPROVAL` (nổi bật các phiếu có cờ ghi đè lô ngoại lệ nếu có). |
| 2 | Quản lý kho chọn phiếu xuất, kiểm tra mục đích xuất, danh sách mặt hàng, số lượng và các lô hàng phân bổ. | Hệ thống hiển thị chi tiết phiếu xuất kèm danh tính Nhân viên kho lập phiếu (`maker_id`). |
| 3 | Quản lý kho bấm nút **"Phê duyệt xuất kho"** (Approve). | Hệ thống kiểm tra điều kiện Maker-Checker: `checker_id != maker_id` (`BR-SEC-03`). |
| 4 | | Hệ thống chuyển trạng thái phiếu xuất sang **"Đã duyệt – Chờ lấy hàng"** (`APPROVED_READY_TO_PICK`), ghi nhận `approver_id` và thời điểm duyệt. |
| 5 | Nhân viên kho mở/in **Phiếu chỉ dẫn lấy hàng** (Pick list) hiển thị chi tiết: Mã SKU, Tên hàng, Số lô cần lấy, Hạn dùng, và Mã vị trí ô kệ cần đến lấy (`BR-OUT-06`). | Hệ thống xuất định dạng Pick list chuẩn hóa theo thứ tự tối ưu hóa quãng đường di chuyển trong kho. |
| 6 | Nhân viên kho đến đúng vị trí ô kệ lấy hàng, kiểm tra thực tế, bàn giao hàng cho đại diện BP. Sản xuất / Bán hàng ký biên bản giao nhận. | |
| 7 | Nhân viên kho truy cập hệ thống và bấm **"Xác nhận hoàn tất giao hàng"** (Confirm Dispatch). | Hệ thống thực thi Giao dịch nguyên tử (Atomic Database Transaction):<br>1. Chuyển trạng thái phiếu xuất sang `COMPLETED`.<br>2. Trừ số dư tồn kho thực tế (*On-hand*) tương ứng tại bảng `StockBatch` (`BR-OUT-04`).<br>3. Giải phóng số lượng giữ chỗ tương ứng khỏi `reserved_quantity`.<br>4. Kiểm tra vị trí kho: nếu tổng tồn tại vị trí bằng 0, tự động cập nhật trạng thái vị trí thành `EMPTY` (`BR-CAT-03`).<br>5. Tự động ghi 01 dòng bút toán vào Thẻ kho điện tử (`StockCard`): Số chứng từ, Loại giao dịch "XUẤT_KHO", Số lượng xuất, Tồn sau giao dịch (`BR-SEC-02`).<br>6. Ghi nhận Audit Log: Maker ID, Approver ID, Dispatcher ID, timestamps.<br>7. Khóa vĩnh viễn chứng từ cấm sửa/xóa (`BR-SEC-01`). |
| 8 | | Hệ thống phản hồi mã `200 OK`, hiển thị thông báo "Xuất kho hoàn tất thành công. Số dư tồn kho thực tế đã được cập nhật giảm", và hoàn thành ca sử dụng. |

#### 3. Luồng phụ / Rẽ nhánh (Alternative Flows)
* **Luồng 3a – Quản lý kho Từ chối phê duyệt (Reject) hoặc Người lập Hủy phiếu (Cancel):**
  * Tại bước 3, Quản lý kho bấm **"Từ chối"** kèm lý do, hoặc tại bước trước khi duyệt, người lập bấm **"Hủy yêu cầu xuất"**.
  * Hệ thống tự động kích hoạt cơ chế Giải phóng Tạm giữ chỗ: trừ số lượng yêu cầu ra khỏi `reserved_quantity` và cộng trả lại vào `available_quantity` của mặt hàng (`BR-OUT-01`).
  * Trạng thái phiếu chuyển thành `REJECTED` hoặc `CANCELLED`. Số dư tồn kho thực tế On-hand hoàn toàn không bị ảnh hưởng.

#### 4. Luồng ngoại lệ (Exception Flows)
* **Luồng 3e1 – Vi phạm nguyên tắc Maker-Checker / Tự duyệt phiếu xuất (`403 Forbidden`):**
  * Tại bước 3, tài khoản Quản lý kho duyệt trùng với tài khoản đã lập phiếu xuất (`checker_id == maker_id`).
  * Hệ thống chặn ngay lập tức, trả về mã lỗi `403 Forbidden`: *"Vi phạm quy tắc Maker-Checker: Người tạo yêu cầu xuất không được phép tự phê duyệt phiếu xuất của chính mình theo BR-SEC-03."*
* **Luồng 7e1 – Xung đột dữ liệu tồn kho đồng thời (`409 Conflict`):**
  * Tại bước 7, trong quá trình xác nhận giao hàng, dữ liệu lô hàng bị xung đột trạng thái trong CSDL.
  * Hệ thống rollback toàn bộ giao dịch, trả về mã lỗi `409 Conflict` và yêu cầu người dùng thử lại sau vài giây.

---

### UC06: TRA CỨU TỒN KHO (QUERY INVENTORY)

#### 1. Bảng Thông tin Metadata Use Case
| Thuộc tính | Chi tiết đặc tả |
| :--- | :--- |
| **Mã Use Case** | `UC06` |
| **Tên Use Case** | Tra cứu tồn kho (Query Inventory) |
| **Tác nhân chính** | Nhân viên kho, Quản lý kho |
| **Tác nhân phụ** | Kế toán kho & Ban Giám đốc |
| **Mô tả tóm tắt (Brief Description)** | Cho phép nhân viên và quản lý tra cứu nhanh số lượng hàng tồn theo từng mặt hàng, lô hàng hoặc vị trí ô kệ, đồng thời xem lại lịch sử nhập xuất trên thẻ kho để đối soát số liệu khi cần thiết. |
| **Điều kiện tiên quyết (Pre-conditions)** | Người dùng (nhân viên kho hoặc quản lý) đã đăng nhập vào hệ thống với quyền tra cứu kho. |
| **Điều kiện được thiết lập (Post-conditions)** | 1. Người dùng nắm bắt được số lượng tồn thực tế, số lượng đang bị giữ chỗ và số lượng còn có thể xuất của từng mặt hàng.<br>2. Trích xuất được bảng lịch sử nhập xuất của thẻ kho theo khoảng thời gian tùy chọn để đối soát số liệu. |
| **Quy tắc nghiệp vụ chi phối** | • [`BR-CAT-03`](../../01_srs/business_rules.md)<br>• [`BR-LOT-01`](../../01_srs/business_rules.md)<br>• [`BR-SEC-02`](../../01_srs/business_rules.md) |
| **Yêu cầu chức năng liên ứng** | • [`FR04.01`](../../01_srs/functional_requirements.md)<br>• [`FR04.02`](../../01_srs/functional_requirements.md) |

#### 2. Bảng Luồng sự kiện chính (Basic Flow / Main Flow)
| Bước | Hành động của Tác nhân | Phản hồi của Hệ thống |
|:---:|:---|:---|
| 1 | Nhân viên kho / Quản lý kho chọn chức năng **"Tra cứu Tồn kho"** trên menu chính. | Hệ thống hiển thị màn hình Tra cứu tồn kho tổng hợp với thanh tìm kiếm đa năng và 3 tab góc nhìn: **Theo SKU**, **Theo Lô / Hạn dùng**, **Theo Vị trí ô kệ**. |
| 2 | Tại tab mặc định **"Theo SKU"**, người dùng nhập từ khóa tìm kiếm (Mã SKU hoặc Tên hàng), chọn Nhóm hàng, Phân vùng kho 3M và bấm **"Tìm kiếm"**. | Hệ thống truy vấn CSDL và tính toán các chỉ số tồn kho tức thời:<br>$$\text{Available} = \text{On-hand} - \text{Reserved} - \text{Quá hạn/Hư hỏng}$$ |
| 3 | | Hệ thống hiển thị bảng dữ liệu kết quả trực quan gồm: Mã SKU, Tên hàng, ĐVT, Tồn thực tế (*On-hand*), Tồn giữ chỗ (*Reserved*), Tồn khả dụng (*Available*), và Trạng thái dự trữ an toàn (Badge màu: Xanh – Bình thường, Đỏ – Chạm ngưỡng Min). |
| 4 | Nhân viên kho / Quản lý kho bấm chọn một dòng mặt hàng cụ thể và bấm nút **"Xem Thẻ kho"** (Electronic Stock Card). | Hệ thống hiển thị modal Tra cứu Thẻ kho điện tử, yêu cầu chọn khoảng thời gian cần truy vấn: **Từ ngày** (`from_date`) và **Đến ngày** (`to_date`). |
| 5 | Nhân viên kho / Quản lý kho chọn khoảng thời gian và bấm **"Trích xuất Thẻ kho"**. | Hệ thống kiểm tra hợp lệ khoảng thời gian ($\text{to\_date} \ge \text{from\_date}$). |
| 6 | | Hệ thống kết xuất bảng dữ liệu Thẻ kho điện tử liên tục theo trật tự thời gian tuyến tính:<br>• Dòng số dư đầu kỳ.<br>• Các dòng phát sinh: Ngày giao dịch, Số chứng từ (Phiếu nhập PO, Phiếu xuất SX, Phiếu xuất bán), Loại giao dịch, Số lượng Nhập, Số lượng Xuất, Số tồn lũy kế sau giao dịch, Danh tính Người lập và Người phê duyệt (`BR-SEC-02`).<br>• Dòng tổng kết cuối kỳ (số tồn cuối khớp $100\%$ với số dư On-hand hiện tại). Phản hồi mã `200 OK`. |

#### 3. Luồng phụ / Rẽ nhánh (Alternative Flows)
* **Luồng 2a – Tra cứu theo góc nhìn Vị trí ô kệ (Location-based View):**
  * Tại bước 2, người dùng chọn tab **"Theo Vị trí ô kệ"**.
  * Hệ thống hiển thị danh sách toàn bộ các vị trí ô kệ theo phân vùng 3M (`RAW`, `FG`, `SCRAP`...), hiển thị rõ trạng thái Trống (`EMPTY`) hoặc Đang chứa (`OCCUPIED`), mã SKU đang đặt tại đó và số lượng tồn chi tiết tại từng ô (`BR-CAT-03`).
* **Luồng 2b – Tra cứu theo góc nhìn Lô & Hạn sử dụng (Batch/Expiry View):**
  * Tại bước 2, người dùng chọn tab **"Theo Lô hàng"**.
  * Hệ thống hiển thị chi tiết từng Số lô (Lot No), Ngày sản xuất (MFG), Hạn sử dụng (EXP), số ngày còn lại đến hạn, số lượng tồn và mã vị trí đang cất giữ (`BR-LOT-01`).

#### 4. Luồng ngoại lệ (Exception Flows)
* **Luồng 5e1 – Khoảng thời gian tra cứu thẻ kho không hợp lệ (`400 Bad Request`):**
  * Tại bước 5, người dùng chọn `from_date` lớn hơn `to_date`.
  * Hệ thống chặn truy vấn, trả về mã lỗi `400 Bad Request` và thông báo: *"Khoảng thời gian tra cứu không hợp lệ: Từ ngày phải nhỏ hơn hoặc bằng Đến ngày."*
* **Luồng 2e1 – Không tìm thấy dữ liệu phù hợp với bộ lọc (`404 Not Found`):**
  * Tại bước 2, từ khóa tìm kiếm không khớp với bất kỳ mặt hàng nào.
  * Hệ thống hiển thị bảng trống kèm thông báo trực quan: *"Không tìm thấy dữ liệu tồn kho phù hợp với điều kiện tìm kiếm."*

---

### UC07: XEM CẢNH BÁO TỒN KHO (VIEW INVENTORY ALERTS)

#### 1. Bảng Thông tin Metadata Use Case
| Thuộc tính | Chi tiết đặc tả |
| :--- | :--- |
| **Mã Use Case** | `UC07` |
| **Tên Use Case** | Xem cảnh báo tồn kho (View Inventory Alerts) |
| **Tác nhân chính** | Nhân viên Mua hàng, Quản lý kho |
| **Tác nhân phụ** | Bộ định thời (Scheduler) |
| **Mô tả tóm tắt (Brief Description)** | Cung cấp màn hình theo dõi các mặt hàng sắp hết hoặc các lô hàng sắp hết hạn sử dụng, đồng thời gợi ý số lượng cần mua thêm để nhân viên mua hàng kịp thời đặt hàng bổ sung cho kho. |
| **Điều kiện tiên quyết (Pre-conditions)** | 1. Người dùng (nhân viên mua hàng hoặc quản lý) đã đăng nhập vào hệ thống.<br>2. Các mặt hàng đã được cài đặt mức tồn kho tối thiểu và tối đa từ trước (tại UC01). |
| **Điều kiện được thiết lập (Post-conditions)** | 1. Danh sách các mặt hàng đang thiếu hụt hoặc các lô hàng sắp hết hạn sử dụng (trong vòng 30 ngày) được hiển thị trực quan.<br>2. Bảng tính sẵn số lượng hàng cần mua thêm được cung cấp để nhân viên mua hàng làm căn cứ đặt hàng bổ sung. |
| **Quy tắc nghiệp vụ chi phối** | • [`BR-CAT-05`](../../01_srs/business_rules.md)<br>• [`BR-INV-01`](../../01_srs/business_rules.md)<br>• [`BR-INV-02`](../../01_srs/business_rules.md)<br>• [`BR-INV-03`](../../01_srs/business_rules.md) |
| **Yêu cầu chức năng liên ứng** | • [`FR04.03`](../../01_srs/functional_requirements.md) |

#### 2. Bảng Luồng sự kiện chính (Basic Flow / Main Flow)
| Bước | Hành động của Tác nhân | Phản hồi của Hệ thống |
|:---:|:---|:---|
| 1 | Nhân viên Mua hàng / Quản lý kho truy cập phân hệ **"Cảnh báo & Đề xuất đặt hàng"** (hoặc mở Dashboard kho). | Hệ thống tự động thực thi chu trình quét số liệu thời gian thực trên CSDL:<br>1. Quét các mặt hàng có $\text{Available} \le \text{Min Stock}$ (`BR-INV-02`).<br>2. Quét các lô hàng có $\text{EXP Date} - \text{Current Date} \le 30\text{ ngày}$ (`BR-INV-03`). |
| 2 | | Hệ thống tải và hiển thị 2 Bảng cảnh báo trực quan theo quy chuẩn kiểm soát trực quan 5S (Seiketsu):<br>• **Bảng 1 – Cảnh báo thiếu hụt dự trữ an toàn:** Gắn nhãn Badge ĐỎ cho các mặt hàng chạm hoặc tụt dưới Min Stock.<br>• **Bảng 2 – Cảnh báo lô hàng cận hạn sử dụng:** Gắn nhãn Badge VÀNG cho các lô còn hạn $\le 30$ ngày kèm thông tin Số lô, Hạn dùng, Số lượng tồn và Vị trí ô kệ. |
| 3 | Nhân viên Mua hàng bấm chọn tab **"Đề xuất đặt hàng bổ sung"** (Reorder Suggestions). | Đối với từng mặt hàng thiếu hụt, hệ thống tự động tính toán số lượng đề xuất đặt mua theo công thức chuẩn (`BR-INV-01`):<br>$$\text{Reorder Quantity} = \max\Big(0, \text{Max Stock} - (\text{Available} + \text{PO On-order})\Big)$$<br>Trong đó $\text{PO On-order}$ là tổng số lượng hàng trên các PO đã duyệt đang chờ nhà cung cấp giao đến kho. |
| 4 | | Hệ thống hiển thị Bảng đề xuất đặt hàng chi tiết gồm: Mã SKU, Tên hàng, ĐVT, Định mức Min, Định mức Max, Tồn khả dụng hiện có (*Available*), Số lượng hàng đang về (*PO On-order*), và **Số lượng đề xuất đặt mua thêm**. Phản hồi mã `200 OK`. |
| 5 | Nhân viên mua hàng xem xét số liệu và sử dụng danh sách đề xuất làm căn cứ khởi tạo Đơn mua hàng (PO) mới với Nhà cung cấp. | Hệ thống cung cấp nút chức năng xuất dữ liệu danh sách đề xuất để phục vụ nghiệp vụ mua sắm. |

#### 3. Luồng phụ / Rẽ nhánh (Alternative Flows)
* **Luồng 1a – Tiến trình quét tự động định kỳ kích hoạt bởi Bộ định thời (Scheduler Trigger):**
  * Lúc 00:00:00 hàng ngày, Bộ định thời (Scheduler) tự động kích hoạt tác vụ Spring Batch / `@Scheduled` quét toàn bộ dữ liệu tồn kho theo `BR-INV-03`.
  * Hệ thống phát hiện các lô hàng có $\text{EXP Date} - \text{Current Date} \le 30\text{ ngày}$ và các SKU có $\text{Available} \le \text{Min Stock}$.
  * Hệ thống tự động đẩy cảnh báo (Alert Notification) vào Dashboard và gửi email báo cáo tổng hợp đến hộp thư của Nhân viên Mua hàng và Quản lý kho trước đầu ca làm việc mỗi ngày.
* **Luồng 4a – Lượng hàng đang về đã đủ bù đắp tồn kho (Không phát sinh đặt trùng):**
  * Tại bước 4, nếu tổng tồn khả dụng và hàng đang về đã đạt hoặc vượt ngưỡng tối đa: $(\text{Available} + \text{PO On-order}) \ge \text{Max Stock}$.
  * Hệ thống tự động gán $\text{Reorder Quantity} = 0$, hiển thị dòng trạng thái màu xanh: *"Hàng đang về theo PO đã đủ bù đắp định mức trần. Không phát sinh đặt trùng (BR-INV-01)"*.
* **Luồng 2a – Lọc danh sách cảnh báo theo Phân vùng kho hoặc Nhóm mặt hàng:**
  * Tại bước 2, người dùng áp dụng bộ lọc theo Nhóm hàng hoặc Phân vùng kho để tập trung kiểm tra nhóm nguyên vật liệu (`RAW`) hoặc thành phẩm (`FG`). Hệ thống tự động làm mới bảng hiển thị theo tiêu chí lọc tương ứng.

#### 4. Luồng ngoại lệ (Exception Flows)
* **Luồng 1e1 – Mặt hàng chưa được cấu hình định mức Min/Max Stock:**
  * Tại bước 1, hệ thống phát hiện có một số mặt hàng trong kho chưa được thiết lập `min_stock` hoặc `max_stock`.
  * Hệ thống không thể chạy thuật toán tính cảnh báo cho các mặt hàng này; tự động hiển thị dòng cảnh báo phụ màu cam trên giao diện: *"Phát hiện [X] mặt hàng chưa được cấu hình định mức Min/Max Stock. Vui lòng hoàn tất thiết lập tại UC01 theo quy tắc BR-CAT-05."*

---

## 3. BẢNG TỔNG HỢP KIỂM CHỨNG BẢO ĐẢM TIÊU CHUẨN (DEFINITION OF DONE - DOD)

| Tiêu chí kiểm chứng DoD | Kết quả kiểm tra | Minh chứng đối soát |
|:---|:---:|:---|
| Đầy đủ 7 Use Case cốt lõi khớp 100% với 3 sơ đồ hình ảnh Use Case | **ĐẠT** | Ánh xạ từ [TQ1.jpg](Usecase_TongQuat/TQ1.jpg), [TQ2.jpg](Usecase_TongQuat/TQ2.jpg), [TQ3.jpg](Usecase_TongQuat/TQ3.jpg). |
| Tuân thủ tuyệt đối cấu trúc Biểu mẫu 2.1 của `SpecificationStandard.md` | **ĐẠT** | Đủ 100% các phần: Metadata, Tiền/Hậu điều kiện, BR chi phối, FR liên ứng, Luồng chính (bảng 3 cột), Luồng phụ, Luồng ngoại lệ. |
| Ràng buộc đầy đủ 25 Quy tắc nghiệp vụ (`BR-CAT`, `BR-LOT`, `BR-INB`, `BR-OUT`, `BR-INV`, `BR-SEC`) | **ĐẠT** | Từng bước xử lý và luồng ngoại lệ đều trích dẫn chính xác mã BR và hành vi tương ứng. |
| Tuân thủ cơ chế kiểm soát 4 mắt (Maker-Checker) và phân định thẩm quyền | **ĐẠT** | Thể hiện tường minh tại `UC02`, `UC03`, `UC04`, `UC05` với ràng buộc `checker_id != maker_id` và mã phản hồi `403 Forbidden`. |
| Thể hiện chuẩn phân vùng 3M Floor Marking và triết lý 5S Nhật Bản | **ĐẠT** | Tích hợp mã phân vùng (`DOCK`, `INSP`, `RAW`, `FG`, `SCRAP`) và 5 bước Seiri, Seiton, Seiso, Seiketsu, Shitsuke vào logic từng ca sử dụng. |
| Chuẩn hóa mã phản hồi HTTP Status Code cho tầng Controller/API | **ĐẠT** | Sử dụng nhất quán `200 OK`, `201 Created`, `400 Bad Request`, `403 Forbidden`, `404 Not Found`, `409 Conflict`. |

---
*Tài liệu được hoàn thiện và xác nhận bởi Thành viên M2 (Nguyễn Bảo Khang – Maker) ngày 04/11/2026.*

