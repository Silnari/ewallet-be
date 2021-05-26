package ewallet.rest;

import ewallet.dto.AccountDto;
import ewallet.entity.Account;
import ewallet.entity.Transaction;
import ewallet.entity.Transfer;
import ewallet.entity.User;
import ewallet.entity.enums.TransactionType;
import ewallet.repository.AccountRepository;
import ewallet.repository.TransactionRepository;
import ewallet.repository.TransferRepository;
import ewallet.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@SpringBootTest
class AccountRestTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRest accountRest;

    @BeforeEach
    void generateData() {
        User user = new User();
        user.setLogin("testUser");
        user.setEmail("test@user.com");
        userRepository.save(user);

        for (int i = 1; i <= 3; i++) {
            Account account = new Account();
            account.setName("account" + i);
            account.setStartBalance((double) i);
            account.setUser(user);
            accountRepository.save(account);
        }

        System.out.println(userRepository.findAll());
    }

    @AfterEach
    void deleteData() {
        accountRepository.deleteAll(accountRepository.findAllByUser_Id(getUserId()));
        userRepository.deleteById(getUserId());
    }

    private long getUserId() {
        return userRepository.findByLogin("testUser").getId();
    }

    @Test
    void getForUserResponseTest() {
        List<AccountDto> accountList = accountRest.getForUser(getUserId());

        Assertions.assertEquals(4, accountList.size());
        Assertions.assertTrue(accountList.stream().anyMatch(a -> a.getId() == 0L));
    }

    @Test
    void createAccountTest() {
        long id = getUserId();
        AccountDto accountDto = new AccountDto();
        accountDto.setName("testAccount");
        accountDto.setStartBalance(200.);
        accountDto.setUserId(id);
        accountRest.createAccount(accountDto);

        Assertions.assertEquals(accountDto.getStartBalance(),
                accountRepository.findByNameAndUser_Id(accountDto.getName(), id).getStartBalance());
    }

    @Test
    void updateAccountTest() {
        long id = getUserId();
        double balance = 222.;
        Account account = accountRepository.findByNameAndUser_Id("account1", id);
        AccountDto accountDto = new AccountDto();
        accountDto.setName(account.getName());
        accountDto.setUserId(id);
        accountDto.setStartBalance(balance);
        accountRest.updateAccount(account.getId(), accountDto);

        Assertions.assertEquals(balance, accountRepository.findByNameAndUser_Id("account1", id).getStartBalance());
    }

    @Test
    void deleteAccountTest() {
        long id = getUserId();
        Account account = accountRepository.findAllByUser_Id(id).get(0);
        Transaction transaction = new Transaction();
        transaction.setDate(new Date());
        transaction.setCategory("test");
        transaction.setTransactionType(TransactionType.INCOME);
        transaction.setValue(20.);
        transaction.setAccount(account);
        String accountName = account.getName();
        accountRest.deleteAccount(account.getId());

        Assertions.assertNull(accountRepository.findByNameAndUser_Id(accountName, id));
        Assertions.assertEquals(Collections.emptyList(), transactionRepository.findAllByAccount_Id(account.getId()));
    }
}