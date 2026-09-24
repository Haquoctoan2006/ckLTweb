package vn.edu.ute.cklt_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {

    private long totalTickets;
    private long pendingTickets;
    private long processingTickets;
    private long resolvedTickets;
}
