# Kịch bản demo SmartWMS — FR01–FR04

## Chuẩn bị

Chạy `mvn spring-boot:run` trong `source/`, mở `http://localhost:8088/auth/login`.
Dùng hai trình duyệt hoặc một cửa sổ thường và một cửa sổ ẩn danh để giữ hai session:

- **Maker:** `staff_thien` / `123456`.
- **Checker:** `manager_trung` / `123456`.
- **Danh mục:** `admin` / `123456` có quyền tạo SKU/ô kệ, không duyệt nhập/xuất.

H2 nằm trong RAM; mỗi lần khởi động lại trở về dữ liệu demo. Có 6 SKU, 8 vị trí và 3 PO đã duyệt `PO-DEMO-001`…`003`; chưa có tồn vật lý.

## Luồng demo chính

1. **Danh mục — FR01:** dùng Manager/Admin mở Danh Mục Hàng Hóa, thử nút “Thiết lập Min/Max”, nhập Min `5.25`, Max `100.50` cho SKU muốn demo. Ô kệ cho tạo mã mới, khu RAW/FG và tầng. Nếu tạo SKU mới, Manager cần tiếp nhận một PO đã duyệt cho SKU đó trước khi nhập.
2. **Lập nhập — FR02.01–02:** với Staff mở Lập Phiếu Nhập, chọn `PO-DEMO-001`, chọn SKU đầu tiên, SL đạt `20.50`, số lô `DEMO-LOT-01`, EXP sau hôm nay ít nhất 30 ngày, chọn `RAW-A01-01`. Có thể ghi hàng từ chối riêng kèm lý do. Bấm Gửi duyệt, mở link chứng từ. Lúc này On-hand vẫn 0.
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
