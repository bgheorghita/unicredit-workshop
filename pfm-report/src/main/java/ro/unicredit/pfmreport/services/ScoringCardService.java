package ro.unicredit.pfmreport.services;

import org.springframework.stereotype.Service;
import ro.unicredit.pfmreport.services.dtos.ScoringResult;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ScoringCardService {
    public ScoringResult computeScoring(List<BigDecimal> transactionAmounts) {
        BigDecimal totalReceived = BigDecimal.ZERO;
        BigDecimal totalSpent = BigDecimal.ZERO;

        for (var amount : transactionAmounts) {
            if (amount.compareTo(BigDecimal.ZERO) >= 0) {
                totalReceived = totalReceived.add(amount);
            } else {
                totalSpent = totalSpent.add(amount);
            }
        }

        return ScoringResult.builder()
                .totalReceived(totalReceived)
                .totalSpent(totalSpent)
                .build();
    }
}