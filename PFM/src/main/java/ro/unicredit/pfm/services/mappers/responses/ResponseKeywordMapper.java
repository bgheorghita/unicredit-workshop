package ro.unicredit.pfm.services.mappers.responses;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ro.unicredit.pfm.repositories.entities.Keyword;
import ro.unicredit.pfm.services.dtos.responses.ResponseKeywordDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResponseKeywordMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ResponseKeywordDto toDto(Keyword keyword);
    Keyword toEntity(ResponseKeywordDto responseKeywordDto);
    @Mapping(source = "category.id", target = "categoryId")
    List<ResponseKeywordDto> toKeywordDtoList(List<Keyword> keywords);
}
