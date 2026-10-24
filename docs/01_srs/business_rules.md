# TRƯỜNG ĐẠI HỌC TÔN ĐỨC THẮNG
## KHOA CÔNG NGHỆ THÔNG TIN

---

# TÀI LIỆU QUY TẮC NGHIỆP VỤ CỐT LÕI (BUSINESS RULES – BR)
### Đề tài: Hệ thống Quản lý Kho Hàng (WMS)
*Học phần: Phân tích và Thiết kế Yêu cầu (Mã môn: 502050)*


### BẢNG THUẬT NGỮ VÀ TỪ VIẾT TẮT (GLOSSARY & ACRONYMS)

| Từ viết tắt | Thuật ngữ đầy đủ (Tiếng Anh) | Giải nghĩa tiếng Việt | Ý nghĩa trong ngữ cảnh Quản lý Kho |
|:---|:---|:---|:---|
| **WMS** | Warehouse Management System | Hệ thống Quản lý Kho Hàng | Ứng dụng quản lý toàn diện các hoạt động vận hành kho của đề tài. |
| **BR** | Business Rule | Quy tắc nghiệp vụ | Các quy định, ràng buộc nghiệp vụ bắt buộc trong vận hành kho. |
| **BABOK** | Business Analysis Body of Knowledge (BABOK Guide) | Hướng dẫn chuẩn Phân tích Nghiệp vụ | Hướng dẫn tổng hợp kiến thức, nhiệm vụ, kỹ thuật và thực hành trong lĩnh vực phân tích nghiệp vụ do IIBA phát triển. |
| **SKU** | Stock Keeping Unit | Mã định danh đơn vị lưu kho | Mã duy nhất dùng để phân biệt và quản lý từng mặt hàng trong kho. |
| **PO** | Purchase Order | Đơn mua hàng (Đơn đặt hàng mua) | Chứng từ mua hàng đã duyệt, làm căn cứ đối chiếu số lượng khi nhập kho. |
| **FIFO** | First In, First Out | Nhập trước, Xuất trước | Nguyên tắc xuất kho: lô hàng nào nhập trước sẽ được ưu tiên xuất trước. |
| **FEFO** | First Expired, First Out | Hết hạn trước, Xuất trước | Nguyên tắc xuất kho: lô hàng nào có hạn dùng gần nhất xuất trước. |
| **MFG** | Manufacturing Date | Ngày sản xuất | Ngày sản phẩm được xuất xưởng/đóng gói tại cơ sở sản xuất. |
| **EXP** | Expiration Date | Hạn sử dụng (Hạn dùng) | Ngày hết hạn của hàng hóa; quá hạn này hàng bị cấm xuất bán/sản xuất. |
| **Lot No** | Lot Number / Batch Number | Số lô hàng | Mã định danh đợt hàng sản xuất cùng mẻ của nhà cung cấp. |
| **Min Stock** | Minimum Stock Level | Mức tồn kho tối thiểu | Định mức tồn an toàn; chạm ngưỡng này sẽ phát cảnh báo thiếu hàng. |
| **Max Stock** | Maximum Stock Level | Mức tồn kho tối đa | Định mức tồn trần nhằm tránh quá tải kho và ứ đọng vốn lưu động. |
| **On-hand** | On-hand Inventory / Stock | Tồn kho thực tế (Tồn vật lý) | Số lượng hàng hóa thực tế đang nằm trong kho có thể kiểm đếm được. |
| **Reserved** | Reserved Inventory / Stock | Tồn giữ chỗ | Lượng hàng đã được tạm giữ cho các phiếu xuất đang xử lý, chưa hoàn tất xuất kho. |
| **Available** | Available Inventory / Stock | Tồn kho khả dụng | Lượng tồn thực tế có thể sử dụng: $\text{Available} = \text{On-hand} - \text{Reserved} - \text{Quá hạn/Hư hỏng}$. |
| **PO On-order** | On-order Inventory | Hàng đang đặt | Số lượng hàng trên các PO đã duyệt đang chờ nhà cung cấp giao đến kho. |
| **ĐVT** | Unit of Measure (UoM) | Đơn vị tính cơ sở | Đơn vị đo lường thống nhất cho từng mặt hàng (Cái, Hộp, Kg...). |

---

## 1. TỔNG QUAN VỀ BỘ QUY TẮC NGHIỆP VỤ

### 1.1. Mục đích tài liệu
Tài liệu này xác lập danh mục các Quy tắc nghiệp vụ cốt lõi (Business Rules – BR) chi phối toàn bộ hoạt động quản lý danh mục, lưu trữ, tiếp nhận hàng, xuất kho, kiểm soát mức tồn, kiểm kê và kiểm soát an toàn dữ liệu tại kho hàng của doanh nghiệp. 


---

### 1.2. Phân loại Quy tắc nghiệp vụ (Theo chuẩn BABOK v3 – Mục 10.9)
Toàn bộ quy tắc trong tài liệu được phân loại theo đúng 2 nhóm chuẩn của BABOK v3:
1. **Definitional Rules (Quy tắc Định nghĩa / Cấu trúc):** Định hình cấu trúc thông tin, khái niệm, mối quan hệ thực thể, ràng buộc dữ liệu toàn vẹn và các công thức tính toán/suy diễn (Derivations). Mang tính chất bất biến hoặc điều kiện cấu trúc tiên quyết (VD: Mã SKU duy nhất, cấu trúc vị trí kho, công thức tính lượng đặt hàng bổ sung tránh đặt trùng).
2. **Behavioral Rules (Quy tắc Hành vi / Vận hành):** Chỉ đạo, dẫn dắt hoặc kiềm chế hành động của con người và các tác vụ nghiệp vụ. Thể hiện các quy định về quyền hạn, điều kiện kiểm tra hợp lệ, hoặc hành vi bị nghiêm cấm trong kho (VD: Cấm xuất vượt tồn khả dụng, chặn xuất hàng hết hạn, phân tách vai trò kiểm kê và duyệt chứng từ, cấm sửa xóa chứng từ đã hoàn tất).

---

### 1.3. Nguồn quy tắc nghiệp vụ
* **Nguồn quy tắc:** Toàn bộ 25 quy tắc nghiệp vụ đều được nghiên cứu, suy luận và chuẩn hóa từ **2 nguồn tài liệu thứ cấp thực tế** (Secondary Research / Case Study & SOP Benchmarking), nhóm tác giả **không thực hiện phỏng vấn trực tiếp tại doanh nghiệp**:
  1. **Nguồn 1: Case Study VTI Solutions (Công bố 28/07/2025):** *"Hành trình cải tiến hệ thống Quản lý kho và Mua hàng cùng Tập đoàn Công nghiệp Nhật Bản"* – Cung cấp bối cảnh thực tế của 3 nhà máy FDI tại Việt Nam, dữ kiện vận hành thủ công (Excel, thẻ kho giấy, email), hàng xếp ngẫu nhiên thiếu mã vị trí, đứt gãy thông tin Mua hàng - Kho và 10 vấn đề hiện trạng cốt lõi (`P-01` đến `P-10`).
  2. **Nguồn 2: Quy trình Quản lý Kho hàng chuẩn – Giao Hàng Nhanh (GHN):** *"Quy trình quản lý kho hàng: 7 bước hiệu quả, hạn chế sai sót"* (URL: [https://ghn.vn/blogs/thong-tin-giao-hang/quy-trinh-quan-ly-kho-hang](https://ghn.vn/blogs/thong-tin-giao-hang/quy-trinh-quan-ly-kho-hang)) – Cung cấp các quy chuẩn thực hành tốt nhất (Standard Operating Procedures - SOP) về quy trình 7 bước (nhập kho kiểm tra chất lượng, lưu kho có mã vị trí, lấy hàng đơn/cụm, nguyên tắc xuất kho FIFO/FEFO, xử lý chênh lệch kiểm kê độc lập, cơ chế phân tách thẩm quyền Maker-Checker và báo cáo đối soát).

---

### 1.4. Ranh giới phạm vi áp dụng (Business Scope & Boundaries)
Bộ quy tắc được thiết kế vừa vặn với quy mô đồ án của nhóm 4 sinh viên:
* Áp dụng cho **1 kho hàng vật lý duy nhất**, không quản trị mạng lưới đa kho.
* Vị trí lưu kho quản lý theo phân vùng Khu vực và mã vị trí (Location Code); một vị trí cho phép chứa nhiều lô của cùng một mặt hàng (SKU). Quy trình luân chuyển vị trí kho nội bộ phức tạp hoặc tối ưu vị trí 3D nâng cao thuộc **ngoại phạm vi Giai đoạn 1** (vị trí được chỉ định lúc nhập kho theo `BR-INB-03` hoặc cập nhật quản lý danh mục).
* Mỗi mặt hàng chỉ sử dụng **1 Đơn vị tính cơ sở duy nhất** trong toàn bộ chứng từ.
* Tiếp nhận hàng theo Đơn mua hàng (PO). Không áp dụng quy trình kiểm định cách ly phức tạp (QC Quarantine) hay quy trình trả hàng nhà cung cấp đa cấp (RMA) / thu hồi hàng khách hàng (hệ thống xử lý từ chối nhận hàng không đạt chuẩn ngay tại thời điểm kiểm đếm cửa kho theo `BR-INB-04`).

---

### 1.5. Ma trận Thẩm quyền Phê duyệt Chứng từ (Approval Authority Matrix)
Để bảo đảm tính nhất quán xuyên suốt toàn bộ các quy tắc hành vi (Behavioral Rules) và tuân thủ nguyên tắc kiểm soát nội bộ bốn mắt (Maker-Checker theo chuẩn BABOK v3), thẩm quyền xử lý chứng từ kho được xác lập duy nhất theo bảng sau:

| Loại chứng từ / Nghiệp vụ | Người lập (Maker) | Người phê duyệt (Checker) | Quy tắc BR chi phối | Quy định tự duyệt |
|:---|:---|:---|:---|:---:|
| **Phiếu nhập kho** | Thủ kho | **Quản lý kho** | `BR-INB-01`, `BR-INB-02`, `BR-INB-04` | **Nghiêm cấm** |
| **Phiếu xuất kho** | Thủ kho | **Quản lý kho** | `BR-OUT-01`, `BR-OUT-02`, `BR-OUT-04`, `BR-OUT-06` | **Nghiêm cấm** |
| **Phiếu xuất hủy / thanh lý** | Thủ kho | **Quản lý kho** | `BR-OUT-03`, `BR-OUT-05` | **Nghiêm cấm** |
| **Biên bản kiểm kê & Điều chỉnh tồn** | Nhân sự đếm (Thủ kho) | **Quản lý kho** | `BR-STK-01`, `BR-STK-02`, `BR-STK-03` | **Nghiêm cấm** |

*Nguyên tắc bất biến:* Quản lý kho là vai trò duy nhất có thẩm quyền phê duyệt mọi chứng từ kho làm thay đổi số lượng tồn kho hoặc trạng thái tài sản. Tuyệt đối không có ngoại lệ cho phép người lập tự phê duyệt chứng từ do chính mình tạo ra.

---

## 2. DANH MỤC 25 QUY TẮC NGHIỆP VỤ CỐT LÕI (BR CATALOG)

| Mã BR | Tên quy tắc | Phân loại | Nội dung quy tắc nghiệp vụ | Nguồn quy tắc | Trạng thái |
|:---|:---|:---|:---|:---|:---:|
| **BR-CAT-01** | Mã SKU duy nhất | Definitional | Mỗi mặt hàng lưu trữ trong kho phải có một mã định danh hàng hóa (SKU) duy nhất, không được trùng lặp trong toàn bộ doanh nghiệp. | Case Study VTI & Chuẩn SOP GHN<br>*(P-01, P-02)* | Confirmed |
| **BR-CAT-02** | Mã vị trí lưu kho duy nhất | Definitional | Mỗi vị trí lưu trữ trong kho phải có một mã định danh duy nhất (Location Code), gắn với một khu vực lưu trữ cụ thể trong kho. | Quy trình kho chuẩn GHN<br>*(P-04, Scope 1 kho)* | Confirmed |
| **BR-CAT-03** | Trạng thái vị trí lưu kho | Definitional | Mỗi vị trí lưu trữ phải có trạng thái được ghi nhận rõ ràng: Trống (Empty - khi số lượng tồn bằng 0) hoặc Đang chứa hàng (Occupied - khi số lượng tồn lớn hơn 0). Trong phạm vi đơn kho, một vị trí cho phép chứa nhiều lô của cùng một mặt hàng (SKU). | Quy trình kho chuẩn GHN<br>*(P-04)* | Confirmed |
| **BR-CAT-04** | Đơn vị tính cơ sở duy nhất | Definitional | Mỗi mặt hàng chỉ sử dụng đúng một Đơn vị tính cơ sở duy nhất (Cái, Hộp, Kg) xuyên suốt mọi hoạt động nhập, xuất, tồn và kiểm kê kho. | Case Study VTI & Chuẩn SOP GHN<br>*(P-01)* | Confirmed |
| **BR-CAT-05** | Thiết lập định mức Min/Max | Definitional | Mọi mặt hàng phải được thiết lập định mức tồn kho an toàn gồm: Tồn tối thiểu (Min Stock ≥ 0) và Tồn tối đa (Max Stock > Min Stock) để làm căn cứ kiểm soát dự trữ. | Case Study VTI Solutions<br>*(P-07)* | Confirmed |
| **BR-LOT-01** | Khai báo Lô & Hạn sử dụng | Definitional | Đối với hàng hóa thuộc nhóm quản lý hạn sử dụng, mỗi lô hàng nhập kho phải có thông tin Số lô (Lot No) và Hạn sử dụng (EXP) được khai báo đầy đủ. Ngày sản xuất (MFG) là thông tin không bắt buộc, được ghi nhận khi nhà sản xuất có công bố. Các thông tin này được sử dụng để truy xuất lô hàng và hỗ trợ kiểm soát hạn sử dụng, bao gồm việc áp dụng nguyên tắc FEFO. | Case Study VTI & Chuẩn SOP GHN<br>*(P-05)* | Confirmed |
| **BR-INV-01** | Công thức xác định lượng hàng cần bổ sung | Definitional | Đề xuất đặt hàng được kích hoạt khi: $(\text{Tồn khả dụng} + \text{Hàng đang về theo PO đã duyệt}) \le \text{Min Stock}$. Số lượng hàng cần đặt bổ sung được xác định theo công thức: $\max(0, \text{Max Stock} - (\text{Tồn khả dụng} + \text{Hàng đang về theo PO đã duyệt}))$. Trường hợp tổng tồn khả dụng và hàng đang về lớn hơn hoặc bằng Tồn tối đa, số lượng cần bổ sung bằng 0 nhằm tránh phát sinh đặt trùng. | Case Study VTI Solutions<br>*(P-07)* | Confirmed |
| **BR-INV-02** | Cảnh báo tồn kho dưới mức tối thiểu | Behavioral | Khi số lượng tồn kho khả dụng của một mặt hàng chạm hoặc giảm xuống dưới mức tồn tối thiểu ($\text{Tồn khả dụng (Available)} \le \text{Min Stock}$), hệ thống phải tự động đưa mặt hàng đó vào danh sách cảnh báo thiếu hàng để chủ động lên kế hoạch bổ sung. | Case Study VTI Solutions<br>*(P-03, P-07)* | Confirmed |
| **BR-INV-03** | Cảnh báo hàng sắp hết hạn | Behavioral | Hàng hóa có thời hạn sử dụng còn lại chạm ngưỡng cận hạn ($\text{EXP Date} - \text{Ngày hiện tại} \le 30\text{ ngày}$, hoặc theo số ngày cấu hình riêng cho từng nhóm hàng) phải được hệ thống tự động đưa vào danh sách cảnh báo cận hạn theo chu kỳ rà soát hàng ngày (lúc $00:00:00$ và khi mở Dashboard kho) để Thủ kho ưu tiên giải phóng hoặc trình phương án thanh lý kịp thời. | Case Study VTI Solutions<br>*(P-05)* | Confirmed |
| **BR-INB-01** | Nhập kho gắn Đơn mua hàng (PO) | Behavioral | Hoạt động tiếp nhận hàng vào kho bắt buộc phải căn cứ trên Đơn mua hàng (PO) đang có hiệu lực (đã phê duyệt, chưa bị hủy hoặc đóng). Số lượng thực nhận của từng mặt hàng không được vượt quá số lượng còn lại chưa giao của đơn hàng (Thực nhận ≤ Còn lại); doanh nghiệp chấp thuận giao hàng làm nhiều đợt đối với hàng giao thiếu nhưng từ chối tiếp nhận phần hàng giao thừa. | Case Study VTI & Chuẩn SOP GHN<br>*(P-03, P-08)* | Confirmed |
| **BR-INB-02** | Cập nhật tồn khi phiếu nhập hoàn tất | Behavioral | Số lượng tồn kho thực tế (On-hand) chỉ được ghi nhận tăng khi Phiếu nhập kho chính thức chuyển sang trạng thái Hoàn tất (Completed) sau khi Thủ kho đã hoàn tất kiểm đếm thực tế và được Quản lý kho thẩm định, phê duyệt. Phiếu ở trạng thái Nháp (Draft) hoặc Chờ phê duyệt (Pending Approval) tuyệt đối không làm thay đổi số liệu tồn kho thực tế. Hệ thống tự động ghi nhận danh tính người lập (Thủ kho), người duyệt (Quản lý kho) và thời điểm hoàn tất để làm vết kiểm toán. | Case Study VTI & Chuẩn SOP GHN<br>*(P-01, P-02, P-08)* | Confirmed |
| **BR-INB-03** | Chỉ định vị trí lưu trữ khi nhập | Behavioral | Hàng hóa khi nhập kho bắt buộc phải được chỉ định và cất vào một vị trí lưu trữ xác định; không cho phép lưu kho hàng hóa ở trạng thái không rõ vị trí. | Quy trình kho chuẩn GHN<br>*(P-04)* | Confirmed |
| **BR-INB-04** | Xử lý hàng không đạt khi nhận | Behavioral | Khi tiếp nhận hàng tại cửa kho, nếu phát hiện hàng hóa bị hư hỏng, rách bao bì, sai quy cách hoặc không đủ hạn sử dụng tối thiểu, Thủ kho chỉ lập phiếu nhập kho cho phần số lượng đạt chuẩn. Phần số lượng không đạt bị từ chối tiếp nhận, ghi rõ lý do từ chối trên biên bản giao nhận và tuyệt đối không được ghi nhận tăng vào tồn kho khả dụng. | Quy trình kho chuẩn GHN<br>*(P-01, P-02)* | Confirmed |
| **BR-OUT-01** | Cấm xuất vượt tồn khả dụng và cơ chế giữ chỗ tự động | Behavioral | Số lượng hàng hóa yêu cầu xuất kho không được vượt quá số lượng tồn kho khả dụng (Available), được xác định bằng tồn kho thực tế (On-hand) trừ tồn kho giữ chỗ (Reserved) và số lượng hàng quá hạn hoặc hư hỏng không đủ điều kiện xuất kho.<br>Khi phiếu xuất kho được gửi đi và chuyển sang trạng thái Chờ phê duyệt (Pending Approval), hệ thống phải kiểm tra số lượng tồn kho khả dụng. Nếu số lượng yêu cầu đáp ứng điều kiện xuất kho, hệ thống tự động tạm giữ (Reserve) số lượng hàng tương ứng nhằm ngăn ngừa nhiều phiếu xuất cùng sử dụng vượt quá lượng tồn khả dụng (việc giữ chỗ chỉ làm giảm Available mà tuyệt đối không làm thay đổi On-hand).<br>Nếu phiếu xuất kho bị Quản lý kho từ chối (Rejected) hoặc bị hủy (Cancelled), hệ thống phải tự động giải phóng lượng hàng đã giữ chỗ và cập nhật lại tồn kho khả dụng.<br>Phiếu xuất kho ở trạng thái Nháp (Draft) chưa phát sinh cơ chế giữ chỗ và được phép chỉnh sửa hoặc xóa bỏ. | Case Study VTI & Chuẩn SOP GHN<br>*(P-01, P-02)* | Confirmed |
| **BR-OUT-02** | Nguyên tắc ưu tiên xuất hàng theo FEFO / FIFO | Behavioral | Đối với hàng hóa có quản lý hạn sử dụng, hệ thống bắt buộc tự động ưu tiên gợi ý xuất lô có hạn dùng ngắn nhất trước (FEFO); đối với hàng hóa không quản lý hạn sử dụng, ưu tiên xuất lô nhập kho trước (FIFO). Trường hợp Thủ kho chọn xuất lô khác thứ tự ưu tiên của hệ thống, bắt buộc phải nhập lý do ngoại lệ và phải được Quản lý kho phê duyệt trước khi tiến hành xuất kho. | Case Study VTI & Chuẩn SOP GHN<br>*(P-05)* | Confirmed |
| **BR-OUT-03** | Chặn xuất lô hàng đã hết hạn | Behavioral | Hàng hóa được phép xuất kho phục vụ sản xuất hoặc bán hàng cho đến hết $23:59:59$ của ngày Hạn sử dụng (EXP Date). Hệ thống tự động khóa và cấm xuất kho dưới mọi hình thức đối với các lô hàng đã quá hạn sử dụng ($\text{Ngày xuất} > \text{EXP Date}$, tính từ $00:00:00$ của ngày kế tiếp sau EXP). | Case Study VTI & Chuẩn SOP GHN<br>*(P-05)* | Confirmed |
| **BR-OUT-04** | Trừ tồn khi phiếu xuất hoàn tất giao hàng | Behavioral | Số lượng tồn kho thực tế (On-hand) chỉ được ghi nhận giảm và giải phóng trạng thái giữ chỗ khi Phiếu xuất kho được Quản lý kho phê duyệt và Thủ kho xác nhận hoàn tất giao hàng thực tế khỏi kho. Phiếu ở các trạng thái trước hoàn tất chỉ tác động đến lượng tồn giữ chỗ (Reserved) mà tuyệt đối không làm thay đổi số lượng tồn kho thực tế. | Case Study VTI & Chuẩn SOP GHN<br>*(P-01, P-02)* | Confirmed |
| **BR-OUT-05** | Cách ly và xuất hủy hàng quá hạn | Behavioral | Các lô hàng khi đã quá hạn sử dụng ($\text{Ngày hiện tại} > \text{EXP Date}$) phải được hệ thống tự động cô lập, loại khỏi Tồn khả dụng và chuyển sang trạng thái "Chờ tiêu hủy/Thanh lý". Hàng quá hạn chỉ được phép xuất khỏi kho thông qua loại chứng từ riêng biệt là Phiếu xuất hủy hoặc Phiếu thanh lý có sự phê duyệt của Quản lý kho. | Case Study VTI Solutions<br>*(P-05)* | Confirmed |
| **BR-OUT-06** | Bắt buộc ghi nhận lô thực xuất | Behavioral | Đối với các mặt hàng thuộc nhóm quản lý theo lô và hạn sử dụng (`Manage_By_Lot = TRUE`), trên Phiếu xuất kho hoàn tất bắt buộc phải ghi nhận chính xác Số lô (Lot Number) và Vị trí lưu kho thực tế đã lấy hàng, không được phép xuất chung chung theo mã SKU để đảm bảo khả năng truy xuất nguồn gốc. | Case Study VTI & Chuẩn SOP GHN<br>*(P-05)* | Confirmed |
| **BR-STK-01** | Phê duyệt chênh lệch kiểm kê | Behavioral | Mọi chênh lệch giữa số lượng tồn kiểm kê thực tế và số lượng tồn trên sổ sách phải được Quản lý kho xem xét và phê duyệt trước khi tiến hành điều chỉnh số liệu tồn kho. | Quy trình kho chuẩn GHN<br>*(P-06, P-08)* | Confirmed |
| **BR-STK-02** | Phân tách vai trò kiểm đếm và duyệt kiểm kê | Behavioral | Nhân sự trực tiếp thực hiện kiểm đếm thực tế tại kho (Thủ kho hoặc Nhân viên kiểm kê) không được đồng thời là người phê duyệt kết quả điều chỉnh chênh lệch của đợt kiểm kê đó nhằm đảm bảo nguyên tắc kiểm soát nội bộ độc lập và phòng ngừa gian lận. Thẩm quyền phê duyệt xử lý chênh lệch tồn kho thuộc về Quản lý kho. | Case Study VTI & Chuẩn SOP GHN<br>*(P-06, P-08)* | Confirmed |
| **BR-STK-03** | Xử lý chênh lệch kiểm kê và bắt buộc mã lý do | Behavioral | Khi chênh lệch giữa số lượng kiểm kê thực tế và số liệu sổ sách vượt ngưỡng sai lệch quy định ($\ge 2\%$ về số lượng hoặc giá trị chênh lệch $\ge 5.000.000$ VNĐ), bắt buộc phải thực hiện kiểm đếm lại lần 2 bởi nhân sự độc lập trước khi chốt biên bản. Mọi bút toán điều chỉnh tồn kho sau kiểm kê bắt buộc phải chọn Mã lý do điều chỉnh hợp lệ (Hao hụt tự nhiên, Hư hỏng, Thất thoát, Sai sót nhập liệu) để phục vụ kiểm toán. | Case Study VTI & Chuẩn SOP GHN<br>*(P-06, P-08)* | Confirmed |
| **BR-SEC-01** | Cấm sửa xóa chứng từ đã hoàn tất | Behavioral | Chứng từ nhập, xuất và biên bản kiểm kê một khi đã ở trạng thái Hoàn tất (Completed) thì trở thành hồ sơ lưu trữ cố định; nghiêm cấm chỉnh sửa trực tiếp số lượng hoặc xóa bỏ dưới mọi hình thức. Mọi sai lệch phát hiện sau khi hoàn tất phải được xử lý thông qua quy trình lập chứng từ điều chỉnh hoặc giao dịch bù trừ có phê duyệt của Quản lý kho nhằm đảm bảo tính toàn vẹn của vết kiểm toán. | Case Study VTI & Chuẩn SOP GHN<br>*(P-08, P-10)* | Confirmed |
| **BR-SEC-02** | Ghi nhận nhật ký thao tác kho | Behavioral | Mọi thao tác làm thay đổi số lượng tồn kho hoặc chuyển đổi trạng thái chứng từ kho đều phải được ghi nhận rõ danh tính người thực hiện và thời điểm thực hiện để phục vụ kiểm toán nội bộ. | Case Study VTI Solutions<br>*(P-08, P-10)* | Confirmed |
| **BR-SEC-03** | Nguyên tắc phân tách thẩm quyền chứng từ (Maker-Checker) | Behavioral | Người lập chứng từ (Thủ kho lập Phiếu nhập/xuất/xuất hủy, người đếm kiểm kê) không được phép tự phê duyệt chứng từ do chính mình tạo ra. Mọi chứng từ kho bắt buộc phải trải qua quy trình 2 bước độc lập (Lập $\rightarrow$ Duyệt) và được phê duyệt bởi Quản lý kho theo Ma trận Thẩm quyền (Mục 1.5) trước khi chính thức có hiệu lực cập nhật tồn kho. Tuyệt đối không có ngoại lệ cho phép tự duyệt dưới mọi hình thức. | Case Study VTI & Chuẩn SOP GHN<br>*(P-08, P-10)* | Confirmed |
