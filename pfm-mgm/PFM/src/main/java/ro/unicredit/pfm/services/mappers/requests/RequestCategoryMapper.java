package ro.unicredit.pfm.services.mappers.requests;

import org.mapstruct.Mapper;
import ro.unicredit.pfm.repositories.entities.Category;
import ro.unicredit.pfm.services.dtos.requests.RequestCategoryDto;

@Mapper(componentModel = "spring")
public interface RequestCategoryMapper {
    RequestCategoryDto toDto(Category category);
    Category toEntity(RequestCategoryDto requestCategoryDto);
}
