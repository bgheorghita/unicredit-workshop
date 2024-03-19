package ro.unicredit.trxclassifier.services;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.unicredit.trxclassifier.exceptions.NotFoundException;
import ro.unicredit.trxclassifier.services.dtos.responses.*;
import ro.unicredit.trxclassifier.utils.WordUtils;

import java.util.*;
import java.util.function.Predicate;

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
                new ParameterizedTypeReference<>() {
                }
        );

        List<ResponseCategoryDto> possibleCategories = categories.getBody();
        if (!categories.getStatusCode().is2xxSuccessful() || possibleCategories == null || possibleCategories.size() == 0) {
            throw new NotFoundException("Unable to classify by description");
        }

        return manualClassifierService.classify(txId, possibleCategories.get(0).getId());
    }

    public ResponseAutoClassifierV2Dto classifyByTxDescriptionV2(Long txId, String description) {
        String[] tokens = description.split("[\\s-*.,]+");
        ResponseEntity<List<ResponseKeywordDto>> keywordsResponse = restTemplate.exchange(
                "http://localhost:8080/keywords?keywords=" + String.join(", ", tokens),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        System.out.println(keywordsResponse.getBody());
        return classifyV2(List.of(tokens), keywordsResponse.getBody());
    }

    public ResponseAutoClassifierV2Dto classifyV2(List<String> descriptionTokens, List<ResponseKeywordDto> keywords) {
        Map<Long, Integer> categoryCount = new HashMap<>();
        Map<Long, Integer> keywordCount = new HashMap<>();
        for (ResponseKeywordDto keyword : keywords) {
            for (String token : descriptionTokens) {
                if (WordUtils.matchWords(keyword.getValue(), token)) {
                    categoryCount.compute(keyword.getCategoryId(), (key, value) -> value == null ? 1 : value + 1);
                    keywordCount.compute(keyword.getId(), (key, value) -> value == null ? 1 : value + 1);
                }
            }
        }
        return ResponseAutoClassifierV2Dto.builder()
                .categoryId(categoryCount.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(null))
                .keywords(keywords.stream().map(responseKeywordDto ->
                        new ResponseKeywordWithOccurrencesDto(responseKeywordDto, keywordCount.get(responseKeywordDto.getId()))
                ).toList())
                .build();

    }
}
