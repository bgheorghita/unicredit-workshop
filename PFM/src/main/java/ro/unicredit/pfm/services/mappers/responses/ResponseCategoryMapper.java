package ro.unicredit.pfm.services.mappers.responses;

import org.mapstruct.Mapper;
import ro.unicredit.pfm.repositories.entities.Category;
import ro.unicredit.pfm.services.dtos.responses.ResponseCategoryDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResponseCategoryMapper {
    ResponseCategoryDto toDto(Category category);
    Category toEntity(ResponseCategoryDto responseCategoryDto);
    List<ResponseCategoryDto> toDto(List<Category> categories);
}
