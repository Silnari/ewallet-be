package ewallet.rest;

import ewallet.dto.AccountDto;
import ewallet.entity.Account;
import ewallet.repository.AccountRepository;
import ewallet.repository.TransactionRepository;
import ewallet.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * Rest to return user account list
     * @param userId user id
     * @return user's account list (added 'All' account to represent all user's accounts)
     */
    @GetMapping("/{userId}")
    public List<AccountDto> getForUser(@PathVariable Long userId) {
        List<Account> accountList = accountRepository.findAllByUser_Id(userId);
        accountList.add(getAllAccount(userId, accountList));
        return accountList.stream().map(AccountDto::new).collect(Collectors.toList());
    }

    /**
     * Method to generate user 'All' account
     * @param userId user id
     * @param accountList user account list
     * @return generated 'All' account for given user
     */
    private Account getAllAccount(Long userId, List<Account> accountList) {
        Account allAccount = new Account();
        allAccount.setId(0L);
        allAccount.setName("All");
        allAccount.setUser(userRepository.findById(userId).get());
        allAccount.setStartBalance(accountList.stream().mapToDouble(Account::getStartBalance).sum());
        return allAccount;
    }

    /**
     * Rest to create account
     * @param accountDto account data transfer object
     * @return generated account
     */
    @PostMapping
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        return new AccountDto(accountRepository.save(new Account(accountDto,
                userRepository.findById(accountDto.getUserId()).get())));
    }

    /**
     * Rest to update account by id
     * @param id account id
     * @param accountDto account data transfer object
     * @return updated account
     */
    @PutMapping("/{id}")
    public AccountDto updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        Account account = accountRepository.findById(id).get();
        account.setName(accountDto.getName());
        account.setStartBalance(accountDto.getStartBalance());
        return new AccountDto(accountRepository.save(account));
    }

    /**
     * Rest to delete account by id
     * @param id account id
     */
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        transactionRepository.deleteAll(transactionRepository.findAllByAccount_Id(id));
        accountRepository.deleteById(id);
    }
}
