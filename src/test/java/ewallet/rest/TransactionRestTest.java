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

    @BeforeEach
    void generateData() {
        User user = dataGenerator.generateUser();
        userRepository.save(user);
        List<Account> accountList = dataGenerator.generateAccountList(user);
        accountRepository.saveAll(accountList);
        accountList.forEach(account -> transactionRepository.saveAll(dataGenerator.generateTransactionList(account)));
    }

    @AfterEach
    void deleteData() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    private long getUserId() {
        return userRepository.findByLogin("testUser").getId();
    }

    private List<Long> getAccountIdList() {
        return accountRepository.findAllByUser_Id(getUserId()).stream().map(Account::getId).collect(Collectors.toList());
    }

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

    @Test
    void addTransaction() {
        long accountId = getAccountIdList().get(0);
        TransactionDto transactionDto = dataGenerator.generateTransactionDto(accountId);
        long transactionId = transactionRest.addTransaction(transactionDto).getId();

        Assertions.assertTrue(transactionRepository.existsById(transactionId));
    }

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

    @Test
    void deleteTransaction() {
        long transactionId = transactionRepository.findAllByAccount_User_Id(getUserId()).get(0).getId();
        transactionRest.deleteTransaction(transactionId);

        Assertions.assertFalse(transactionRepository.existsById(transactionId));
    }
}