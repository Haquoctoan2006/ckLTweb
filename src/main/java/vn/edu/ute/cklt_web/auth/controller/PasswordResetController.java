package vn.edu.ute.cklt_web.auth.controller;

import vn.edu.ute.cklt_web.auth.service.PasswordResetService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/forgot-password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping
    public String showStep1() {
        return "public/forgot-password-step1";
    }

    @PostMapping("/request-otp")
    public String requestOtp(@RequestParam String mssv, Model model, HttpSession session) {
        try {
            String maskedEmail = passwordResetService.requestOtp(mssv);
            session.setAttribute("resetMssv", mssv);
            model.addAttribute("maskedEmail", maskedEmail);
            return "public/forgot-password-step2";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "public/forgot-password-step1";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String otpCode, Model model, HttpSession session) {
        String mssv = (String) session.getAttribute("resetMssv");
        if (mssv == null) {
            return "redirect:/forgot-password";
        }
        try {
            passwordResetService.verifyOtp(mssv, otpCode);
            session.setAttribute("resetOtp", otpCode);
            return "public/forgot-password-step3";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "public/forgot-password-step2";
        }
    }

    @PostMapping("/confirm-reset")
    public String confirmReset(Model model, HttpSession session) {
        String mssv = (String) session.getAttribute("resetMssv");
        String otpCode = (String) session.getAttribute("resetOtp");
        if (mssv == null || otpCode == null) {
            return "redirect:/forgot-password";
        }
        try {
            passwordResetService.resetPassword(mssv, otpCode);
            session.removeAttribute("resetMssv");
            session.removeAttribute("resetOtp");
            return "public/forgot-password-success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "public/forgot-password-step3";
        }
    }
}