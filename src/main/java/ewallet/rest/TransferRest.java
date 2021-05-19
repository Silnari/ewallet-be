package ewallet.rest;

import ewallet.dto.TransferDto;
import ewallet.entity.Transfer;
import ewallet.repository.AccountRepository;
import ewallet.repository.TransferRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transfer")
public class TransferRest {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public TransferRest(TransferRepository transferRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Rest to return all outgoing transfers for given account
     * @param fromId account id
     * @return List of outgoing transfer for given account
     */
    @GetMapping("/from/{fromId}")
    public List<TransferDto> getFrom(@PathVariable Long fromId) {
        return transferRepository.findAllByFrom_Id(fromId)
                .stream().map(TransferDto::new).collect(Collectors.toList());
    }

    /**
     * Rest to return all ingoing transfers for given account
     * @param toId account id
     * @return List of ingoing transfer for given account
     */
    @GetMapping("/to/{toId}")
    public List<TransferDto> getTo(@PathVariable Long toId) {
        return transferRepository.findAllByTo_Id(toId)
                .stream().map(TransferDto::new).collect(Collectors.toList());
    }

    /**
     * Rest to add new transfer
     * @param transferDto transfer data transfer object
     * @return added transfer
     */
    @PostMapping
    public TransferDto addTransfer(@RequestBody TransferDto transferDto) {
        return new TransferDto(transferRepository.save(new Transfer(transferDto,
                accountRepository.findById(transferDto.getFromAccountId()).get(),
                accountRepository.findById(transferDto.getToAccountId()).get())));
    }

    /**
     * Rest to update transfer by id
     * @param id transfer id
     * @param transferDto transfer data transfer object
     * @return updated transfer
     */
    @PutMapping("/{id}")
    public TransferDto updateTransfer(@PathVariable Long id, @RequestBody TransferDto transferDto) {
        Transfer transfer = transferRepository.findById(id).get();
        transfer.setFrom(accountRepository.findById(transferDto.getFromAccountId()).get());
        transfer.setTo(accountRepository.findById(transferDto.getToAccountId()).get());
        transfer.setDate(transferDto.getDate());
        transfer.setNote(transferDto.getNote());
        transfer.setValue(transferDto.getValue());
        return new TransferDto(transferRepository.save(transfer));
    }

    /**
     * Rest to delete transfer by id
     * @param id transfer id
     */
    @DeleteMapping("/{id}")
    public void deleteTransfer(@PathVariable Long id) {
        transferRepository.deleteById(id);
    }
}
