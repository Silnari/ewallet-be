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

    @GetMapping("/from/{fromId}")
    public List<TransferDto> getFrom(@PathVariable Long fromId) {
        return transferRepository.findAllByFrom_Id(fromId)
                .stream().map(TransferDto::new).collect(Collectors.toList());
    }

    @GetMapping("/to/{toId}")
    public List<TransferDto> getTo(@PathVariable Long toId) {
        return transferRepository.findAllByTo_Id(toId)
                .stream().map(TransferDto::new).collect(Collectors.toList());
    }

    @PostMapping
    public TransferDto addTransfer(@RequestBody TransferDto transferDto) {
        return new TransferDto(transferRepository.save(new Transfer(transferDto,
                accountRepository.findById(transferDto.getFromAccountId()).get(),
                accountRepository.findById(transferDto.getToAccountId()).get())));
    }

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

    @DeleteMapping("/{id}")
    public void deleteTransfer(@PathVariable Long id) {
        transferRepository.deleteById(id);
    }
}
