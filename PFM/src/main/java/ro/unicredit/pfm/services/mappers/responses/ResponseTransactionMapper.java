package ro.unicredit.pfm.services.mappers.responses;

import org.mapstruct.Mapper;
import ro.unicredit.pfm.repositories.entities.Transaction;
import ro.unicredit.pfm.services.dtos.responses.ResponseTransactionDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResponseTransactionMapper {
    Transaction toEntity(ResponseTransactionDto responseTransactionDto);
    ResponseTransactionDto toDto(Transaction transaction);
    List<ResponseTransactionDto> toDto(List<Transaction> transactions);
}
