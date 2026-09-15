package vn.edu.ute.cklt_web.common.repository;

import vn.edu.ute.cklt_web.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMssv(String mssv);
}