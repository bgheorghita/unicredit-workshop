package ro.unicredit.pfmreport.services.dtos.reports;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CustomerReportEntry(String categoryName, BigDecimal amount, Float percentage,
                                  List<CustomerReportEntry> subcategories) {
}
