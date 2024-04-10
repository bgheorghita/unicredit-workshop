package ro.unicredit.trxclassifier.services.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseCategoryDto {
    private Long id;
    private String value;
    private ResponseCategoryDto parent;
}
