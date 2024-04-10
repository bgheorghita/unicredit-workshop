package ro.unicredit.pfmreport.services;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ro.unicredit.pfmreport.services.dtos.CategoryDetails;
import ro.unicredit.pfmreport.services.dtos.TransactionDetails;
import ro.unicredit.pfmreport.services.dtos.reports.CustomerReport;
import ro.unicredit.pfmreport.services.dtos.reports.CustomerReportEntry;

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
        if (ObjectUtils.isEmpty(category)) {
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
            if(ObjectUtils.isEmpty(category.getParent())) {
                return;
            }
            BigDecimal amount = categoryStatistics.get(category);
            List<CustomerReportEntry> subcategories = computeSubcategoriesEntries(
                    category,
                    categoryStatistics,
                    percentagesIndexedByCategory
            );

            CustomerReportEntry customerReportEntry = CustomerReportEntry.builder()
                    .categoryName(category.getValue())
                    .amount(amount)
                    .percentage(percentage)
                    .subcategories(subcategories)
                    .build();
            customerReport.addRecord(customerReportEntry);
        });
        return customerReport;
    }

    private List<CustomerReportEntry> computeSubcategoriesEntries(CategoryDetails parentCategory,
                                                                  Map<CategoryDetails, BigDecimal> categoryStatistics,
                                                                  Map<CategoryDetails, Float> percentagesIndexedByCategory) {
        List<CustomerReportEntry> records = new ArrayList<>();
        categoryStatistics.keySet().forEach(category -> {
            CategoryDetails categoryParent = category.getParent();
            if (!ObjectUtils.isEmpty(categoryParent) && categoryParent.getId().equals(parentCategory.getId())) {
                List<CustomerReportEntry> subcategories = computeSubcategoriesEntries(
                        category,
                        categoryStatistics,
                        percentagesIndexedByCategory
                );

                records.add(CustomerReportEntry.builder()
                        .categoryName(category.getValue())
                        .amount(categoryStatistics.get(category))
                        .percentage(percentagesIndexedByCategory.get(category))
                        .subcategories(subcategories)
                        .build());
            }
        });
        return records;
    }

    private Map<CategoryDetails, Float> computePercentages(Map<CategoryDetails, BigDecimal> statistics) {
        BigDecimal totalAmount = computeTotalAmount(statistics);

        Map<CategoryDetails, Float> percentagesIndexedByCategory = new HashMap<>();
        computePercentage(statistics, totalAmount, percentagesIndexedByCategory);

        return percentagesIndexedByCategory;
    }

    private void computePercentage(Map<CategoryDetails, BigDecimal> statistics, BigDecimal totalAmount, Map<CategoryDetails, Float> percentagesIndexedByCategory) {
        for (Map.Entry<CategoryDetails, BigDecimal> entry : statistics.entrySet()) {
            BigDecimal categoryAmount = entry.getValue();
            BigDecimal percentage;
            CategoryDetails parentCategory = entry.getKey().getParent();

            if (!ObjectUtils.isEmpty(parentCategory)) {
                percentage = categoryAmount.divide(totalAmount, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            } else {
                percentage = BigDecimal.valueOf(100);
            }

            percentagesIndexedByCategory.put(entry.getKey(), percentage.floatValue());
        }
    }

    private BigDecimal computeTotalAmount(Map<CategoryDetails, BigDecimal> statistics) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Map.Entry<CategoryDetails, BigDecimal> entry : statistics.entrySet()) {
            CategoryDetails parentCategory = entry.getKey().getParent();
            if (!ObjectUtils.isEmpty(parentCategory)) {
                totalAmount = totalAmount.add(entry.getValue());
            }
        }
        return totalAmount;
    }
}