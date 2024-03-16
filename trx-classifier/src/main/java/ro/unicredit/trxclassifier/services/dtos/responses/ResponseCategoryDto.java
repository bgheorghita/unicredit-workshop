package ro.unicredit.trxclassifier.services.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseCategoryDto {
    private Long id;
    private String value;
    private ResponseCategoryDto parent;
}
