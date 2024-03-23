package ro.unicredit.pfm.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ro.unicredit.pfm.repositories.entities.Category;
import ro.unicredit.pfm.repositories.entities.Keyword;
import ro.unicredit.pfm.exceptions.NotFoundException;
import ro.unicredit.pfm.repositories.CategoryRepository;
import ro.unicredit.pfm.repositories.KeywordRepository;
import ro.unicredit.pfm.services.dtos.requests.RequestKeywordDto;
import ro.unicredit.pfm.services.dtos.responses.ResponseKeywordDto;
import ro.unicredit.pfm.services.mappers.requests.RequestKeywordMapper;
import ro.unicredit.pfm.services.mappers.responses.ResponseKeywordMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;
    private final CategoryRepository categoryRepository;
    private final ResponseKeywordMapper responseKeywordMapper;
    private final RequestKeywordMapper requestKeywordMapper;

    public ResponseKeywordDto findById(Long id) {
        Keyword keyword = keywordRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Keyword with id " + id + " not found."));
        return responseKeywordMapper.toDto(keyword);
    }

    public List<ResponseKeywordDto> findAll() {
        List<Keyword> keywords = keywordRepository.findAll();
        return responseKeywordMapper.toKeywordDtoList(keywords);
    }

    public ResponseKeywordDto save(RequestKeywordDto requestKeywordDto) {
        Keyword keywordToSave = requestKeywordMapper.toEntity(requestKeywordDto);
        return responseKeywordMapper.toDto(keywordToSave);
    }

    public ResponseKeywordDto deleteById(Long id) {
        ResponseKeywordDto keywordToDelete = findById(id);
        keywordRepository.deleteById(id);
        return keywordToDelete;
    }

    public ResponseKeywordDto update(Long keywordId, RequestKeywordDto requestKeywordDto) {
        Keyword keywordToUpdate =  keywordRepository.findById(keywordId).orElseThrow(
                () -> new NotFoundException("Update failed. Keyword not found."));
        Category category = categoryRepository.findById(requestKeywordDto.getCategoryId())
                .orElse(null);
        keywordToUpdate.setValue(requestKeywordDto.getValue());
        keywordToUpdate.setCategory(category);
        Keyword savedKeyword = keywordRepository.save(keywordToUpdate);
        return responseKeywordMapper.toDto(savedKeyword);
    }

    public List<ResponseKeywordDto> findByValueContainedIn(List<String> keywords) {
        List<Keyword> keywordsList = keywordRepository.findInTokens(keywords);
        return responseKeywordMapper.toKeywordDtoList(keywordsList);
    }
}
