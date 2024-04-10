package ro.unicredit.pfm.services.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class ResponseTransactionDto {
    private Long id;
    private String description;
    private Date date;
    private BigDecimal amount;
    private ResponseCategoryDto category;
    private ResponseKeywordDto keyword;
    private ResponseTransactionDto parent;
}
