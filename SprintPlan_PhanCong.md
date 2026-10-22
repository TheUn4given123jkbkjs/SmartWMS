# KẾ HOẠCH SPRINT & PHÂN CÔNG CÔNG VIỆC (MAKER – REVIEWER)
**Học phần:** Phân tích và Thiết kế Yêu cầu (Mã môn: 502050 - ĐH Tôn Đức Thắng)  
**Thời gian:** 23/10/2026 – 09/11/2026 (Hạn nộp: 23g59 ngày 09/11/2026)  
**Công nghệ chốt:** Java Web – Spring Boot 3.x (Spring Data JPA, MySQL/PostgreSQL, Thymeleaf / Bootstrap 5)  
**Quy tắc:** 1 Maker làm + 1 Reviewer duyệt (Maker $\neq$ Reviewer). SLA review $\le 24$h.

---

## I. DANH SÁCH THÀNH VIÊN NHÓM

| Mã | Họ và tên sinh viên | Mã số sinh viên (MSSV) | Trách nhiệm chính | Reviewer mặc định |
| :---: | :--- | :---: | :--- | :---: |
| **M1** | [ &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; ] | [ &emsp;&emsp;&emsp;&emsp; ] | Nhóm trưởng | **M2** |
| **M2** | [ &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; ] | [ &emsp;&emsp;&emsp;&emsp; ] | Thành viên | **M3** |
| **M3** | [ &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; ] | [ &emsp;&emsp;&emsp;&emsp; ] | Thành viên | **M4** |
| **M4** | [ &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; ] | [ &emsp;&emsp;&emsp;&emsp; ] | Thành viên | **M1** |

---

## II. KẾ HOẠCH PHÂN CÔNG CÔNG VIỆC THEO 6 SPRINT (MỖI SPRINT 3 NGÀY)

### SPRINT 1: KHẢO SÁT & ĐẶC TẢ YÊU CẦU (SRS)
*Thời gian: 23/10 – 25/10/2026*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T1.1** | Xây dựng danh mục Quy tắc nghiệp vụ cốt lõi (BR) | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | 24/10 | Chờ làm |
| **T1.2** | Liệt kê danh mục 10–15 Yêu cầu chức năng (FR) & Bảng phân loại MoSCoW | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | 24/10 | Chờ làm |
| **T1.3** | Đặc tả chi tiết 2 bảng cho nhóm FR Must-have (tập trung 4–5 phân hệ lõi) | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | 25/10 | Chờ làm |
| **T1.4** | Đặc tả Yêu cầu phi chức năng (NFR) theo ISO/IEC 25010 (có chỉ số định lượng) | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | 25/10 | Chờ làm |

---

### SPRINT 2: MÔ HÌNH HÓA YÊU CẦU BẰNG UML
*Thời gian: 26/10 – 28/10/2026 (Tập trung sâu vào 5–7 Use Case chính)*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T2.1** | Vẽ Biểu đồ Use Case tổng quát (toàn bộ hệ thống, actor, phân ranh giới) | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | 27/10 | Chờ làm |
| **T2.2** | Đặc tả chi tiết 5–7 Use Case cốt lõi (Main flow, Alt flow, Exception flow) | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | 27/10 | Chờ làm |
| **T2.3** | Vẽ Biểu đồ lớp phân tích (Class Diagram) & ERD mức phân tích nghiệp vụ | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | 28/10 | Chờ làm |
| **T2.4** | Vẽ Biểu đồ tuần tự (Sequence Diagram) cho các Use Case trọng tâm (tối thiểu 3 SQ) | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | 28/10 | Chờ làm |

---

### SPRINT 3: THIẾT KẾ CƠ SỞ DỮ LIỆU & COMPONENT DESIGN
*Thời gian: 29/10 – 31/10/2026*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T3.1** | Thiết kế Lược đồ quan hệ CSDL 3NF & Viết mã SQL DDL (Khóa, ràng buộc) | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | 30/10 | Chờ làm |
| **T3.2** | Lập Bảng Từ điển dữ liệu (Data Dictionary 8 cột) cho các bảng chính | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | 30/10 | Chờ làm |
| **T3.3** | Thiết kế cấu trúc Component & Tổ chức Package Spring Boot (`entity`, `repository`, `service`, `controller`, `dto`) | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | 31/10 | Chờ làm |
| **T3.4** | Thiết kế chữ ký hàm Service & REST API / Controller Endpoints (Request/Response DTO) | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | 31/10 | Chờ làm |

---

### SPRINT 4: THIẾT KẾ GIAO DIỆN WEB (UI/UX) & TEST DESIGN
*Thời gian: 01/11 – 03/11/2026*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T4.1** | Thiết kế Wireframe/Layout Web Dashboard & Forms (Thymeleaf / Bootstrap 5) | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | 02/11 | Chờ làm |
| **T4.2** | Lập Bảng Đặc tả màn hình Web & Luồng điều hướng Route (SCR Navigation Flow) | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | 02/11 | Chờ làm |
| **T4.3** | Thiết kế bộ Ca kiểm thử nghiệp vụ (**Test Design**: Positive `-P` và Negative `-N`) | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | 03/11 | Chờ làm |
| **T4.4** | Xây dựng Ma trận truy vết yêu cầu (RTM: Vấn đề $\rightarrow$ FR $\rightarrow$ UC $\rightarrow$ DB $\rightarrow$ SCR $\rightarrow$ TC) | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | 03/11 | Chờ làm |

---

### SPRINT 5: CÀI ĐẶT ỨNG DỤNG SPRING BOOT & CHẠY KIỂM THỬ (TEST EXECUTION)
*Thời gian: 04/11 – 06/11/2026 (Demo 3–5 chức năng cốt lõi trên Web)*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T5.1** | Xây dựng tầng Dữ liệu & Nghiệp vụ (JPA Entities, Spring Repositories, Services `@Transactional`) | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | 05/11 | Chờ làm |
| **T5.2** | Xây dựng tầng Giao diện Web (Controllers, Thymeleaf Views / Web Forms, Bootstrap UI) | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | 05/11 | Chờ làm |
| **T5.3** | Nối hoàn chỉnh luồng nghiệp vụ 3–5 chức năng Must-have chạy thực tế trên Web (localhost:8080) | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | 06/11 | Chờ làm |
| **T5.4** | Chạy kiểm thử (**Test Execution**) theo bộ Test Case đã thiết kế, lập biên bản test | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | 06/11 | Chờ làm |

---

### SPRINT 6: TÍCH HỢP BÁO CÁO WORD, SLIDE & TỔNG DUYỆT
*Thời gian: 07/11 – 09/11/2026*

| Mã Task | Hạng mục công việc | Maker | Reviewer | Hạn chót | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: |
| **T6.1** | Tích hợp toàn bộ nội dung vào file Word, căn chỉnh lề in, mục lục, kết luận | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | 08/11 | Chờ làm |
| **T6.2** | Thiết kế Slide thuyết trình nhóm (12–15 phút) | **M3** [ &emsp;&emsp;&emsp;&emsp; ] | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | 08/11 | Chờ làm |
| **T6.3** | Biên soạn Kịch bản Demo Script Web & Bộ câu hỏi phản biện Q&A | **M4** [ &emsp;&emsp;&emsp;&emsp; ] | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | 08/11 | Chờ làm |
| **T6.4** | Hoàn thiện Phụ lục đóng góp thành viên (tỷ lệ 100%) và đóng gói mã nguồn | **M1** [ &emsp;&emsp;&emsp;&emsp; ] | **M2** [ &emsp;&emsp;&emsp;&emsp; ] | 09/11 | Chờ làm |
| **T6.5** | Tổng kiểm duyệt chéo toàn bộ hồ sơ báo cáo trước khi nộp lên Elearning | **Cả 4 TV** | **Cả 4 TV** | 09/11 | Chờ làm |

---

## III. BẢNG TỔNG HỢP KHỐI LƯỢNG CÔNG VIỆC

| Thành viên | Danh sách Task phụ trách (Maker) | Số task Maker | Danh sách Task kiểm tra duyệt (Reviewer) | Số task Reviewer | Tỷ lệ khối lượng (%) |
| :---: | :--- | :---: | :--- | :---: | :---: |
| **M1** | `T1.3`, `T2.3`, `T3.1`, `T4.4`, `T5.1`, `T6.4` | 6 task | `T1.2`, `T2.4`, `T3.4`, `T4.1`, `T5.2`, `T6.3` | 6 task | **25%** |
| **M2** | `T1.1`, `T2.2`, `T3.2`, `T4.3`, `T5.4`, `T6.1` | 6 task | `T1.3`, `T2.3`, `T3.1`, `T4.4`, `T5.1`, `T6.4` | 6 task | **25%** |
| **M3** | `T1.4`, `T2.1`, `T3.3`, `T4.2`, `T5.3`, `T6.2` | 6 task | `T1.1`, `T2.2`, `T3.2`, `T4.3`, `T5.4`, `T6.1` | 6 task | **25%** |
| **M4** | `T1.2`, `T2.4`, `T3.4`, `T4.1`, `T5.2`, `T6.3` | 6 task | `T1.4`, `T2.1`, `T3.3`, `T4.2`, `T5.3`, `T6.2` | 6 task | **25%** |
| **Cả nhóm** | `T6.5` (Tổng kiểm duyệt chéo toàn bộ sản phẩm và hồ sơ) | 1 task | `T6.5` (Toàn bộ 4 thành viên cùng kiểm tra chéo) | 1 task | Đồng đều |
| **Tổng** | **24 task cá nhân + 1 task chung** | **25 task** | **24 task cá nhân + 1 task chung** | **25 task** | **100%** |
