package ro.unicredit.pfm.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ro.unicredit.pfm.repositories.entities.Category;
import ro.unicredit.pfm.repositories.entities.Keyword;
import ro.unicredit.pfm.repositories.entities.Transaction;
import ro.unicredit.pfm.exceptions.NotFoundException;
import ro.unicredit.pfm.repositories.CategoryRepository;
import ro.unicredit.pfm.repositories.KeywordRepository;
import ro.unicredit.pfm.repositories.TransactionRepository;
import ro.unicredit.pfm.services.dtos.requests.RequestTransactionDto;
import ro.unicredit.pfm.services.dtos.requests.TransactionSplitDto;
import ro.unicredit.pfm.services.dtos.responses.ResponseTransactionDto;
import ro.unicredit.pfm.services.mappers.requests.RequestTransactionMapper;
import ro.unicredit.pfm.services.mappers.responses.ResponseTransactionMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final ResponseTransactionMapper responseTransactionMapper;
    private final RequestTransactionMapper requestTransactionMapper;
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;

    public List<ResponseTransactionDto> findAll(){
        return responseTransactionMapper.toDto(transactionRepository.findAll());
    }

    public ResponseTransactionDto findById(Long id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction with id " + id + " not found."));
        return responseTransactionMapper.toDto(transaction);
    }

    public ResponseTransactionDto save(RequestTransactionDto requestTransactionDto){
        Transaction transaction = requestTransactionMapper.toEntity(requestTransactionDto);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return responseTransactionMapper.toDto(savedTransaction);
    }

    public ResponseTransactionDto deleteById(Long id){
        ResponseTransactionDto transactionToDelete = findById(id);
        transactionRepository.deleteById(id);
        return transactionToDelete;
    }

    public ResponseTransactionDto update(Long id, RequestTransactionDto requestTransactionDto){
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Update failed. Transaction not found."));
        Category associatedCategory = getAssociatedCategory(requestTransactionDto, existingTransaction);
        Keyword associatedKeyword = getAssociatedKeyword(requestTransactionDto, existingTransaction);
        Transaction associatedParent = getAssociatedParent(requestTransactionDto, existingTransaction);

        existingTransaction.setCategory(associatedCategory);
        existingTransaction.setKeyword(associatedKeyword);
        existingTransaction.setAmount(requestTransactionDto.getAmount());
        existingTransaction.setDate(requestTransactionDto.getDate());
        existingTransaction.setDescription(requestTransactionDto.getDescription());
        existingTransaction.setParent(associatedParent);
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return responseTransactionMapper.toDto(updatedTransaction);
    }

    private Transaction getAssociatedParent(RequestTransactionDto requestTransactionDto, Transaction existingTransaction) {
        Transaction associatedParent;
        if(requestTransactionDto.getParentId() != null) {
            associatedParent = transactionRepository.findById(requestTransactionDto.getParentId()).orElse(null);
        } else {
            associatedParent = existingTransaction.getParent();
        }
        return associatedParent;
    }

    private Keyword getAssociatedKeyword(RequestTransactionDto requestTransactionDto, Transaction existingTransaction) {
        Keyword associatedKeyword;
        if(requestTransactionDto.getKeywordId() != null) {
            associatedKeyword = keywordRepository.findById(requestTransactionDto.getKeywordId()).orElse(null);
        } else {
            associatedKeyword = existingTransaction.getKeyword();
        }
        return associatedKeyword;
    }

    private Category getAssociatedCategory(RequestTransactionDto requestTransactionDto, Transaction existingTransaction) {
        Category associatedCategory;
        if(requestTransactionDto.getCategoryId() != null) {
            associatedCategory = categoryRepository.findById(requestTransactionDto.getCategoryId()).orElse(null);
        } else {
            associatedCategory = existingTransaction.getCategory();
        }
        return associatedCategory;
    }

    public void splitTransaction(TransactionSplitDto transactionSplitDto, Transaction transaction) {
        Transaction parentTransaction = Transaction.builder()
                .amount(transactionSplitDto.getAmount())
                .date(transactionSplitDto.getDate())
                .description(transactionSplitDto.getDescription())
                .parent(transaction).build();
        transactionRepository.save(parentTransaction);
        if(transactionSplitDto.getChildren() != null) {
           for(TransactionSplitDto childTransactionDto : transactionSplitDto.getChildren()) {
               splitTransaction(childTransactionDto, parentTransaction);
           }
        }
    }
}

