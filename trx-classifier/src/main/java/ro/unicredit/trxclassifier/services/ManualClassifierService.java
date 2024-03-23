package ro.unicredit.trxclassifier.services;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.unicredit.trxclassifier.exceptions.NotFoundException;
import ro.unicredit.trxclassifier.services.dtos.requests.RequestTransactionDto;
import ro.unicredit.trxclassifier.services.dtos.responses.ResponseCategoryDto;
import ro.unicredit.trxclassifier.services.dtos.responses.ResponseTransactionDto;

@Service
@AllArgsConstructor
public class ManualClassifierService {
    private final RestTemplate restTemplate;

    // TODO: split the method into smaller methods
    // TODO: move urls to the properties file

    public ResponseTransactionDto classify(Long txId, Long categoryId) {
        String transactionUrl = "http://localhost:8080/transactions/" + txId;
        String categoryUrl = "http://localhost:8080/categories/" + categoryId;

        ResponseEntity<ResponseTransactionDto> transactionDtoResponseEntity = restTemplate.getForEntity(
                transactionUrl, ResponseTransactionDto.class);
        ResponseTransactionDto responseTransactionDto = transactionDtoResponseEntity.getBody();
        if (responseTransactionDto == null || !transactionDtoResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new NotFoundException("Transaction with id: " + txId + " not found.");
        }

        ResponseEntity<ResponseCategoryDto> categoryDtoResponseEntity = restTemplate.getForEntity(
                categoryUrl, ResponseCategoryDto.class);
        ResponseCategoryDto responseCategoryDto = categoryDtoResponseEntity.getBody();
        if (responseCategoryDto == null || !categoryDtoResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new NotFoundException("Category with id: " + categoryId + " not found.");
        }

        RequestTransactionDto requestTransactionDto = new RequestTransactionDto(
                responseTransactionDto.getDescription(),
                responseTransactionDto.getDate(),
                responseTransactionDto.getAmount(),
                categoryId,
                responseTransactionDto.getKeyword() == null ? null : responseTransactionDto.getKeyword().getId(),
                responseTransactionDto.getParent() == null ? null : responseTransactionDto.getParent().getId()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestTransactionDto> requestTransactionDtoHttpEntity = new HttpEntity<>(requestTransactionDto, headers);

        ResponseEntity<ResponseTransactionDto> responseTransactionDtoResponseEntity = restTemplate.exchange(
                transactionUrl,
                HttpMethod.PUT,
                requestTransactionDtoHttpEntity,
                ResponseTransactionDto.class
        );

        return responseTransactionDtoResponseEntity.getBody();
    }
}