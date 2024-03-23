package ro.unicredit.pfm.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.unicredit.pfm.services.KeywordService;
import ro.unicredit.pfm.services.dtos.requests.RequestKeywordDto;
import ro.unicredit.pfm.services.dtos.responses.ResponseKeywordDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/keywords")
public class KeywordController {
    private final KeywordService keywordService;

    @GetMapping
    public List<ResponseKeywordDto> findAll(@RequestParam List<String> keywords) {
        if (keywords != null && !keywords.isEmpty()) {
            return keywordService.findByValueContainedIn(keywords);
        }
        return keywordService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseKeywordDto findById(@PathVariable Long id) {
        return keywordService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseKeywordDto deleteById(@PathVariable Long id) {
        return keywordService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseKeywordDto update(@PathVariable Long id, @RequestBody RequestKeywordDto requestKeywordDto) {
        return keywordService.update(id, requestKeywordDto);
    }

    @PostMapping
    public ResponseKeywordDto save(@RequestBody RequestKeywordDto requestKeywordDto) {
        return keywordService.save(requestKeywordDto);
    }
}
