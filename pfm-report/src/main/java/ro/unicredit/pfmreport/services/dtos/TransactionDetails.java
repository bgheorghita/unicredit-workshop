package ro.unicredit.pfmreport.services.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDetails {
    private BigDecimal amount;
    private CategoryDetails category;
}
