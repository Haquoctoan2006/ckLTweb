package vn.edu.ute.cklt_web.repository;

import vn.edu.ute.cklt_web.entity.ConsultationAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationAnswerRepository extends JpaRepository<ConsultationAnswer, Long> {

    List<ConsultationAnswer> findByRequestId(Long requestId);
}
