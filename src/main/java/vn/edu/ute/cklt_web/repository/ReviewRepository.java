package vn.edu.ute.cklt_web.repository;

import vn.edu.ute.cklt_web.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
