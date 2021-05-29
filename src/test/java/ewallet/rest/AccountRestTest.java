package ewallet.rest;

import ewallet.DataGenerator;
import ewallet.dto.AccountDto;
import ewallet.entity.Account;
import ewallet.entity.User;
import ewallet.repository.AccountRepository;
import ewallet.repository.TransactionRepository;
import ewallet.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
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

    @Autowired
    private DataGenerator dataGenerator;

    /**
     * Method to generate test data before each test
     */
    @BeforeEach
    void generateData() {
        User user = dataGenerator.generateUser();
        userRepository.save(user);
        accountRepository.saveAll(dataGenerator.generateAccountList(user));
    }

    /**
     * Cleanup method
     */
    @AfterEach
    void deleteData() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Method to return test user id
     *
     * @return test user id
     */
    private long getUserId() {
        return userRepository.findByLogin("testUser").getId();
    }

    /**
     * Test method to verify rest GET "/api/account/{userId}" - get accounts for user
     * Verifies length of returned account list and if there is a account with id equals to 0 ("All" account)
     */
    @Test
    void getForUser() {
        List<AccountDto> accountList = accountRest.getForUser(getUserId());

        Assertions.assertEquals(4, accountList.size());
        Assertions.assertTrue(accountList.stream().anyMatch(a -> a.getId() == 0L));
    }

    /**
     * Test method to verify rest POST "/api/account" - add new account
     * Verifies if added account is present in db
     */
    @Test
    void createAccount() {
        long userId = getUserId();
        AccountDto accountDto = dataGenerator.generateAccountDto(userId);
        long id = accountRest.createAccount(accountDto).getId();

        Assertions.assertTrue(accountRepository.existsById(id));
    }

    /**
     * Test method to verify rest PUT "/api/account/{id}" - update account
     * Verifies if updated account is present in db and its updated field is correct
     */
    @Test
    void updateAccount() {
        long userId = getUserId();
        double balance = 222.;
        Account account = accountRepository.findAllByUser_Id(userId).get(0);
        AccountDto accountDto = dataGenerator.generateAccountDto(account);
        accountDto.setStartBalance(balance);
        accountRest.updateAccount(account.getId(), accountDto);

        Assertions.assertEquals(balance, accountRepository.findById(account.getId()).get().getStartBalance());
    }

    /**
     * Test method to verify rest DELETE "/api/account/{id}" - delete account
     * Verifies if deleted account is no longer present in db and verifies if all its transactions are deleted as well
     */
    @Test
    void deleteAccount() {
        Account account = accountRepository.findAllByUser_Id(getUserId()).get(0);
        transactionRepository.saveAll(dataGenerator.generateTransactionList(account));
        accountRest.deleteAccount(account.getId());

        Assertions.assertFalse(accountRepository.existsById(account.getId()));
        Assertions.assertEquals(Collections.emptyList(), transactionRepository.findAllByAccount_Id(account.getId()));
    }
}