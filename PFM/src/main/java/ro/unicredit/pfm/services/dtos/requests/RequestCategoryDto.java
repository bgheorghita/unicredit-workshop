package ro.unicredit.pfm.services.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestCategoryDto {
    private String value;
    private Long parentId;
}
