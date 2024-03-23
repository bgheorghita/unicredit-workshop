package ro.unicredit.pfmreport.services.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ScoringResult {
    private final BigDecimal totalSpent;
    private final BigDecimal totalReceived;
}
