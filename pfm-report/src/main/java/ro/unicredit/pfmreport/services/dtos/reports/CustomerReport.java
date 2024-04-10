package ro.unicredit.pfmreport.services.dtos.reports;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerReport {
    private List<CustomerReportEntry> entries = new ArrayList<>();
    public void addRecord(CustomerReportEntry entry) {
        entries.add(entry);
    }
}
