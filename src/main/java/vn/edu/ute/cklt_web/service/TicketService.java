package vn.edu.ute.cklt_web.service;

import vn.edu.ute.cklt_web.dto.request.AnswerCreateRequest;
import vn.edu.ute.cklt_web.entity.Attachment;
import vn.edu.ute.cklt_web.entity.ConsultationAnswer;
import vn.edu.ute.cklt_web.entity.ConsultationRequest;
import vn.edu.ute.cklt_web.entity.User;
import vn.edu.ute.cklt_web.exception.ResourceNotFoundException;
import vn.edu.ute.cklt_web.repository.ConsultationAnswerRepository;
import vn.edu.ute.cklt_web.repository.ConsultationRequestRepository;
import vn.edu.ute.cklt_web.repository.AttachmentRepository;
import vn.edu.ute.cklt_web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {

    private static final String PROCESSING = "PROCESSING";
    private static final String RESOLVED = "RESOLVED";

    private final ConsultationRequestRepository consultationRequestRepository;
    private final ConsultationAnswerRepository consultationAnswerRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    public ConsultationRequest claimTicket(Long ticketId, Long managerId) {
        ConsultationRequest ticket = findTicket(ticketId);
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy manager với id: " + managerId));

        ticket.setAssignedManager(manager);
        ticket.setStatus(PROCESSING);
        ticket.setUpdatedAt(LocalDateTime.now());

        return consultationRequestRepository.save(ticket);
    }

    @Transactional
    public ConsultationAnswer answerTicket(
            AnswerCreateRequest request,
            Long responderId) {
        ConsultationRequest ticket = findTicket(request.getTicketId());
        User responder = userRepository.findById(responderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy người trả lời với id: " + responderId));

        ConsultationAnswer answer = ConsultationAnswer.builder()
                .request(ticket)
                .responder(responder)
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        ConsultationAnswer savedAnswer = consultationAnswerRepository.save(answer);

        if (request.getAttachmentUrl() != null
            && !request.getAttachmentUrl().isBlank()) {
            Attachment attachment = Attachment.builder()
                .answer(savedAnswer)
                .fileUrl(request.getAttachmentUrl())
                .fileType("URL")
                .build();
            attachmentRepository.save(attachment);
        }

        ticket.setStatus(RESOLVED);
        ticket.setUpdatedAt(LocalDateTime.now());
        if (Boolean.TRUE.equals(request.getIsFaqEligible())) {
            ticket.setIsFaqEligible(true);
        }
        consultationRequestRepository.save(ticket);

        if (ticket.getStudent() != null && ticket.getStudent().getEmail() != null) {
            emailService.sendConsultationResultEmail(
                    ticket.getStudent().getEmail(),
                    ticket.getTicketCode());
        }

        return savedAnswer;
    }

    private ConsultationRequest findTicket(Long ticketId) {
        return consultationRequestRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy ticket với id: " + ticketId));
    }
}
