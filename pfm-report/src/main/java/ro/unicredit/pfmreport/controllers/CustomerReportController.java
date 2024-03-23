package ro.unicredit.pfmreport.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.unicredit.pfmreport.services.CustomerReportService;
import ro.unicredit.pfmreport.services.dtos.TransactionDetails;
import ro.unicredit.pfmreport.services.dtos.reports.CustomerReport;

import java.util.List;

@RestController
@RequestMapping("/report")
@AllArgsConstructor
public class CustomerReportController {
    private final CustomerReportService customerReportService;

    @GetMapping
    public CustomerReport computeReport(@RequestBody List<TransactionDetails> transactions) {
        return customerReportService.computeCategoryStatistics(transactions);
    }
}
