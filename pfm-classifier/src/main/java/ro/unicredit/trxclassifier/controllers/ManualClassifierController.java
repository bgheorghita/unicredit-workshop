package ro.unicredit.trxclassifier.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.unicredit.trxclassifier.services.ManualClassifierService;
import ro.unicredit.trxclassifier.services.dtos.ResponseTransactionDto;

@RestController
@RequestMapping("/manual-classifier")
@AllArgsConstructor
public class ManualClassifierController {
    private final ManualClassifierService manualClassifierService;

    @GetMapping
    public ResponseTransactionDto classify(@RequestParam Long txId, @RequestParam Long categoryId) {
        return manualClassifierService.classify(txId, categoryId);
    }
}
