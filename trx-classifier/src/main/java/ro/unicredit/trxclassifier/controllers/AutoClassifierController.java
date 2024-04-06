package ro.unicredit.trxclassifier.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.unicredit.trxclassifier.services.AutoClassifierService;
import ro.unicredit.trxclassifier.services.dtos.responses.ResponseAutoClassifierV2Dto;
import ro.unicredit.trxclassifier.services.dtos.responses.ResponseTransactionDto;

@RestController
@RequestMapping("/auto-classifier")
@AllArgsConstructor
public class AutoClassifierController {
    private final AutoClassifierService autoClassifierService;

    @PutMapping
    public ResponseTransactionDto classifyByTxDescription(@RequestParam Long txId, @RequestParam String description) {
        return autoClassifierService.classifyByTxDescription(txId, description);
    }

    @PutMapping("/v2")
    public ResponseAutoClassifierV2Dto classifyByTxDescriptionV2(@RequestParam Long txId, @RequestParam String description) {
        return autoClassifierService.classifyByTxDescriptionV2(txId, description);
    }
}
