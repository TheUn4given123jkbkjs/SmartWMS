# Kịch bản demo SmartWMS — FR01–FR04

## Chuẩn bị

Chạy `mvn spring-boot:run` trong `source/`, mở `http://localhost:8088/auth/login`.
Dùng hai trình duyệt hoặc một cửa sổ thường và một cửa sổ ẩn danh để giữ hai session:

- **Maker (Thủ kho):** `staff_thien` (hoặc `staff_quan`) / `123456`.
- **Checker & Quản lý danh mục (Quản lý kho):** `manager_trung` (hoặc `manager_khang`) / `123456`.

Khi khởi tạo mới có 6 SKU, 8 vị trí chuẩn 3M và 3 PO đã duyệt `PO-DEMO-001`…`003`; chưa có tồn vật lý.

## Luồng demo chính

1. **Danh mục — FR01:** dùng Manager (`manager_trung`) mở Danh Mục Hàng Hóa, thử nút “Thiết lập Min/Max”, nhập Min `5.25`, Max `100.50` cho SKU muốn demo. Ô kệ cho tạo mã vị trí mới theo chuẩn 3M (`KHU-DÃY-KỆ-TẦNG-Ô`, VD: `RAW-C01-01`), chọn khu `RAW`/`FG` và sức chứa thiết kế (hệ thống tự động phân rã tọa độ Dãy/Kệ/Tầng/Ô trên sơ đồ kho). Nếu tạo SKU mới, Manager cần tiếp nhận một PO đã duyệt cho SKU đó trước khi nhập.
2. **Lập nhập — FR02.01–02:** với Staff mở Lập Phiếu Nhập, chọn `PO-DEMO-001`, chọn SKU đầu tiên, SL đạt `20.50`, số lô `DEMO-LOT-01`, EXP sau hôm nay, ví dụ còn 31 ngày, chọn `RAW-A01-01`. Có thể ghi hàng từ chối riêng kèm lý do. Bấm Gửi duyệt, mở link chứng từ. Lúc này On-hand vẫn 0.
3. **Duyệt nhập — FR02.03:** với Manager mở Phê Duyệt Nhập, mở phiếu vừa lập, bấm Chấp thuận và xác nhận. Phiếu thành COMPLETED; tồn thực tế là `20.50`, vị trí OCCUPIED. Duyệt lại bị chặn.
4. **Giữ chỗ xuất — FR03.01–02:** với Staff mở Lập Lệnh Xuất, nhập số đơn/đơn vị nhận, chọn đúng SKU, số lượng `5.25`, chiến lược FEFO. Bấm Gợi ý phân bổ để thấy lô/vị trí, rồi Tạo lệnh xuất. Tồn: On-hand `20.50`, Reserved `5.25`, Available `15.25`.
5. **Duyệt và bàn giao — FR03.03:** Manager mở Bốc Hàng Pick List, mở lệnh và Chấp thuận. Tồn vật lý vẫn `20.50`. Staff mở lại lệnh, bấm Xác nhận bàn giao. Phiếu COMPLETED; On-hand `15.25`, Reserved `0`, Available `15.25`.
6. **Đối soát — FR04:** chọn SKU và ngày hôm nay trong Sổ Thẻ Kho. Có dòng nhập `+20.50`, xuất `−5.25`; tồn cuối `15.25`, có người thực hiện/người duyệt. Bảng lô/vị trí hỗ trợ lọc SKU, nhóm, khu và tình trạng. Cảnh Báo Tồn Kho hiển thị chạm Min và lô trong 30 ngày; hàng đang về/gợi ý bổ sung tính từ PO thật.

## Luồng ngoại lệ có thể trình bày

- Staff không duyệt được; không dùng một tài khoản giả làm cả Maker/Checker bằng `X-User-Id`.
- Nhập vượt số PO còn nhận hoặc EXP bằng/trước hôm nay bị chặn. Lập nhiều phiếu chờ không vượt tổng PO còn nhận.
- Xuất vượt Available bị chặn; số giữ chỗ của phiếu trước được trừ khỏi khả dụng phiếu sau.
- Manager từ chối hoặc Maker/Manager hủy phiếu xuất chưa hoàn tất: Reserved trở về trước khi tạo phiếu, On-hand không đổi; cần lý do.
- Dùng hai lô cùng SKU nhưng khác EXP để chứng minh FEFO; hàng không quản lý hạn dùng tự dùng FIFO. Chọn lô khác ưu tiên hoặc FIFO cho hàng quản lý hạn phải nhập lý do ngoại lệ.
- Xuất hết lô cuối trong vị trí → vị trí EMPTY. PO nhận đủ toàn bộ các dòng → CLOSED.
- Không sửa/xóa chứng từ đã hoàn tất hoặc sửa nhật ký thẻ kho.

## Kiểm thử tự động

Chạy `mvn test`. `MustHaveIntegrationTest` kiểm tra giao dịch, rollback, số lượng thập phân và hai phiếu đồng thời; `WorkspaceIntegrationTest` kiểm tra trang/session/danh mục/role.

---

## Bộ Câu Hỏi & Trả Lời Phản Biện Hội Đồng (Q&A Defense — `T6.3`)

1. **Tại sao bảng `stock_batch` không lưu cột `available_qty` (Tồn khả dụng)?**
   * **Trả lời:** Để tuân thủ tuyệt đối **Dạng chuẩn 3 (3NF)** — khử phụ thuộc hàm bắc cầu $(\text{on\_hand\_qty}, \text{reserved\_qty}) \rightarrow \text{available\_qty}$. Tồn khả dụng của một mặt hàng phụ thuộc vào hai biến số biến động liên tục là tồn thực tế, lượng đang giữ chỗ và cả ngày hiện tại so với hạn dùng ($\text{EXP} \ge \text{CURRENT\_DATE}$). Việc tính toán động theo thời gian thực loại bỏ hoàn toàn nguy cơ bất đồng bộ số liệu (Data Anomaly).

2. **Tại sao hệ thống chỉ cần lưu duy nhất cột `location_code` trong bảng `warehouse_location` mà trên sơ đồ kho vẫn hiển thị được đầy đủ `Dãy / Kệ / Tầng / Ô`?**
   * **Trả lời:** Theo quy tắc `BR-CAT-02`, mã vị trí được chuẩn hóa theo cấu trúc phân cấp 3M Floor Marking `ZONE-AISLE-RACK-SHELF-BIN` (ví dụ: `RAW-A01-02` hoặc `FG-B-01-02-01`). Tầng ứng dụng sử dụng hàm phân rã tọa độ tự động (`@Transient parseCoordinates()`) để bóc tách Dãy, Kệ, Tầng, Ô trực tiếp từ `location_code`, giúp CSDL đạt chuẩn 3NF mà không cần lưu 4 cột vật lý dư thừa.

3. **Hệ thống xử lý thế nào đối với phụ tùng cơ khí không có hạn sử dụng (`manage_by_lot = FALSE` như bugi, ốc vít, gương chiếu hậu)?**
   * **Trả lời:** Theo quy tắc `BR-LOT-01` và `BR-OUT-02`, đối với mặt hàng có `manage_by_lot = FALSE`, cột `exp_date` trên `receipt_note_item` và `stock_batch` được phép mang giá trị `NULL`; nếu nhân viên để trống số lô thì hệ thống tự sinh mã lô theo phiếu nhập để định vị ô kệ. Khi xuất kho, hệ thống tự động chuyển sang thuật toán **FIFO (Nhập trước xuất trước dựa theo `created_at`)**, còn hàng có hạn dùng (dầu nhớt, ắc quy, hóa chất) bắt buộc áp dụng **FEFO (Hết hạn trước xuất trước)**.

4. **Làm sao hệ thống ngăn chặn việc người lập phiếu tự phê duyệt phiếu của chính mình hoặc sửa phiếu sau khi đã duyệt?**
   * **Trả lời:** Hệ thống áp dụng kiểm soát kép **Maker-Checker (`BR-SEC-03`)** ở cả 3 tầng: (1) Tầng Giao diện ẩn/khóa nút duyệt nếu `maker_id == current_user_id`; (2) Tầng Service kiểm tra vai trò `ROLE_MANAGER` và ném `SecurityViolationException` nếu trùng người lập; (3) Tầng CSDL MySQL có ràng buộc cứng `CONSTRAINT chk_maker_checker CHECK (checker_id IS NULL OR checker_id <> maker_id)`. Khi chứng từ đã sang `COMPLETED`, mọi thao tác sửa/xóa đều bị khóa bất biến (`BR-SEC-01`).

5. **Nếu một lô hàng lúc lập phiếu xuất chưa hết hạn (đã giữ chỗ `Reserved`), nhưng đến lúc Quản lý bấm Duyệt hoặc Thủ kho bấm Xác nhận bàn giao thì lô đó vừa qua ngày hết hạn, hệ thống xử lý ra sao?**
   * **Trả lời:** Hệ thống áp dụng cơ chế **Chặn kép lô quá hạn (`BR-OUT-03`)**: Kiểm tra lại hạn dùng $\text{EXP} < \text{CURRENT\_DATE}$ tại cả thời điểm **Duyệt phiếu (`approveIssueNote`)** lẫn thời điểm **Xác nhận thực xuất (`confirmDispatch`)**. Nếu phát hiện lô đã quá hạn, hệ thống chặn giao dịch (`409 Conflict`) và yêu cầu từ chối/hủy lệnh để giải phóng lượng `Reserved`, buộc lập lệnh mới từ lô còn hạn.

6. **Tại sao không có màn hình quản trị tài khoản riêng cho Admin trong bản Demo?**
   * **Trả lời:** Theo phương pháp phân tích ưu tiên **MoSCoW**, nhóm tập trung 100% nguồn lực Giai đoạn 1 (`Must-have FR01–FR04`) để giải quyết 10 vấn đề cốt lõi tại kho cho **2 nhóm Actor vận hành chính** là **Thủ kho (`ROLE_STAFF` - Maker)** và **Quản lý kho (`ROLE_MANAGER` - Checker)**. Giao diện quản trị tài khoản Admin chuyên biệt (`FR09.02`) được xếp ở mức **`Could-have`** vì nhân sự kho cố định, ít biến động hàng ngày và không ảnh hưởng trực tiếp tới luồng luân chuyển vật tư.
