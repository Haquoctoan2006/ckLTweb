# Ghi chú tiến độ - Cập nhật ngày 14/09/2026

## Đã hoàn thành (nhánh develop)

### 1. Cấu hình cơ bản
- `pom.xml`: đã thêm đủ JSP, JSTL, Sitemesh, JWT, Cloudinary, Mail
- `application.properties`: đã cấu hình MySQL + Gmail SMTP (dùng App Password, xem phần "Lưu ý" bên dưới)
- `SecurityConfig.java`: cho phép truy cập tự do các trang `/`, `/public/**`, `/login`, `/forgot-password/**`, các trang khác yêu cầu đăng nhập

### 2. Trang FAQ công khai — `/public/faq`
- Controller: `faq/controller/FaqPublicController.java`
- View: `WEB-INF/views/public/faq.jsp`
- Chức năng: hiện danh sách câu hỏi (đang dùng data mẫu cứng trong code), lọc theo chuyên mục (bấm badge), tìm kiếm theo từ khóa (gõ trực tiếp, không cần Enter)
- ⚠️ Data hiện tại là **mẫu cứng trong Controller**, chưa lấy từ DB thật — cần thay bằng Service + Repository sau

### 3. Trang đăng nhập — `/login`
- Controller: `auth/controller/AuthController.java`
- View: `WEB-INF/views/public/login.jsp`
- ⚠️ Form login hiện **chưa xử lý logic thật** (chưa nối với UserDetailsService), bấm submit sẽ lỗi. Cần làm tiếp phần xác thực JWT.

### 4. Quên mật khẩu — `/forgot-password` (3 bước)
- Controller: `auth/controller/PasswordResetController.java`
- Service: `auth/service/PasswordResetService.java`
- Entity: `common/entity/User.java`, `common/entity/PasswordResetOtp.java`
- Repository: `common/repository/UserRepository.java`, `common/repository/PasswordResetOtpRepository.java`
- View: 4 file JSP trong `WEB-INF/views/public/` (`forgot-password-step1/2/3.jsp`, `forgot-password-success.jsp`)
- Luồng: Nhập MSSV → tìm user trong DB → gửi OTP 6 số qua email (hết hạn 5 phút) → xác thực OTP → reset mật khẩu về `SPKT@2026` (mã hóa bcrypt)
- Đã test thành công với 1 user mẫu insert tay trong MySQL Workbench

## Cách chạy project (cho người B)

1. Pull code từ nhánh `develop`
2. Mở `application.properties`, sửa lại:
   - `spring.datasource.password` → mật khẩu MySQL của máy bạn
   - `spring.mail.username` / `spring.mail.password` → dùng Gmail + App Password của bạn (không dùng chung với người khác)
3. Chạy: `mvnw spring-boot:run`
4. Test các trang:
   - `http://localhost:8080/public/faq`
   - `http://localhost:8080/login`
   - `http://localhost:8080/forgot-password`

## Việc cần làm tiếp (gợi ý phân công)

- [ ] Xử lý logic đăng nhập thật (JWT, UserDetailsService) — người A
- [ ] Trang Student (/student/*) — người A
- [ ] Trang Admin (/admin/*) — người B
- [ ] WebSocket Chat — người B
- [ ] FAQ lấy dữ liệu thật từ DB thay vì data mẫu — người A hoặc B tùy phân công

## Lưu ý quan trọng

- **KHÔNG** commit password/API key thật vào Git (đã để trong `application.properties`, nên nếu repo public cần tách ra file riêng `.gitignore`)
- Nhánh `main` và `develop` đã bật Branch Protection — mọi thay đổi phải qua Pull Request + được duyệt mới merge được