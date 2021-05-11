package ewallet.dto;

import ewallet.entity.Transaction;
import ewallet.entity.enums.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TransactionDto {

    private Long id;
    private Long accountId;
    private String category;
    private Date date;
    private Double value;
    private String note;
    private TransactionType transactionType;

    public TransactionDto(Transaction transaction) {
        this.id = transaction.getId();
        this.accountId = transaction.getAccount().getId();
        this.category = transaction.getCategory();
        this.date = transaction.getDate();
        this.value = transaction.getValue();
        this.note = transaction.getNote();
        this.transactionType = transaction.getTransactionType();
    }
}
