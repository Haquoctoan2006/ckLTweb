package vn.edu.ute.cklt_web.repository;

import vn.edu.ute.cklt_web.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
