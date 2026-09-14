package vn.edu.ute.cklt_web.faq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FaqPublicController {

    @GetMapping("/public/faq")
    public String showFaqPage(Model model) {
        List<String> categories = List.of("Đào tạo", "Học phí", "CTSV", "KTX", "Cựu sinh viên");

        List<FaqItem> faqList = new ArrayList<>();
        faqList.add(new FaqItem("Làm sao để đăng ký học lại?", "Sinh viên đăng ký học lại qua hệ thống Portal, mục Đăng ký học phần.", "Đào tạo"));
        faqList.add(new FaqItem("Thời hạn đóng học phí học kỳ này?", "Hạn đóng học phí thường vào tuần thứ 3 của học kỳ, xem thông báo cụ thể trên cổng thông tin.", "Học phí"));
        faqList.add(new FaqItem("Thủ tục xin ở ký túc xá?", "Sinh viên nộp đơn tại Phòng CTSV kèm giấy tờ theo quy định.", "KTX"));

        model.addAttribute("categories", categories);
        model.addAttribute("faqList", faqList);
        return "public/faq";
    }

    public static class FaqItem {
        private String question;
        private String answer;
        private String category;

        public FaqItem(String question, String answer, String category) {
            this.question = question;
            this.answer = answer;
            this.category = category;
        }

        public String getQuestion() { return question; }
        public String getAnswer() { return answer; }
        public String getCategory() { return category; }
    }
}