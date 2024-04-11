package ro.unicredit.pfmreport.services.dtos.reports;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
public class CustomerReport {
    private List<CustomerReportEntry> entries;
    public void addEntry(CustomerReportEntry entry) {
        entries.add(entry);
    }
}
