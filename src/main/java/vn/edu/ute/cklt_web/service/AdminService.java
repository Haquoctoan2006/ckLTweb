package vn.edu.ute.cklt_web.service;

import vn.edu.ute.cklt_web.dto.response.DashboardStatsResponse;
import vn.edu.ute.cklt_web.entity.User;
import vn.edu.ute.cklt_web.exception.ResourceNotFoundException;
import vn.edu.ute.cklt_web.repository.CategoryRepository;
import vn.edu.ute.cklt_web.repository.ConsultationRequestRepository;
import vn.edu.ute.cklt_web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private static final String PENDING = "PENDING";
    private static final String PROCESSING = "PROCESSING";
    private static final String RESOLVED = "RESOLVED";

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ConsultationRequestRepository consultationRequestRepository;

    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        return DashboardStatsResponse.builder()
                .totalTickets(consultationRequestRepository.count())
                .pendingTickets(consultationRequestRepository.findByStatus(PENDING).size())
                .processingTickets(consultationRequestRepository.findByStatus(PROCESSING).size())
                .resolvedTickets(consultationRequestRepository.findByStatus(RESOLVED).size())
                .build();
    }

    @Transactional
    public User lockUser(Long userId) {
        User user = findUser(userId);
        user.setStatus("LOCKED");
        return userRepository.save(user);
    }

    @Transactional
    public User unlockUser(Long userId) {
        User user = findUser(userId);
        user.setStatus("ACTIVE");
        return userRepository.save(user);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy người dùng với id: " + userId));
    }
}
