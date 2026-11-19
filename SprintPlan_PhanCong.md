# KẾ HOẠCH SPRINT & PHÂN CÔNG CÔNG VIỆC (MAKER – REVIEWER)
**Học phần:** Phân tích và Thiết kế Yêu cầu (Mã môn: 502050 - ĐH Tôn Đức Thắng)  
**Thời gian điều chỉnh:** 23/10/2026 – 19/11/2026 (Hạn nộp chính thức: 23g59 ngày 19/11/2026)  
**Công nghệ chốt:** Java Web – Spring Boot 3.x (Spring Data JPA, MySQL/PostgreSQL, Thymeleaf / Bootstrap 5)  
**Quy tắc:** 1 Maker làm + 1 Reviewer duyệt (Maker $\neq$ Reviewer). SLA review $\le 24$h.

---

## I. DANH SÁCH THÀNH VIÊN NHÓM

| Mã | Họ và tên sinh viên | Mã số sinh viên (MSSV) | Trách nhiệm chính | Reviewer mặc định |
| :---: | :--- | :---: | :--- | :---: |
| **M1** | **Nguyễn Thành Trung** | [ &emsp;&emsp;&emsp;&emsp; ] | Nhóm trưởng | **M2** (Khang) |
| **M2** | **Nguyễn Bảo Khang** | [ &emsp;&emsp;&emsp;&emsp; ] | Thành viên | **M3** (Thiên) |
| **M3** | **Tôn Thanh Thiên** | [ &emsp;&emsp;&emsp;&emsp; ] | Thành viên | **M4** (Quân) |
| **M4** | **Thạch Hoàng Quân** | [ &emsp;&emsp;&emsp;&emsp; ] | Thành viên | **M1** (Trung) |

---

## II. KẾ HOẠCH PHÂN CÔNG CÔNG VIỆC THEO 6 SPRINT (ĐÃ ĐIỀU CHỈNH ĐẾN 19/11/2026)

### SPRINT 1: KHẢO SÁT & ĐẶC TẢ YÊU CẦU (SRS)
*Thời gian thực hiện: 23/10 – 27/10/2026 (Đã hoàn thành 100%)*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T1.1** | Xây dựng danh mục Quy tắc nghiệp vụ cốt lõi (BR) | **M2** (Nguyễn Bảo Khang) | **M3** (Tôn Thanh Thiên) | 24/10 | **Hoàn thành** |
| **T1.2** | Liệt kê danh mục 10–15 Yêu cầu chức năng (FR) & Bảng phân loại MoSCoW | **M4** (Thạch Hoàng Quân) | **M1** (Nguyễn Thành Trung) | 24/10 | **Hoàn thành** |
| **T1.3** | Đặc tả chi tiết 2 bảng cho nhóm FR Must-have (tập trung 4–5 phân hệ lõi) | **M1** (Nguyễn Thành Trung) | **M2** (Nguyễn Bảo Khang) | 25/10 | **Hoàn thành** |
| **T1.4** | Đặc tả Yêu cầu phi chức năng (NFR) theo ISO/IEC 25010 (có chỉ số định lượng) | **M3** (Tôn Thanh Thiên) | **M4** (Thạch Hoàng Quân) | 27/10 | **Hoàn thành** |

---

### SPRINT 2: MÔ HÌNH HÓA YÊU CẦU BẰNG UML
*Thời gian thực hiện: 28/10 – 04/11/2026 (Tập trung sâu vào 5–7 Use Case chính)*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T2.1** | Vẽ Biểu đồ Use Case tổng quát (toàn bộ hệ thống, actor, phân ranh giới) | **M3** (Tôn Thanh Thiên) | **M4** (Thạch Hoàng Quân) | 03/11 | **Hoàn thành** |
| **T2.2** | Đặc tả chi tiết 5–7 Use Case cốt lõi (Main flow, Alt flow, Exception flow) | **M2** (Nguyễn Bảo Khang) | **M3** (Tôn Thanh Thiên) | 03/11 | **Hoàn thành** |
| **T2.3** | Vẽ Biểu đồ lớp phân tích (Class Diagram) & ERD mức phân tích nghiệp vụ | **M1** (Nguyễn Thành Trung) | **M2** (Nguyễn Bảo Khang) | 04/11 | **Hoàn thành** |
| **T2.4** | Vẽ Biểu đồ tuần tự (Sequence Diagram) cho các Use Case trọng tâm (tối thiểu 3 SQ) | **M4** (Thạch Hoàng Quân) | **M1** (Nguyễn Thành Trung) | 04/11 | **Hoàn thành** |

---

### SPRINT 3: THIẾT KẾ CƠ SỞ DỮ LIỆU & COMPONENT DESIGN
*Thời gian thực hiện: 05/11 – 08/11/2026 (4 ngày)*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T3.1** | Thiết kế Lược đồ quan hệ CSDL 3NF & Viết mã SQL DDL (Khóa, ràng buộc) | **M1** (Nguyễn Thành Trung) | **M2** (Nguyễn Bảo Khang) | 07/11 | **Hoàn thành** |
| **T3.2** | Lập Bảng Từ điển dữ liệu (Data Dictionary 8 cột) cho các bảng chính | **M2** (Nguyễn Bảo Khang) | **M3** (Tôn Thanh Thiên) | 07/11 | **Hoàn thành** |
| **T3.3** | Thiết kế cấu trúc Component & Tổ chức Package Spring Boot (`entity`, `repository`, `service`, `controller`, `dto`) | **M3** (Tôn Thanh Thiên) | **M4** (Thạch Hoàng Quân) | 08/11 | **Hoàn thành** |
| **T3.4** | Thiết kế chữ ký hàm Service & REST API / Controller Endpoints (Request/Response DTO) | **M4** (Thạch Hoàng Quân) | **M1** (Nguyễn Thành Trung) | 08/11 | **Hoàn thành** |

---

### SPRINT 4: THIẾT KẾ GIAO DIỆN WEB (UI/UX) & TEST DESIGN
*Thời gian thực hiện: 09/11 – 12/11/2026 (4 ngày)*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T4.1** | Thiết kế Wireframe/Layout Web Dashboard & Forms (Thymeleaf / Bootstrap 5) | **M4** (Thạch Hoàng Quân) | **M1** (Nguyễn Thành Trung) | 11/11 | **Hoàn thành** |
| **T4.2** | Lập Bảng Đặc tả màn hình Web & Luồng điều hướng Route (SCR Navigation Flow) | **M3** (Tôn Thanh Thiên) | **M4** (Thạch Hoàng Quân) | 11/11 | **Hoàn thành** |
| **T4.3** | Thiết kế bộ Ca kiểm thử nghiệp vụ (**Test Design**: Positive `-P` và Negative `-N`) | **M2** (Nguyễn Bảo Khang) | **M3** (Tôn Thanh Thiên) | 12/11 | **Hoàn thành** |
| **T4.4** | Xây dựng Ma trận truy vết yêu cầu (RTM: Vấn đề $\rightarrow$ FR $\rightarrow$ UC $\rightarrow$ DB $\rightarrow$ SCR $\rightarrow$ TC) | **M1** (Nguyễn Thành Trung) | **M2** (Nguyễn Bảo Khang) | 12/11 | **Hoàn thành** |

---

### SPRINT 5: CÀI ĐẶT ỨNG DỤNG SPRING BOOT & CHẠY KIỂM THỬ (TEST EXECUTION)
*Thời gian thực hiện: 13/11 – 16/11/2026 (4 ngày - Demo 3–5 chức năng cốt lõi trên Web)*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T5.1** | Xây dựng tầng Dữ liệu & Nghiệp vụ (JPA Entities, Spring Repositories, Services `@Transactional`) | **M1** (Nguyễn Thành Trung) | **M2** (Nguyễn Bảo Khang) | 15/11 | **Hoàn thành** |
| **T5.2** | Xây dựng tầng Giao diện Web (Controllers, Thymeleaf Views / Web Forms, Bootstrap UI) | **M4** (Thạch Hoàng Quân) | **M1** (Nguyễn Thành Trung) | 15/11 | **Hoàn thành** |
| **T5.3** | Nối hoàn chỉnh luồng nghiệp vụ 3–5 chức năng Must-have chạy thực tế trên Web (localhost:8080/8088) | **M3** (Tôn Thanh Thiên) | **M4** (Thạch Hoàng Quân) | 16/11 | **Hoàn thành** |
| **T5.4** | Chạy kiểm thử (**Test Execution**) theo bộ Test Case đã thiết kế, lập biên bản test | **M2** (Nguyễn Bảo Khang) | **M3** (Tôn Thanh Thiên) | 16/11 | **Hoàn thành** |

---

### SPRINT 6: TÍCH HỢP BÁO CÁO WORD, SLIDE & TỔNG DUYỆT
*Thời gian thực hiện: 17/11 – 19/11/2026 (3 ngày - Hạn nộp 23g59 ngày 19/11/2026)*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T6.1** | Tích hợp toàn bộ nội dung vào file Word, căn chỉnh lề in, mục lục, kết luận | **M2** (Nguyễn Bảo Khang) | **M3** (Tôn Thanh Thiên) | 18/11 | **Hoàn thành** |
| **T6.2** | Thiết kế Slide thuyết trình nhóm (12–15 phút) | **M3** (Tôn Thanh Thiên) | **M4** (Thạch Hoàng Quân) | 18/11 | **Hoàn thành** |
| **T6.3** | Biên soạn Kịch bản Demo Script Web & Bộ câu hỏi phản biện Q&A | **M4** (Thạch Hoàng Quân) | **M1** (Nguyễn Thành Trung) | 19/11 (Trưa) | **Hoàn thành** |
| **T6.4** | Hoàn thiện Phụ lục đóng góp thành viên (tỷ lệ 100%) và đóng gói mã nguồn | **M1** (Nguyễn Thành Trung) | **M2** (Nguyễn Bảo Khang) | 19/11 (Chiều) | **Hoàn thành** |
| **T6.5** | Tổng kiểm duyệt chéo toàn bộ hồ sơ báo cáo trước khi nộp lên Elearning | **Cả 4 thành viên** | **Cả 4 thành viên** | **19/11 (23g59)** | **Hoàn thành** |

---

## III. BẢNG TỔNG HỢP KHỐI LƯỢNG CÔNG VIỆC (MAKER – REVIEWER)

| Thành viên | Danh sách Task phụ trách (Maker) | Số task Maker | Danh sách Task kiểm tra duyệt (Reviewer) | Số task Reviewer | Tỷ lệ đóng góp (%) |
| :---: | :--- | :---: | :--- | :---: | :---: |
| **M1 (Nguyễn Thành Trung)** | `T1.3`, `T2.3`, `T3.1`, `T4.4`, `T5.1`, `T6.4` | 6 task | `T1.2`, `T2.4`, `T3.4`, `T4.1`, `T5.2`, `T6.3` | 6 task | **25%** |
| **M2 (Nguyễn Bảo Khang)** | `T1.1`, `T2.2`, `T3.2`, `T4.3`, `T5.4`, `T6.1` | 6 task | `T1.3`, `T2.3`, `T3.1`, `T4.4`, `T5.1`, `T6.4` | 6 task | **25%** |
| **M3 (Tôn Thanh Thiên)** | `T1.4`, `T2.1`, `T3.3`, `T4.2`, `T5.3`, `T6.2` | 6 task | `T1.1`, `T2.2`, `T3.2`, `T4.3`, `T5.4`, `T6.1` | 6 task | **25%** |
| **M4 (Thạch Hoàng Quân)** | `T1.2`, `T2.4`, `T3.4`, `T4.1`, `T5.2`, `T6.3` | 6 task | `T1.4`, `T2.1`, `T3.3`, `T4.2`, `T5.3`, `T6.2` | 6 task | **25%** |
| **Cả nhóm** | `T6.5` (Tổng kiểm duyệt chéo toàn bộ sản phẩm và hồ sơ) | 1 task | `T6.5` (Toàn bộ 4 thành viên cùng kiểm tra chéo) | 1 task | Đồng đều |
| **Tổng cộng** | **24 task cá nhân + 1 task chung** | **25 task** | **24 task cá nhân + 1 task chung** | **25 task** | **100%** |

---

## IV. PHỤ LỤC CHI TIẾT ĐÓNG GÓP THÀNH VIÊN THEO TỪNG SPRINT (`T6.4`)

### 4.1. Bảng Ma trận Đóng góp Xuyên suốt 6 Sprint

| Giai đoạn (Sprint) | M1 – Nguyễn Thành Trung (Nhóm trưởng) | M2 – Nguyễn Bảo Khang (Thành viên) | M3 – Tôn Thanh Thiên (Thành viên) | M4 – Thạch Hoàng Quân (Thành viên) | Tỷ lệ hoàn thành |
| :--- | :--- | :--- | :--- | :--- | :---: |
| **Sprint 1**<br>*Khảo sát & Đặc tả SRS* | • **Maker `T1.3`:** Đặc tả chi tiết 2 bảng cho 12 yêu cầu chức năng Must-have (`FR01`–`FR04`).<br>• **Reviewer `T1.2`:** Rà soát bảng phân loại MoSCoW. | • **Maker `T1.1`:** Xây dựng danh mục 25 Quy tắc nghiệp vụ cốt lõi (`BR`) theo chuẩn BABOK v3.<br>• **Reviewer `T1.3`:** Thẩm định đặc tả chi tiết FR. | • **Maker `T1.4`:** Đặc tả Yêu cầu phi chức năng (`NFR`) theo chuẩn ISO/IEC 25010 có chỉ số SLA định lượng.<br>• **Reviewer `T1.1`:** Rà soát bộ quy tắc BR. | • **Maker `T1.2`:** Khảo sát hiện trạng Chương 1–2, tổng hợp 10 vấn đề (`P-01`..`P-10`) và bảng phân loại MoSCoW.<br>• **Reviewer `T1.4`:** Kiểm tra NFR. | **100%** |
| **Sprint 2**<br>*Mô hình hóa UML* | • **Maker `T2.3`:** Thiết kế Class Diagram (12 lớp Entity, 6 lớp Controller) & Lược đồ ERD 12 bảng chuẩn 3NF.<br>• **Reviewer `T2.4`:** Duyệt Sequence Diagram. | • **Maker `T2.2`:** Đặc tả chi tiết 7 Use Case cốt lõi (`UC01`–`UC07`) đầy đủ Main/Alt/Exception flows.<br>• **Reviewer `T2.3`:** Đối soát Class & ERD. | • **Maker `T2.1`:** Vẽ Biểu đồ Use Case tổng quát và sơ đồ phân rã theo phân hệ, xác định Actor & Boundary.<br>• **Reviewer `T2.2`:** Duyệt đặc tả 7 UC. | • **Maker `T2.4`:** Vẽ và giải trình từng bước 3 Biểu đồ Tuần tự trọng tâm (`SQ01`–`SQ03`).<br>• **Reviewer `T2.1`:** Kiểm tra sơ đồ Use Case. | **100%** |
| **Sprint 3**<br>*Thiết kế CSDL & Kiến trúc* | • **Maker `T3.1`:** Chứng minh chuẩn hóa 3NF, viết 12 kịch bản SQL DDL độc lập và `schema.sql`.<br>• **Reviewer `T3.4`:** Duyệt thiết kế REST API/DTO. | • **Maker `T3.2`:** Lập Từ điển dữ liệu (Data Dictionary 8 cột) cho toàn bộ 12 bảng CSDL.<br>• **Reviewer `T3.1`:** Kiểm tra khóa ngoại & CHECK SQL. | • **Maker `T3.3`:** Thiết kế kiến trúc phân tầng Component & tổ chức Package Spring Boot.<br>• **Reviewer `T3.2`:** Đối soát Từ điển dữ liệu. | • **Maker `T3.4`:** Thiết kế chữ ký hàm tầng Service, REST API Endpoints và Request/Response DTO.<br>• **Reviewer `T3.3`:** Duyệt Component Design. | **100%** |
| **Sprint 4**<br>*Thiết kế UI/UX & Kiểm thử* | • **Maker `T4.4`:** Xây dựng Ma trận truy vết yêu cầu 3 giai đoạn (`RTM`: `P-xx` $\rightarrow$ `FR` $\rightarrow$ `UC` $\rightarrow$ `DB` $\rightarrow$ `SCR` $\rightarrow$ `TC`).<br>• **Reviewer `T4.1`:** Duyệt giao diện Web. | • **Maker `T4.3`:** Thiết kế bộ 54 Ca kiểm thử nghiệp vụ (`test_cases.md`: 25 ca Positive, 29 ca Negative).<br>• **Reviewer `T4.4`:** Kiểm tra ma trận RTM. | • **Maker `T4.2`:** Đặc tả chi tiết 6 màn hình cốt lõi (`SCR01`–`SCR06`) và sơ đồ điều hướng Route.<br>• **Reviewer `T4.3`:** Thẩm định 54 Test Cases. | • **Maker `T4.1`:** Thiết kế Layout Web Dashboard, Sơ đồ mặt bằng kho 3M/5S và hệ thống Form.<br>• **Reviewer `T4.2`:** Kiểm tra đặc tả màn hình. | **100%** |
| **Sprint 5**<br>*Lập trình & Kiểm thử* | • **Maker `T5.1`:** Xây dựng tầng Entity, Repository, Service `@Transactional` (FEFO/FIFO, Maker-Checker, Thẻ kho).<br>• **Reviewer `T5.2`:** Duyệt tầng Controller/UI. | • **Maker `T5.4`:** Thực thi 53 bài test tự động (`JUnit 5` / `MockMvc`) đạt PASS 100%, lập báo cáo nghiệm thu.<br>• **Reviewer `T5.1`:** Review code Backend. | • **Maker `T5.3`:** Kết nối luồng tích hợp End-to-End 4 phân hệ Must-have và cấu hình dữ liệu khởi tạo.<br>• **Reviewer `T5.4`:** Kiểm chứng kết quả Test. | • **Maker `T5.2`:** Xây dựng Controllers, Thymeleaf Views, JavaScript tương tác bản đồ kho & chứng từ.<br>• **Reviewer `T5.3`:** Kiểm tra luồng tích hợp. | **100%** |
| **Sprint 6**<br>*Tích hợp, Slide & Tổng duyệt* | • **Maker `T6.4`:** Tổng hợp phụ lục đánh giá đóng góp, đồng bộ cầu nối XAMPP/Docker & đóng gói mã nguồn.<br>• **Reviewer `T6.3`:** Duyệt kịch bản Demo. | • **Maker `T6.1`:** Biên tập và định dạng toàn bộ tài liệu sang báo cáo Word hoàn chỉnh.<br>• **Reviewer `T6.4`:** Kiểm tra gói mã nguồn nộp. | • **Maker `T6.2`:** Thiết kế bộ Slide thuyết trình báo cáo đồ án cuối kỳ.<br>• **Reviewer `T6.1`:** Rà soát định dạng file Word. | • **Maker `T6.3`:** Biên soạn kịch bản Demo trực tiếp (`Demo_Script.md`) và bộ câu hỏi phản biện.<br>• **Reviewer `T6.2`:** Kiểm tra nội dung Slide. | **100%** |

### 4.2. Bảng Xác nhận Đánh giá Đóng góp Cuối kỳ

| Mã TV | Họ và tên sinh viên | Vai trò trong nhóm | Số Task Maker | Số Task Reviewer | Điểm đánh giá chéo | Tỷ lệ đóng góp | Xác nhận của thành viên |
| :---: | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **M1** | **Nguyễn Thành Trung** | Nhóm trưởng | 6/6 | 6/6 | 100 / 100 | **25%** | *Đã xác nhận* |
| **M2** | **Nguyễn Bảo Khang** | Thành viên | 6/6 | 6/6 | 100 / 100 | **25%** | *Đã xác nhận* |
| **M3** | **Tôn Thanh Thiên** | Thành viên | 6/6 | 6/6 | 100 / 100 | **25%** | *Đã xác nhận* |
| **M4** | **Thạch Hoàng Quân** | Thành viên | 6/6 | 6/6 | 100 / 100 | **25%** | *Đã xác nhận* |
| **Tổng** | **4 Thành viên** | **Đồng đều 100%** | **24 + 1 chung** | **24 + 1 chung** | **Hoàn thành đúng hạn** | **100%** | **Thống nhất 100%** |
