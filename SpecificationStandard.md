# BỘ TIÊU CHUẨN LÀM VIỆC & KHUNG PATTERN ĐẶC TẢ ĐỒ ÁN (SPECIFICATION STANDARD) – v2

---

## TỔNG QUAN CÁC CHƯƠNG TIÊU CHUẨN TRONG ĐỒ ÁN

- **Chương 1:** Tiêu chuẩn Đặc tả Yêu cầu phần mềm (SRS - Software Requirements Specification)
- **Chương 2:** Tiêu chuẩn Mô hình hóa Yêu cầu bằng UML (UML Modeling Standard)
- **Chương 3:** Tiêu chuẩn Thiết kế Cơ sở Dữ liệu (Database Design Standard)
- **Chương 4:** Tiêu chuẩn Thiết kế Giao diện Người dùng (UI/UX Design Standard)
- **Chương 5:** Tiêu chuẩn Cài đặt Ứng dụng Java & Kịch bản Demo (Java Demo & Script Standard)
- **Chương 6:** Tiêu chuẩn Đặc tả Ca kiểm thử (Test Case Standard)
- **Chương 7:** Tiêu chuẩn Hình thức Báo cáo & Thuyết trình (Formatting & Submission Standard)
- **Chương 8:** Quy trình làm việc nhóm, Review chéo & Definition of Done (Teamwork Standard)
- **Tài liệu đi kèm:** `SprintPlan_PhanCong.md` (danh mục mã định danh khởi tạo, kế hoạch sprint, phân công & người review)

> **Ghi chú các chương “Chương n” trong tài liệu này** là chương của *bộ tiêu chuẩn*, không phải chương của quyển báo cáo.
>
> **Công nghệ cài đặt chính thức:** Nền tảng **Java Web – Spring Boot 3.x (JDK 17/21)**, Spring Data JPA, cơ sở dữ liệu quan hệ (MySQL/PostgreSQL), giao diện Web (Thymeleaf / HTML5 / Bootstrap).

---

## HỆ THỐNG MÃ ĐỊNH DANH XUYÊN SUỐT DỰ ÁN (GLOBAL IDENTIFIERS)

Tất cả các tài liệu, sơ đồ và mã nguồn phải dùng chung một bảng ký hiệu mã định danh duy nhất:
- **`P-xx`**: Vấn đề hiện trạng từ khảo sát (Problem: P-01, P-02,...)
- **`URxx`**: Yêu cầu người dùng (User Requirement: UR01, UR02,...)
- **`BRxx`**: Quy tắc nghiệp vụ cốt lõi (Business Rule: BR01, BR02,...)
- **`FRxx`**: Nhóm chức năng cấp 1 (FR01, FR02,...)
- **`FRxx.yy`**: Yêu cầu chức năng chi tiết cấp 2 (FR01.01, FR01.02,...)
- **`NFRxx`**: Nhóm phi chức năng theo ISO/IEC 25010 (NFR01, NFR02,...)
- **`NFRxx.yy`**: Chỉ tiêu lượng hóa phi chức năng chi tiết (NFR01.01, NFR01.02,...)
- **`UCxx`**: Use Case trong mô hình UML (UC01, UC02,...)
- **`SQxx`**: Biểu đồ tuần tự (Sequence Diagram: SQ01, SQ02,...)
- **`ACTxx`**: Biểu đồ hoạt động (Activity Diagram: ACT01, ACT02,...)
- **`SCRxx`**: Màn hình giao diện (Screen/Wireframe: SCR01, SCR02,...)
- **`DSxx`**: Kịch bản Demo Java (Demo Script: DS01, DS02,...)
- **`TC-FRxx.yy-P`**: Ca kiểm thử **Positive** (luồng đúng) của yêu cầu `FRxx.yy`
- **`TC-FRxx.yy-N`**: Ca kiểm thử **Negative** (dữ liệu sai / vi phạm quy tắc) của yêu cầu `FRxx.yy`
  - *Kết quả* thực thi **PASS/FAIL** chỉ ghi ở cột “Đánh giá”, **không** đưa vào mã. Có thể có nhiều ca cho một yêu cầu: `TC-FR03.03-N1`, `TC-FR03.03-N2`.

---

# NGUYÊN TẮC MỨC ĐỘ ĐẶC TẢ THEO MoSCoW (ÁP DỤNG CHO MỌI CHƯƠNG)

Nhóm **liệt kê toàn bộ** yêu cầu, nhưng chỉ **đặc tả chi tiết và thiết kế chuyên sâu** cho phần **Must-have** (phần sẽ cài đặt Java và demo). Phần còn lại chỉ liệt kê dạng bảng rút gọn theo đúng pattern.

| Sản phẩm | Must-have | Should-have / Could-have | Won't-have |
| :--- | :--- | :--- | :--- |
| **FR** (`FRxx`, `FRxx.yy`) | Đủ **2 bảng** (Metadata + chi tiết FRxx.yy) | **1 bảng liệt kê rút gọn**: Mã, Tên, Mô tả 1 câu, MoSCoW, truy vết `P-xx`/`URxx` | Tên + **lý do loại** (Out-of-scope) |
| **BR** | Đặc tả đủ 4 trường | Liệt kê tên + công thức 1 dòng | – |
| **Use Case** | Có trên UC tổng quát **và** đặc tả đầy đủ (biểu mẫu 2.1) | Chỉ xuất hiện trên UC tổng quát (đánh dấu ưu tiên) + 1 dòng mô tả | Không vẽ |
| **Sequence / Activity** | Bắt buộc (tối thiểu 3 SQ cho 3 UC trọng tâm) | Không làm | Không làm |
| **Class / ERD** | Vẽ đầy đủ cho thực thể thuộc FR Must | Chỉ liệt kê tên bảng/thực thể dự kiến + 1 dòng mô tả | Không |
| **Từ điển dữ liệu** | Đủ biểu mẫu 3.2 cho từng bảng thuộc FR Must | Liệt kê tên bảng + 1 dòng mô tả | Không |
| **Wireframe / SCR** | Wireframe **và** bảng đặc tả `SCRxx` | Wireframe (nếu cần cho prototype), không cần bảng đặc tả | Không |
| **Cài đặt Java** | **Bắt buộc 100%** (rubric yêu cầu ≥ 80% chức năng chính) | Chỉ làm khi Must đã xong, đã test | Không |
| **Test case / Demo script** | Bắt buộc | Không | Không |
| **NFR** | Chỉ tiêu lượng hóa đầy đủ | Liệt kê tên nhóm + mục tiêu 1 dòng | Tên + lý do loại |

**Quy tắc ràng buộc:**
1. **Số nhóm FR Must-have tối đa 4–5 nhóm** để khớp yêu cầu demo Java 3–5 chức năng chính. Muốn thêm một nhóm vào Must phải bỏ một nhóm khác hoặc được cả nhóm đồng ý.
2. **Won't-have không có nghĩa là bỏ quên:** mỗi mục phải có lý do trong báo cáo (ngoài phạm vi, thuộc hướng phát triển).
3. Mọi mục Should/Could muốn nâng thành Must phải cập nhật lại **RTM (mục 1.5)**, danh mục mã định danh và kế hoạch sprint.

---

# CHƯƠNG 1: TIÊU CHUẨN ĐẶC TẢ YÊU CẦU PHẦN MỀM (SRS)

## 1.1. Tiêu chuẩn Đặc tả Quy tắc Nghiệp vụ (Business Rules - BR)
Mọi logic xử lý, ràng buộc kiểm tra, công thức tính toán và thuật toán rẽ nhánh phải được tách riêng thành bảng quy tắc nghiệp vụ:

| Trường thông tin | Quy chuẩn nội dung |
| :--- | :--- |
| **Mã BR** | `BRxx` (BR01, BR02,...) |
| **Tên quy tắc** | Tên định danh ngắn gọn của quy tắc |
| **Bản chất logic & Công thức** | Mô tả tường minh bằng công thức toán học, thuật toán, hoặc bảng chân trị điều kiện logic |
| **Phạm vi áp dụng** | Danh sách các `FRxx.yy` bị chi phối bởi quy tắc này |

---

## 1.2. Tiêu chuẩn Đặc tả Yêu cầu Chức năng (FR Pattern - 2 cấp)

> **Phạm vi áp dụng:** Đủ 2 bảng cho nhóm **Must-have**. Nhóm Should/Could dùng **1 bảng liệt kê rút gọn** (`Mã | Tên | Mô tả 1 câu | MoSCoW | Truy vết P/UR`); nhóm Won't chỉ ghi tên và lý do loại.

Mỗi phân hệ chức năng bắt buộc gồm **2 bảng**:

### Bảng 1: Metadata Nhóm chức năng (`FRxx`)

| Trường thông tin | Nội dung chi tiết |
| :--- | :--- |
| **Mã nhóm FR** | `FRxx` |
| **Tên chức năng** | [Hành động nghiệp vụ tổng quát] |
| **Mức độ ưu tiên (MoSCoW)** | `Must-have (Bắt buộc - Cài đặt Java Demo)` / `Should-have (Nên có)` / `Could-have (Có thể có)` / `Won't-have (Không làm)` |
| **Ma trận truy vết** | • **Vấn đề giải quyết:** `P-xx`<br>• **Yêu cầu người dùng:** `URxx`<br>• **Quy tắc nghiệp vụ:** `BRxx`<br>• **Use Case tương ứng:** `UCxx`<br>• **Biểu đồ tuần tự:** `[SQxx](đường_dẫn)`<br>• **Ca kiểm thử:** `TC-FRxx.yy-P`, `TC-FRxx.yy-N` |
| **Mô tả tổng quát** | [Tóm tắt 2–4 câu về mục đích, phạm vi nghiệp vụ và giá trị kiểm soát dữ liệu] |

### Bảng 2: Chi tiết các yêu cầu chức năng con (`FRxx.yy`)

| Mã ID | Tên chức năng | Mô tả hành vi hệ thống | Dữ liệu đầu vào & Ràng buộc | Kết quả kỳ vọng & Mã phản hồi |
| :--- | :--- | :--- | :--- | :--- |
| `FRxx.01` | [Tên chức năng] | [Mô tả chi tiết logic xử lý ngầm, kiểm tra hợp lệ, biến đổi dữ liệu, ghi log, kích hoạt sự kiện] | • [Danh sách trường dữ liệu].<br>• [Ràng buộc: kiểu dữ liệu, độ dài, null, unique, regex]. | • [Trạng thái dữ liệu thay đổi trong CSDL].<br>• [Mã phản hồi thành công (200 OK, 201 Created)].<br>• [Mã phản hồi lỗi (400, 401, 404, 409)]. |
| `FRxx.02` | [Tên chức năng] | ... | ... | ... |

> **Mã phản hồi chuẩn (HTTP Status Codes):** Sử dụng chuẩn HTTP cho tầng Controller/API: `200 OK` (Thành công), `201 Created` (Tạo bản ghi mới thành công), `400 Bad Request` (Vi phạm ràng buộc validation/BR), `401 Unauthorized` (Chưa đăng nhập), `403 Forbidden` (Sai quyền truy cập), `404 Not Found` (Không tìm thấy bản ghi), `409 Conflict` (Xung đột dữ liệu/trùng mã).

---

## 1.3. Tiêu chuẩn Đặc tả Yêu cầu Phi Chức năng (NFR Pattern - Chuẩn ISO/IEC 25010)
Nghiêm cấm dùng từ ngữ cảm tính. Bắt buộc có **chỉ số đo lường định lượng (Metrics)** và **môi trường kiểm thử giả định**.

### Bảng 1: Metadata Nhóm phi chức năng (`NFRxx`)

| Thuộc tính | Nội dung chi tiết |
| :--- | :--- |
| **Mã nhóm NFR** | `NFRxx` |
| **Phân loại ISO/IEC 25010** | [Performance Efficiency / Security / Reliability / Usability / Maintainability / Scalability / Compatibility] |
| **Môi trường kiểm thử chuẩn** | [Định nghĩa: Mạng `Ex`, Kích thước CSDL `Dx`, Tải đồng thời `Lx`] |
| **Ma trận truy vết** | • **Yêu cầu người dùng:** `URxx, ...`<br>• **Use Case liên quan:** `UCxx, ...`<br>• **Yêu cầu chức năng:** `FRxx, ...` |
| **Mục tiêu chất lượng** | [Tuyên bố mục tiêu chất lượng kỹ thuật bắt buộc phải đạt được] |

### Bảng 2: Chi tiết các chỉ tiêu lượng hóa (`NFRxx.yy`)

| Mã ID | Tên tiêu chí chất lượng | Đặc tả yêu cầu & Ngưỡng định lượng (Metrics) | Điều kiện kiểm thử & Môi trường | Phương pháp đo kiểm & Công cụ |
| :--- | :--- | :--- | :--- | :--- |
| `NFRxx.01` | [Tên tiêu chí] | [Ngưỡng con số định lượng: thời gian $T \le ...$, tỷ lệ lỗi $\le ...\%$, chuẩn thuật toán] | [Môi trường: số bản ghi CSDL, số người dùng ảo VU, băng thông] | [Công cụ đo: JMeter, k6, Lighthouse, Chrome DevTools...] |
| `NFRxx.02` | [Tên tiêu chí] | ... | ... | ... |

> **Công cụ đo kiểm NFR (Web):** Sử dụng JMeter / k6 / Postman để kiểm thử độ trễ API ($T_{resp}$), Lighthouse / Chrome DevTools để kiểm tra thời gian tải trang (FCP), và JUnit 5 / Mockito để kiểm thử tự động các lớp Service.

---

## 1.4. Tiêu chuẩn Bảng Ưu tiên Hóa Yêu cầu (MoSCoW)

> **Quy tắc:** Must-have **4–5 nhóm FR**, tất cả phải cài đặt và demo Java. Bảng này liệt kê **toàn bộ** FR/NFR (Must, Should, Could, Won't), không bỏ mục nào.

Bảng ưu tiên hóa yêu cầu bắt buộc áp dụng chuẩn **MoSCoW** theo đúng yêu cầu đề tài, phân định rõ 4 mức: **Must-have**, **Should-have**, **Could-have**, **Won't-have**:

| Mã Yêu cầu | Tên Yêu cầu Chức năng / Phi Chức năng | Phân loại MoSCoW | Căn cứ & Tiêu chí xếp loại | Cài đặt Demo Java |
| :---: | :--- | :---: | :--- | :---: |
| `FRxx` | [Tên nhóm chức năng cốt lõi] | **Must-have** | Nghiệp vụ sống còn của hệ thống; bắt buộc phải có để chấm điểm demo Java | [x] Bắt buộc demo |
| `FRyy` | [Tên nhóm chức năng quan trọng] | **Should-have** | Rất quan trọng cho quy trình toàn diện nhưng hệ thống vẫn tạm vận hành được nếu thiếu | [ ] Không demo |
| `FRzz` | [Tên nhóm chức năng tiện ích] | **Could-have** | Tính năng tiện ích phụ trợ nâng cao trải nghiệm; chỉ làm khi còn dư thời gian | [ ] Không demo |
| `FRww` | [Tên chức năng ngoài phạm vi] | **Won't-have** | Thống nhất không thực hiện trong phạm vi đồ án học phần (Out-of-scope) | [ ] Không demo |

---

## 1.5. Tiêu chuẩn Ma trận Truy vết Yêu cầu (Traceability Matrix - RTM)
Bảng tổng kết truy vết toàn diện liên kết từ Khảo sát hiện trạng đến Kiểm thử và Demo:

| Mã Vấn đề (P-xx) | Mã Yêu cầu người dùng (UR) | Mã Quy tắc nghiệp vụ (BR) | Mã Nhóm FR / NFR | Mã Use Case (UML) | Mức độ ưu tiên (MoSCoW) | Cài đặt Demo Java |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| `P-01` | `UR01` | `BR01` | `FR01` | `UC01` | **Must-have** | [x] Bắt buộc |
| `P-05` | `UR04` | `BR05` | `FR04` | `UC03` | **Must-have** | [x] Bắt buộc |
| `P-06` | `UR06` | `BR08` | `FR06` | `UC05` | **Should-have** | [ ] Không |

---

# CHƯƠNG 2: TIÊU CHUẨN MÔ HÌNH HÓA YÊU CẦU BẰNG UML

## 2.1. Tiêu chuẩn Biểu đồ Use Case & Biểu mẫu Đặc tả chi tiết Use Case

> **Phạm vi áp dụng:** UC tổng quát vẽ **toàn bộ** use case (đánh dấu mức ưu tiên). Biểu mẫu đặc tả chi tiết chỉ cho UC thuộc **FR Must-have** (5–7 UC chính).

- **Biểu đồ Use Case tổng quát:** Bao quát toàn bộ Actor và Use Case của hệ thống; thể hiện đúng quan hệ: `<<include>>`, `<<extend>>`, `Generalization`.
- **Biểu mẫu đặc tả chi tiết Use Case (Bắt buộc tối thiểu 5–7 Use Case chính):**

### Biểu mẫu đặc tả: `UCxx` - [Tên Use Case]

| Thuộc tính | Chi tiết đặc tả |
| :--- | :--- |
| **Mã Use Case** | `UCxx` |
| **Tên Use Case** | [Động từ + Bổ ngữ (VD: Tạo phiếu xuất kho theo FIFO)] |
| **Tác nhân chính (Primary Actor)** | [Người dùng hoặc Hệ thống kích hoạt] |
| **Tác nhân phụ (Secondary Actor)** | [Các đối tượng nhận thông báo hoặc liên đới] |
| **Mô tả tóm tắt (Brief Description)** | [Tóm tắt 1–2 câu mục đích của ca sử dụng] |
| **Tiền điều kiện (Pre-conditions)** | [Trạng thái bắt buộc của hệ thống/dữ liệu trước khi kích hoạt UC] |
| **Hậu điều kiện thành công (Post-conditions)** | [Trạng thái hệ thống/dữ liệu thay đổi khi hoàn tất thành công] |
| **Quy tắc nghiệp vụ chi phối** | `BRxx, BRyy...` |
| **Yêu cầu chức năng liên ứng** | `FRxx.yy...` |

#### Bảng Luồng sự kiện chính (Basic Flow / Main Flow):
| Bước | Hành động của Tác nhân (Actor Action) | Phản hồi của Hệ thống (System Response) |
| :---: | :--- | :--- |
| 1 | Người dùng yêu cầu thực hiện chức năng... | Hệ thống hiển thị form nhập liệu... |
| 2 | Người dùng điền thông tin và bấm xác nhận... | Hệ thống kiểm tra hợp lệ theo BRxx... |
| 3 | ... | Hệ thống cập nhật CSDL và hiển thị thông báo thành công. |

#### Luồng phụ / Rẽ nhánh (Alternative Flows):
- **Luồng 2a - [Tên nhánh]:** Tại bước X, nếu [điều kiện], hệ thống [xử lý rẽ nhánh].

#### Luồng ngoại lệ (Exception Flows):
- **Luồng 2e - [Tên ngoại lệ]:** Tại bước X, nếu [lỗi xảy ra], hệ thống [thông báo lỗi, rollback giao dịch và quay về bước Y].

---

## 2.2. Tiêu chuẩn Biểu đồ Lớp (Class Diagram) & ERD

> **Phạm vi áp dụng:** Chỉ vẽ lớp/thực thể thuộc **FR Must-have**. Thực thể của Should/Could chỉ liệt kê tên và mô tả 1 dòng.

- **Biểu đồ lớp (Class Diagram):**
  - Đầy đủ 3 ngăn: **Tên Class**, **Thuộc tính** (kèm Visibility `-`/`+` và Kiểu dữ liệu), **Phương thức** (kèm tham số và kiểu trả về).
  - Quan hệ thể hiện chính xác: *Association, Aggregation (hình thoi rỗng), Composition (hình thoi đặc), Generalization/Inheritance (tam giác rỗng), Dependency (mũi tên đứt nét)*.
  - Bắt buộc ghi rõ bội số (*Multiplicity: `1`, `0..1`, `1..*`, `*`*).
- **Mô hình Thực thể - Kết hợp (ERD):**
  - Thể hiện rõ các Entity, Primary Key (PK gạch chân), Foreign Key (FK), và Cardinality (1-1, 1-N, N-N).
  - Khử toàn bộ quan hệ N-N bằng bảng trung gian (Junction Table).

---

## 2.3. Tiêu chuẩn Biểu đồ Tuần tự (Sequence Diagram)

> **Phạm vi áp dụng:** Chỉ cho UC Must-have trọng tâm (tối thiểu 3).

- **Số lượng bắt buộc:** Tối thiểu 3 biểu đồ tuần tự cho 3 Use Case trọng tâm.
- **Cấu trúc đối tượng (Lifelines):** Phân chia rõ ràng theo kiến trúc chuẩn:
  - `Actor` $\rightarrow$ `Boundary / UI Form` $\rightarrow$ `Controller / Handler` $\rightarrow$ `Service / Manager` $\rightarrow$ `Repository / DAO` $\rightarrow$ `Database`.
- **Thông điệp (Messages):**
  - Thông điệp đồng bộ (Synchronous call: mũi tên nét liền, đầu đặc).
  - Thông điệp trả về (Return message: mũi tên nét đứt, đầu mở).
  - Bắt buộc sử dụng các Interaction Fragments: `alt` (rẽ nhánh điều kiện), `opt` (tùy chọn), `loop` (lặp qua danh sách), `ref` (tham chiếu Use Case khác).
  - Các bước trong Sequence Diagram phải khớp 100% với Main Flow và Exception Flow của bản đặc tả Use Case.

---

## 2.4. Tiêu chuẩn Biểu đồ Hoạt động (Activity Diagram)

> **Phạm vi áp dụng:** Chỉ cho quy trình Must-have phức tạp (xuất kho FIFO, kiểm kê). Đề cho phép dùng Sequence **hoặc** Activity; Activity là phần bổ trợ, làm khi còn thời gian.

- Bắt buộc chia **Swimlanes (Phân làn hoạt động)** theo các vai trò: `Actor`, `Giao diện (UI)`, `Hệ thống xử lý (System/Backend)`.
- Sử dụng đúng ký hiệu UML: Initial Node (chấm tròn đen), Action State (hình chữ nhật bo tròn), Decision / Merge Node (hình thoi), Fork / Join Bar (thanh đặc ngang), Activity Final Node (chấm tròn bao ngoài).

---

# CHƯƠNG 3: TIÊU CHUẨN THIẾT KẾ CƠ SỞ DỮ LIỆU (DATABASE DESIGN)

## 3.1. Quy chuẩn Đặt tên & Lược đồ Quan hệ (Relational Schema)
- **Quy ước đặt tên:**
  - Tên bảng: `snake_case`, danh từ số ít (ví dụ: `product`, `warehouse`, `receipt_note`, `stock_batch`).
  - Tên cột: `snake_case`, mô tả rõ nghĩa (ví dụ: `product_id`, `created_at`, `total_amount`).
  - Khóa chính (PK): Đặt là `id` hoặc `<table_name>_id` (kiểu số nguyên tự tăng hoặc UUID).
  - Khóa ngoại (FK): Đặt tên theo cú pháp `<target_table>_id` để tường minh quan hệ.
- **Chuẩn hóa dữ liệu:** Toàn bộ lược đồ bắt buộc đạt chuẩn tối thiểu **3NF (Third Normal Form)**, loại bỏ triệt để dị thường thêm/xóa/sửa (Anomaly).

---

## 3.2. Tiêu chuẩn Bảng Từ điển Dữ liệu (Data Dictionary Template)

> **Phạm vi áp dụng:** Biểu mẫu đầy đủ cho bảng thuộc **FR Must-have**. Bảng thuộc Should/Could chỉ liệt kê tên bảng + mô tả 1 dòng.

Mỗi bảng trong cơ sở dữ liệu phải được đặc tả theo biểu mẫu chuẩn:

### Bảng: `[tên_bảng]` ([Tên mô tả nghiệp vụ])
*Mô tả: [Đoạn văn ngắn mô tả dữ liệu bảng này lưu trữ gì và liên kết với bảng nào]*

| Tên trường (Column) | Kiểu dữ liệu | Độ dài | Nullable | Khóa | Giá trị mặc định | Ràng buộc nghiệp vụ (Constraints) | Ý nghĩa nghiệp vụ |
| :--- | :--- | :---: | :---: | :---: | :---: | :--- | :--- |
| `id` | INT / BIGINT | - | NO | **PK** | AUTO_INCREMENT | `id > 0` | Khóa chính định danh bản ghi |
| `code` | VARCHAR | 50 | NO | **UK** | None | UNIQUE, Not empty | Mã nghiệp vụ duy nhất |
| `quantity` | DECIMAL | (12, 2) | NO | - | 0.00 | `quantity >= 0` (NFR02) | Số lượng tồn kho thực tế |
| `status` | VARCHAR | 20 | NO | - | 'PENDING' | CHECK (IN ('PENDING', 'APPROVED', 'CANCELLED')) | Trạng thái phiếu |
| `created_at` | TIMESTAMP | - | NO | - | CURRENT_TIMESTAMP | - | Thời điểm tạo bản ghi |

---

# CHƯƠNG 4: TIÊU CHUẨN THIẾT KẾ GIAO DIỆN NGƯỜI DÙNG (UI/UX)

## 4.1. Quy chuẩn Wireframe / Mockup Màn hình
- Đầy đủ các màn hình chính theo phạm vi: Đăng nhập, Dashboard tổng quan, Danh mục hàng hóa, Nhập kho, Xuất kho FIFO, Tra cứu thẻ kho, Kiểm kê, Cảnh báo tồn min.
- Thể hiện rõ: Vùng điều hướng (Navigation Bar / Sidebar), Vùng thao tác dữ liệu (Table grid, Form input, Filter search), Nút hành động chính (Primary/Secondary Button), Modal cảnh báo/xác nhận.

---

## 4.2. Tiêu chuẩn Đặc tả Màn hình & Luồng Điều hướng (Screen Specification Template)

> **Phạm vi áp dụng:** Bảng đặc tả chỉ cho màn hình Must-have. Màn hình Should/Could chỉ cần wireframe.

Mỗi màn hình chính phải có bảng đặc tả chi tiết đi kèm hình ảnh mockup:

### Màn hình: `SCRxx` - [Tên Màn hình]

| Thuộc tính | Chi tiết đặc tả |
| :--- | :--- |
| **Mã màn hình** | `SCRxx` |
| **Tên màn hình** | [Tên hiển thị giao diện] |
| **Tác nhân sử dụng** | [Actor có quyền truy cập] |
| **Yêu cầu chức năng** | `FRxx.yy...` |
| **Mục đích giao diện** | [Mô tả công việc người dùng thực hiện trên màn hình này] |

#### Bảng Tương tác UI và Luồng chuyển trang (Navigation Flow):
| Mã phần tử | Loại UI (Component) | Nhãn / Tên | Hành vi khi tương tác (User Action) | Luồng xử lý & Màn hình đích (Next Screen) |
| :---: | :--- | :--- | :--- | :--- |
| `BTN-01` | Button | "Tạo phiếu mới" | Click | Mở Modal tạo phiếu `SCRxx.01` |
| `INP-02` | Dropdown Select | "Chọn Lô hàng" | Change value | Tự động điền ngày hết hạn và vị trí kệ |
| `BTN-03` | Button Primary | "Xác nhận xuất" | Click | Validate dữ liệu $\rightarrow$ Gọi service trừ tồn $\rightarrow$ Hiển thị thông báo |

> **Tương tác Web UI:** Các nút bấm kích hoạt gửi yêu cầu (Form Submit / REST Fetch API), hiển thị thông báo Toast / Alert Bootstrap trực quan và điều hướng route web.

---

# CHƯƠNG 5: TIÊU CHUẨN CÀI ĐẶT JAVA WEB (SPRING BOOT) & KỊCH BẢN DEMO

## 5.1. Tiêu chuẩn Kiến trúc Mã nguồn Spring Boot
- **Nền tảng kỹ thuật:** Java JDK 17+ (hoặc JDK 21), **Spring Boot 3.x**, Spring Data JPA, Hibernate, Embedded Tomcat, Thymeleaf / HTML5 / Bootstrap 5.
- **Kiến trúc phân lớp chuẩn (Spring Boot Layered Architecture):**
  - `entity / model`: Chứa các lớp `@Entity`, `@Table` ánh xạ trực tiếp từ các bảng CSDL quan hệ 3NF (ví dụ: `Product.java`, `StockBatch.java`, `ReceiptNote.java`).
  - `repository`: Kế thừa `JpaRepository<T, ID>` để thực hiện truy vấn dữ liệu CRUD và Custom JPQL Queries, có khai báo khóa ngoại quan hệ (`@ManyToOne`, `@OneToMany`).
  - `service`: Chứa các lớp `@Service` xử lý **100% logic nghiệp vụ** (thuật toán FIFO, tính toán tồn khả dụng `BR01`, kiểm tra hạn dùng, chặn xuất âm `BR03`). Bắt buộc áp dụng `@Transactional` để đảm bảo toàn vẹn dữ liệu ACID (NFR02).
  - `controller`: Tiếp nhận HTTP Request (`@Controller` render Thymeleaf View hoặc `@RestController` trả về JSON DTO).
  - `dto / payload`: Các lớp Data Transfer Object đóng gói dữ liệu Request/Response kèm chú thích validation (`@Valid`, `@NotNull`, `@Min`, `@NotBlank`).
  - `templates / view`: Giao diện Web (Thymeleaf layout, Bootstrap 5) hiển thị Dashboard, Form nhập/xuất, Bảng dữ liệu.
- **Ràng buộc chất lượng code:**
  - Tuyệt đối không viết logic nghiệp vụ trong Controller hay View.
  - Controller chỉ gọi Service; Service gọi Repository; Repository tương tác DB.
  - Xử lý ngoại lệ tập trung bằng `@ControllerAdvice` / `@ExceptionHandler`.

---

## 5.2. Tiêu chuẩn Kịch bản Demo (Demo Script Template)
*(Yêu cầu bắt buộc theo Mục 3.2 đề bài: Chạy thực tế tối thiểu 3–5 chức năng chính trên trình duyệt Web)*

### Kịch bản Demo: `DSxx` - [Tên quy trình nghiệp vụ]
- **Mã kịch bản:** `DSxx`
- **Chức năng demo liên quan:** `FRxx, FRyy...` (Mức Must-have - Bắt buộc demo Java)
- **Thời lượng dự kiến:** 2–3 phút
- **Môi trường demo:** Trình duyệt Web (Chrome/Edge), truy cập URL `http://localhost:8080/wms/...`
- **Dữ liệu chuẩn bị trước (Pre-conditions / Test Data):**
  - Tài khoản demo: `admin@wms.com` (Role: Quản lý kho).
  - Mặt hàng mẫu: Mã `SP001` - Bánh răng hộp số, Tồn tối thiểu: 20 cái.
  - Dữ liệu lô sẵn có trong CSDL: Lô `BATCH-01` (Nhập ngày 01/10/2026, SL: 10), Lô `BATCH-02` (Nhập ngày 10/10/2026, SL: 15).

#### Các bước thực hiện Demo chi tiết:
| Bước | Thao tác của người thuyết trình | Dữ liệu nhập thực tế | Hành vi hệ thống & Hiển thị trên màn hình Web | Điểm nhấn giải thích cho Giảng viên |
| :---: | :--- | :--- | :--- | :--- |
| 1 | Truy cập `/login` và đăng nhập | Nhập `admin@wms.com` | Điều hướng sang `/dashboard`, hiển thị badge cảnh báo tồn | Chứng minh cơ chế phân quyền và kiểm soát phiên |
| 2 | Mở màn hình Tạo phiếu xuất `/issue/create` | Chọn mặt hàng `SP001`, nhập SL = 12 | Hệ thống tự động gợi ý xuất: Lô BATCH-01 (10 cái) + Lô BATCH-02 (2 cái) | **Chứng minh thuật toán FIFO hoạt động chuẩn xác (Service layer)** |
| 3 | Bấm nút "Xác nhận xuất kho" | Click submit form | Giao dịch commit thành công, tồn kho giảm còn 13, hiển thị Toast xanh | Chứng minh toàn vẹn dữ liệu `@Transactional` (ACID) |
| 4 | Kiểm tra phản ứng hệ thống tại `/alerts` | F5 hoặc bấm vào menu Cảnh báo | Hệ thống hiển thị `SP001` trong bảng cảnh báo đỏ (vì $13 < 20$) | **Chứng minh cảnh báo tồn tự động thời gian thực** |

---

# CHƯƠNG 6: TIÊU CHUẨN ĐẶC TẢ CA KIỂM THỬ (TEST CASE STANDARD)

Mỗi chức năng demo (FR Must-have) bắt buộc có bảng Test Case kiểm thử chứng minh phần mềm chạy đúng. **Mỗi `FRxx.yy` Must có tối thiểu 1 ca Positive (`-P`) và 1 ca Negative (`-N`) khi có ràng buộc.** Kết quả PASS/FAIL ghi ở cột “Đánh giá”.

### Bảng Ca kiểm thử chức năng: [Tên chức năng]

| Mã Ca kiểm thử | Yêu cầu kiểm thử | Loại kiểm thử | Tiền điều kiện | Dữ liệu kiểm thử (Test Data) | Các bước thực hiện (Test Steps) | Kết quả kỳ vọng (Expected Result) | Đánh giá |
| :---: | :--- | :---: | :--- | :--- | :--- | :--- | :---: |
| `TC-FR02.01-P` | Nhập kho thành công | Positive | Đã có tài khoản kho | Mã SP: `SP001`, SL: 50, Lô: `L01` | 1. Nhập form<br>2. Bấm Lưu | Tạo bản ghi phiếu nhập, tồn kho tăng đúng 50 | **PASS** |
| `TC-FR03.03-N` | Chặn xuất quá tồn kho | Negative | Tồn kho hiện có = 10 | Nhập SL xuất = 15 | 1. Chọn SP<br>2. Nhập 15<br>3. Bấm Xuất | Báo lỗi "Vượt tồn khả dụng", không trừ CSDL | **PASS** |

---

# CHƯƠNG 7: TIÊU CHUẨN HÌNH THỨC BÁO CÁO & THUYẾT TRÌNH

## 7.1. Quy chuẩn Trình bày Quyển Báo cáo (Word / PDF)
- **Định dạng văn bản bắt buộc (Theo Mục 5 đề bài):**
  - Kiểu chữ: `Times New Roman` (Unicode), cỡ chữ: `12–13pt`.
  - Giãn dòng: `1.5 lines`. Mật độ chữ bình thường (không nén/dãn dải).
  - Căn lề trang in: Lề trên `3.5 cm`, Lề dưới `3.0 cm`, Lề trái `3.5 cm` (để đóng gáy), Lề phải `2.0 cm`.
  - Đánh số trang: Đặt ở giữa, **phía trên đầu mỗi trang giấy** (Top - Center).
- **Cấu trúc khung bài:** Bìa chính $\rightarrow$ Bìa phụ $\rightarrow$ Mục lục tự động $\rightarrow$ Danh mục hình vẽ $\rightarrow$ Danh mục bảng biểu $\rightarrow$ Danh mục viết tắt $\rightarrow$ Nội dung các chương $\rightarrow$ Kết luận & Hướng phát triển $\rightarrow$ Tài liệu tham khảo $\rightarrow$ Phụ lục phân công công việc.

---

## 7.2. Tiêu chuẩn Buổi Báo cáo & Thuyết trình
- **Thời lượng:** $12 - 15$ phút trình bày & demo; $5 - 8$ phút trả lời câu hỏi phản biện của Hội đồng.
- **Phân công trình bày:** $100\%$ thành viên trong nhóm bắt buộc phải thuyết trình ít nhất một phần nội dung và cùng tham gia trả lời câu hỏi phản biện.
- **Quy chuẩn Slide:** Thiết kế tối giản, tập trung vào sơ đồ kiến trúc, biểu đồ luồng nghiệp vụ và số liệu khảo sát; tuyệt đối không copy nguyên văn các đoạn văn dài vào slide.


---

# CHƯƠNG 8: TIÊU CHUẨN QUY TRÌNH LÀM VIỆC NHÓM (MAKER–REVIEWER)

## 8.1. Nguyên tắc Maker–Reviewer
- Mỗi công việc có đúng **1 Maker** (người làm) và **1 Reviewer** (người kiểm tra). **Reviewer khác Maker.**
- Vòng review mặc định: **M1 → M2 → M3 → M4 → M1** (người kế tiếp trong vòng review công việc của người trước). Danh sách cụ thể nằm trong `SprintPlan_PhanCong.md`.
- Một công việc chỉ được xem là xong khi Reviewer đã **xác nhận** theo checklist 8.2 và đạt Definition of Done (8.3).
- Thời hạn review: **tối đa 24 giờ** kể từ lúc Maker báo hoàn thành. Reviewer ghi nhận xét theo từng mục, Maker sửa rồi báo lại (tối đa 2 vòng; còn bất đồng thì nhóm trưởng quyết định).
- **Mỗi thành viên phải đọc và hiểu toàn bộ báo cáo**, không chỉ phần mình làm, vì phần Response to Questions chiếm 20% điểm.

## 8.2. Checklist review theo loại sản phẩm
| Loại sản phẩm | Reviewer kiểm tra |
| :--- | :--- |
| **Đặc tả FR / BR / NFR** | Đúng pattern chương 1; mỗi `FRxx.yy` có đầu vào, ràng buộc, kết quả, mã lỗi; có đủ truy vết `P/UR/BR/UC`; NFR có số đo, không dùng từ cảm tính |
| **Use Case** | Đúng biểu mẫu 2.1; luồng chính/phụ/ngoại lệ đánh số; tiền/hậu điều kiện rõ; BR và FR được dẫn đúng mã |
| **Sequence / Activity** | Khớp **100%** Main Flow và Exception Flow của UC; đủ lifeline, `alt/opt/loop/ref` đúng chỗ; Activity có swimlane |
| **ERD / DB** | Đạt 3NF; không còn quan hệ N-N; đặt tên `snake_case`, số ít; từ điển dữ liệu khớp DDL; ràng buộc khớp BR |
| **Wireframe / SCR** | Đủ vùng điều hướng, thao tác, nút chính, modal xác nhận; bảng `SCRxx` dẫn đúng `FRxx.yy` |
| **Code Java** | Đúng phân lớp (không có SQL/logic nghiệp vụ trong View); chạy được; có xử lý ngoại lệ; tên lớp/hàm có mã `FR/UC` trong chú thích; chạy qua các TC liên quan |
| **Test case / Demo script** | Mỗi `FRxx.yy` Must có TC `-P` và `-N`; kịch bản chạy được từ dữ liệu mẫu, khớp thời lượng 2–3 phút |
| **Báo cáo (Word)** | Đúng chuẩn 7.1; mục lục, danh mục hình/bảng cập nhật; không lỗi chính tả; có nguồn tham khảo |

## 8.3. Definition of Done (DoD)
Một công việc đạt DoD khi **đồng thời**:
1. Sản phẩm nằm đúng thư mục repo, tên file có mã định danh (ví dụ `UC03_LapPhieuXuatFIFO.md`).
2. Mã định danh khớp danh mục trong `SprintPlan_PhanCong.md` và đã được cập nhật vào **RTM**.
3. Reviewer đã xác nhận bằng checklist 8.2 (ghi tên và ngày).
4. Với code: build thành công, các TC liên quan PASS, không còn lỗi nghiêm trọng đã biết.
5. Không để lại mục `TODO` trong tài liệu.

## 8.4. Quy ước Git, đặt tên và thay đổi mã định danh
- **Nhánh:** `feature/FR03-xuat-kho-fifo`, `docs/UC03-dac-ta`, `design/ERD`. Không commit trực tiếp vào `main`; gộp qua Pull Request có Reviewer duyệt.
- **Commit message:** bắt đầu bằng mã định danh, ví dụ `FR03.02: gợi ý lô theo FIFO`, `UC03: bổ sung luồng ngoại lệ 4e`.
- **Danh mục mã định danh** do **M1** giữ. Muốn thêm/đổi/xóa mã (P, UR, BR, FR, UC…) phải báo trong nhóm, M1 cập nhật danh mục và RTM, rồi mới được dùng.
- **Họp:** stand-up 10 phút mỗi ngày (làm gì hôm qua, làm gì hôm nay, vướng gì); cuối mỗi sprint họp review/retrospective 30 phút và chốt sprint kế tiếp.
- **Ước lượng:** 1 điểm ≈ 2 giờ làm việc (đã gồm tự kiểm tra, chưa gồm thời gian review của người khác).