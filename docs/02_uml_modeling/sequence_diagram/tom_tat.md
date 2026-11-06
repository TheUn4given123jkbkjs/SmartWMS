## 1. SƠ ĐỒ LẬP PHIẾU NHẬP KHO (UC02)

### Mục đích
Nhân viên kho tiếp nhận hàng theo đơn đặt hàng (PO), kiểm tra số lượng, ghi số lô, hạn sử dụng và chỉ định vị trí cất hàng. Phiếu tạo xong ở trạng thái Chờ duyệt và chưa làm tăng số lượng hàng thực tế trong kho.

### Các lớp xử lý chính
* Giao diện (Web UI): Nhận dữ liệu nhập từ nhân viên.
* ReceiptNoteController: Tiếp nhận yêu cầu HTTP POST.
* ReceiptNoteService: Kiểm tra dữ liệu và thực thi quy tắc nghiệp vụ.
* PurchaseOrderRepository: Đọc thông tin đơn đặt hàng PO.
* ReceiptNoteRepository: Lưu phiếu nhập vào cơ sở dữ liệu.

### Quy trình và quy tắc xử lý
1. Kiểm tra đơn hàng: Lấy số lượng còn lại của PO để đối chiếu.
2. Kiểm tra các lỗi rẽ nhánh:
   * Nếu hàng giao nhiều hơn số lượng còn lại trên PO (BR-INB-01): Báo lỗi 400 Bad Request, từ chối nhận hàng thừa.
   * Nếu hạn sử dụng nhỏ hơn hoặc bằng ngày hiện tại (BR-LOT-01): Báo lỗi 400 Bad Request, từ chối nhận hàng hết hạn.
   * Nếu không điền vị trí cất hàng (BR-INB-03): Báo lỗi 400 Bad Request.
3. Lưu dữ liệu: Khi dữ liệu hợp lệ, tạo phiếu mới với trạng thái PENDING_APPROVAL và lưu mã nhân viên lập phiếu (makerId).
4. Kết quả: Trả về mã 201 Created kèm số phiếu. Tồn kho thực tế (On-hand) giữ nguyên không đổi (BR-INB-02).

---

## 2. SƠ ĐỒ PHÊ DUYỆT PHIẾU NHẬP KHO (UC03)

### Mục đích
Quản lý kho kiểm tra lại thông tin và ra quyết định phê duyệt. Khi duyệt thành công, hệ thống chính thức cộng hàng vào kho, cập nhật trạng thái ô kệ và khóa phiếu.

### Các lớp xử lý chính
* ApprovalController: Tiếp nhận lệnh duyệt qua HTTP POST.
* ApprovalService: Kiểm tra thẩm quyền và thực hiện giao dịch cập nhật kho (@Transactional).
* ReceiptNoteRepository, StockBatchRepository, LocationRepository, StockCardRepository, AuditLogRepository: Các kho dữ liệu liên quan để cập nhật trạng thái phiếu, số dư kho, trạng thái ô kệ, thẻ kho và lịch sử thao tác.

### Quy trình và quy tắc xử lý
1. Kiểm tra thẩm quyền Maker-Checker (BR-SEC-03):
   * Nếu người duyệt chính là người lập phiếu (checkerId trùng makerId): Hệ thống chặn ngay, trả về mã 403 Forbidden.
2. Kiểm tra trạng thái phiếu:
   * Nếu phiếu đã được xử lý từ trước: Trả về mã 409 Conflict.
3. Thực hiện giao dịch nguyên tử khi hợp lệ:
   * Đổi trạng thái phiếu sang COMPLETED và khóa không cho sửa xóa (BR-SEC-01).
   * Tăng số lượng hàng tồn thực tế (On-hand) trong bảng StockBatch.
   * Đổi trạng thái ô kệ chứa hàng từ EMPTY sang OCCUPIED (BR-CAT-03).
   * Ghi 1 dòng giao dịch vào Thẻ kho (StockCard) để ghi nhận số tồn lũy kế (BR-SEC-02).
   * Ghi nhận nhật ký phê duyệt (Audit Log).
4. Kết quả: Trả về mã 200 OK và gửi thông báo hoàn tất đến người lập.

---

## 3. SƠ ĐỒ LẬP PHIẾU XUẤT KHO VÀ ĐỀ XUẤT XUẤT LÔ (UC04)

### Mục đích
Nhân viên kho tạo yêu cầu xuất hàng. Hệ thống tự động kiểm tra tồn kho khả dụng, áp dụng thuật toán đề xuất xuất lô (FEFO/FIFO), chỉ định vị trí lấy hàng và tạm giữ chỗ (Reserve) để chống xuất âm.

### Các lớp xử lý chính
* Giao diện (Web UI): Nhận yêu cầu xuất hàng (SKU, số lượng cần xuất).
* IssueNoteController: Tiếp nhận yêu cầu HTTP POST.
* GoodsIssueService: Điều phối kiểm tra tồn kho và kích hoạt thuật toán cấp phát.
* AllocationEngine: Thuật toán phân bổ lô (lọc quá hạn, xếp theo FEFO/FIFO).
* StockBatchRepository: Lấy danh sách lô còn hạn và cập nhật số lượng giữ chỗ.
* IssueNoteRepository: Lưu phiếu xuất và bảng chỉ dẫn lấy hàng (Pick list).

### Quy trình và quy tắc xử lý
1. Kiểm tra tồn khả dụng:
   * Truy vấn các lô hàng còn hạn sử dụng (EXP lớn hơn ngày hiện tại, BR-OUT-03).
   * Nếu số lượng yêu cầu xuất lớn hơn tồn khả dụng Available (BR-OUT-01): Báo lỗi 400 Bad Request.
2. Đề xuất xuất lô thông minh:
   * Chuyển danh sách lô đủ điều kiện cho AllocationEngine.
   * Áp dụng quy tắc FEFO (hàng hạn ngắn xuất trước) hoặc FIFO (nhập trước xuất trước) theo BR-OUT-02.
   * Tạo bảng chỉ dẫn lấy hàng chi tiết theo ô kệ (BR-OUT-06).
3. Tạm giữ chỗ (Reserve Stock):
   * Tăng số lượng giữ chỗ (reserved_quantity).
   * Giảm số lượng khả dụng (available_quantity).
   * Giữ nguyên số lượng thực tế trong kho (on_hand_quantity) không đổi.
4. Lưu dữ liệu:
   * Lưu phiếu xuất kho với trạng thái PENDING_APPROVAL.
5. Kết quả: Trả về mã 201 Created kèm bảng chỉ dẫn lấy hàng (Pick list) để chờ quản lý duyệt.

---

## 4. BẢNG SO SÁNH TỔNG HỢP 3 USE CASE

| Tiêu chí | Lập phiếu nhập (UC02) | Phê duyệt phiếu nhập (UC03) | Lập phiếu xuất (UC04) |
| :--- | :--- | :--- | :--- |
| **Người thực hiện** | Nhân viên kho (Maker) | Quản lý kho (Checker) | Nhân viên kho (Maker) |
| **Trạng thái phiếu** | PENDING_APPROVAL | COMPLETED (Khóa vĩnh viễn) | PENDING_APPROVAL |
| **Tồn kho thực tế (On-hand)** | Không thay đổi | Tăng lên tương ứng | Không thay đổi |
| **Tồn khả dụng (Available)** | Không thay đổi | Tăng lên tương ứng | Giảm xuống (do bị giữ chỗ) |
| **Trạng thái vị trí kho** | Giữ nguyên (tạm gán) | Chuyển sang OCCUPIED | Giữ nguyên (chỉ định trên Pick list) |
| **Quy tắc nghiệp vụ cốt lõi** | Chống nhận hàng thừa (BR-INB-01), kiểm tra hạn dùng (BR-LOT-01) | Không tự duyệt phiếu của chính mình (BR-SEC-03) | Chống xuất âm (BR-OUT-01), thuật toán FEFO/FIFO (BR-OUT-02) |
| **Tính chất thao tác dữ liệu** | Ghi nhận bản ghi chờ duyệt | Giao dịch nguyên tử nhiều bảng cùng lúc | Giữ chỗ (Reserve) và tạo Pick list |