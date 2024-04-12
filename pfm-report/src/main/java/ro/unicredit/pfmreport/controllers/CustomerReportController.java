package ro.unicredit.pfmreport.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.unicredit.pfmreport.services.CustomerReportService;
import ro.unicredit.pfmreport.services.dtos.TransactionDetails;
import ro.unicredit.pfmreport.services.dtos.reports.CustomerReport;

import java.util.List;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
public class CustomerReportController {
    private final CustomerReportService customerReportService;

    @PostMapping
    public CustomerReport computeReport(@RequestBody List<TransactionDetails> transactions) {
        return customerReportService.computeCategoryStatistics(transactions);
    }
}
