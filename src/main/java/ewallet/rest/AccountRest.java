package ewallet.rest;

import ewallet.dto.AccountDto;
import ewallet.entity.Account;
import ewallet.entity.User;
import ewallet.repository.AccountRepository;
import ewallet.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountRest {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountRest(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @GetMapping("/{user_id}")
    public List<Account> getForUser(@PathVariable Long user_id) {
        User user = userRepository.findById(user_id).orElseThrow();
        return accountRepository.findAllByUser(user);
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
        accountRepository.deleteById(id);
    }
}
