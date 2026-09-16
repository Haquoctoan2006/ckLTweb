package vn.edu.ute.cklt_web.repository;

import vn.edu.ute.cklt_web.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
