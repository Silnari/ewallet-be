package ewallet;

import ewallet.dto.AccountDto;
import ewallet.dto.TransactionDto;
import ewallet.dto.TransferDto;
import ewallet.dto.UserDto;
import ewallet.entity.Account;
import ewallet.entity.Transaction;
import ewallet.entity.Transfer;
import ewallet.entity.User;
import ewallet.entity.enums.TransactionType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class to generate example data
 */
@Component
public class DataGenerator {

    private final BCryptPasswordEncoder passwordEncoder;

    public DataGenerator() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Method to generate example user
     *
     * @return generated user
     */
    public User generateUser() {
        User user = new User();
        user.setLogin("testUser");
        user.setEmail("test@user.com");
        user.setPassword(passwordEncoder.encode("password"));
        return user;
    }

    /**
     * Method to generate example user data transfer object
     *
     * @return generated user data transfer object
     */
    public UserDto generateUserDto() {
        UserDto userDto = new UserDto();
        userDto.setLogin("testUser");
        userDto.setEmail("test@user.com");
        userDto.setPassword("password");
        return userDto;
    }

    /**
     * Method to generate example account list for given user
     *
     * @param user user to be linked with generated accounts
     * @return generated account list
     */
    public List<Account> generateAccountList(User user) {
        List<Account> accountList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Account account = new Account();
            account.setName("account" + i);
            account.setStartBalance((double) i);
            account.setUser(user);
            accountList.add(account);
        }
        return accountList;
    }

    /**
     * Method to generate example account data transfer object
     *
     * @param userId user id to be included in account data transfer object
     * @return generated account data transfer object
     */
    public AccountDto generateAccountDto(Long userId) {
        AccountDto accountDto = new AccountDto();
        accountDto.setName("account");
        accountDto.setStartBalance(200.);
        accountDto.setUserId(userId);
        return accountDto;
    }

    /**
     * Method to generate account data transfer object based on given account object
     *
     * @param account example account
     * @return generated account data transfer object
     */
    public AccountDto generateAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setName(account.getName());
        accountDto.setStartBalance(account.getStartBalance());
        accountDto.setUserId(account.getUser().getId());
        return accountDto;
    }

    /**
     * Method to generate example transaction list for given account
     *
     * @param account account to be linked with generated transactions
     * @return generated transaction list
     */
    public List<Transaction> generateTransactionList(Account account) {
        List<Transaction> transactionList = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Transaction transaction = new Transaction();
            transaction.setCategory("test");
            transaction.setDate(new Date());
            transaction.setAccount(account);
            transaction.setValue(22.);
            transaction.setTransactionType(i % 2 == 0 ? TransactionType.INCOME : TransactionType.OUTCOME);
            transactionList.add(transaction);
        }
        return transactionList;
    }

    /**
     * Method to generate example transaction data transfer object
     *
     * @param accountId account id to be included in transaction data transfer object
     * @return generated transaction data transfer object
     */
    public TransactionDto generateTransactionDto(Long accountId) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(TransactionType.INCOME);
        transactionDto.setDate(new Date());
        transactionDto.setValue(20.);
        transactionDto.setCategory("test");
        transactionDto.setAccountId(accountId);
        return transactionDto;
    }

    /**
     * Method to generate account transaction transfer object based on given transaction object
     *
     * @param transaction example transaction
     * @return generated transaction data transfer object
     */
    public TransactionDto generateTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(transaction.getTransactionType());
        transactionDto.setDate(transaction.getDate());
        transactionDto.setValue(transaction.getValue());
        transactionDto.setCategory(transaction.getCategory());
        transactionDto.setAccountId(transaction.getAccount().getId());
        return transactionDto;
    }

    /**
     * Method to generate example transfer list for given accounts
     *
     * @param account1 account to be linked with generated transfer
     * @param account2 account to be linked with generated transfer
     * @return generated transfer list
     */
    public List<Transfer> generateTransferList(Account account1, Account account2) {
        List<Transfer> transferList = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            Transfer transfer = new Transfer();
            transfer.setDate(new Date());
            transfer.setValue(20.);
            if (i % 2 == 0) {
                transfer.setFrom(account2);
                transfer.setTo(account1);
            } else {
                transfer.setFrom(account1);
                transfer.setTo(account2);
            }
            transferList.add(transfer);
        }
        return transferList;
    }

    /**
     * Method to generate example transfer data transfer object
     *
     * @param fromId from account id included in transfer data transfer object
     * @param toId   to account id included in transfer data transfer object
     * @return generated transfer data transfer object
     */
    public TransferDto generateTransferDto(Long fromId, Long toId) {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(fromId);
        transferDto.setToAccountId(toId);
        transferDto.setDate(new Date());
        transferDto.setValue(20.);
        return transferDto;
    }

    /**
     * Method to generate account transfer transfer object based on given transfer object
     *
     * @param transfer example transfer
     * @return generated transfer data transfer object
     */
    public TransferDto generateTransferDto(Transfer transfer) {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(transfer.getFrom().getId());
        transferDto.setToAccountId(transfer.getTo().getId());
        transferDto.setDate(transfer.getDate());
        transferDto.setValue(transfer.getValue());
        return transferDto;
    }
}
