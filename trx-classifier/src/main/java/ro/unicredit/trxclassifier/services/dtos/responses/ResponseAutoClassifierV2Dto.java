package ro.unicredit.trxclassifier.services.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAutoClassifierV2Dto {
    private Long categoryId;
    private String transactionDescription;
    private Long transactionId;
    private List<ResponseKeywordWithOccurrencesDto> keywords;

}
