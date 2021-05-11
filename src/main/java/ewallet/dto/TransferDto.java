package ewallet.dto;

import ewallet.entity.Transfer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TransferDto {

    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private Double value;
    private String note;
    private Date date;

    public TransferDto(Transfer transfer) {
        this.id = transfer.getId();
        this.fromAccountId = transfer.getFrom().getId();
        this.toAccountId = transfer.getTo().getId();
        this.value = transfer.getValue();
        this.note = transfer.getNote();
        this.date = transfer.getDate();
    }
}
