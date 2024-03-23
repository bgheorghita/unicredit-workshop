package ro.unicredit.pfmreport.services.dtos.reports;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Builder
public record CustomerReportRecord(String categoryName, BigDecimal amount, Float percentage,
                                   List<CustomerReportRecord> subcategories) {
}
