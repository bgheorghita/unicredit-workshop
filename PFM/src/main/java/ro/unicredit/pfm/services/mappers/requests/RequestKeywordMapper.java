package ro.unicredit.pfm.services.mappers.requests;

import org.mapstruct.Mapper;
import ro.unicredit.pfm.repositories.entities.Keyword;
import ro.unicredit.pfm.services.dtos.requests.RequestKeywordDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestKeywordMapper {
    RequestKeywordDto toDto(Keyword keyword);
    Keyword toEntity(RequestKeywordDto requestKeywordDto);
    List<RequestKeywordDto> toKeywordDtoList(List<Keyword> keywords);
}
