# TÀI LIỆU ĐẶC TẢ YÊU CẦU CHỨC NĂNG (FUNCTIONAL REQUIREMENTS - FR)
<!-- 
Tài liệu thuộc Sprint 1 (Task T1.2, T1.3)
Tham chiếu tiêu chuẩn: SpecificationStandard.md (Mục 1.2)
Quy cách:
- Đủ 2 bảng (Metadata nhóm FR + Bảng con FRxx.yy) cho 4-5 nhóm Must-have.
- Bảng rút gọn 1 dòng cho nhóm Should-have / Could-have.
- Tên và lý do loại biên cho nhóm Won't-have.
-->


## 1. BẢNG TỔNG HỢP ƯU TIÊN HÓA YÊU CẦU CHỨC NĂNG THEO MoSCoW

### Bảng 1.1: Bảng phân loại danh mục FR toàn hệ thống theo mô hình MoSCoW

| Mã Nhóm FR | Tên Nhóm Chức Năng | Phân loại MoSCoW | Căn cứ & Tiêu chí xếp loại | Cài đặt Demo Java |
| :---: | :--- | :---: | :--- | :---: |
| **FR01** | **Quản lý Danh mục Kho & Hàng hóa** | **Must-have** | Thiết lập cấu trúc dữ liệu nền tảng (SKU, ĐVT, Vị trí Ô kệ, Định mức Min/Max); điều kiện tiên quyết để hệ thống vận hành. | [x] Bắt buộc demo |
| **FR02** | **Quản lý Nhập kho theo PO & Phê duyệt Maker-Checker** | **Must-have** | Quy trình đầu vào huyết mạch; ghi nhận lô/vị trí, đối chiếu đơn mua và tăng tồn kho thực tế qua thẩm quyền xét duyệt 2 bước. | [x] Bắt buộc demo |
| **FR03** | **Quản lý Xuất kho, Cơ chế Giữ chỗ (Reserve) & Gợi ý FEFO/FIFO** | **Must-have** | Nghiệp vụ trung tâm xử lý bài toán chống bán âm/tranh chấp hàng, tự động thuật toán hóa xuất lô hạn ngắn và trừ tồn kho. | [x] Bắt buộc demo |
| **FR04** | **Tra cứu Tồn kho Khả dụng, Thẻ kho & Cảnh báo Dự trữ** | **Must-have** | Cung cấp thông tin thời gian thực xóa bỏ độ trễ số liệu, tự động kích hoạt cảnh báo tồn thấp và danh sách đặt hàng bù trừ. | [x] Bắt buộc demo |
| **FR05** | Quản lý Đợt Kiểm kê & Xử lý Chênh lệch Số liệu | **Should-have** | Rất quan trọng để duy trì độ tin cậy của sổ sách nhưng có thể tách biệt chu kỳ kiểm đếm định kỳ mà không chặn luồng nhập xuất hàng ngày. | [ ] Không demo |
| **FR06** | Quản lý Cách ly & Lập Phiếu Xuất Hủy Hàng Quá hạn | **Should-have** | Xử lý chứng từ chuyên biệt cho hàng hỏng/hết hạn cách ly; có thể tạm xử lý qua phiếu xuất điều chỉnh trong giai đoạn đầu. | [ ] Không demo |
| **FR07** | Báo cáo Thống kê Tổng hợp Nhập - Xuất - Tồn | **Should-have** | Tổng hợp số liệu phục vụ kế toán và quản lý cấp cao; có thể truy xuất gián tiếp qua bảng dữ liệu tồn kho thời gian thực. | [ ] Không demo |
| **FR08** | Bảng điều khiển Quản trị Trực quan (Dashboard Metrics) | **Could-have** | Biểu đồ trực quan hóa dữ liệu thống kê nâng cao trải nghiệm người dùng; không ảnh hưởng logic xử lý giao dịch kho. | [ ] Không demo |
| **FR09** | Xuất dữ liệu Thẻ kho & Báo cáo sang định dạng Excel/PDF | **Could-have** | Tính năng tiện ích phụ trợ kết xuất file; chỉ triển khai khi hoàn tất toàn bộ các phân hệ lõi. | [ ] Không demo |
| **FR10** | Tích hợp Đồng bộ trực tuyến với ERP/MES qua Web API | **Won't-have** | Ngoài phạm vi Giai đoạn 1 do giới hạn công nghệ môi trường học phần và thiếu hệ sinh thái ERP thật từ doanh nghiệp. | [ ] Không demo |
| **FR11** | Tối ưu hóa Sơ đồ Xếp dỡ và Mô phỏng Kho 3D | **Won't-have** | Phức tạp về mặt thuật toán hình học không gian và đồ họa; vượt quá nguồn lực của nhóm 4 thành viên. | [ ] Không demo |
| **FR12** | Quản lý Chào thầu, Đấu thầu & Theo dõi Hợp đồng Mua sắm | **Won't-have** | Thuộc phân hệ Mua hàng mở rộng (Procurement Management), hệ thống chỉ tiếp nhận thông tin PO đã duyệt làm căn cứ giao nhận. | [ ] Không demo |
| **FR13** | Tự động gửi Tin nhắn SMS / Email Cảnh báo Tức thời | **Won't-have** | Giảm thiểu phụ thuộc dịch vụ cổng thông tin bên thứ 3 có trả phí; hệ thống tập trung thông báo nội bộ trực tiếp trên Web UI. | [ ] Không demo |

---

## 2. ĐẶC TẢ CHI TIẾT CÁC NHÓM CHỨC NĂNG CỐT LÕI (MUST-HAVE)

---

### FR01: QUẢN LÝ DANH MỤC KHO & HÀNG HÓA

#### Bảng 1: Metadata Nhóm chức năng FR01
| Trường thông tin | Nội dung chi tiết |
| :--- | :--- |
| **Mã nhóm FR** | `FR01` |
| **Tên chức năng** | Quản lý Danh mục Kho & Hàng hóa |
| **Mức độ ưu tiên (MoSCoW)** | **Must-have (Bắt buộc - Cài đặt Java Demo)** |
| **Ma trận truy vết** | • **Vấn đề giải quyết:** `P-01`, `P-04`, `P-07`<br>• **Quy tắc nghiệp vụ:** `BR-CAT-01`, `BR-CAT-02`, `BR-CAT-03`, `BR-CAT-04`, `BR-CAT-05` |
| **Mô tả tổng quát** | Phân hệ cung cấp các chức năng cấu hình và quản trị toàn bộ dữ liệu danh mục tĩnh nền tảng của kho hàng, bao gồm danh mục mặt hàng, đơn vị tính cơ sở thống nhất, định mức tồn kho an toàn (Min/Max Stock) và mô hình hóa không gian vị trí lưu kho theo mã phân vùng (Zone - Location Code). Đây là điều kiện dữ liệu toàn vẹn bắt buộc trước khi phát sinh bất kỳ nghiệp vụ giao dịch nhập/xuất nào. |

#### Bảng 2: Chi tiết các yêu cầu chức năng con (FR01.yy)
| Mã ID | Tên chức năng | Mô tả hành vi hệ thống | Dữ liệu đầu vào & Ràng buộc | Kết quả kỳ vọng & Mã phản hồi |
| :--- | :--- | :--- | :--- | :--- |
| `FR01.01` | Khởi tạo thông tin mặt hàng mới | Cho phép Thủ kho trưởng/Quản trị viên thiết lập một mặt hàng mới vào hệ thống. Hệ thống kiểm tra tính duy nhất của mã SKU trên toàn bộ CSDL (`BR-CAT-01`), gán cố định đúng một Đơn vị tính cơ sở (`BR-CAT-04`), lưu cờ đánh dấu quản lý theo lô (`manage_by_lot`) và ghi nhận ngày tạo. | • `sku_code`: String [3..30], Not null, Unique, chỉ chứa chữ hoa, số và dấu gạch ngang.<br>• `name`: String [1..255], Not null.<br>• `uom`: String [1..20], Not null (Cái, Hộp, Kg...).<br>• `manage_by_lot`: Boolean, Not null (Mặc định `TRUE`).<br>• `category_id`: Integer, Foreign key, Not null. | • Bản ghi mặt hàng được lưu vào bảng `product` với trạng thái `ACTIVE`.<br>• `201 Created`: Tạo mới thành công.<br>• `400 Bad Request`: Sai định dạng hoặc thiếu trường bắt buộc.<br>• `409 Conflict`: Mã `sku_code` đã tồn tại trong CSDL. |
| `FR01.02` | Thiết lập định mức tồn kho an toàn | Cung cấp chức năng cấu hình ngưỡng tồn an toàn cho từng mặt hàng để phục vụ thuật toán phát cảnh báo và gợi ý đơn hàng bổ sung (`BR-CAT-05`). Hệ thống bắt buộc kiểm tra điều kiện logic nghiệp vụ: $\text{Min Stock} \ge 0$ và $\text{Max Stock} > \text{Min Stock}$. | • `product_id`: Bigint, Not null, Khóa ngoại tham chiếu bảng `product`.<br>• `min_stock`: Decimal(12,2), Not null, Ràng buộc: $\text{min\_stock} \ge 0$.<br>• `max_stock`: Decimal(12,2), Not null, Ràng buộc: $\text{max\_stock} > \text{min\_stock}$. | • Dữ liệu định mức được cập nhật vào bản ghi sản phẩm.<br>• `200 OK`: Thiết lập thành công.<br>• `400 Bad Request`: Vi phạm ràng buộc logic $\text{max\_stock} \le \text{min\_stock}$ hoặc giá trị âm.<br>• `404 Not Found`: Không tìm thấy `product_id`. |
| `FR01.03` | Quản lý danh mục vị trí lưu kho | Cho phép thiết lập hệ thống vị trí lưu trữ trong kho vật lý theo cấu trúc Khu vực - Ô kệ (`BR-CAT-02`). Hệ thống kiểm tra mã vị trí `location_code` là duy nhất và khởi tạo trạng thái mặc định của vị trí là Trống (`EMPTY`). Cập nhật tự động trạng thái thành `OCCUPIED` khi phát sinh tồn kho thực tế (`BR-CAT-03`). | • `location_code`: String [3..20], Not null, Unique (Ví dụ: `A-01-01`).<br>• `zone`: String [1..50], Not null (Khu A, Khu B...).<br>• `shelf`: String [1..20], Not null.<br>• `status`: Enum (`EMPTY`, `OCCUPIED`), Mặc định: `EMPTY`. | • Bản ghi vị trí được ghi nhận vào bảng `warehouse_location`.<br>• `201 Created`: Tạo mới vị trí thành công.<br>• `400 Bad Request`: Định dạng mã không hợp lệ.<br>• `409 Conflict`: Trùng mã vị trí `location_code`. |

---

### FR02: QUẢN LÝ NHẬP KHO THEO PO & PHÊ DUYỆT MAKER-CHECKER

#### Bảng 1: Metadata Nhóm chức năng FR02
| Trường thông tin | Nội dung chi tiết |
| :--- | :--- |
| **Mã nhóm FR** | `FR02` |
| **Tên chức năng** | Quản lý Nhập kho theo PO & Phê duyệt Maker-Checker |
| **Mức độ ưu tiên (MoSCoW)** | **Must-have (Bắt buộc - Cài đặt Java Demo)** |
| **Ma trận truy vết** | • **Vấn đề giải quyết:** `P-01`, `P-03`, `P-04`, `P-05`, `P-08`<br>• **Quy tắc nghiệp vụ:** `BR-LOT-01`, `BR-INB-01`, `BR-INB-02`, `BR-INB-03`, `BR-INB-04`, `BR-SEC-01`, `BR-SEC-02`, `BR-SEC-03` |
| **Mô tả tổng quát** | Phân hệ số hóa quy trình tiếp nhận hàng hóa đầu vào căn cứ trên Đơn mua hàng (PO) hợp lệ. Hỗ trợ Thủ kho kiểm đếm thực tế, bóc tách hàng lỗi/không đạt chuẩn, ghi nhận chi tiết Số lô (Lot No), Hạn sử dụng (EXP) và chỉ định vị trí cất hàng cụ thể. Toàn bộ quy trình tuân thủ nghiêm ngặt nguyên tắc kiểm soát 4 mắt (Maker-Checker): chỉ khi Quản lý kho chính thức phê duyệt, hệ thống mới cập nhật tăng số dư tồn kho thực tế ($On\text{-}hand$) theo thời gian thực và khóa bất biến chứng từ. |

#### Bảng 2: Chi tiết các yêu cầu chức năng con (FR02.yy)
| Mã ID | Tên chức năng | Mô tả hành vi hệ thống | Dữ liệu đầu vào & Ràng buộc | Kết quả kỳ vọng & Mã phản hồi |
| :--- | :--- | :--- | :--- | :--- |
| `FR02.01` | Lập Phiếu nhập kho từ Đơn mua hàng (PO) | Cho phép Thủ kho (Maker) tạo mới phiếu nhập kho từ danh sách PO đã duyệt (`BR-INB-01`). Hệ thống tải danh mục mặt hàng theo PO; kiểm tra số lượng thực nhận không được vượt quá số lượng chưa giao ($\text{Quantity Received} \le \text{Quantity Remaining}$). Nếu phát hiện hàng lỗi, rách hỏng, chỉ nhập phần đạt chuẩn (`BR-INB-04`). Phiếu tạo xong có trạng thái ban đầu là `PENDING_APPROVAL`, tuyệt đối chưa làm tăng tồn kho thực tế (`BR-INB-02`). | • `po_id`: Bigint, Not null, Tham chiếu PO ở trạng thái `APPROVED`.<br>• `received_date`: Date, Not null, Mặc định ngày hiện tại.<br>• `note_details`: Danh sách các dòng hàng gồm `product_id`, `quantity_received` (Decimal, $> 0$), `rejected_quantity` (Decimal, $\ge 0$), `rejection_reason` (String, Bắt buộc nếu có hàng lỗi). | • Bản ghi phiếu nhập được lưu vào bảng `receipt_note` với trạng thái `PENDING_APPROVAL`. Chi tiết lưu vào `receipt_note_item`.<br>• `201 Created`: Lập phiếu thành công.<br>• `400 Bad Request`: Số lượng nhận $\le 0$ hoặc vượt quá số lượng còn lại trên PO (`BR-INB-01`).<br>• `404 Not Found`: Không tìm thấy mã PO. |
| `FR02.02` | Khai báo Số lô, Hạn dùng và Chỉ định vị trí cất hàng | Khi lập chi tiết dòng nhập cho mặt hàng có quản lý hạn dùng, hệ thống bắt buộc Thủ kho nhập Số lô (Lot No), Hạn sử dụng (EXP Date) và chọn một mã vị trí lưu trữ xác định (`BR-LOT-01`, `BR-INB-03`). Hệ thống kiểm tra: Ngày hết hạn phải lớn hơn ngày hiện tại ($\text{EXP Date} > \text{Current Date}$). Không cho phép lưu kho hàng hóa không rõ vị trí. | • `lot_number`: String [1..50], Not null đối với hàng có `manage_by_lot = TRUE`.<br>• `exp_date`: Date, Not null đối với hàng có quản lý hạn dùng, Ràng buộc: $\text{exp\_date} > \text{current\_date}$.<br>• `mfg_date`: Date, Nullable, Ràng buộc: $\text{mfg\_date} \le \text{current\_date}$.<br>• `location_id`: Bigint, Not null, Khóa ngoại tham chiếu `warehouse_location`. | • Dữ liệu lô và vị trí tạm được liên kết với chi tiết phiếu nhập.<br>• `200 OK`: Ghi nhận thông tin lô và vị trí thành công.<br>• `400 Bad Request`: Hạn dùng $\le$ ngày hiện tại hoặc để trống vị trí lưu kho. |
| `FR02.03` | Thẩm định và Phê duyệt Phiếu nhập kho (Maker-Checker) | Cho phép Quản lý kho (Checker) xem xét, thẩm định và phê duyệt phiếu nhập kho đang chờ (`BR-SEC-03`). Hệ thống kiểm tra người duyệt phải khác người lập (`checker_id` $\neq$ `maker_id`). Khi bấm Duyệt: Hệ thống thực thi giao dịch nguyên tử (`@Transactional`): (1) Chuyển trạng thái phiếu sang `COMPLETED`, (2) Tăng tồn kho thực tế $On\text{-}hand$ tại bảng `stock_batch`, (3) Cập nhật trạng thái vị trí thành `OCCUPIED`, (4) Ghi nhận Thẻ kho điện tử và Nhật ký thao tác (`BR-INB-02`, `BR-SEC-02`), (5) Khóa chứng từ cấm sửa xóa (`BR-SEC-01`). | • `receipt_id`: Bigint, Not null, Trạng thái hiện tại phải là `PENDING_APPROVAL`.<br>• `decision`: Enum (`APPROVE`, `REJECT`), Not null.<br>• `rejection_note`: String [1..255], Bắt buộc nếu chọn `REJECT`. | • Trạng thái phiếu cập nhật thành `COMPLETED` (hoặc `REJECTED`). Số tồn $On\text{-}hand$ tăng tương ứng.<br>• `200 OK`: Phê duyệt hoàn tất, dữ liệu ghi sổ thành công.<br>• `403 Forbidden`: Người lập tự phê duyệt chứng từ của chính mình (`BR-SEC-03`).<br>• `409 Conflict`: Phiếu đã được xử lý trước đó (không ở trạng thái chờ duyệt). |

---

### FR03: QUẢN LÝ XUẤT KHO, CƠ CHẾ GIỮ CHỖ (RESERVE) & GỢI Ý FEFO/FIFO

#### Bảng 1: Metadata Nhóm chức năng FR03
| Trường thông tin | Nội dung chi tiết |
| :--- | :--- |
| **Mã nhóm FR** | `FR03` |
| **Tên chức năng** | Quản lý Xuất kho, Cơ chế Giữ chỗ (Reserve) & Gợi ý FEFO/FIFO |
| **Mức độ ưu tiên (MoSCoW)** | **Must-have (Bắt buộc - Cài đặt Java Demo)** |
| **Ma trận truy vết** | • **Vấn đề giải quyết:** `P-01`, `P-02`, `P-04`, `P-05`, `P-08`<br>• **Quy tắc nghiệp vụ:** `BR-OUT-01`, `BR-OUT-02`, `BR-OUT-03`, `BR-OUT-04`, `BR-OUT-06`, `BR-SEC-01`, `BR-SEC-02`, `BR-SEC-03` |
| **Mô tả tổng quát** | Phân hệ giải quyết bài toán cốt lõi chống xung đột cấp phát dữ liệu tồn kho bằng cơ chế tạm giữ chỗ tự động ($Reserved$). Đồng thời, hệ thống áp dụng thuật toán tự động đề xuất xuất kho thông minh: ưu tiên lô có hạn dùng ngắn xuất trước (FEFO) và lô nhập trước xuất trước (FIFO), khóa chặn triệt để xuất hàng đã hết hạn (`BR-OUT-03`). Khi hoàn tất bàn giao hàng, hệ thống chính thức trừ số lượng vật lý $On\text{-}hand$ và lưu vết kiểm toán chi tiết. |

#### Bảng 2: Chi tiết các yêu cầu chức năng con (FR03.yy)
| Mã ID | Tên chức năng | Mô tả hành vi hệ thống | Dữ liệu đầu vào & Ràng buộc | Kết quả kỳ vọng & Mã phản hồi |
| :--- | :--- | :--- | :--- | :--- |
| `FR03.01` | Tạo yêu cầu xuất kho và Tự động tạm giữ chỗ (Reserve) | Cho phép nhân viên tạo phiếu xuất kho. Hệ thống kiểm tra: Số lượng yêu cầu xuất không được vượt quá số tồn khả dụng: $\text{Quantity Request} \le \text{Available}$ (`BR-OUT-01`). Khi phiếu được bấm "Gửi phê duyệt" (`PENDING_APPROVAL`), hệ thống kích hoạt cơ chế giữ chỗ: tự động cộng dồn số lượng yêu cầu vào $Reserved$ và trừ trực tiếp khỏi $Available$ để ngăn ngừa tranh chấp từ các phiếu khác. Tuyệt đối không thay đổi tồn thực tế $On\text{-}hand$. Nếu phiếu bị Hủy hoặc Từ chối, hệ thống tự động giải phóng lượng $Reserved$. | • `export_type`: Enum (`PRODUCTION`, `SALE`), Not null.<br>• `export_details`: Danh sách các mặt hàng gồm `product_id`, `requested_quantity` (Decimal, $> 0$).<br>• Ràng buộc: Đối với mỗi mặt hàng: $\text{requested\_quantity} \le \text{Available}$. | • Phiếu xuất tạo với trạng thái `PENDING_APPROVAL`. Trường `reserved_quantity` của mặt hàng tăng lên.<br>• `201 Created`: Tạo phiếu và tạm giữ chỗ thành công.<br>• `400 Bad Request`: Số lượng xuất $\le 0$ hoặc vượt quá số lượng khả dụng $\text{Available}$ (`BR-OUT-01`). |
| `FR03.02` | Tự động thuật toán hóa gợi ý xuất kho theo FEFO / FIFO | Khi hệ thống phân bổ hàng cho phiếu xuất, hệ thống tự động truy vấn các lô hàng hợp lệ còn hạn ($\text{EXP Date} \ge \text{Current Date}$, `BR-OUT-03`). Thuật toán ưu tiên: Lô có `exp_date` sớm nhất xuất trước (FEFO); nếu hàng không quản lý hạn thì lô có `created_at` cũ nhất xuất trước (FIFO) (`BR-OUT-02`). Hệ thống hiển thị rõ ràng Số lô, Mã vị trí ô kệ cần đến lấy (`BR-OUT-06`). Nếu Thủ kho đổi sang lấy lô khác, bắt buộc phải nhập `override_reason` và phải được Quản lý duyệt. | • `issue_note_id`: Bigint, Not null.<br>• `override_reason`: String [1..255], Bắt buộc nếu người dùng chọn sai thứ tự lô gợi ý của thuật toán. | • Danh sách chỉ dẫn lấy hàng chi tiết (Pick list) hiển thị: Vị trí - Ô kệ, Số lô, Số lượng cần bốc dỡ.<br>• `200 OK`: Trả về danh sách gợi ý lô tối ưu.<br>• `400 Bad Request`: Chọn lô đã hết hạn (`BR-OUT-03`) hoặc đổi lô mà không có lý do ngoại lệ. |
| `FR03.03` | Phê duyệt và Xác nhận xuất kho thực tế (Trừ tồn kho) | Sau khi Quản lý kho phê duyệt phiếu xuất và Thủ kho xác nhận đã bàn giao hàng vật lý ra khỏi kho (`BR-SEC-03`), hệ thống cập nhật trạng thái phiếu thành `COMPLETED`. Trong cùng giao dịch (`@Transactional`): (1) Trừ tồn thực tế $On\text{-}hand$ tại bảng `stock_batch`, (2) Giải phóng trừ bỏ lượng giữ chỗ $Reserved$ tương ứng, (3) Nếu vị trí hết sạch hàng thì tự động đổi trạng thái vị trí thành `EMPTY` (`BR-CAT-03`), (4) Ghi nhận Thẻ kho và Audit Log (`BR-SEC-02`), (5) Khóa chứng từ cấm sửa xóa (`BR-SEC-01`). | • `issue_id`: Bigint, Not null, Trạng thái phải là `PENDING_APPROVAL`.<br>• `confirmed_by`: Bigint, Not null (ID Thủ kho xác nhận bàn giao).<br>• `approved_by`: Bigint, Not null (ID Quản lý duyệt, phải khác `maker_id`). | • Bản ghi phiếu xuất chuyển sang `COMPLETED`. Tồn $On\text{-}hand$ và $Reserved$ cùng giảm.<br>• `200 OK`: Xuất kho thành công, số tồn cập nhật tức thời.<br>• `403 Forbidden`: Vi phạm thẩm quyền Maker-Checker (`BR-SEC-03`).<br>• `409 Conflict`: Tồn kho bị xung đột hoặc sai lệch trạng thái. |

---

### FR04: TRA CỨU TỒN KHO KHẢ DỤNG, THẺ KHO & CẢNH BÁO DỰ TRỮ

#### Bảng 1: Metadata Nhóm chức năng FR04
| Trường thông tin | Nội dung chi tiết |
| :--- | :--- |
| **Mã nhóm FR** | `FR04` |
| **Tên chức năng** | Tra cứu Tồn kho Khả dụng, Thẻ kho & Cảnh báo Dự trữ |
| **Mức độ ưu tiên (MoSCoW)** | **Must-have (Bắt buộc - Cài đặt Java Demo)** |
| **Ma trận truy vết** | • **Vấn đề giải quyết:** `P-01`, `P-02`, `P-03`, `P-05`, `P-07`, `P-09`, `P-10`<br>• **Quy tắc nghiệp vụ:** `BR-CAT-03`, `BR-LOT-01`, `BR-INV-01`, `BR-INV-02`, `BR-INV-03`, `BR-SEC-02` |
| **Mô tả tổng quát** | Phân hệ cung cấp khả năng hiển thị dữ liệu tồn kho đa chiều theo thời gian thực (phân tách tường minh $On\text{-}hand$, $Reserved$, $Available$), xóa bỏ hoàn toàn việc dùng sổ thẻ kho giấy hay file Excel phân tán. Hệ thống tích hợp Thẻ kho điện tử truy vết toàn bộ lịch sử luân chuyển chứng từ và cơ chế tự động quét phát hiện nguy cơ: cảnh báo hàng chạm ngưỡng tối thiểu (Min Stock), cảnh báo lô hàng cận hạn ($\le 30$ ngày) và tự động tính toán số lượng đề xuất đặt hàng bù trừ tránh đặt trùng. |

#### Bảng 2: Chi tiết các yêu cầu chức năng con (FR04.yy)
| Mã ID | Tên chức năng | Mô tả hành vi hệ thống | Dữ liệu đầu vào & Ràng buộc | Kết quả kỳ vọng & Mã phản hồi |
| :--- | :--- | :--- | :--- | :--- |
| `FR04.01` | Tra cứu tồn kho đa chiều thời gian thực | Cho phép người dùng tra cứu dữ liệu tồn kho theo 3 góc nhìn: Theo Mặt hàng (SKU), Theo Lô hàng (Lot/EXP) và Theo Vị trí lưu kho (Location). Màn hình phản ánh chính xác số liệu tính toán theo công thức bất biến: $\text{Available} = \text{On-hand} - \text{Reserved} - \text{Expired/Damaged}$ (`BR-OUT-01`). Hỗ trợ bộ lọc tìm kiếm theo từ khóa SKU, tên sản phẩm, khu vực kho hoặc tình trạng hàng. | • Bộ lọc tìm kiếm (Query params): `keyword` (String, tùy chọn), `category_id` (Integer, tùy chọn), `zone` (String, tùy chọn), `status` (Tất cả, Còn hàng, Hết hàng). | • Danh sách kết quả dạng bảng hiển thị trực quan: SKU, Tên, ĐVT, Tồn thực tế ($On\text{-}hand$), Tồn giữ chỗ ($Reserved$), Tồn khả dụng ($Available$).<br>• `200 OK`: Truy vấn thành công.<br>• `400 Bad Request`: Tham số bộ lọc không hợp lệ. |
| `FR04.02` | Truy xuất Thẻ kho điện tử (Audit Trail) | Cung cấp báo cáo Thẻ kho chi tiết cho từng mặt hàng trong một khoảng thời gian xác định (`BR-SEC-02`). Hệ thống liệt kê toàn bộ các biến động nhập/xuất đã hoàn tất theo trật tự thời gian tuyến tính, gồm: Ngày giao dịch, Số chứng từ, Loại chứng từ (Nhập PO, Xuất SX, Xuất hủy), Số lượng Nhập, Số lượng Xuất, Số tồn tích lũy sau giao dịch và Người thực hiện/Người duyệt. | • `product_id`: Bigint, Not null.<br>• `from_date`: Date, Not null.<br>• `to_date`: Date, Not null, Ràng buộc: $\text{to\_date} \ge \text{from\_date}$. | • Bảng dữ liệu dòng lịch sử thẻ kho liên tục, số tồn cuối khớp $100\%$ với số dư thực tế hiện tại.<br>• `200 OK`: Kết xuất thẻ kho thành công.<br>• `400 Bad Request`: Khoảng thời gian không hợp lệ ($\text{to\_date} < \text{from\_date}$).<br>• `404 Not Found`: Không tìm thấy mặt hàng. |
| `FR04.03` | Cảnh báo tồn an toàn và Đề xuất đặt hàng bổ sung | Hệ thống tự động quét dữ liệu tồn kho theo thời gian thực để kích hoạt 2 cơ chế cảnh báo trên Dashboard: (1) Cảnh báo thiếu hàng khi $\text{Available} \le \text{Min Stock}$ (`BR-INV-02`); (2) Cảnh báo lô hàng cận hạn khi $\text{EXP Date} - \text{Current Date} \le 30\text{ ngày}$ (`BR-INV-03`). Đồng thời, đối với mặt hàng thiếu hụt, hệ thống tự động tính số lượng đề xuất đặt hàng theo công thức chuẩn (`BR-INV-01`): $\max(0, \text{Max Stock} - (\text{Available} + \text{On-order}))$. | • Tham số hệ thống: `current_date` (Mặc định ngày hệ thống).<br>• Dữ liệu tham chiếu: `min_stock`, `max_stock`, tồn `Available`, số lượng hàng đang về trên các PO đã duyệt nhưng chưa nhập kho (`On-order`). | • Bảng danh sách cảnh báo trực quan hiển thị badge màu (Đỏ: Thiếu hàng/Quá hạn; Vàng: Cận hạn) kèm bảng đề xuất đặt hàng (Mã SKU, Tên, Tồn hiện tại, Đang về, Số lượng gợi ý đặt mua).<br>• `200 OK`: Tải danh sách cảnh báo thành công. |

---

## 3. DANH MỤC CÁC YÊU CẦU CHỨC NĂNG RÚT GỌN (SHOULD-HAVE & COULD-HAVE)

Tuân thủ đúng tiêu chuẩn bảng liệt kê rút gọn 1 dòng cho các nhóm chức năng không nằm trong danh mục cài đặt Java Demo, chỉ truy vết về Vấn đề hiện trạng (`P-xx`) và Quy tắc nghiệp vụ (`BRxx`):

### Bảng 3.1: Bảng danh mục yêu cầu chức năng Should-have và Could-have

| Mã ID | Tên Yêu Cầu Chức Năng | Mô tả tóm tắt hành vi hệ thống (1 câu) | Phân loại MoSCoW | Ma trận truy vết (`P-xx` / `BRxx`) |
| :---: | :--- | :--- | :---: | :--- |
| **FR05.01** | Khởi tạo Đợt kiểm kê & Khóa sổ | Cho phép Quản lý kho tạo đợt kiểm kê định kỳ/đột xuất theo khu vực và xuất danh sách kiểm đếm trắng phục vụ nhân viên đếm thực tế. | **Should-have** | `P-06`, `BR-STK-01` |
| **FR05.02** | Nhập kết quả kiểm đếm & Tính sai lệch | Hệ thống ghi nhận số lượng kiểm đếm thực tế độc lập của nhân sự đếm và tự động tính toán chênh lệch thừa/thiếu so với số dư sổ sách. | **Should-have** | `P-06`, `BR-STK-02` |
| **FR05.03** | Xử lý chênh lệch & Điều chỉnh tồn kho | Bắt buộc đếm lại lần 2 nếu lệch $\ge 2\%$ hoặc $\ge 5.000.000$ VNĐ; Quản lý kho duyệt biên bản kèm mã lý do trước khi hệ thống ghi bút toán cân bằng tồn kho. | **Should-have** | `P-06`, `P-08`, `BR-STK-03`, `BR-SEC-01` |
| **FR06.01** | Tự động cô lập lô hàng quá hạn | Hệ thống tự động chuyển trạng thái các lô hàng có $\text{Ngày hiện tại} > \text{EXP Date}$ sang diện "Cách ly chờ tiêu hủy" và loại bỏ hoàn toàn khỏi Tồn khả dụng. | **Should-have** | `P-05`, `BR-OUT-03`, `BR-OUT-05` |
| **FR06.02** | Lập và Phê duyệt Phiếu xuất hủy | Cho phép tạo phiếu xuất hủy/thanh lý hàng quá hạn theo chứng từ riêng biệt và bắt buộc Quản lý kho thẩm tra, phê duyệt trước khi hạ giải tồn kho. | **Should-have** | `P-05`, `P-08`, `BR-OUT-05`, `BR-SEC-03` |
| **FR07.01** | Báo cáo Tổng hợp Nhập - Xuất - Tồn | Hệ thống tự động tổng hợp số dư đầu kỳ, tổng phát sinh nhập, tổng phát sinh xuất và số dư cuối kỳ theo từng mặt hàng trong kỳ báo cáo. | **Should-have** | `P-10`, `BR-INB-02`, `BR-OUT-04` |
| **FR07.02** | Báo cáo Sai lệch Kiểm kê định kỳ | Kết xuất bảng tổng hợp các đợt kiểm kê, tỷ lệ sai lệch số lượng/giá trị và phân loại theo nhóm nguyên nhân để phục vụ đối chiếu kế toán. | **Should-have** | `P-06`, `P-10`, `BR-STK-03` |
| **FR08.01** | Hiển thị Chỉ số Hiệu năng Kho (Dashboard) | Hiển thị các widget thống kê tổng số lượng SKU đang quản lý, giá trị tồn kho ước tính, tỷ lệ chính xác tồn kho và biểu đồ biến động nhập xuất 7 ngày gần nhất. | **Could-have** | `P-10` |
| **FR09.01** | Xuất dữ liệu Thẻ kho & Báo cáo (Excel/PDF) | Cho phép người dùng kết xuất bảng dữ liệu Thẻ kho điện tử và Báo cáo Nhập - Xuất - Tồn ra định dạng file Excel (.xlsx) hoặc in ấn biểu mẫu PDF tiêu chuẩn. | **Could-have** | `P-10` |

---

## 4. DANH MỤC CÁC YÊU CẦU NGOÀI PHẠM VI (WON'T-HAVE)

Thống nhất loại bỏ khỏi phạm vi hiện thực của đồ án học phần (kèm theo lý do kỹ thuật và nghiệp vụ cụ thể):

1. **FR10 - Tích hợp Đồng bộ với hệ thống ERP / MES qua Web API:**
   * *Lý do loại:* Môi trường nghiên cứu học phần không có hệ thống ERP/MES thực tế từ phía doanh nghiệp đối tác; việc giả lập API phức tạp làm phân tán nguồn lực nhóm. CSDL hệ thống được chuẩn hóa sẵn sàng để kết nối trong giai đoạn phát triển thương mại sau.
2. **FR11 - Tối ưu hóa Sơ đồ Xếp dỡ và Mô phỏng Không gian Kho 3D:**
   * *Lý do loại:* Đòi hỏi nền tảng đồ họa chuyên sâu và dữ liệu kích thước hình học 3 chiều của từng kiện pallet, vượt quá thời lượng và mục tiêu học phần Phân tích Thiết kế Yêu cầu.
3. **FR12 - Quản lý Chào thầu, Đấu thầu & Quản trị Hợp đồng Mua sắm Toàn diện:**
   * *Lý do loại:* Thuộc phạm vi của hệ thống Quản lý Mua sắm (Procurement System); hệ thống SmartWMS chỉ đóng vai trò tiếp nhận thông tin PO đã duyệt làm căn cứ đối chiếu số lượng nhập kho.
4. **FR13 - Tự động Gửi Thông báo Cảnh báo qua SMS Gateway / Dịch vụ Email bên thứ 3:**
   * *Lý do loại:* Đòi hỏi tích hợp dịch vụ bên thứ ba có phát sinh chi phí duy trì tài khoản (Twilio, SendGrid); toàn bộ cảnh báo được tập trung tối ưu hiển thị ngay trên Web UI Dashboard và danh sách thông báo In-app.