package ro.unicredit.pfmreport.services;

import org.springframework.stereotype.Service;
import ro.unicredit.pfmreport.services.dtos.CategoryDetails;
import ro.unicredit.pfmreport.services.dtos.TransactionDetails;
import ro.unicredit.pfmreport.services.reports.CustomerReport;
import ro.unicredit.pfmreport.services.reports.CustomerReportRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerReportService {
    public CustomerReport computeCategoryStatistics(List<TransactionDetails> transactions) {
        Map<CategoryDetails, BigDecimal> categoryStatistics = new HashMap<>();
        for (TransactionDetails transaction : transactions) {
            addToCategoryStatistics(transaction.getCategory(), transaction.getAmount(), categoryStatistics);
        }
        Map<CategoryDetails, Float> percentagesIndexedByCategory = computePercentages(categoryStatistics);
        return createCustomerReport(categoryStatistics, percentagesIndexedByCategory);
    }

    private void addToCategoryStatistics(CategoryDetails category, BigDecimal amount,
                                         Map<CategoryDetails, BigDecimal> categoryStatistics) {
        if (category == null) {
            return;
        }
        categoryStatistics.put(category, categoryStatistics.getOrDefault(category, BigDecimal.ZERO).add(amount));
        CategoryDetails parentCategory = category.getParent();
        if (parentCategory != null) {
            addToCategoryStatistics(parentCategory, amount, categoryStatistics);
        }
    }

    private CustomerReport createCustomerReport(Map<CategoryDetails, BigDecimal> categoryStatistics,
                                                Map<CategoryDetails, Float> percentagesIndexedByCategory) {
        CustomerReport customerReport = new CustomerReport();
        percentagesIndexedByCategory.forEach((category, percentage) -> {
            if (!isParentCategory(category)) {
                return;
            }
            BigDecimal amount = categoryStatistics.get(category);
            List<CustomerReportRecord> subcategories = computeSubcategoriesEntries(
                    category,
                    categoryStatistics,
                    percentagesIndexedByCategory
            );

            CustomerReportRecord customerReportRecord = CustomerReportRecord.builder()
                    .categoryName(category.getValue())
                    .amount(amount)
                    .percentage(percentage)
                    .subcategories(subcategories)
                    .build();
            customerReport.addRecord(customerReportRecord);
        });
        return customerReport;
    }

    private List<CustomerReportRecord> computeSubcategoriesEntries(CategoryDetails parentCategory,
           Map<CategoryDetails, BigDecimal> categoryStatistics, Map<CategoryDetails, Float> percentagesIndexedByCategory) {
        List<CustomerReportRecord> records = new ArrayList<>();
        categoryStatistics.keySet().forEach(category -> {
            CategoryDetails categoryParent = category.getParent();
            if (categoryParent != null && categoryParent.getId().equals(parentCategory.getId())) {
                List<CustomerReportRecord> subcategories = computeSubcategoriesEntries(
                        category,
                        categoryStatistics,
                        percentagesIndexedByCategory
                );
                 CustomerReportRecord record = CustomerReportRecord.builder()
                         .categoryName(category.getValue())
                         .amount(categoryStatistics.get(category))
                         .percentage(percentagesIndexedByCategory.get(category))
                         .subcategories(subcategories)
                         .build();

                records.add(record);
            }
        });
        return records;
    }

    private boolean isParentCategory(CategoryDetails categoryDetails) {
        return categoryDetails.getParent() == null;
    }

    private Map<CategoryDetails, Float> computePercentages(Map<CategoryDetails, BigDecimal> statistics) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (Map.Entry<CategoryDetails, BigDecimal> entry : statistics.entrySet()) {
            CategoryDetails parentCategory = entry.getKey().getParent();
            if (parentCategory != null) {
                totalAmount = totalAmount.add(entry.getValue());
            }
        }

        Map<CategoryDetails, Float> percentagesIndexedByCategory = new HashMap<>();
        for (Map.Entry<CategoryDetails, BigDecimal> entry : statistics.entrySet()) {
            BigDecimal categoryAmount = entry.getValue();
            BigDecimal percentage;
            CategoryDetails parentCategory = entry.getKey().getParent();

            if (parentCategory != null) {
                percentage = categoryAmount.divide(totalAmount, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            } else {
                percentage = BigDecimal.valueOf(100);
            }

            percentagesIndexedByCategory.put(entry.getKey(), percentage.floatValue());
        }

        return percentagesIndexedByCategory;
    }
}