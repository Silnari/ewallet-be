package ewallet.rest;

import ewallet.dto.TransactionDto;
import ewallet.entity.Transaction;
import ewallet.repository.AccountRepository;
import ewallet.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction")
public class TransactionRest {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionRest(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Rest to return user transaction list for given account
     *
     * @param userId    user id
     * @param accountId account id
     * @return user's transaction list for given account
     */
    @GetMapping("/{userId}/{accountId}")
    public List<TransactionDto> getByAccount(@PathVariable Long userId, @PathVariable Long accountId) {
        List<Transaction> transactionList = accountId == 0 ? transactionRepository.findAllByAccount_User_Id(userId) :
                transactionRepository.findAllByAccount_Id(accountId);
        return transactionList.stream().map(TransactionDto::new).collect(Collectors.toList());
    }

    /**
     * Rest to add new transaction
     *
     * @param transactionDto transaction data transfer object
     * @return added transaction
     */
    @PostMapping
    public TransactionDto addTransaction(@RequestBody TransactionDto transactionDto) {
        return new TransactionDto(transactionRepository.save(new Transaction(transactionDto,
                accountRepository.findById(transactionDto.getAccountId()).get())));
    }

    /**
     * Rest to update transaction by id
     *
     * @param id             transaction id
     * @param transactionDto transaction data transfer object
     * @return updated transaction
     */
    @PutMapping("/{id}")
    public TransactionDto updateTransaction(@PathVariable Long id, @RequestBody TransactionDto transactionDto) {
        Transaction transaction = transactionRepository.findById(id).get();
        transaction.setCategory(transactionDto.getCategory());
        transaction.setDate(transactionDto.getDate());
        transaction.setNote(transactionDto.getNote());
        transaction.setValue(transactionDto.getValue());
        return new TransactionDto(transactionRepository.save(transaction));
    }

    /**
     * Rest to delete transaction by id
     *
     * @param id transaction id
     */
    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
    }
}
