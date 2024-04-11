package ro.unicredit.trxclassifier.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ro.unicredit.trxclassifier.exceptions.ClassificationException;
import ro.unicredit.trxclassifier.services.dtos.*;
import ro.unicredit.trxclassifier.utils.WordUtils;

import java.util.*;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class AutoClassifierService {
    private static final String TOKEN_SPLITTER_REGEX = "[\\s-*.,]+";
    private final RestTemplate restTemplate;
    private final ManualClassifierService manualClassifierService;
    @Value("${url.categories}")
    private String BASE_CATEGORIES_URL;
    @Value("${url.keywords}")
    private String BASE_KEYWORDS_URL;

    public ResponseTransactionDto classifyByTxDescription(Long txId, String txDescription) {
        ResponseEntity<List<ResponseCategoryDto>> categories = restTemplate.exchange(
                BASE_CATEGORIES_URL + "/keywords?text=" + txDescription,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        List<ResponseCategoryDto> possibleCategories = categories.getBody();
        if (!categories.getStatusCode().is2xxSuccessful() || isEmpty(possibleCategories)
                || possibleCategories.size() == 0) {
            throw new ClassificationException();
        }

        return manualClassifierService.classify(txId, possibleCategories.get(0).getId());
    }

    public ResponseAutoClassifierV2Dto classifyByTxDescriptionV2(String description) {
        String[] tokens = description.split(TOKEN_SPLITTER_REGEX);
        ResponseEntity<List<ResponseKeywordDto>> keywordsResponse = restTemplate.exchange(
                BASE_KEYWORDS_URL + "?keywords=" + String.join(", ", tokens),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        var body = keywordsResponse.getBody();
        if (isEmpty(body)) {
            throw new ClassificationException();
        }
        return classifyV2(List.of(tokens), body);
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
                .categoryId(getCategoryId(categoryCount))
                .keywords(getKeywords(keywords, keywordCount))
                .build();

    }

    private List<ResponseKeywordWithOccurrencesDto> getKeywords(List<ResponseKeywordDto> keywords, Map<Long, Integer> keywordCount) {
        return keywords.stream()
                .map(responseKeywordDto -> getResponseKeywordWithOccurrencesDto(keywordCount, responseKeywordDto))
                .toList();
    }

    private ResponseKeywordWithOccurrencesDto getResponseKeywordWithOccurrencesDto(Map<Long, Integer> keywordCount,
                                                                                   ResponseKeywordDto responseKeywordDto) {
        Integer countResult = keywordCount.get(responseKeywordDto.getId());
        if(isEmpty(countResult)) {
            countResult = 0;
        }
        return new ResponseKeywordWithOccurrencesDto(responseKeywordDto, countResult);
    }

    private Long getCategoryId(Map<Long, Integer> categoryCount) {
        return categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
