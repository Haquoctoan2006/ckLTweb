package vn.edu.ute.cklt_web.repository;

import vn.edu.ute.cklt_web.entity.ConsultationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationRequestRepository extends JpaRepository<ConsultationRequest, Long> {

    List<ConsultationRequest> findByStudentId(Long studentId);

    List<ConsultationRequest> findByStatus(String status);

    List<ConsultationRequest> findByAssignedManagerId(Long assignedManagerId);
}
