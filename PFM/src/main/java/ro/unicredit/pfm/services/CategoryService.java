package ro.unicredit.pfm.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ro.unicredit.pfm.repositories.entities.Category;
import ro.unicredit.pfm.exceptions.NotFoundException;
import ro.unicredit.pfm.repositories.CategoryRepository;
import ro.unicredit.pfm.services.dtos.requests.RequestCategoryDto;
import ro.unicredit.pfm.services.dtos.responses.ResponseCategoryDto;
import ro.unicredit.pfm.services.mappers.requests.RequestCategoryMapper;
import ro.unicredit.pfm.services.mappers.responses.ResponseCategoryMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final RequestCategoryMapper requestCategoryMapper;
    private final ResponseCategoryMapper responseCategoryMapper;

    public List<ResponseCategoryDto> findAll(){
        return responseCategoryMapper.toDto(categoryRepository.findAll());
    }

    public ResponseCategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found."));
        return responseCategoryMapper.toDto(category);
    }

    public ResponseCategoryDto save(RequestCategoryDto requestCategoryDto) {
        Category category = requestCategoryMapper.toEntity(requestCategoryDto);
        Category savedCategory = categoryRepository.save(category);
        return responseCategoryMapper.toDto(savedCategory);
    }

    public ResponseCategoryDto deleteById(Long id) {
        ResponseCategoryDto responseCategoryDto = findById(id);
        categoryRepository.deleteById(id);
        return responseCategoryDto;
    }

    public ResponseCategoryDto update(Long id, RequestCategoryDto requestCategoryDto) {
        Category categoryToUpdate =  categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Update failed. Category not found."));
        Category categoryParent = categoryRepository.findById(id).orElse(null);
        categoryToUpdate.setValue(requestCategoryDto.getValue());
        categoryToUpdate.setParent(categoryParent);
        return save(requestCategoryMapper.toDto(categoryToUpdate));
    }

    public List<ResponseCategoryDto> findByKeywordsValueContainedIn(String text) {
        List<Category> categories = categoryRepository.findByKeywordsValueContainedIn(text);
        return responseCategoryMapper.toDto(categories);
    }
}
