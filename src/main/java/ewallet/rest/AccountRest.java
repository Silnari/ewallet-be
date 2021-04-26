package ewallet.rest;

import ewallet.dto.AccountDto;
import ewallet.entity.Account;
import ewallet.repository.AccountRepository;
import ewallet.repository.TransactionRepository;
import ewallet.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountRest {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public AccountRest(AccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @GetMapping("/{userId}")
    public List<Account> getForUser(@PathVariable Long userId) {
        return accountRepository.findAllByUser_Id(userId);
    }

    @PostMapping
    public Account createAccount(@RequestBody AccountDto accountDto) {
        return accountRepository.save(new Account(accountDto,
                userRepository.findById(accountDto.getUserId()).orElseThrow()));
    }

    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setName(accountDto.getName());
        account.setStartBalance(accountDto.getStartBalance());
        return accountRepository.save(account);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        transactionRepository.deleteAll(transactionRepository.findAllByAccount_Id(id));
        accountRepository.deleteById(id);
    }
}
