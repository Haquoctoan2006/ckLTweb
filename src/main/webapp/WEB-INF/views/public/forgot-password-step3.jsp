<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Quên mật khẩu - Bước 3</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        </head>

        <body class="bg-light d-flex align-items-center" style="min-height:100vh;">

            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-4">
                        <div class="card shadow-sm">
                            <div class="card-body p-4">
                                <h4 class="text-center mb-2 text-primary">Quên mật khẩu</h4>
                                <p class="text-center text-muted small mb-4">Bước 3/3: Xác nhận đặt lại mật khẩu</p>

                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger">${error}</div>
                                </c:if>

                                <div class="alert alert-info small">
                                    Mật khẩu sẽ được đặt lại về mặc định: <strong>SPKT@2026</strong><br>
                                    Bạn cần đổi mật khẩu ngay ở lần đăng nhập tiếp theo.
                                </div>

                                <form method="post" action="/forgot-password/confirm-reset">
                                    <button type="submit" class="btn btn-primary w-100">Xác nhận đặt lại mật
                                        khẩu</button>
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