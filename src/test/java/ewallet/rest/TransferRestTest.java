package ewallet.rest;

import ewallet.DataGenerator;
import ewallet.dto.TransferDto;
import ewallet.entity.Account;
import ewallet.entity.Transfer;
import ewallet.entity.User;
import ewallet.repository.AccountRepository;
import ewallet.repository.TransferRepository;
import ewallet.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class TransferRestTest {

    @Autowired
    private TransferRest transferRest;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private DataGenerator dataGenerator;

    @BeforeEach
    void generateData() {
        User user = dataGenerator.generateUser();
        userRepository.save(user);
        List<Account> accountList = dataGenerator.generateAccountList(user);
        accountRepository.saveAll(accountList);
        transferRepository.saveAll(dataGenerator.generateTransferList(accountList.get(0), accountList.get(1)));
    }

    @AfterEach
    void deleteData() {
        transferRepository.deleteAll();
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
    void getFrom() {
        getAccountIdList().forEach(accountId -> {
            List<TransferDto> transferDtoList = transferRest.getFrom(accountId);
            List<Transfer> transferList = transferRepository.findAllByFrom_Id(accountId);

            Assertions.assertEquals(transferList.size(), transferDtoList.size());
        });
    }

    @Test
    void getTo() {
        getAccountIdList().forEach(accountId -> {
            List<TransferDto> transferDtoList = transferRest.getTo(accountId);
            List<Transfer> transferList = transferRepository.findAllByTo_Id(accountId);

            Assertions.assertEquals(transferList.size(), transferDtoList.size());
        });
    }

    @Test
    void addTransfer() {
        List<Long> accountIdList = getAccountIdList();
        TransferDto transferDto = dataGenerator.generateTransferDto(accountIdList.get(0), accountIdList.get(1));
        long transferId = transferRest.addTransfer(transferDto).getId();

        Assertions.assertTrue(transferRepository.existsById(transferId));
    }

    @Test
    void updateTransfer() {
        String note = "testNote";
        List<Long> accountIdList = getAccountIdList();
        Transfer transfer = transferRepository.findAllByFrom_Id(accountIdList.get(0)).get(0);
        TransferDto transferDto = dataGenerator.generateTransferDto(transfer);
        transferDto.setNote(note);
        transferRest.updateTransfer(transfer.getId(), transferDto);

        Assertions.assertEquals(note, transferRepository.findById(transfer.getId()).get().getNote());
    }

    @Test
    void deleteTransfer() {
        long transferId = transferRepository.findAllByFrom_Id(getAccountIdList().get(0)).get(0).getId();
        transferRest.deleteTransfer(transferId);

        Assertions.assertFalse(transferRepository.existsById(transferId));
    }
}