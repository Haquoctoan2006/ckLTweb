package vn.edu.ute.cklt_web.repository;

import vn.edu.ute.cklt_web.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findByEmailAndOtpCodeAndType(
            String email,
            String otpCode,
            String type);
}
