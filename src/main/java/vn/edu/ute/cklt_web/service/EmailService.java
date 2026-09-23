package vn.edu.ute.cklt_web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendOtpEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Mã xác thực QAUTE Helpdesk");
        message.setText("Mã OTP của bạn là: " + otpCode
                + "\nMã có hiệu lực trong 5 phút."
                + "\nVui lòng không chia sẻ mã này cho bất kỳ ai.");

        mailSender.send(message);
    }
}
