package ro.unicredit.pfmreport.services.dtos;

import lombok.Data;

@Data
public class CategoryDetails {
    private Long id;
    private String value;
    private CategoryDetails parent;
}
