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
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final RequestCategoryMapper requestCategoryMapper;
    private final ResponseCategoryMapper responseCategoryMapper;

    public List<ResponseCategoryDto> findAll(){
        List<Category> categories = categoryRepository.findAll();
        return responseCategoryMapper.toDto(categories);
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
        Category categoryToBeDeleted = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found."));
        categoryRepository.deleteById(id);
        return responseCategoryMapper.toDto(categoryToBeDeleted);
    }

    public ResponseCategoryDto update(Long id, RequestCategoryDto requestCategoryDto) {
        Category categoryToUpdate =  categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Update failed. Category not found."));
        Optional<Category> categoryParent = categoryRepository.findById(id);
        categoryParent.ifPresent(categoryToUpdate::setParent);
        categoryToUpdate.setValue(requestCategoryDto.getValue());
        return save(requestCategoryMapper.toDto(categoryToUpdate));
    }

    public List<ResponseCategoryDto> findByKeywordsValueContainedIn(String text) {
        List<Category> categories = categoryRepository.findByKeywordsValueContainedIn(text);
        return responseCategoryMapper.toDto(categories);
    }
}
