package ro.unicredit.trxclassifier.services;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.unicredit.trxclassifier.exceptions.NotFoundException;
import ro.unicredit.trxclassifier.services.dtos.responses.ResponseCategoryDto;
import ro.unicredit.trxclassifier.services.dtos.responses.ResponseTransactionDto;

import java.util.List;

@Service
@AllArgsConstructor
public class AutoClassifierService {
    private final RestTemplate restTemplate;
    private final ManualClassifierService manualClassifierService;

    public ResponseTransactionDto classifyByTxDescription(Long txId, String txDescription) {
        ResponseEntity<List<ResponseCategoryDto>> categories = restTemplate.exchange(
                "http://localhost:8080/categories/keywords?text=" + txDescription,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<ResponseCategoryDto> possibleCategories = categories.getBody();
        if(!categories.getStatusCode().is2xxSuccessful() || possibleCategories == null || possibleCategories.size() == 0) {
            throw new NotFoundException("Unable to classify by description");
        }

        return manualClassifierService.classify(txId, possibleCategories.get(0).getId());
    }
}
