package vn.edu.ute.cklt_web.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "public/login";
    }
}