package ro.unicredit.trxclassifier.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import ro.unicredit.trxclassifier.exceptions.NotFoundException;
import ro.unicredit.trxclassifier.services.dtos.responses.*;
import ro.unicredit.trxclassifier.utils.WordUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AutoClassifierService {
    private final static String KEYWORDS_PATH = "keywords";
    private final static String TEXT_PARAMETER = "text";
    private final RestTemplate restTemplate;
    private final ManualClassifierService manualClassifierService;
    @Value("${url.category}")
    private String BASE_CATEGORY_URL;
    @Value("${url.keywords}")
    private String BASE_KEYWORD_URL;

    public ResponseTransactionDto classifyByTxDescription(Long txId, String txDescription) {
        ResponseEntity<List<ResponseCategoryDto>> categories = restTemplate.exchange(
                BASE_CATEGORY_URL + "/" + KEYWORDS_PATH + "?" + TEXT_PARAMETER + "=" + txDescription,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );

        List<ResponseCategoryDto> possibleCategories = categories.getBody();
        if (!categories.getStatusCode().is2xxSuccessful() || ObjectUtils.isEmpty(possibleCategories)
                || possibleCategories.size() == 0) {
            throw new NotFoundException("Unable to classify by description");
        }

        return manualClassifierService.classify(txId, possibleCategories.get(0).getId());
    }

    public ResponseAutoClassifierV2Dto classifyByTxDescriptionV2(Long txId, String description) {
        String[] tokens = description.split("[\\s-*.,]+");
        ResponseEntity<List<ResponseKeywordDto>> keywordsResponse = restTemplate.exchange(
                BASE_KEYWORD_URL + "?" + KEYWORDS_PATH + "=" + String.join(", ", tokens),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                }
        );
        var body = keywordsResponse.getBody();
        if (ObjectUtils.isEmpty(body)) {
            throw new RuntimeException("Unable to classifyV2 by description.");
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
        return new ResponseKeywordWithOccurrencesDto(responseKeywordDto, countResult);
    }

    private Long getCategoryId(Map<Long, Integer> categoryCount) {
        return categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
