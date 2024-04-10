package ro.unicredit.pfmreport.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unicredit.pfmreport.services.ScoringCardService;
import ro.unicredit.pfmreport.services.dtos.ScoringResult;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/scoring")
@AllArgsConstructor
public class ScoringCardController {

    private final ScoringCardService scoringCardService;

    @GetMapping
    public ScoringResult computeScoring(@RequestBody List<BigDecimal> transactionAmounts) {
        return scoringCardService.computeScoring(transactionAmounts);
    }
}
