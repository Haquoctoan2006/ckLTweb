package vn.edu.ute.cklt_web.service;

import vn.edu.ute.cklt_web.dto.request.LoginRequest;
import vn.edu.ute.cklt_web.dto.request.RegisterRequest;
import vn.edu.ute.cklt_web.dto.request.VerifyOtpRequest;
import vn.edu.ute.cklt_web.dto.response.JwtResponse;
import vn.edu.ute.cklt_web.entity.OtpVerification;
import vn.edu.ute.cklt_web.entity.Role;
import vn.edu.ute.cklt_web.entity.User;
import vn.edu.ute.cklt_web.exception.BadRequestException;
import vn.edu.ute.cklt_web.repository.OtpVerificationRepository;
import vn.edu.ute.cklt_web.repository.RoleRepository;
import vn.edu.ute.cklt_web.repository.UserRepository;
import vn.edu.ute.cklt_web.security.CustomUserDetails;
import vn.edu.ute.cklt_web.security.JwtTokenProvider;
import vn.edu.ute.cklt_web.util.OTPGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String REGISTER_OTP_TYPE = "REGISTER";
    private static final String STUDENT_ROLE = "ROLE_STUDENT";
    private static final String UNVERIFIED_STATUS = "UNVERIFIED";
    private static final String ACTIVE_STATUS = "ACTIVE";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.existsByMssv(request.getUsername())) {
            throw new BadRequestException("Username đã tồn tại");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email đã tồn tại");
        }

        Role studentRole = roleRepository.findByName(STUDENT_ROLE)
                .orElseThrow(() -> new BadRequestException(
                        "Role ROLE_STUDENT chưa được khởi tạo"));

        User user = User.builder()
                .mssv(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .status(UNVERIFIED_STATUS)
                .roles(new java.util.HashSet<>(List.of(studentRole)))
                .build();

        userRepository.save(user);

        String otpCode = OTPGeneratorUtil.generateOtp();
        OtpVerification otpVerification = OtpVerification.builder()
                .email(request.getEmail())
                .otpCode(otpCode)
                .type(REGISTER_OTP_TYPE)
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .isUsed(false)
                .build();

        otpVerificationRepository.save(otpVerification);
        emailService.sendOtpEmail(request.getEmail(), otpCode);

        return "Đăng ký thành công. Vui lòng kiểm tra email để xác thực tài khoản.";
    }

    @Transactional
    public String verifyOtp(VerifyOtpRequest request) {
        OtpVerification otpVerification = otpVerificationRepository
                .findByEmailAndOtpCodeAndType(
                        request.getEmail(),
                        request.getOtpCode(),
                        REGISTER_OTP_TYPE)
                .orElseThrow(() -> new BadRequestException("Mã OTP không hợp lệ"));

        if (Boolean.TRUE.equals(otpVerification.getIsUsed())) {
            throw new BadRequestException("Mã OTP đã được sử dụng");
        }

        if (otpVerification.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Mã OTP đã hết hạn");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException(
                        "Không tìm thấy tài khoản với email này"));

        otpVerification.setIsUsed(true);
        user.setStatus(ACTIVE_STATUS);
        otpVerificationRepository.save(otpVerification);
        userRepository.save(user);

        return "Xác thực tài khoản thành công";
    }

    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByMssv(request.getUsername())
                .orElseThrow(() -> new BadRequestException(
                        "Tên đăng nhập hoặc mật khẩu không đúng"));

        if (UNVERIFIED_STATUS.equalsIgnoreCase(user.getStatus())) {
            throw new BadRequestException(
                    "Tài khoản chưa được xác thực. Vui lòng xác thực OTP trước khi đăng nhập");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));

            CustomUserDetails userDetails =
                    (CustomUserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(userDetails);
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return JwtResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .username(userDetails.getUsername())
                    .roles(roles)
                    .build();
        } catch (AuthenticationException exception) {
            throw new BadRequestException(
                    "Tên đăng nhập hoặc mật khẩu không đúng", exception);
        }
    }
}
