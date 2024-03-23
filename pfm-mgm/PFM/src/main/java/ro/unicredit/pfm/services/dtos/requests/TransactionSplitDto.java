package ro.unicredit.pfm.services.dtos.requests;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class TransactionSplitDto {
    private BigDecimal amount;
    private Date date;
    private String description;
    private Long categoryId;
    private List<TransactionSplitDto> children;
}
