<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Quên mật khẩu - Bước 2</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        </head>

        <body class="bg-light d-flex align-items-center" style="min-height:100vh;">

            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-4">
                        <div class="card shadow-sm">
                            <div class="card-body p-4">
                                <h4 class="text-center mb-2 text-primary">Quên mật khẩu</h4>
                                <p class="text-center text-muted small mb-1">Bước 2/3: Nhập mã OTP</p>
                                <p class="text-center small mb-4">Mã đã được gửi tới: <strong>${maskedEmail}</strong>
                                </p>

                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger">${error}</div>
                                </c:if>

                                <form method="post" action="/forgot-password/verify-otp">
                                    <div class="mb-3">
                                        <label class="form-label">Mã OTP (6 chữ số)</label>
                                        <input type="text" name="otpCode" class="form-control" maxlength="6"
                                            placeholder="VD: 123456" required>
                                    </div>
                                    <button type="submit" class="btn btn-primary w-100">Xác thực OTP</button>
                                </form>

                                <div class="text-center mt-3">
                                    <a href="/forgot-password">← Nhập lại MSSV</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </body>

        </html>