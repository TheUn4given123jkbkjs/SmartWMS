# ĐẶC TẢ YÊU CẦU PHI CHỨC NĂNG (NON-FUNCTIONAL REQUIREMENTS - NFR)
**Tiêu chuẩn áp dụng:** ISO/IEC 25010:2011 

## THIẾT LẬP MÔI TRƯỜNG KIỂM THỬ (TESTING ENVIRONMENTS)
*Để các chỉ số định lượng NFR có giá trị nghiệm thu, chúng phải được đo lường trong các điều kiện môi trường tiêu chuẩn sau:*

| Mã | Loại môi trường | Thông số cấu hình chi tiết để nghiệm thu |
| :---: | :--- | :--- |
| **E1** | **Môi trường mạng** *(Network Environment)* | Mạng LAN/WIFI nội bộ nhà máy; Băng thông tối thiểu: 50 Mbps; Độ trễ (Ping) đến Server < 10ms. |
| **D1** | **Thiết bị trạm** *(Client Device)* | PC/Laptop văn phòng: CPU Core i3/i5 thế hệ 8+, RAM 8GB. Trình duyệt: Google Chrome (v90+) hoặc Microsoft Edge (v90+). |
| **L1** | **Môi trường Tải** *(Load Environment)* | Dữ liệu nền: $\ge 10.000$ mã SKU, $\ge 50.000$ dòng lịch sử thẻ kho. Mức tải: 50 kết nối (Concurrent users) thao tác đồng thời. |

---

## 1. Yêu cầu về Hiệu năng (Performance Efficiency)

| Mã NFR | Tiêu chí ISO | Mô tả yêu cầu | Chỉ số định lượng (Metrics) | Môi trường |
| :--- | :--- | :--- | :--- | :---: |
| **NFR-PER-01** | Time Behaviour | Thời gian tải trang (Page Load Time) đối với các màn hình giao diện (Thymeleaf HTML). | **< 2.0 giây** cho trang thường.<br>**< 5.0 giây** cho trang Báo cáo tồn chứa dữ liệu lớn. | **E1, D1** |
| **NFR-PER-02** | Time Behaviour | Thời gian thực thi các giao dịch có tính toán nghiệp vụ (VD: Duyệt phiếu xuất, thuật toán FIFO/FEFO). | **< 1.5 giây** / giao dịch. | **E1, L1** |
| **NFR-PER-03** | Capacity | Hệ thống phải chịu tải được lượng người dùng truy cập và thao tác cùng lúc mà không bị treo (Deadlock). | Hỗ trợ xử lý mượt mà tối thiểu **50 User kết nối đồng thời**. | **L1** |
| **NFR-PER-04** | Resource Utilization | Tối ưu hóa tài nguyên phần cứng của Server khi chạy ứng dụng Spring Boot. | Ứng dụng tiêu thụ RAM JVM **< 1024 MB** ở trạng thái hoạt động bình thường. | **L1** |

## 2. Yêu cầu về Tính khả dụng (Usability)

| Mã NFR | Tiêu chí ISO | Mô tả yêu cầu | Chỉ số định lượng (Metrics) | Môi trường |
| :--- | :--- | :--- | :--- | :---: |
| **NFR-USA-01** | Operability | Tối ưu hóa số lần nhấp chuột (click) cho các luồng nghiệp vụ lõi (Nhập kho, Xuất kho) để tăng tốc độ làm việc. | Tối đa **4 clicks** từ màn hình Dashboard để hoàn tất lập một Phiếu nhập/xuất nháp. | **D1** |
| **NFR-USA-02** | Learnability | Hệ thống phải dễ hiểu, các thuật ngữ rõ ràng để nhân sự mới có thể tiếp quản nhanh. | Nhân viên kho mới tự thao tác luồng cơ bản sau **< 2 giờ** đào tạo. | **D1** |
| **NFR-USA-03** | User Error Protection | Ngăn chặn người dùng nhập sai kiểu dữ liệu hoặc thao tác nhầm lẫn nhờ tầng Validation và JS. | **100%** form nhập liệu bắt lỗi Client (hiển thị màu đỏ) trong thời gian **< 0.5 giây**. | **D1** |

## 3. Yêu cầu về Tính bảo mật (Security)

| Mã NFR | Tiêu chí ISO | Mô tả yêu cầu | Chỉ số định lượng (Metrics) | Môi trường |
| :--- | :--- | :--- | :--- | :---: |
| **NFR-SEC-01** | Confidentiality | Không ai có thể đọc được mật khẩu thực sự của người dùng trong CSDL. | **100%** mật khẩu được mã hóa băm (Hash) bằng thuật toán **BCrypt**. | **L1** |
| **NFR-SEC-02** | Authenticity | Kiểm soát phiên đăng nhập để tránh rủi ro quên đăng xuất tại máy tính chung. | Tự động Log-out (Session Timeout) sau **30 phút** không có thao tác chuột/phím. | **D1** |
| **NFR-SEC-03** | Accountability | Mọi hành vi làm thay đổi dữ liệu kho (Thêm, Sửa, Xóa, Duyệt) đều phải lưu vết (Audit Trail). | **100%** các bảng nghiệp vụ có đủ 4 trường: `CreatedBy`, `CreatedDate`, `ModifiedBy`, `ModifiedDate`. | **L1** |
| **NFR-SEC-04** | Authorization | Chỉ những Role (Vai trò) hợp lệ mới được truy cập các URL hoặc nút bấm tương ứng. | **100%** Endpoint API (`/controller`) được bảo vệ bằng phân quyền (`@PreAuthorize`). | **L1** |

## 4. Yêu cầu về Tính tin cậy (Reliability)

| Mã NFR | Tiêu chí ISO | Mô tả yêu cầu | Chỉ số định lượng (Metrics) | Môi trường |
| :--- | :--- | :--- | :--- | :---: |
| **NFR-REL-01** | Availability | Hệ thống phải hoạt động ổn định trong suốt các ca làm việc của nhà máy. | Uptime đạt **99.9%** trong khung giờ 06:00 - 22:00. | **E1, L1** |
| **NFR-REL-02** | Fault Tolerance | Tránh tình trạng "Trừ tồn thành công nhưng không ghi thẻ kho" do đứt cáp, sập nguồn. | **100%** giao dịch tuân thủ ACID (`@Transactional`), Rollback hoàn toàn nếu có Exception. | **E1, L1** |
| **NFR-REL-03** | Recoverability | Thời gian khôi phục dịch vụ khi ứng dụng bị dừng đột ngột (Crash). | Khởi động lại Server (RTO) mất **< 30 giây**. | **E1** |

## 5. Yêu cầu về Tính bảo trì (Maintainability)

| Mã NFR | Tiêu chí ISO | Mô tả yêu cầu | Chỉ số định lượng (Metrics) | Môi trường |
| :--- | :--- | :--- | :--- | :---: |
| **NFR-MNT-01** | Modularity | Tuân thủ tuyệt đối Kiến trúc Phân lớp đã định nghĩa trong hướng dẫn mã nguồn. | **0%** code SQL/Logic nghiệp vụ được phép tồn tại ở tầng `Controller`. | *(Mã nguồn)* |
| **NFR-MNT-02** | Testability | Mức độ bao phủ của mã kiểm thử tự động (Unit Test) đối với các hàm xử lý thuật toán phức tạp. | Độ phủ mã (Code Coverage) của tầng `service` đạt tối thiểu **> 80%**. | *(Môi trường Dev)* |

## 6. Yêu cầu về Khả năng di động & Tương thích (Portability & Compatibility)

| Mã NFR | Tiêu chí ISO | Mô tả yêu cầu | Chỉ số định lượng (Metrics) | Môi trường |
| :--- | :--- | :--- | :--- | :---: |
| **NFR-POR-01** | Portability | Khả năng triển khai phần mềm không phụ thuộc vào hệ điều hành. | Chạy ổn định **100% tính năng** bằng file `.jar` trên Windows, Linux và macOS. | **E1** |
| **NFR-POR-02** | Browser Compatibility | Giao diện Bootstrap 5/Thymeleaf phải render chuẩn xác trên các trình duyệt phổ biến. | Hiển thị không vỡ khung trên độ phân giải **1024x768** trở lên. | **D1** |
