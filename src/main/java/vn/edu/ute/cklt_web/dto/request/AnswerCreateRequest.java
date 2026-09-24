package vn.edu.ute.cklt_web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateRequest {

    private Long ticketId;
    private String content;
    private String attachmentUrl;
    private Boolean isFaqEligible;
}
