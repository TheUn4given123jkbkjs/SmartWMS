# CHƯƠNG 2: KHẢO SÁT VÀ MÔ TẢ HIỆN TRẠNG

---

### GHI CHÚ VỀ NGUỒN DỮ LIỆU KHẢO SÁT
Toàn bộ dữ liệu hiện trạng trong chương này được trích xuất trực tiếp từ **bài viết nghiên cứu trường hợp (Case study) được công bố công khai trên website của VTI Solutions** (ngày 28/07/2025): *"Hành trình cải tiến hệ thống Quản lý kho và Mua hàng cùng Tập đoàn Công nghiệp Nhật Bản"*. 

Nhóm tác giả đóng vai trò là đội ngũ phân tích nghiệp vụ độc lập, tiếp nhận các thông tin bối cảnh, thực trạng vận hành thủ công và các chỉ số đo lường thực tế do VTI Solutions công bố để làm đề bài nghiên cứu. Nhóm **không thực hiện phỏng vấn thực tế tại nhà máy hay tiếp cận tài liệu nội bộ không công khai**, mà sử dụng phương pháp **Suy luận nghiệp vụ (Business Deduction)**, **Phân tích chân dung vai trò (Persona Analysis)** và **Đối chiếu chuẩn vận hành kho tiêu chuẩn (SOP Benchmarking)** để bóc tách luồng công việc AS-IS, xác định điểm nghẽn và tổng hợp nhu cầu cải tiến.

---

## 2.1. PHƯƠNG PHÁP KHẢO SÁT

Từ nội dung bài viết tình huống của VTI Solutions, nhóm BA áp dụng quy trình xử lý thông tin gồm ba kỹ thuật phân tích nghiệp vụ chuẩn mực:
1. **Trích xuất dữ kiện thực tế (Case Study Fact Extraction):** Thu thập toàn bộ các mô tả về thực trạng vận hành thủ công, các điểm nghẽn (bottlenecks) và số liệu đo lường định lượng có thật được VTI Solutions nêu rõ trong bài viết (như: tình trạng sử dụng Excel/thẻ kho giấy/email, hàng xếp ngẫu nhiên, độ chính xác chỉ đạt 85–90%, đứt gãy thông tin giữa Mua hàng và Kho).
2. **Phân tích chân dung vai trò (Persona Analysis):** Từ các bộ phận được đề cập trong bài viết (Thủ kho, Mua hàng, Kế hoạch sản xuất, Quản lý), nhóm tái hiện lại trách nhiệm, khó khăn thực tế và luồng thao tác hàng ngày của từng vai trò trong môi trường kho thủ công.
3. **Đối chiếu với Chuẩn vận hành kho (SOP Benchmarking):** Đối sánh các bất cập trong bài viết với quy trình quản lý kho hàng chuẩn mực trong ngành sản xuất - phân phối (nhập kho theo PO, lưu kho có vị trí, lấy hàng FIFO/FEFO, xuất kho kiểm soát tồn khả dụng, kiểm kê định kỳ) để làm rõ các mắt xích đang bị lỗi.

### Bảng 2.1: Bảng trích xuất dữ kiện hiện trạng từ Case Study theo từng vai trò nghiệp vụ

| Nhóm vai trò trong chuỗi vận hành | Dữ kiện thực tế được ghi nhận trong Case Study VTI | Suy luận nghiệp vụ và Thao tác thực tế tương ứng |
| :--- | :--- | :--- |
| **Nhân viên kho (Thủ kho)** | - *"Sử dụng bảng tính Excel để ghi chép, thẻ kho giấy để theo dõi tồn kho và thông báo qua email để xử lý các giao dịch nhập xuất".*<br>- *"Sản phẩm và nguyên liệu xếp ngẫu nhiên... mất rất nhiều thời gian để tìm kiếm".* | Phải ghi sổ sách giấy tại giá kệ khi tiếp nhận hoặc xuất hàng; cuối ngày mới gõ lại vào Excel; đi tìm hàng theo trí nhớ hoặc hỏi người làm lâu năm; tiện đâu bốc đó ở ngoài cùng làm vi phạm nguyên tắc FIFO. |
| **Phòng Mua hàng (Procurement)** | - *"Phòng mua hàng không thể nắm bắt kịp thời tình trạng tồn kho thực tế".*<br>- *"Thiếu vắng hệ thống tích hợp dẫn đến thông tin bất đối xứng".* | Không nhìn thấy số tồn khả dụng thời gian thực; chỉ biết kho thiếu hàng khi xưởng báo sang; phải gọi điện/email hỏi thủ kho $\rightarrow$ rơi vào thế bị động, phải đặt hàng khẩn cấp. |
| **Bộ phận Kho & Kế hoạch tiếp nhận** | - *"Bộ phận kho lại không được thông báo trước về kế hoạch nhập hàng để chuẩn bị không gian và nhân lực".* | Xe nhà cung cấp đến bất ngờ mới biết có hàng về; không kịp bố trí người bốc dỡ và dọn dẹp kệ; hàng cất tạm ở lối đi hoặc nhét vào bất kỳ chỗ trống nào. |
| **Quản lý kho & Ban Lãnh đạo** | - *"Quản lý kho không đồng nhất: 1 nhà máy có WMS hạn chế, 2 nhà máy làm truyền thống".*<br>- *"Mỗi nhà máy vận hành theo quy trình riêng biệt, tạo ra bất đồng bộ dữ liệu".*<br>- *"Độ chính xác tồn kho với phương pháp thủ công chỉ 85–90%".* | Lãnh đạo không có số liệu tổng quan toàn tập đoàn; báo cáo tổng hợp Excel trễ nhiều ngày; kiểm kê định kỳ sai lệch lớn (10–15%) nhưng không thể truy vết nguyên nhân do chứng từ cũ thiếu chi tiết. |

---

## 2.2. TỔNG QUAN TỔ CHỨC VÀ BỐI CẢNH NGHIỆP VỤ

Như đã giới thiệu tại **Chương 1**, đề tài dựa trên Case Study của VTI Solutions công bố tháng 07/2025 về một tập đoàn công nghiệp Nhật Bản có nhiều nhà máy tại Việt Nam. Trong đó, hai nhà máy thuộc phân mảng sản xuất phụ tùng và linh kiện điện tử vận hành kho hoàn toàn thủ công, chưa có phần mềm quản lý. Đây chính là phạm vi khảo sát của đề tài.

Mỗi nhà máy sở hữu kho nguyên vật liệu đầu vào và kho thành phẩm đầu ra riêng biệt. Đề tài tập trung vào **hiện trạng kho thủ công** — nơi các vấn đề nghiêm trọng nhất và lợi ích của hệ thống mới là rõ rệt nhất. Các bộ phận liên quan trực tiếp đến hoạt động kho gồm: Bộ phận Kho, Phòng Mua hàng, Bộ phận Kế hoạch Sản xuất, Bộ phận Kế toán và Ban Quản lý Nhà máy.

---

## 2.3. CÁC ĐỐI TƯỢNG LIÊN QUAN (STAKEHOLDER)

Trong chuỗi vận hành kho thủ công của doanh nghiệp, các bên liên quan trực tiếp và gián tiếp có vai trò cùng các mối quan tâm cốt lõi được tổng hợp trong bảng sau:

### Bảng 2.2: Danh sách các bên liên quan trong chuỗi vận hành kho

| Đối tượng liên quan | Vai trò trong nghiệp vụ hiện tại | Mối quan tâm & "Nỗi đau" chính |
| :--- | :--- | :--- |
| **Quản lý kho / Thủ kho trưởng** | Điều hành hoạt động kho, duyệt phiếu nhập xuất, tổng hợp báo cáo tồn, tổ chức kiểm kê định kỳ. | Số liệu tồn kho phải chính xác; kiểm kê nhanh chóng, ít sai sót; phê duyệt chứng từ minh bạch, không mất mát. |
| **Nhân viên kho (Thủ kho)** | Nhận hàng, cất hàng, lấy hàng xuất, ghi chép thẻ kho giấy và nhập lại vào bảng tính Excel. | Thao tác đơn giản, tìm kiếm hàng nhanh chóng; có chỉ dẫn vị trí rõ ràng; không phải ghi chép sổ sách trùng lặp. |
| **Nhân viên Phòng Mua hàng** | Lập đơn mua hàng (PO), theo dõi nhà cung cấp giao hàng, quyết định thời điểm đặt hàng bổ sung. | Nắm được số tồn thực tế và hàng sắp về theo PO; được cảnh báo kịp thời khi hàng chạm ngưỡng tồn tối thiểu. |
| **Bộ phận Sản xuất / Kế hoạch** | Gửi yêu cầu xuất nguyên vật liệu, phụ tùng phục vụ các ca sản xuất. | Được cấp phát hàng đúng hẹn, đúng quy cách; biết trước số lượng còn hay hết để chủ động lịch trình dây chuyền. |
| **Nhà cung cấp** *(bên ngoài hệ thống)* | Giao hàng đến kho theo Đơn mua hàng (PO) đã cam kết. SmartWMS không cung cấp giao diện trực tiếp cho nhà cung cấp; tương tác được ghi nhận gián tiếp qua chứng từ nhập kho. | Quá trình giao nhận tại cửa kho diễn ra minh bạch; biên bản đối soát số lượng thực nhận chính xác, tránh tranh chấp. |
| **Bộ phận Kế toán kho** | Đối chiếu số liệu nhập - xuất - tồn kho với chứng từ hóa đơn và sổ cái tài chính. | Chứng từ đầy đủ, số liệu trên phần mềm khớp với chứng từ vật lý; có vết kiểm toán (Audit Trail) để đối chiếu. |
| **Quản lý nhà máy / Ban Lãnh đạo** | Giám sát tổng thể hoạt động các nhà máy và ra quyết định chiến lược. | Báo cáo tồn kho tổng quan, chính xác theo thời gian thực để ra quyết định phân bổ nguồn lực kịp thời. |
| **Hệ thống ERP / MES** *(ngoài phạm vi)* | Cung cấp kế hoạch sản xuất tổng thể và đơn hàng mua. | Chuẩn hóa cấu trúc dữ liệu để sẵn sàng kết nối, đồng bộ dữ liệu hai chiều trong tương lai. |

---

## 2.4. QUY TRÌNH NGHIỆP VỤ HIỆN TẠI (AS-IS)

Dựa trên các dữ kiện thực tế từ Case Study của VTI Solutions kết hợp với quy chuẩn vận hành kho hàng công nghiệp, quy trình vận hành hiện tại của kho hàng thủ công được mô tả chi tiết qua 5 quy trình cốt lõi sau:

### 2.4.1. Quy trình Nhập kho
Đối với hoạt động tiếp nhận hàng từ nhà cung cấp, quy trình nhập kho diễn ra theo các bước sau:
1. Phòng Mua hàng thông báo kế hoạch nhập hàng cho bộ phận kho thông qua email hoặc gửi file Excel/PO. Tuy nhiên, thông báo thường đến muộn hoặc đến cùng lúc khi xe hàng của nhà cung cấp đã tới cửa kho, khiến nhân viên kho bị động, không kịp chuẩn bị trước vị trí giá kệ và nhân lực bốc dỡ.
2. Xe nhà cung cấp đến, nhân viên kho đối chiếu phiếu giao hàng của lái xe với bản in Đơn mua hàng (PO).
3. Nhân viên kho tiến hành mở kiện, kiểm đếm số lượng và kiểm tra ngoại quan sản phẩm. Mọi số liệu thực tế được nhân viên ghi tay bằng bút bi vào góc phiếu giao hàng.
4. Nhân viên kho tự chọn vị trí cất hàng theo thói quen và chỗ trống hiện có trên kệ, không có tiêu chí phân vùng rõ ràng theo chủng loại hay đặc tính hàng hóa.
5. Nhân viên lập Phiếu nhập kho bằng tay (hoặc in từ mẫu Excel có sẵn), Thủ kho và lái xe ký xác nhận.
6. Nhân viên kho lấy cuốn Thẻ kho giấy của mặt hàng đó để ghi thêm số lượng vừa nhập.
7. Đến cuối ngày làm việc hoặc sang ngày hôm sau, nhân viên mới gom toàn bộ phiếu nhập kho giấy để nhập số liệu vào file Excel tổng hợp tồn kho.
8. Bộ phận kho gửi email xác nhận đã nhận hàng cho Phòng Mua hàng và Kế toán.
* **Tình huống ngoại lệ thường gặp:** Giao thiếu hàng, giao thừa hàng, sai mã quy cách, hàng bị rách bao bì hoặc biến chất. Khi đó, nhân viên kho chỉ gọi điện thoại hoặc gửi email cho Mua hàng và Nhà cung cấp; kết quả xử lý không được ghi nhận thống nhất trên hệ thống. Hàng hóa cận hạn hoặc không rõ số lô cũng không được theo dõi chặt chẽ vì thẻ kho giấy không theo dõi hạn dùng cho từng lô.

### 2.4.2. Quy trình Xuất kho
Khi phát sinh nhu cầu xuất nguyên vật liệu cho xưởng sản xuất hoặc thành phẩm giao cho khách hàng:
1. Bộ phận sản xuất (hoặc bộ phận bán hàng) gửi yêu cầu xuất hàng bằng email hoặc phiếu giấy sang kho.
2. Nhân viên kho kiểm tra số tồn trên file Excel hoặc xem thẻ kho giấy. Do dữ liệu cập nhật chậm trễ, nhiều trường hợp nhân viên phải trực tiếp đi ra giá kệ đếm lại mới dám khẳng định còn hàng hay không.
3. Nhân viên kho đi tìm hàng trên các dãy kệ. Vì hàng hóa xếp ngẫu nhiên theo chỗ trống lúc nhập, người mới hầu như phải hỏi người làm lâu năm mới biết mặt hàng đó đang nằm ở đâu.
4. Việc lựa chọn hàng cũ xuất trước (FIFO/FEFO) phụ thuộc hoàn toàn vào ý thức và trí nhớ của nhân viên, không có công cụ tự động hỗ trợ. Nhân viên thường tiện tay lấy các kiện hàng nằm ở phía ngoài cùng.
5. Nhân viên lập Phiếu xuất kho giấy, bên nhận ký xác nhận bàn giao.
6. Nhân viên ghi giảm số lượng trên Thẻ kho giấy của mặt hàng đó, sau đó đến cuối ngày mới cập nhật trừ tồn trên file Excel.
* **Tình huống ngoại lệ:** Nếu hàng trên kệ không đủ số lượng như yêu cầu, kho báo lại cho bộ phận yêu cầu bằng điện thoại và buộc phải xuất một phần (giao thiếu), gây xáo trộn kế hoạch sản xuất.

### 2.4.3. Quản lý Tồn kho và Báo cáo
* **Tình trạng hai nguồn số liệu song song:** Mỗi mặt hàng có một Thẻ kho giấy treo tại kệ; đồng thời thủ kho duy trì một file bảng tính Excel tổng hợp tồn kho trên máy tính. Hai nguồn dữ liệu này được cập nhật vào các thời điểm khác nhau nên thường xuyên xảy ra tình trạng lệch số liệu, không biết nguồn nào là chính xác.
* **Độ trễ báo cáo:** Các báo cáo Nhập - Xuất - Tồn (tuần, tháng) do thủ kho tổng hợp thủ công, cắt dán từ nhiều sheet Excel rồi gửi email cho Quản lý và Phòng Mua hàng. Báo cáo vì thế luôn bị trễ từ 3 đến 5 ngày so với biến động thực tế tại kho. Ban Lãnh đạo chỉ có cái nhìn tổng quan sau khi số liệu được gom lại, gây khó khăn cho việc ra quyết định kịp thời.

### 2.4.4. Quy trình Kiểm kê
Đối với công tác kiểm kê định kỳ tại kho hàng:
1. Định kỳ cuối mỗi quý hoặc cuối năm, Quản lý kho thông báo lịch kiểm kê toàn diện. Đợt kiểm kê thường bắt buộc phải **tạm dừng hoặc hạn chế tối đa hoạt động xuất nhập kho từ 2 đến 3 ngày**.
2. Thủ kho in danh sách hàng hóa và số tồn trên sổ sách từ file Excel ra giấy và chia khu vực cho các nhóm nhân viên đi đếm.
3. Nhân viên kiểm đếm thực tế từng kiện hàng và ghi số đếm vào danh sách giấy.
4. Toàn bộ số liệu giấy được nhập ngược lại vào máy tính để chạy công thức đối chiếu với số sổ sách.
5. Đối với các mặt hàng có chênh lệch, các nhóm phải tổ chức đếm lại lần hai, lập biên bản giải trình thừa/thiếu và đề xuất phương án xử lý.
6. Quản lý kho duyệt biên bản, sau đó thủ kho tiến hành chỉnh sửa số liệu trên Excel và Thẻ kho giấy.
* **Hậu quả:** Quy trình tốn rất nhiều ngày công, dễ nhầm lẫn khi chép tay và nhập lại máy tính, đặc biệt là khó truy cứu nguyên nhân chênh lệch vì các giao dịch cũ không lưu vết chi tiết. Theo số liệu của case study, **độ chính xác tồn kho khi làm thủ công chỉ đạt mức 85% – 90%**. Các đợt kiểm kê đột xuất gần như không thể thực hiện vì quá tốn kém thời gian và công sức.

### 2.4.5. Quy trình Theo dõi Tồn thấp và Đặt hàng bổ sung
Đối với việc theo dõi mức dự trữ và đặt hàng bổ sung:
* Hiện tại kho **hoàn toàn không có định mức tồn kho tối thiểu (Min Stock) hay tồn tối đa (Max Stock)** được quản lý có hệ thống. Thủ kho nhận biết hàng sắp hết chủ yếu dựa vào kinh nghiệm nhìn thấy kệ vơi đi hoặc khi xưởng sản xuất sang đòi hàng thì mới phát hiện hết hàng.
* Phòng Mua hàng muốn biết số lượng tồn kho để lên kế hoạch đặt hàng phải gọi điện hoặc gửi email sang kho rồi chờ thủ kho kiểm tra phản hồi.
* Phòng Mua hàng lập PO dựa trên thông tin nhận được nhưng thường xuyên rơi vào tình trạng "đặt hàng khẩn cấp", dẫn đến giá mua kém thuận lợi và chi phí vận tải phát sinh cao. Ngược lại, bộ phận kho không biết trước các PO nào sắp về để chủ động chuẩn bị nhân lực và sắp xếp mặt bằng giá kệ.

---

## 2.5. CÔNG CỤ, BIỂU MẪU VÀ DỮ LIỆU ĐANG SỬ DỤNG

### 2.5.1. Công cụ và biểu mẫu hiện tại
Trong phương thức vận hành truyền thống tại hai nhà máy, các bộ phận sử dụng tập hợp các công cụ thủ công và biểu mẫu rời rạc:

### Bảng 2.3: Công cụ và biểu mẫu hiện tại trong vận hành kho

| Công cụ / Biểu mẫu | Mục đích sử dụng | Hạn chế và Rủi ro vận hành |
| :--- | :--- | :--- |
| **File Excel tồn kho** | Tổng hợp số liệu nhập - xuất - tồn, lập báo cáo tuần/tháng. | Cập nhật thủ công, nhiều phiên bản phân tán, không có kiểm soát truy cập phân quyền, dễ lỗi công thức tính. |
| **Thẻ kho giấy** | Treo tại đầu kệ để theo dõi biến động nhập, xuất, tồn của từng mặt hàng. | Chép tay hoàn toàn, dễ rách, ố mờ hoặc thất lạc; không thể tra cứu nhanh từ xa. |
| **Phiếu nhập / xuất giấy** | Chứng từ pháp lý ghi nhận bàn giao giữa lái xe, thủ kho và người nhận. | Chữ viết tay khó đọc; lưu trữ thùng cồng kềnh; mất nhiều thời gian lục tìm đối soát khi có sự cố. |
| **Email trao đổi** | Thông báo kế hoạch giao hàng của PO, gửi yêu cầu xuất hàng, gửi file báo cáo. | Thông tin phân tán trong hộp thư cá nhân, dễ sót thông tin, không gắn liền với chứng từ kho. |
| **Điện thoại / Zalo** | Hỏi số lượng tồn tức thời, giải quyết các phát sinh khẩn cấp. | Không lưu vết lịch sử; không thể dùng làm bằng chứng đối soát khi xảy ra sai lệch số lượng. |
| **Phần mềm WMS cũ** *(1 nhà máy)* | Quản lý kho điện tử bước đầu. | Còn nhiều hạn chế khi sử dụng thực tế; quy trình riêng biệt, không đồng bộ dữ liệu với 2 nhà máy còn lại. |

Dữ liệu đang quản lý bao gồm các trường cơ bản: Mã sản phẩm (SKU), Tên, Đơn vị tính, Số lượng tồn sổ sách, Ngày ghi nhận, Tên nhà cung cấp, Số PO, Số phiếu nhập/xuất, Người giao/nhận và Ghi chú. Tuy nhiên, các trường nghiệp vụ thiết yếu hoàn toàn vắng mặt: **mã vị trí kho chuẩn hóa** theo phân vùng Khu vực - Ô kệ (Zone - Location Code), **số lô và hạn dùng** (Lot/EXP) từng kiện hàng, **định mức dự trữ an toàn** (Min/Max Stock), và **phân tách trạng thái tồn** giữa $On\text{-}hand$, $Reserved$ và $Available$. Đây chính là căn nguyên kỹ thuật dữ liệu của các vấn đề `P-02`, `P-04`, `P-05` và `P-07` được trình bày ở mục 2.6.

---

## 2.6. VẤN ĐỀ VÀ HẠN CHẾ CỦA HIỆN TRẠNG

Từ các nút thắt trong 5 quy trình AS-IS và hạn chế công cụ nêu trên, nhóm BA đã tổng hợp và chuẩn hóa thành **10 vấn đề hiện trạng cốt lõi (`P-01` đến `P-10`)**. Bảng danh mục này là căn cứ đầu vào trực tiếp để thiết lập 25 Quy tắc nghiệp vụ (BR) và Đặc tả yêu cầu chức năng (FR) của hệ thống:

### Bảng 2.4: Danh mục 10 vấn đề hiện trạng cốt lõi và nguyên nhân gốc rễ

| Mã vấn đề | Tên vấn đề hiện trạng | Biểu hiện thực tế tại kho | Hậu quả đối với doanh nghiệp | Nguyên nhân gốc rễ |
| :---: | :--- | :--- | :--- | :--- |
| **P-01** | **Dữ liệu tồn kho phân tán, không đồng bộ** | Số liệu Thẻ kho giấy và File Excel lệch nhau; dữ liệu Excel trễ 12–24h so với thực tế. | Độ chính xác tồn chỉ 85–90%; không nắm được tồn thật, phải đếm lại nhiều lần. | Chưa có hệ thống ghi nhận giao dịch tại thời điểm xảy ra (Real-time Transaction). |
| **P-02** | **Không kiểm soát được tồn khả dụng, nguy cơ cấp phát thiếu hàng** | Nhiều bộ phận sản xuất cùng yêu cầu xuất một mặt hàng đang có tồn thấp; số liệu Excel chậm trễ khiến không ai biết còn đủ hàng hay không cho đến khi ra kệ. | Thiếu hàng khi bốc dỡ; buộc phải xuất từng phần, làm gián đoạn kế hoạch sản xuất. | Không có cơ chế phân tách giữa $Available$ và $On\text{-}hand$; thiếu cơ chế tự động tạm giữ chỗ ($Reserved$) khi phiếu xuất chuyển sang trạng thái chờ phê duyệt (Pending Approval). |
| **P-03** | **Đứt gãy thông tin giữa Mua hàng và Kho** | Mua hàng không biết tồn thực; Kho không biết trước thông tin PO sắp giao về. | Đặt hàng khẩn cấp chi phí cao; kho bị động về nhân lực và mặt bằng giá kệ. | Giao tiếp qua email và điện thoại rời rạc; thiếu cơ sở dữ liệu PO liên thông. |
| **P-04** | **Bố trí hàng ngẫu nhiên, thiếu mã vị trí kho** | Hàng hóa xếp theo chỗ trống; không có phân vùng hay mã vị trí (Zone - Location Code). | Nhân viên mất 30–60 phút để tìm hàng; phụ thuộc hoàn toàn người làm lâu năm. | Không có quy tắc định danh vị trí lưu kho và chỉ dẫn cất/lấy hàng trên phần mềm. |
| **P-05** | **Không quản lý Lô và Hạn dùng, vi phạm FIFO/FEFO** | Thẻ kho không theo dõi Lot/EXP; nhân viên lấy hàng theo thói quen tiện tay ở ngoài. | Hàng cũ tồn đọng đáy kệ; phụ tùng gỉ sét, hóa chất hết hạn phải tiêu hủy lãng phí. | Thiếu mô hình dữ liệu Lot/EXP và thuật toán tự động gợi ý xuất theo FEFO/FIFO. |
| **P-06** | **Kiểm kê định kỳ kéo dài, sai số lớn (10–15%)** | Kiểm kê phải dừng kho 2–3 ngày; ghi chép giấy dễ nhầm lẫn; không kiểm kê đột xuất được. | Tốn nhiều ngày công; sai sót chép tay; khó truy cứu nguyên nhân chênh lệch. | Quy trình thủ công, thiếu công cụ đối chiếu tự động giữa thực tế và sổ sách. |
| **P-07** | **Thiếu định mức an toàn và cảnh báo tồn thấp** | Không có ngưỡng Min/Max Stock; kho hết hàng đột ngột hoặc nhập quá trần làm ứ đọng vốn. | Gián đoạn dây chuyền sản xuất; bị động trong kế hoạch đặt hàng bổ sung. | Thiếu tham số định mức tồn an toàn và cơ chế tự động kích hoạt cảnh báo. |
| **P-08** | **Chứng từ lỏng lẻo, thiếu cơ chế Maker-Checker** | Chứng từ giấy ký sơ sài; nhân viên vừa kiểm đếm vừa tự chỉnh sửa số liệu sổ sách. | Thất thoát khó kiểm soát; rủi ro thao túng số liệu; vi phạm kiểm soát nội bộ. | Quy trình thiếu phân tách trách nhiệm giữa người lập phiếu và người phê duyệt. |
| **P-09** | **Khó truy xuất lịch sử luân chuyển (Audit Trail)** | Thẻ kho giấy dễ rách/mất; không thể tra cứu ai đã thay đổi số lượng vào thời điểm nào. | Khó quy trách nhiệm cá nhân khi xảy ra khiếu nại, thiếu hụt hoặc hư hỏng hàng. | Không có nhật ký thao tác điện tử (Audit Log) lưu vết lịch sử giao dịch. |
| **P-10** | **Báo cáo quản trị chậm trễ, cắt dán thủ công** | Báo cáo tuần/tháng mất từ 3 đến 5 ngày tổng hợp Excel; rủi ro mất dữ liệu khi máy hỏng. | Ban Lãnh đạo ra quyết định chậm trễ; thiếu bức tranh tổng quan toàn tập đoàn. | Không có hệ thống tổng hợp báo cáo tự động theo thời gian thực (Real-time Dashboard). |

### 2.6.1. Phân tích nguyên nhân gốc rễ

Để làm rõ bản chất của các vấn đề hiện trạng, nhóm BA đã áp dụng kỹ thuật **5 Whys** để phân tích sâu hai bài toán nhức nhối nhất trong kho:

#### 1. Phân tích nguyên nhân vì sao độ chính xác tồn kho chỉ đạt 85% – 90% (P-01, P-06)
1. *Tại sao số liệu kiểm kê thực tế tại kho luôn lệch so với sổ sách từ 10% đến 15%?*  
   $\rightarrow$ Vì số liệu trên file Excel không phản ánh đúng các biến động xuất nhập phát sinh trong ngày.
2. *Tại sao file Excel không phản ánh kịp thời các giao dịch phát sinh?*  
   $\rightarrow$ Vì thủ kho phải ghi thẻ kho giấy tại kệ trước, sau đó đến cuối ngày hoặc hôm sau mới gom phiếu về gõ lại vào Excel.
3. *Tại sao phải ghi chép qua nhiều bước trung gian như vậy?*  
   $\rightarrow$ Vì kho chưa được trang bị phần mềm có cơ sở dữ liệu tập trung để cập nhật trực tuyến ngay tại thời điểm giao dịch hoàn tất.
4. *Tại sao việc dồn phiếu nhập cuối ngày lại sinh ra sai lệch lớn?*  
   $\rightarrow$ Vì trong suốt độ trễ 12–24 giờ đó, hàng hóa tiếp tục được xuất nhập mà không ai biết số tồn thực tế còn lại là bao nhiêu, dẫn đến việc quên ghi chép hoặc gõ nhầm số liệu giữa các mặt hàng.
5. *Nguyên nhân gốc rễ là gì?*  
   $\rightarrow$ **Thiếu một hệ thống phần mềm quản lý kho thời gian thực (Real-time WMS)** giúp tự động hóa việc cập nhật tồn kho ngay khi giao dịch được phê duyệt hoàn tất.

#### 2. Phân tích nguyên nhân vì sao hàng hóa bị quá hạn sử dụng và mất nhiều thời gian tìm kiếm (P-04, P-05)
1. *Tại sao doanh nghiệp thường xuyên phải hủy bỏ các lô phụ tùng bị gỉ sét hoặc hóa chất quá hạn sử dụng?*  
   $\rightarrow$ Vì các lô hàng cũ nhập trước bị tồn đọng ở đáy kệ mà không được mang ra xuất dùng trước.
2. *Tại sao nhân viên không mang các lô hàng cũ ra xuất dùng trước?*  
   $\rightarrow$ Vì khi lấy hàng, nhân viên có thói quen tiện tay bốc dỡ các kiện hàng mới nhập xếp ở hàng ngoài cùng.
3. *Tại sao nhân viên không chủ động tìm các lô hàng cũ hơn ở phía trong?*  
   $\rightarrow$ Vì trên thẻ kho giấy và bao bì không ghi chú nổi bật hạn sử dụng, và nhân viên không biết chính xác lô nào cũ hơn để tìm.
4. *Tại sao kho không nắm được thông tin lô nào cũ hơn và đang nằm ở đâu?*  
   $\rightarrow$ Vì bảng tính Excel hiện tại chỉ quản lý tổng số lượng theo mã sản phẩm (SKU) mà không có cấu trúc dữ liệu quản lý chi tiết theo **Vị trí (Location)** và **Số lô / Hạn dùng (Lot/EXP)**.
5. *Nguyên nhân gốc rễ là gì?*  
   $\rightarrow$ **Thiếu mô hình dữ liệu quản lý Lô - Vị trí và thuật toán tự động đề xuất xuất hàng theo FEFO/FIFO** được tích hợp trực tiếp vào quy trình xuất kho.

---

## 2.7. NHU CẦU VÀ KỲ VỌNG CỦA NGƯỜI DÙNG

Từ phân tích chân dung vai trò nghiệp vụ (Persona Analysis) và các điểm nghẽn thực tế ghi nhận từ Case Study, nhu cầu của người dùng được chuẩn hóa theo từng vai trò nghiệp vụ. Phần này là căn cứ đầu vào trực tiếp cho Chương 3 (Đặc tả yêu cầu chức năng và phi chức năng):

### Bảng 2.5: Nhu cầu và kỳ vọng của người dùng theo từng vai trò nghiệp vụ

| Nhóm vai trò nghiệp vụ | Nhu cầu và Kỳ vọng nghiệp vụ cụ thể | Mã vấn đề liên quan giải quyết |
| :--- | :--- | :---: |
| **Nhân viên kho (Thủ kho)** | - Nhập liệu một lần trên giao diện số, loại bỏ việc ghi chép trùng lặp giữa thẻ kho giấy và Excel.<br>- Hệ thống chỉ dẫn rõ ràng mặt hàng đang nằm ở Khu vực nào, Ô kệ nào theo mã vị trí (Zone - Location Code).<br>- Tự động hiển thị danh sách các lô hàng cần lấy xuất trước theo quy tắc FEFO/FIFO. | `P-01`, `P-04`, `P-05` |
| **Quản lý kho / Thủ kho trưởng** | - Quy trình kiểm kê điện tử nhanh chóng, tự động tính chênh lệch thừa/thiếu, hỗ trợ kiểm đếm lại lần 2.<br>- Phê duyệt chứng từ nhập/xuất điện tử theo cơ chế Maker-Checker; chỉ khi duyệt mới thay đổi số tồn.<br>- Tra cứu số tồn thực tế thời gian thực tại mọi thời điểm mà không phải chờ tổng hợp cuối tuần. | `P-01`, `P-06`, `P-08`, `P-09` |
| **Phòng Mua hàng** | - Hệ thống tự động phát cảnh báo khi tồn kho khả dụng chạm ngưỡng tối thiểu (Min Stock).<br>- Tự động gợi ý danh sách và số lượng hàng cần đặt bổ sung dựa trên định mức Max Stock và lượng hàng đang về theo PO đã duyệt (On-order) nhằm tránh đặt trùng.<br>- Theo dõi tiến độ giao hàng thực tế của các đơn mua hàng (PO) đã gửi cho nhà cung cấp. | `P-03`, `P-07` |
| **Bộ phận Sản xuất / Kế hoạch** | - Gửi yêu cầu xuất kho trực tuyến và biết ngay lượng hàng khả dụng ($Available$) có đủ hay không.<br>- Được cấp phát nguyên vật liệu đúng chủng loại, đúng hạn dùng, không bị gián đoạn dây chuyền. | `P-02`, `P-03`, `P-07` |
| **Bộ phận Kế toán kho** | - Chứng từ nhập xuất điện tử minh bạch, khớp 100% với số liệu biến động thực tế tại kho.<br>- Đầy đủ vết kiểm toán (Audit Trail) ghi nhận ai tạo, ai duyệt, vào thời gian nào để đối soát tài chính. | `P-01`, `P-08`, `P-09` |
| **Quản lý nhà máy / Ban Lãnh đạo** | - Dashboard tổng quan trực quan: theo dõi tỷ lệ chính xác tồn kho, hàng cảnh báo, giá trị tồn kho.<br>- Báo cáo tổng hợp Nhập - Xuất - Tồn kết xuất tức thời chỉ với một thao tác bấm chuột. | `P-09`, `P-10` |

---

## 2.8. TỔNG KẾT KHẢO SÁT

Hiện trạng quản lý kho thủ công bằng bảng tính Excel, thẻ kho giấy và trao đổi rời rạc gây ra ba nhóm rủi ro vận hành chính:
1. **Số liệu không chính xác và chậm trễ:** Tồn kho chênh lệch thường xuyên (độ chính xác chỉ đạt 85–90%), cập nhật dồn cuối ngày làm sai lệch số liệu và kiểm kê kéo dài (`P-01`, `P-02`, `P-06`, `P-10`).
2. **Đứt gãy thông tin giữa Mua hàng và Kho:** Thiếu chia sẻ dữ liệu đơn mua hàng (PO) và định mức dự trữ khiến phòng mua hàng bị động, phát sinh đơn hàng khẩn cấp (`P-03`, `P-07`).
3. **Thất thoát và vận hành kém hiệu quả:** Không quản lý vị trí khiến mất nhiều thời gian tìm hàng; không theo dõi Lô và Hạn dùng làm vi phạm nguyên tắc FIFO/FEFO dẫn đến nguy cơ hư hỏng, quá hạn (`P-04`, `P-05`, `P-08`, `P-09`).

Nguyên nhân cốt lõi là doanh nghiệp thiếu một hệ thống phần mềm quản lý kho tập trung thời gian thực. Toàn bộ 10 vấn đề hiện trạng (`P-01` đến `P-10`) và các kỳ vọng của người dùng nêu trên là căn cứ trực tiếp để thiết lập 25 Quy tắc nghiệp vụ (BR), đồng thời được cụ thể hóa thành Danh mục Yêu cầu chức năng (FR) và Yêu cầu phi chức năng (NFR) ở Chương 3.

