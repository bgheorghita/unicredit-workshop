package ro.unicredit.pfmreport.services.reports;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerReport {
    private List<CustomerReportRecord> records = new ArrayList<>();
    public void addRecord(CustomerReportRecord record) {
        records.add(record);
    }
}
