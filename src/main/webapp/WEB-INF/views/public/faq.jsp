<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>FAQ - Hỏi đáp sinh viên UTE</title>
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
            <style>
                .badge-filter {
                    cursor: pointer;
                }

                .badge-filter.active {
                    background-color: #0d6efd !important;
                }

                .faq-item.hidden {
                    display: none;
                }
            </style>
        </head>

        <body class="bg-light">

            <div class="container py-5">
                <h1 class="mb-4 text-primary">Câu hỏi thường gặp</h1>

                <div class="mb-4">
                    <input type="text" id="searchInput" class="form-control" placeholder="Tìm kiếm câu hỏi...">
                </div>

                <div class="mb-4">
                    <span class="badge bg-primary me-2 p-2 badge-filter active" data-category="all">Tất cả</span>
                    <c:forEach var="cat" items="${categories}">
                        <span class="badge bg-secondary me-2 p-2 badge-filter" data-category="${cat}">${cat}</span>
                    </c:forEach>
                </div>

                <div class="accordion" id="faqAccordion">
                    <c:forEach var="item" items="${faqList}" varStatus="loop">
                        <div class="accordion-item faq-item" data-category="${item.category}"
                            data-question="${item.question}">
                            <h2 class="accordion-header">
                                <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#faq${loop.index}">
                                    <span class="badge bg-info me-2">${item.category}</span> ${item.question}
                                </button>
                            </h2>
                            <div id="faq${loop.index}" class="accordion-collapse collapse"
                                data-bs-parent="#faqAccordion">
                                <div class="accordion-body">
                                    ${item.answer}
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <div id="noResult" class="text-center text-muted mt-4" style="display:none;">
                    Không tìm thấy câu hỏi phù hợp.
                </div>

                <div class="mt-4">
                    <a href="/login" class="btn btn-primary">Đăng nhập để gửi câu hỏi mới</a>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                const searchInput = document.getElementById('searchInput');
                const badges = document.querySelectorAll('.badge-filter');
                const faqItems = document.querySelectorAll('.faq-item');
                const noResult = document.getElementById('noResult');

                let currentCategory = 'all';

                function applyFilter() {
                    const keyword = searchInput.value.trim().toLowerCase();
                    let visibleCount = 0;

                    faqItems.forEach(item => {
                        const category = item.getAttribute('data-category');
                        const question = item.getAttribute('data-question').toLowerCase();

                        const matchCategory = (currentCategory === 'all' || category === currentCategory);
                        const matchKeyword = (keyword === '' || question.includes(keyword));

                        if (matchCategory && matchKeyword) {
                            item.classList.remove('hidden');
                            visibleCount++;
                        } else {
                            item.classList.add('hidden');
                        }
                    });

                    noResult.style.display = (visibleCount === 0) ? 'block' : 'none';
                }

                badges.forEach(badge => {
                    badge.addEventListener('click', () => {
                        badges.forEach(b => {
                            b.classList.remove('active', 'bg-primary');
                            b.classList.add('bg-secondary');
                        });
                        badge.classList.add('active', 'bg-primary');
                        badge.classList.remove('bg-secondary');

                        currentCategory = badge.getAttribute('data-category');
                        applyFilter();
                    });
                });

                searchInput.addEventListener('input', applyFilter);
            </script>

        </body>

        </html>