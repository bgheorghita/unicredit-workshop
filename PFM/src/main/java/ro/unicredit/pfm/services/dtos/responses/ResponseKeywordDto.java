package ro.unicredit.pfm.services.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseKeywordDto {
    private Long id;
    private String value;
    private ResponseKeywordDto parent;
    public Long categoryId;

}
