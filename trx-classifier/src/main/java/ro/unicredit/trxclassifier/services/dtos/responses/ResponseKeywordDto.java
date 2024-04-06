package ro.unicredit.trxclassifier.services.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseKeywordDto {
    private Long id;
    private String value;
    private ResponseKeywordDto parent;
    private Long categoryId;
}
