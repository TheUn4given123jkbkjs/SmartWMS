# MA TRẬN TRUY VẾT YÊU CẦU (REQUIREMENTS TRACEABILITY MATRIX - RTM)
<!-- 
Tài liệu khởi tạo ở Sprint 1 (Task T1.5) và hoàn thiện ở Sprint 4 (Task T4.4)
Tham chiếu tiêu chuẩn: SpecificationStandard.md (Mục 1.5)
Quy cách:
- Bảng ánh xạ xuyên suốt: Problem (P-xx) -> UR -> BR -> FR/NFR -> Use Case (UC) -> Sequence (SQ) -> Database -> Screen (SCR) -> Test Case (TC).
-->

## 1. MỤC ĐÍCH MA TRẬN TRUY VẾT
Ma trận truy vết yêu cầu (Requirements Traceability Matrix - RTM) bảo đảm tính nhất quán và tính kết nối tuyến tính xuyên suốt đồ án: từ **Vấn đề hiện trạng (`P-xx`)** $\rightarrow$ **Yêu cầu người dùng (`URxx`)** $\rightarrow$ **Quy tắc nghiệp vụ (`BRxx`)** $\rightarrow$ **Yêu cầu chức năng (`FRxx`) / Phi chức năng (`NFRxx`)** $\rightarrow$ **Mô hình UML** $\rightarrow$ **Cơ sở dữ liệu** $\rightarrow$ **Giao diện** $\rightarrow$ **Ca kiểm thử (Test Case)**.

---

## 2. MA TRẬN TRUY VẾT GIAI ĐOẠN 1: TỪ HIỆN TRẠNG ĐẾN ĐẶC TẢ YÊU CẦU (SPRINT 1)

### Bảng 1: Ánh xạ từ Vấn đề Hiện trạng (`P-xx`) sang Nhu cầu (`URxx`), Quy tắc (`BRxx`) và Yêu cầu (`FRxx` / `NFRxx`)

| Mã Vấn đề | Tên Vấn đề Hiện trạng (Chương 2) | Yêu cầu Người dùng (`URxx`) | Quy tắc Nghiệp vụ chi phối (`BRxx`) | Yêu cầu Chức năng (`FRxx`) | Yêu cầu Phi chức năng (`NFRxx`) | Ghi chú & Định hướng Triển khai |
| :---: | :--- | :---: | :--- | :---: | :---: | :--- |
| **P-01** | Dữ liệu tồn kho phân tán, không đồng bộ | `UR01`, `UR02`, `UR05` | `BR-CAT-01`, `BR-CAT-04`, `BR-INB-02`, `BR-OUT-01`, `BR-OUT-04` | `FR01.01`, `FR02.01`, `FR02.03`, `FR03.03`, `FR04.01`, `FR04.02` | `NFR-REL-01`, `NFR-REL-02`, `NFR-PER-01` | CSDL tập trung, cập nhật tồn kho real-time khi duyệt chứng từ. |
| **P-02** | Không kiểm soát tồn khả dụng, nguy cơ cấp phát thiếu hàng | `UR04`, `UR01` | `BR-OUT-01`, `BR-OUT-04` | `FR03.01`, `FR03.03`, `FR04.01` | `NFR-PER-02`, `NFR-REL-02` | Tự động giữ chỗ ($Reserved$) khi gửi phiếu xuất; chống bán âm/tranh chấp hàng. |
| **P-03** | Đứt gãy thông tin giữa Mua hàng và Kho | `UR03`, `UR02` | `BR-INV-01`, `BR-INV-02`, `BR-INB-01` | `FR02.01`, `FR04.03` | `NFR-USA-01`, `NFR-SEC-04` | Nhập kho gắn PO đã duyệt; liên thông trạng thái PO và hàng đang về (On-order). |
| **P-04** | Bố trí hàng ngẫu nhiên, thiếu mã vị trí kho | `UR01` | `BR-CAT-02`, `BR-CAT-03`, `BR-INB-03` | `FR01.03`, `FR02.02`, `FR03.02` | `NFR-USA-01`, `NFR-USA-02` | Mô hình hóa vị trí theo Zone - Shelf - Bin chuẩn mặt bằng 3M Floor Marking. |
| **P-05** | Không quản lý Lô và Hạn dùng, vi phạm FIFO/FEFO | `UR01`, `UR04` | `BR-LOT-01`, `BR-INV-03`, `BR-OUT-02`, `BR-OUT-03`, `BR-OUT-05`, `BR-OUT-06` | `FR02.02`, `FR03.02`, `FR04.03`, `FR06.01`, `FR06.02` | `NFR-PER-02`, `NFR-USA-03` | Quản lý Lot/EXP; thuật toán tự động gợi ý xuất lô hạn ngắn trước (FEFO); khóa chặn hàng quá hạn. |
| **P-06** | Kiểm kê định kỳ kéo dài, sai số lớn (10–15%) | `UR02` | `BR-STK-01`, `BR-STK-02`, `BR-STK-03` | `FR05.01`, `FR05.02`, `FR05.03`, `FR07.02` | `NFR-PER-01`, `NFR-SEC-03` | Kiểm kê điện tử; tự động tính sai lệch thừa/thiếu; cơ chế đếm lại lần 2 độc lập. |
| **P-07** | Thiếu định mức an toàn và cảnh báo tồn thấp | `UR03`, `UR04` | `BR-CAT-05`, `BR-INV-01`, `BR-INV-02` | `FR01.02`, `FR04.03` | `NFR-PER-02`, `NFR-USA-03` | Thiết lập Min/Max Stock; cảnh báo trực quan; tính toán lượng đề xuất đặt hàng bù trừ. |
| **P-08** | Chứng từ lỏng lẻo, thiếu cơ chế Maker-Checker | `UR02`, `UR05` | `BR-SEC-01`, `BR-SEC-03`, `BR-INB-02`, `BR-OUT-03`, `BR-STK-02` | `FR02.03`, `FR03.03`, `FR05.03`, `FR06.02` | `NFR-SEC-03`, `NFR-SEC-04` | Cơ chế kiểm soát 4 mắt (Maker-Checker); cấm người lập tự duyệt; cấm sửa xóa phiếu Completed. |
| **P-09** | Khó truy xuất lịch sử luân chuyển (Audit Trail) | `UR02`, `UR05`, `UR06` | `BR-SEC-01`, `BR-SEC-02` | `FR04.02` | `NFR-SEC-03`, `NFR-REL-02` | Thẻ kho điện tử; lưu vết người tạo, người duyệt, thời gian và số tồn sau giao dịch. |
| **P-10** | Báo cáo quản trị chậm trễ, cắt dán thủ công | `UR06` | `BR-SEC-02` | `FR04.01`, `FR07.01`, `FR08.01`, `FR09.01` | `NFR-PER-01`, `NFR-POR-02` | Báo cáo NXT tổng hợp tự động theo thời gian thực; widget hiệu năng kho trực quan. |

---

*Ghi chú:* Các cột tiếp theo (Use Case `UCxx`, Biểu đồ tuần tự `SQxx`, Bảng CSDL, Màn hình `SCRxx`, Ca kiểm thử `TC-xx`) sẽ được tiếp tục hoàn thiện ở **Sprint 2, Sprint 3 và Sprint 4** theo đúng kế hoạch `SprintPlan_PhanCong.md`.
