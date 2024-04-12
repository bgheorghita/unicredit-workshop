package ro.unicredit.pfmreport.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.unicredit.pfmreport.services.ScoringCardService;
import ro.unicredit.pfmreport.services.dtos.ScoringResult;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/scoring")
@AllArgsConstructor
public class ScoringCardController {

    private final ScoringCardService scoringCardService;

    @PostMapping
    public ScoringResult computeScoring(@RequestBody List<BigDecimal> transactionAmounts) {
        return scoringCardService.computeScoring(transactionAmounts);
    }
}
