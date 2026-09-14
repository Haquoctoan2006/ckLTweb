package vn.edu.ute.cklt_web.auth.service;

import vn.edu.ute.cklt_web.common.entity.PasswordResetOtp;
import vn.edu.ute.cklt_web.common.entity.User;
import vn.edu.ute.cklt_web.common.repository.PasswordResetOtpRepository;
import vn.edu.ute.cklt_web.common.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    private static final String DEFAULT_PASSWORD = "SPKT@2026";
    private static final int OTP_EXPIRE_MINUTES = 5;

    public PasswordResetService(UserRepository userRepository,
                                 PasswordResetOtpRepository otpRepository,
                                 JavaMailSender mailSender,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public String requestOtp(String mssv) {
        User user = userRepository.findByMssv(mssv)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy MSSV này trong hệ thống"));

        String email = user.getEmail();
        String otpCode = generateOtp();

        PasswordResetOtp otp = new PasswordResetOtp();
        otp.setMssv(mssv);
        otp.setOtpCode(otpCode);
        otp.setExpiredAt(LocalDateTime.now().plusMinutes(OTP_EXPIRE_MINUTES));
        otp.setUsed(false);
        otpRepository.save(otp);

        sendOtpEmail(email, otpCode);

        return maskEmail(email);
    }

    public void verifyOtp(String mssv, String otpInput) {
        PasswordResetOtp otp = otpRepository.findTopByMssvAndUsedFalseOrderByIdDesc(mssv)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu OTP, vui lòng thực hiện lại từ đầu"));

        if (otp.isUsed()) {
            throw new IllegalArgumentException("Mã OTP đã được sử dụng");
        }
        if (otp.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Mã OTP đã hết hạn, vui lòng yêu cầu lại");
        }
        if (!otp.getOtpCode().equals(otpInput)) {
            throw new IllegalArgumentException("Mã OTP không đúng");
        }
    }

    public void resetPassword(String mssv, String otpInput) {
        PasswordResetOtp otp = otpRepository.findTopByMssvAndUsedFalseOrderByIdDesc(mssv)
                .orElseThrow(() -> new IllegalArgumentException("Yêu cầu không hợp lệ"));

        if (otp.isUsed() || otp.getExpiredAt().isBefore(LocalDateTime.now())
                || !otp.getOtpCode().equals(otpInput)) {
            throw new IllegalArgumentException("OTP không hợp lệ hoặc đã hết hạn");
        }

        User user = userRepository.findByMssv(mssv)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setMustChangePassword(true);
        userRepository.save(user);

        otp.setUsed(true);
        otpRepository.save(otp);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int number = random.nextInt(1_000_000);
        return String.format("%06d", number);
    }

    private void sendOtpEmail(String toEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Mã OTP đặt lại mật khẩu - UTE Support");
        message.setText("Mã OTP của bạn là: " + otpCode + "\nMã có hiệu lực trong " + OTP_EXPIRE_MINUTES + " phút.\nKhông chia sẻ mã này cho bất kỳ ai.");
        mailSender.send(message);
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex <= 2) return email;
        String visible = email.substring(0, 2);
        String hidden = "*".repeat(atIndex - 2);
        return visible + hidden + email.substring(atIndex);
    }
}