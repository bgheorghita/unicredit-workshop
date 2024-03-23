package ro.unicredit.pfm.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.unicredit.pfm.services.CategoryService;
import ro.unicredit.pfm.services.dtos.requests.RequestCategoryDto;
import ro.unicredit.pfm.services.dtos.responses.ResponseCategoryDto;

import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<ResponseCategoryDto> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseCategoryDto findById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseCategoryDto update(@PathVariable Long id, @RequestBody RequestCategoryDto requestCategoryDto) {
        return categoryService.update(id, requestCategoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseCategoryDto deleteById(@PathVariable Long id) {
        return categoryService.deleteById(id);
    }

    @GetMapping("/keywords")
    public List<ResponseCategoryDto> findByKeywordsValueContainedIn(@RequestParam String text) {
        return categoryService.findByKeywordsValueContainedIn(text);
    }
}
