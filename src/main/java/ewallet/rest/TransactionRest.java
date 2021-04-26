package ewallet.rest;

import ewallet.dto.TransactionDto;
import ewallet.entity.Transaction;
import ewallet.repository.AccountRepository;
import ewallet.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionRest {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionRest(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    @GetMapping("/{accountId}")
    public List<Transaction> getByAccount(@PathVariable Long accountId) {
        return transactionRepository.findAllByAccount_Id(accountId);
    }

    @PostMapping
    public Transaction addTransaction(@RequestBody TransactionDto transactionDto) {
        return transactionRepository.save(new Transaction(transactionDto,
                accountRepository.findById(transactionDto.getAccountId()).orElseThrow()));
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody TransactionDto transactionDto) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow();
        transaction.setCategory(transactionDto.getCategory());
        transaction.setDate(transactionDto.getDate());
        transaction.setNote(transactionDto.getNote());
        transaction.setValue(transactionDto.getValue());
        return transactionRepository.save(transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
    }
}
