package vn.edu.ute.cklt_web.repository;

import vn.edu.ute.cklt_web.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    Optional<PasswordResetOtp> findTopByMssvAndUsedFalseOrderByIdDesc(String mssv);
}