package ro.unicredit.pfm.services.mappers.requests;

import org.mapstruct.Mapper;
import ro.unicredit.pfm.repositories.entities.Keyword;
import ro.unicredit.pfm.services.dtos.requests.RequestKeywordDto;

@Mapper(componentModel = "spring")
public interface RequestKeywordMapper {
    Keyword toEntity(RequestKeywordDto requestKeywordDto);
}
