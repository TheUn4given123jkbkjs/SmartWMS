# CHƯƠNG 1: GIỚI THIỆU VÀ PHẠM VI ĐỀ TÀI

---

## 1.1. LÝ DO CHỌN ĐỀ TÀI

Kho hàng là mắt xích nối liền và quyết định nhịp độ giữa mua hàng, sản xuất và phân phối giao hàng trong doanh nghiệp. Nếu số liệu kho bị sai lệch hoặc cập nhật chậm trễ, mọi bộ phận phía sau đều bị ảnh hưởng trực tiếp: dây chuyền sản xuất thiếu nguyên vật liệu đột ngột, phòng mua hàng rơi vào thế bị động dẫn đến đặt trùng hoặc đặt thiếu, bộ phận kế toán không thể đối chiếu chính xác giá trị tồn kho thực tế.

Nghiên cứu trường hợp (Case study) thực tế từ **VTI Solutions** công bố vào tháng 07/2025 mô tả chính xác tình huống này tại một tập đoàn công nghiệp Nhật Bản vận hành nhiều nhà máy tại Việt Nam, trong đó phần lớn kho hàng vẫn vận hành hoàn toàn thủ công với Excel, thẻ kho giấy và email — dẫn đến nhiều điểm nghẽn nghiêm trọng về độ chính xác dữ liệu, tốc độ phản ứng và kiểm soát nội bộ. Hiện trạng chi tiết được khảo sát và mô tả đầy đủ tại **Chương 2**.

Nhóm tác giả lựa chọn đề tài **"Phân tích và Thiết kế Hệ thống Quản lý Kho hàng (SmartWMS)"** xuất phát từ ba lý do chính:
1. **Tính thực tiễn cao:** Bài toán quản lý kho bãi xuất hiện ở hầu hết các doanh nghiệp sản xuất, phân phối và bán lẻ. Đề tài có các bên liên quan (stakeholders) và quy trình nghiệp vụ rõ ràng, nguồn dữ liệu đối sánh thực tế từ doanh nghiệp.
2. **Phù hợp mục tiêu học phần:** Đề tài cung cấp đầy đủ các thành phần để vận dụng toàn diện quy trình phân tích và thiết kế yêu cầu theo chuẩn BABOK: nhiều tác nhân (actors), nhiều quy trình nghiệp vụ liên hoàn (nhập kho, xuất kho, kiểm soát tồn, kiểm kê, cảnh báo), mô hình dữ liệu quan hệ chặt chẽ (sản phẩm, lô hàng, vị trí, chứng từ, đối tác) và nhiều luồng ngoại lệ nghiệp vụ phức tạp.
3. **Tính khả thi trong cài đặt:** Đề tài có thể khoanh vùng phạm vi vừa sức cho nhóm sinh viên nhưng vẫn bảo toàn đầy đủ các nghiệp vụ cốt lõi, đủ điều kiện để hiện thực hóa sản phẩm demo chạy thực tế bằng Java Spring Boot với 3–5 chức năng trọng tâm.

---

## 1.2. MỤC TIÊU ĐỀ TÀI

### 1.2.1. Mục tiêu tổng quát
Phân tích và thiết kế hệ thống Quản lý Kho hàng (SmartWMS) giúp doanh nghiệp số hóa toàn diện các nghiệp vụ tiếp nhận nhập kho, lưu trữ vị trí, xuất kho theo nguyên tắc FIFO/FEFO, theo dõi tồn kho theo thời gian thực, kiểm kê và cảnh báo tồn thấp; thay thế hoàn toàn phương thức vận hành thủ công bằng bảng tính Excel, thẻ kho giấy và email trao đổi rời rạc.

### 1.2.2. Mục tiêu cụ thể
1. Khảo sát và mô tả chính xác hiện trạng quản lý kho từ case study thực tế, xác định rõ các điểm nghẽn (bottlenecks) và nguyên nhân gốc rễ.
2. Xác định đầy đủ các bên liên quan (stakeholders), tác nhân tương tác (actors) và quy trình nghiệp vụ AS-IS / TO-BE.
3. Đặc tả chi tiết danh mục Yêu cầu chức năng (FR) và Yêu cầu phi chức năng (NFR), gán mã định danh duy nhất và phân loại độ ưu tiên theo mô hình MoSCoW.
4. Mô hình hóa yêu cầu bằng hệ thống sơ đồ UML chuẩn mực (Use Case, Sequence Diagram, Class Diagram/ERD) làm cầu nối sang thiết kế cơ sở dữ liệu quan hệ và giao diện người dùng.
5. Xây dựng prototype giao diện người dùng (Thymeleaf/Bootstrap 5) và cài đặt ứng dụng Web bằng Java Spring Boot 3.x cho các chức năng trọng tâm, bảo đảm tính nhất quán và khả năng truy vết xuyên suốt từ Vấn đề $\rightarrow$ Yêu cầu $\rightarrow$ Thiết kế $\rightarrow$ Mã nguồn và Kiểm thử.

### 1.2.3. Kết quả mong đợi của hệ thống (Định hướng cho phần đặc tả)
* **Cập nhật tức thời (Real-time):** Số liệu tồn kho thực tế ($On\text{-}hand$) được cập nhật ngay khi chứng từ nhập/xuất chuyển sang trạng thái hoàn tất, xóa bỏ độ trễ 12–24 giờ của việc nhập Excel cuối ngày.
* **Chứng từ điện tử và Vết kiểm toán:** Mỗi giao dịch kho đều được lập thành chứng từ điện tử, ghi nhận định danh người thực hiện, người phê duyệt và mốc thời gian phục vụ kiểm toán nội bộ.
* **Tự động hóa lấy hàng:** Hệ thống tự động gợi ý vị trí và số lô cần lấy xuất theo nguyên tắc FEFO (hạn dùng ngắn xuất trước) hoặc FIFO (nhập trước xuất trước).
* **Kiểm kê chuẩn hóa:** Quy trình kiểm kê điện tử hỗ trợ tự động tính chênh lệch giữa thực tế và sổ sách, quản lý biên bản giải trình và điều chỉnh tồn kho có thẩm quyền phê duyệt.
* **Cảnh báo dự trữ tự động:** Tự động phát cảnh báo trực quan trên giao diện khi tồn kho khả dụng chạm ngưỡng an toàn (Min Stock) hoặc có lô hàng cận hạn sử dụng trong vòng 30 ngày.

---

## 1.3. ĐỐI TƯỢNG VÀ BỐI CẢNH ÁP DỤNG

* **Bối cảnh doanh nghiệp:** Hệ thống hướng tới một công ty sản xuất - phân phối phụ tùng, linh kiện kỹ thuật (tương ứng với một nhà máy thành viên trong tập đoàn của case study). Doanh nghiệp sở hữu một kho trung tâm thực hiện chức năng kép: quản lý nguyên vật liệu đầu vào và quản lý thành phẩm đầu ra.
* **Đối tượng sử dụng chính:**
  - *Quản lý kho (Thủ kho trưởng):* Phê duyệt chứng từ, giám sát số liệu, tổ chức kiểm kê.
  - *Nhân viên kho (Thủ kho):* Thực hiện các thao tác nhập, xuất, kiểm đếm và cất hàng.
  - *Nhân viên Phòng Mua hàng:* Theo dõi mức tồn khả dụng và tiến độ giao hàng của các đơn mua hàng (PO).
  - *Bộ phận Kế hoạch Sản xuất / Bán hàng:* Tạo các yêu cầu xuất kho nguyên vật liệu hoặc thành phẩm.
  - *Bộ phận Kế toán kho & Ban Giám đốc:* Đối soát số liệu và xem báo cáo tổng hợp.
* **Quy mô áp dụng:** Đề tài lựa chọn mô hình **quản lý một kho vật lý duy nhất** để giữ phạm vi nghiên cứu vừa sức với nguồn lực nhóm, đồng thời cấu trúc cơ sở dữ liệu vẫn được thiết kế linh hoạt để sẵn sàng mở rộng mạng lưới đa kho trong các giai đoạn phát triển tiếp theo.

---

## 1.4. PHẠM VI HỆ THỐNG

Dự án thực tế của VTI Solutions có quy mô rất lớn (triển khai đa nhà máy, phần cứng PDA quét mã vạch chuyên dụng, tích hợp sâu ERP/MES, quản lý chào thầu mua sắm phức tạp). Nhóm tác giả thực hiện thu hẹp phạm vi có chọn lọc để phù hợp với thời lượng học phần và nguồn lực nhóm sinh viên:

### 1.4.1. Hệ thống làm (Trong phạm vi hệ thống)

### Bảng 1.1: Phạm vi chức năng của hệ thống SmartWMS

| Phân hệ nghiệp vụ | Nội dung chức năng cụ thể trong phạm vi |
| :--- | :--- |
| **Quản lý Danh mục** | Quản lý danh mục hàng hóa (mã SKU, tên, đơn vị tính cơ sở, phân loại, hạn sử dụng); danh mục Nhà cung cấp; danh mục Vị trí lưu kho theo cấu trúc phân vùng Khu vực - Ô kệ. |
| **Quản lý Nhập kho** | Tạo phiếu nhập kho từ Đơn mua hàng (PO) hoặc nhập bổ sung; ghi nhận số lượng thực nhận; ghi nhận thông tin Số lô (Lot No) và Hạn sử dụng (EXP); chỉ định vị trí cất hàng; phê duyệt phiếu nhập. |
| **Quản lý Xuất kho** | Tiếp nhận yêu cầu xuất kho; tự động kiểm tra tồn kho khả dụng; cơ chế tạm giữ chỗ ($Reserved$); tự động gợi ý xuất lô theo thuật toán FEFO/FIFO; chỉ dẫn vị trí lấy hàng; quản lý xuất hủy/thanh lý hàng quá hạn, hư hỏng theo chứng từ riêng biệt; phê duyệt phiếu xuất. |
| **Quản lý Tồn kho** | Tra cứu tồn kho trực quan theo thời gian thực (theo mã hàng, theo lô, theo vị trí); Thẻ kho điện tử ghi nhận chi tiết lịch sử biến động nhập - xuất của từng mặt hàng. |
| **Quản lý Kiểm kê** | Tạo đợt kiểm kê (định kỳ hoặc đột xuất); nhập số lượng kiểm đếm thực tế; hệ thống tự động đối soát và kết xuất báo cáo sai lệch thừa/thiếu; hỗ trợ điều chỉnh số tồn sau khi Quản lý duyệt. |
| **Cảnh báo & Đề xuất** | Cảnh báo trực quan trên giao diện (In-app Notification): cảnh báo tồn kho dưới mức tối thiểu (Min Stock); cảnh báo hàng cận hạn sử dụng 30 ngày; danh sách đề xuất số lượng cần đặt hàng bổ sung. |
| **Báo cáo Thống kê** | Báo cáo tổng hợp Nhập - Xuất - Tồn theo kỳ; báo cáo chi tiết theo mặt hàng/lô hàng; báo cáo kết quả chênh lệch kiểm kê; xuất dữ liệu báo cáo phục vụ kế toán. |
| **Hệ thống & Bảo mật** | Đăng nhập hệ thống; phân quyền người dùng theo vai trò (Quản lý kho, Thủ kho, Mua hàng); cơ chế kiểm soát chéo Maker-Checker; lưu vết nhật ký thao tác (Audit Log). |

### 1.4.2. Hệ thống không làm (Ngoài phạm vi hệ thống)
* Tích hợp thực tế qua API/Webservice với các hệ thống ERP, MES, hay phần mềm kế toán bên ngoài (chỉ thiết kế kiến trúc sẵn sàng kết nối).
* Trang bị phần cứng chuyên dụng: Máy quét mã vạch cầm tay PDA, cổng quét RFID, xe tự hành AGV (hệ thống cung cấp giao diện Web nhập liệu và tìm kiếm mã nhanh trên máy tính/màn hình cảm ứng).
* Quản lý mua sắm toàn diện: Quy trình đấu thầu, so sánh báo giá nhà cung cấp, quản lý hợp đồng kinh tế và theo dõi công nợ chi tiết (chỉ tiếp nhận thông tin PO đã duyệt làm căn cứ nhập kho).
* Tối ưu hóa vị trí lưu kho bằng sơ đồ mô phỏng 3D nâng cao.
* Quản trị mạng lưới đa nhà máy và quy trình điều chuyển hàng hóa liên kho.
* Tự động gửi thông báo qua SMS hoặc Email bên thứ ba (hệ thống tập trung cảnh báo trực tiếp trên màn hình Dashboard và danh sách thông báo in-app).
* Các thuật toán dự báo nhu cầu thị trường phức tạp hoặc tách/gộp bộ sản phẩm chuyên sâu ngành nội thất.

### 1.4.3. Giả định và ràng buộc
* **Ràng buộc phạm vi:** Một kho hàng vật lý duy nhất, sử dụng một đơn vị tiền tệ thống nhất (VND) và một ngôn ngữ giao diện chính thức (tiếng Việt).
* **Ràng buộc dữ liệu:** Dữ liệu danh mục ban đầu (sản phẩm, nhà cung cấp, vị trí) được nạp sẵn hoặc import từ bảng tính Excel mẫu. Mỗi mặt hàng sử dụng đúng một đơn vị tính cơ sở duy nhất.
* **Môi trường vận hành:** Người dùng thao tác thông qua trình duyệt Web hiện đại (Chrome, Edge, Firefox) trên máy tính làm việc tại kho hoặc văn phòng.
* **Sản phẩm cài đặt:** Ứng dụng demo được xây dựng trên nền tảng Java Web (Spring Boot 3.x, Spring Data JPA, Thymeleaf, MySQL/H2) và kịch bản demo chạy trên bộ dữ liệu kiểm thử chuẩn mực.

---

## 1.5. THUẬT NGỮ VÀ TỪ VIẾT TẮT

### Bảng 1.2: Bảng thuật ngữ và từ viết tắt

| Thuật ngữ | Tên tiếng Anh đầy đủ | Định nghĩa và Ý nghĩa nghiệp vụ |
| :--- | :--- | :--- |
| **WMS** | Warehouse Management System | Hệ thống phần mềm quản lý toàn diện các hoạt động vận hành kho bãi. |
| **PO** | Purchase Order | Đơn mua hàng (Đơn đặt hàng mua) do phòng Mua hàng gửi cho nhà cung cấp, làm căn cứ đối chiếu số lượng khi nhập kho. |
| **SKU** | Stock Keeping Unit | Mã định danh đơn vị lưu kho, là chuỗi ký tự duy nhất dùng để phân biệt từng mặt hàng cụ thể. |
| **FIFO** | First In, First Out | Nguyên tắc "Nhập trước, Xuất trước" – lô hàng nào nhập vào kho trước sẽ được ưu tiên xuất trước. |
| **FEFO** | First Expired, First Out | Nguyên tắc "Hết hạn trước, Xuất trước" – lô hàng nào có hạn sử dụng ngắn hơn sẽ được ưu tiên xuất trước. |
| **Lot / Batch** | Batch Number / Lot Number | Số lô sản xuất – tập hợp các sản phẩm được sản xuất cùng một đợt, có cùng ngày sản xuất và hạn dùng. |
| **EXP** | Expiration Date | Hạn sử dụng của sản phẩm – mốc thời gian mà sau đó hàng hóa bị cấm xuất kho hoặc sử dụng. |
| **On-hand** | On-hand Inventory | Số lượng tồn kho thực tế đang hiện diện vật lý trong kho tại thời điểm hiện tại. |
| **Reserved** | Reserved Inventory | Số lượng hàng tồn giữ chỗ – lượng hàng đã được cam kết cho các phiếu xuất đang xử lý nhưng chưa hoàn tất giao hàng. |
| **Available** | Available Inventory | Số lượng tồn kho khả dụng có thể xuất: $\text{Available} = \text{On-hand} - \text{Reserved} - \text{Hàng hỏng/Hết hạn}$. |
| **Min Stock** | Minimum Stock Level | Định mức tồn kho tối thiểu – ngưỡng an toàn mà khi số lượng chạm hoặc giảm xuống dưới mức này, hệ thống sẽ phát cảnh báo. |
| **Max Stock** | Maximum Stock Level | Định mức tồn kho tối đa – ngưỡng tồn trần nhằm tránh nguy cơ quá tải kho và ứ đọng vốn lưu động. |
| **Thẻ kho** | Stock Card | Bản ghi theo dõi chi tiết lịch sử phát sinh nhập, xuất và số tồn tích lũy của từng mặt hàng theo thời gian. |
| **Kiểm kê** | Stocktaking / Physical Inventory | Hoạt động kiểm đếm số lượng hàng hóa thực tế tại kho và đối chiếu với số liệu trên sổ sách quản lý. |
