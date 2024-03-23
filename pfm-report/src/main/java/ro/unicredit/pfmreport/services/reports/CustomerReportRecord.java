package ro.unicredit.pfmreport.services.reports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class CustomerReportRecord {
    private final String categoryName;
    private final BigDecimal amount;
    private final Float percentage;
    private final List<CustomerReportRecord> subcategories;
}
