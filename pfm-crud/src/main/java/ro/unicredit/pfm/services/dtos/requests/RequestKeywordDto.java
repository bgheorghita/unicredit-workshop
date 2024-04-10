package ro.unicredit.pfm.services.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestKeywordDto {
    private String value;
    private Long categoryId;
}
