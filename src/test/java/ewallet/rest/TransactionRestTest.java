package ewallet.rest;

import ewallet.DataGenerator;
import ewallet.dto.TransactionDto;
import ewallet.entity.Account;
import ewallet.entity.Transaction;
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

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class TransactionRestTest {

    @Autowired
    private TransactionRest transactionRest;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataGenerator dataGenerator;

    /**
     * Method to generate test data before each test
     */
    @BeforeEach
    void generateData() {
        User user = dataGenerator.generateUser();
        userRepository.save(user);
        List<Account> accountList = dataGenerator.generateAccountList(user);
        accountRepository.saveAll(accountList);
        accountList.forEach(account -> transactionRepository.saveAll(dataGenerator.generateTransactionList(account)));
    }

    /**
     * Cleanup method
     */
    @AfterEach
    void deleteData() {
        transactionRepository.deleteAll();
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
     * Method to return test user account id list
     *
     * @return test user account id list
     */
    private List<Long> getAccountIdList() {
        return accountRepository.findAllByUser_Id(getUserId()).stream().map(Account::getId).collect(Collectors.toList());
    }

    /**
     * Test method to verify rest GET "/api/transaction/{userId}/{accountId}" - get account list for user
     * Verifies length of returned transaction list for specified account and if "All" account has all user transactions
     */
    @Test
    void getByAccount() {
        long userId = getUserId();
        getAccountIdList().forEach(accountId -> {
            List<TransactionDto> transactionDtoList = transactionRest.getByAccount(userId, accountId);
            List<Transaction> transactionList = transactionRepository.findAllByAccount_Id(accountId);

            Assertions.assertEquals(transactionList.size(), transactionDtoList.size());
        });

        Assertions.assertEquals(transactionRepository.findAllByAccount_User_Id(userId).size(),
                transactionRest.getByAccount(userId, 0L).size());
    }

    /**
     * Test method to verify rest POST "/api/transaction" - add new transaction
     * Verifies if added transaction is present in db
     */
    @Test
    void addTransaction() {
        long accountId = getAccountIdList().get(0);
        TransactionDto transactionDto = dataGenerator.generateTransactionDto(accountId);
        long transactionId = transactionRest.addTransaction(transactionDto).getId();

        Assertions.assertTrue(transactionRepository.existsById(transactionId));
    }

    /**
     * Test method to verify rest PUT "/api/transaction/{id}" - update transaction
     * Verifies if updated transaction is present in db and its updated field is correct
     */
    @Test
    void updateTransaction() {
        String category = "newTestCategory";
        long accountId = getAccountIdList().get(0);
        Transaction transaction = transactionRepository.findAllByAccount_Id(accountId).get(0);
        TransactionDto transactionDto = dataGenerator.generateTransactionDto(transaction);
        transactionDto.setCategory(category);
        transactionRest.updateTransaction(transaction.getId(), transactionDto);

        Assertions.assertEquals(category, transactionRepository.findById(transaction.getId()).get().getCategory());
    }

    /**
     * Test method to verify rest DELETE "/api/transaction/{id}" - delete transaction
     * Verifies if deleted transaction is no longer present in db
     */
    @Test
    void deleteTransaction() {
        long transactionId = transactionRepository.findAllByAccount_User_Id(getUserId()).get(0).getId();
        transactionRest.deleteTransaction(transactionId);

        Assertions.assertFalse(transactionRepository.existsById(transactionId));
    }
}