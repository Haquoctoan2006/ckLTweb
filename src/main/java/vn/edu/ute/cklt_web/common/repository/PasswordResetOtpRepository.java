package vn.edu.ute.cklt_web.common.repository;

import vn.edu.ute.cklt_web.common.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {
    Optional<PasswordResetOtp> findTopByMssvAndUsedFalseOrderByIdDesc(String mssv);
}