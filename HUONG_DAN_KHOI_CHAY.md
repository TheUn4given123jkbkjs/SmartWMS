# Hướng dẫn sử dụng SmartWMS

## Nếu dự án đang chạy trên máy này

1. Mở [SmartWMS](http://127.0.0.1:8089/auth/login).
2. Đăng nhập bằng `staff_thien` (nhân viên) hoặc `manager_trung` (quản lý). Mật khẩu demo là `123456`.
3. Để xem dữ liệu, mở [phpMyAdmin](http://127.0.0.1:8081). Nhập tên người dùng `smartwms_app` và lấy mật khẩu ở dòng `SMARTWMS_DB_PASSWORD` trong file `.env`.
4. Trong phpMyAdmin, chọn **smartwms** → **receipt_note** để xem phiếu nhập hoặc **purchase_order** để xem đơn mua.

## Khởi chạy từ đầu

### 1. Mở terminal trong thư mục gốc SmartWMS

Cần có Docker đang chạy, JDK 17 hoặc 21 và Maven. Thư mục hiện tại phải có file `compose.yaml`.

### 2. Tạo mật khẩu MySQL một lần

Nếu đã có file `.env`, bỏ qua bước này. Trên máy mới, chạy khối lệnh sau bằng Bash:

```bash
if [ ! -e .env ]; then
  (
    set -euC
    umask 077
    db_password=$(openssl rand -hex 24)
    root_password=$(openssl rand -hex 24)
    printf 'SMARTWMS_DB_PASSWORD=%s\nSMARTWMS_DB_ROOT_PASSWORD=%s\nSMARTWMS_WEB_PORT=8089\n' \
      "$db_password" "$root_password" > .env
  )
fi
```

### 3. Bật MySQL và phpMyAdmin

```bash
docker compose up -d --wait
```

### 4. Bật SmartWMS

Nếu bản SmartWMS đang mở được ở cổng 8089, bỏ qua bước này. Nếu chưa có file JAR hoặc vừa sửa mã nguồn, chạy:

```bash
mvn -f source/pom.xml verify
```

Sau đó chạy:

```bash
java -jar source/target/warehouse-management-system-1.0.0.jar
```

Giữ terminal mở. Khi thấy `Started WmsApplication`, mở [SmartWMS](http://127.0.0.1:8089/auth/login). Nếu file `.env` đặt `SMARTWMS_WEB_PORT` khác, dùng cổng đó trong đường dẫn.

### 5. Dừng và mở lại

- Nếu chạy Java trong terminal, nhấn **Ctrl+C**. Nếu đang dùng dịch vụ nền đã cài trên máy này, chạy `systemctl --user stop smartwms-mysql`.
- Để dừng MySQL và phpMyAdmin, chạy `docker compose stop`. Dữ liệu vẫn nằm trong volume `smartwms_mysql_data`.
- Lần sau, làm lại bước **3** và **4**. Nếu mã nguồn chưa đổi và đã có file JAR, chỉ cần chạy lệnh `java -jar`.

## Khi cần kiểm tra hoặc sao lưu

- Nếu phpMyAdmin chưa mở được, chạy `docker compose ps` để kiểm tra hai container.
- Nếu Java báo cổng đã được dùng, thử mở SmartWMS trước. Nếu cổng dành cho ứng dụng khác, sửa `SMARTWMS_WEB_PORT` trong `.env` rồi chạy lại Java.
- Để sao lưu trong phpMyAdmin, chọn **smartwms** → **Export** → **SQL** → **Export**. Lệnh `docker compose down -v` sẽ xóa volume cùng dữ liệu MySQL.
