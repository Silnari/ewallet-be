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

@Component
public class DataGenerator {

    private final BCryptPasswordEncoder passwordEncoder;

    public DataGenerator() {
        passwordEncoder = new BCryptPasswordEncoder();
    }


    public User generateUser() {
        User user = new User();
        user.setLogin("testUser");
        user.setEmail("test@user.com");
        user.setPassword(passwordEncoder.encode("password"));
        return user;
    }

    public UserDto generateUserDto() {
        UserDto userDto = new UserDto();
        userDto.setLogin("testUser");
        userDto.setEmail("test@user.com");
        userDto.setPassword("password");
        return userDto;
    }

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

    public AccountDto generateAccountDto(Long userId) {
        AccountDto accountDto = new AccountDto();
        accountDto.setName("account");
        accountDto.setStartBalance(200.);
        accountDto.setUserId(userId);
        return accountDto;
    }

    public AccountDto generateAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setName(account.getName());
        accountDto.setStartBalance(account.getStartBalance());
        accountDto.setUserId(account.getUser().getId());
        return accountDto;
    }

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

    public TransactionDto generateTransactionDto(Long accountId) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(TransactionType.INCOME);
        transactionDto.setDate(new Date());
        transactionDto.setValue(20.);
        transactionDto.setCategory("test");
        transactionDto.setAccountId(accountId);
        return transactionDto;
    }

    public TransactionDto generateTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionType(transaction.getTransactionType());
        transactionDto.setDate(transaction.getDate());
        transactionDto.setValue(transaction.getValue());
        transactionDto.setCategory(transaction.getCategory());
        transactionDto.setAccountId(transaction.getAccount().getId());
        return transactionDto;
    }

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

    public TransferDto generateTransferDto(Long fromId, Long toId) {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(fromId);
        transferDto.setToAccountId(toId);
        transferDto.setDate(new Date());
        transferDto.setValue(20.);
        return transferDto;
    }

    public TransferDto generateTransferDto(Transfer transfer) {
        TransferDto transferDto = new TransferDto();
        transferDto.setFromAccountId(transfer.getFrom().getId());
        transferDto.setToAccountId(transfer.getTo().getId());
        transferDto.setDate(transfer.getDate());
        transferDto.setValue(transfer.getValue());
        return transferDto;
    }
}
