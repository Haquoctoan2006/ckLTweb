package vn.edu.ute.cklt_web.controller;

import vn.edu.ute.cklt_web.dto.request.AnswerCreateRequest;
import vn.edu.ute.cklt_web.dto.response.ApiResponse;
import vn.edu.ute.cklt_web.dto.response.DashboardStatsResponse;
import vn.edu.ute.cklt_web.entity.ConsultationAnswer;
import vn.edu.ute.cklt_web.entity.ConsultationRequest;
import vn.edu.ute.cklt_web.service.AdminService;
import vn.edu.ute.cklt_web.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final TicketService ticketService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success(
                adminService.getDashboardStats()));
    }

    @PutMapping("/users/{userId}/lock")
    public ResponseEntity<ApiResponse<String>> lockUser(
            @PathVariable Long userId) {
        adminService.lockUser(userId);
        return ResponseEntity.ok(ApiResponse.success(
                "Khóa người dùng thành công", null));
    }

    @PutMapping("/users/{userId}/unlock")
    public ResponseEntity<ApiResponse<String>> unlockUser(
            @PathVariable Long userId) {
        adminService.unlockUser(userId);
        return ResponseEntity.ok(ApiResponse.success(
                "Mở khóa người dùng thành công", null));
    }

    @PostMapping("/tickets/{ticketId}/claim")
    public ResponseEntity<ApiResponse<ConsultationRequest>> claimTicket(
            @PathVariable Long ticketId,
            @RequestParam Long managerId) {
        return ResponseEntity.ok(ApiResponse.success(
                ticketService.claimTicket(ticketId, managerId)));
    }

    @PostMapping("/tickets/answer")
    public ResponseEntity<ApiResponse<ConsultationAnswer>> answerTicket(
            @RequestBody AnswerCreateRequest request,
            @RequestParam Long responderId) {
        return ResponseEntity.ok(ApiResponse.success(
                ticketService.answerTicket(request, responderId)));
    }
}
