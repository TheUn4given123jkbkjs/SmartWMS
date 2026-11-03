# BẢNG ƯU TIÊN HÓA YÊU CẦU THEO PHƯƠNG PHÁP MoSCoW
<!-- 
Tài liệu thuộc Sprint 1 (Task T1.2)
Tham chiếu tiêu chuẩn: SpecificationStandard.md (Mục 1.4)
Quy cách:
- Bảng phân loại 4 mức: Must-have, Should-have, Could-have, Won't-have.
- Có cột giải trình căn cứ xếp loại và đánh dấu [x] cài đặt Demo Java.
-->

## 1. NGUYÊN TẮC ÁP DỤNG MoSCoW
Để tối ưu hóa nguồn lực triển khai của nhóm 4 thành viên trong thời lượng học phần Phân tích và Thiết kế Yêu cầu (Mã môn: 502050), nhóm áp dụng mô hình phân loại ưu tiên **MoSCoW** nhằm phân định ranh giới rõ ràng giữa các chức năng bắt buộc phải cài đặt demo và các tính năng mở rộng:

* **Must-have (Bắt buộc phải có):** Các chức năng sống còn, quyết định quy trình vận hành kho tối thiểu (Minimum Viable Product - MVP). Khống chế tối đa 4–5 phân hệ lõi, **bắt buộc cài đặt 100% bằng Java Spring Boot và demo trực tiếp**.
* **Should-have (Nên có):** Các chức năng quan trọng nhưng có thể tách biệt chu kỳ (như kiểm kê, xuất hủy cách ly, báo cáo tổng hợp), hệ thống vẫn có thể vận hành giao dịch hàng ngày nếu thiếu chúng trong giai đoạn 1.
* **Could-have (Có thể có):** Các tính năng nâng cao trải nghiệm người dùng (Dashboard trực quan, xuất báo cáo ra file Excel/PDF).
* **Won't-have (Chưa làm trong giai đoạn này):** Các tính năng ngoài phạm vi đồ án học phần (kèm theo lý do loại biên giải trình minh bạch).

---

## 2. BẢNG PHÂN LOẠI DANH MỤC YÊU CẦU TOÀN HỆ THỐNG

### Bảng 1: Bảng phân loại ưu tiên MoSCoW cho các nhóm chức năng SmartWMS

| Mã Nhóm FR | Tên Nhóm Chức Năng | Phân loại MoSCoW | Căn cứ & Tiêu chí xếp loại nghiệp vụ | Cài đặt Demo Java |
| :---: | :--- | :---: | :--- | :---: |
| **FR01** | **Quản lý Danh mục Kho & Hàng hóa** | **Must-have** | Thiết lập cấu trúc dữ liệu nền tảng (SKU, ĐVT, Vị trí Ô kệ Zone-Shelf-Bin, Định mức Min/Max); điều kiện tiên quyết để hệ thống vận hành. | [x] Bắt buộc demo |
| **FR02** | **Quản lý Nhập kho theo PO & Phê duyệt Maker-Checker** | **Must-have** | Quy trình đầu vào huyết mạch; ghi nhận lô/vị trí, đối chiếu đơn mua PO và tăng tồn kho thực tế qua thẩm quyền xét duyệt 2 bước độc lập. | [x] Bắt buộc demo |
| **FR03** | **Quản lý Xuất kho, Cơ chế Giữ chỗ (Reserve) & Gợi ý FEFO/FIFO** | **Must-have** | Nghiệp vụ trung tâm xử lý bài toán chống xuất âm/tranh chấp hàng, tự động thuật toán hóa xuất lô hạn ngắn và trừ tồn kho vật lý. | [x] Bắt buộc demo |
| **FR04** | **Tra cứu Tồn kho Khả dụng, Thẻ kho & Cảnh báo Dự trữ** | **Must-have** | Cung cấp thông tin thời gian thực xóa bỏ độ trễ số liệu, tự động kích hoạt cảnh báo tồn thấp và danh sách đặt hàng bù trừ tránh đặt trùng. | [x] Bắt buộc demo |
| **FR05** | Quản lý Đợt Kiểm kê & Xử lý Chênh lệch Số liệu | **Should-have** | Rất quan trọng để duy trì độ tin cậy của sổ sách nhưng có thể tách biệt chu kỳ kiểm đếm định kỳ mà không chặn luồng nhập xuất hàng ngày. | [ ] Không demo |
| **FR06** | Quản lý Cách ly & Lập Phiếu Xuất Hủy Hàng Quá hạn | **Should-have** | Xử lý chứng từ chuyên biệt cho hàng hỏng/hết hạn cách ly tại khu `SCRAP`; có thể tạm xử lý qua phiếu xuất điều chỉnh trong giai đoạn đầu. | [ ] Không demo |
| **FR07** | Báo cáo Thống kê Tổng hợp Nhập - Xuất - Tồn | **Should-have** | Tổng hợp số liệu phục vụ kế toán và quản lý cấp cao; có thể truy xuất gián tiếp qua bảng dữ liệu tồn kho thời gian thực. | [ ] Không demo |
| **FR08** | Bảng điều khiển Quản trị Trực quan (Dashboard Metrics) | **Could-have** | Biểu đồ trực quan hóa dữ liệu thống kê nâng cao trải nghiệm người dùng; không ảnh hưởng logic xử lý giao dịch kho. | [ ] Không demo |
| **FR09** | Xuất dữ liệu Thẻ kho & Báo cáo sang định dạng Excel/PDF | **Could-have** | Tính năng tiện ích phụ trợ kết xuất file; chỉ triển khai khi hoàn tất toàn bộ các phân hệ lõi. | [ ] Không demo |
| **FR10** | Tích hợp Đồng bộ trực tuyến với ERP/MES qua Web API | **Won't-have** | Ngoài phạm vi Giai đoạn 1 do giới hạn công nghệ môi trường học phần và thiếu hệ sinh thái ERP thật từ doanh nghiệp. | [ ] Không demo |
| **FR11** | Tối ưu hóa Sơ đồ Xếp dỡ và Mô phỏng Kho 3D | **Won't-have** | Phức tạp về mặt thuật toán hình học không gian và đồ họa; vượt quá nguồn lực của nhóm 4 thành viên. | [ ] Không demo |
| **FR12** | Quản lý Chào thầu, Đấu thầu & Theo dõi Hợp đồng Mua sắm | **Won't-have** | Thuộc phân hệ Mua hàng mở rộng (Procurement Management), hệ thống chỉ tiếp nhận thông tin PO đã duyệt làm căn cứ giao nhận. | [ ] Không demo |
| **FR13** | Tự động gửi Tin nhắn SMS / Email Cảnh báo Tức thời | **Won't-have** | Giảm thiểu phụ thuộc dịch vụ cổng thông tin bên thứ 3 có trả phí; hệ thống tập trung thông báo nội bộ trực tiếp trên Web UI. | [ ] Không demo |

---

## 3. GIẢI TRÌNH CĂN CỨ LOẠI BIÊN CÁC YÊU CẦU WON'T-HAVE

Tuân thủ quy chuẩn của môn học, mọi yêu cầu xếp loại **Won't-have** đều phải có lý do kỹ thuật và nghiệp vụ giải trình minh bạch:

1. **FR10 - Tích hợp Đồng bộ trực tuyến với ERP/MES qua Web API:**
   * *Lý do loại:* Môi trường nghiên cứu học phần không có hệ thống ERP/MES thực tế từ đối tác doanh nghiệp. Việc giả lập các API phức tạp sẽ gây phân tán nguồn lực nhóm. Cấu trúc CSDL của SmartWMS được thiết kế chuẩn 3NF sẵn sàng để mở rộng kết nối trong tương lai.
2. **FR11 - Tối ưu hóa Sơ đồ Xếp dỡ và Mô phỏng Không gian Kho 3D:**
   * *Lý do loại:* Đòi hỏi nền tảng đồ họa WebGL/Three.js và dữ liệu kích thước hình học 3 chiều của từng pallet, vượt quá thời lượng và mục tiêu trọng tâm của môn Phân tích và Thiết kế Yêu cầu.
3. **FR12 - Quản lý Chào thầu, Đấu thầu & Quản trị Hợp đồng Mua sắm Toàn diện:**
   * *Lý do loại:* Thuộc phạm vi của hệ thống Quản lý Mua hàng (Procurement System); SmartWMS đóng vai trò là hệ thống tiếp nhận PO đã duyệt để đối chiếu số lượng nhập kho.
4. **FR13 - Tự động Gửi Thông báo Cảnh báo qua SMS Gateway / Dịch vụ Email bên thứ 3:**
   * *Lý do loại:* Đòi hỏi tích hợp các dịch vụ bên thứ ba có trả phí (Twilio, SendGrid); hệ thống tập trung toàn bộ cảnh báo trên màn hình Dashboard và danh sách thông báo In-app.
