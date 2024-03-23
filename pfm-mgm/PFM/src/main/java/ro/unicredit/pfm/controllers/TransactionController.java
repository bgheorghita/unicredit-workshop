package ro.unicredit.pfm.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.unicredit.pfm.services.TransactionService;
import ro.unicredit.pfm.services.dtos.requests.RequestTransactionDto;
import ro.unicredit.pfm.services.dtos.requests.TransactionSplitDto;
import ro.unicredit.pfm.services.dtos.responses.ResponseTransactionDto;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    public List<ResponseTransactionDto> findAll(){
        return transactionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseTransactionDto getTransactionById(@PathVariable Long id){
        return transactionService.findById(id);
    }

    @PostMapping
    public ResponseTransactionDto save(@RequestBody RequestTransactionDto requestTransactionDto){
        return transactionService.save(requestTransactionDto);
    }

    @PutMapping("/{id}")
    public ResponseTransactionDto updateTransaction(@PathVariable Long id, @RequestBody RequestTransactionDto requestTransactionDto){
        return transactionService.update(id, requestTransactionDto);
    }

    @DeleteMapping("/{id}")
    public ResponseTransactionDto deleteTransaction(@PathVariable Long id){
        return transactionService.deleteById(id);
    }

    @PostMapping("/split")
    public void splitTransaction(@RequestBody TransactionSplitDto transactionSplitDto){
        transactionService.splitTransaction(transactionSplitDto, null);
    }
}
