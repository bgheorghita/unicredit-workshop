package ro.unicredit.pfm.services.mappers.requests;

import org.mapstruct.Mapper;
import ro.unicredit.pfm.repositories.entities.Transaction;
import ro.unicredit.pfm.services.dtos.requests.RequestTransactionDto;

@Mapper(componentModel = "spring")
public interface RequestTransactionMapper {
    Transaction toEntity(RequestTransactionDto requestTransactionDto);
    RequestTransactionDto toDto(Transaction transaction);
}
