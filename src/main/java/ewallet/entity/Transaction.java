package ewallet.entity;

import ewallet.dto.TransactionDto;
import ewallet.entity.enums.TransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private String category;
    private Date date;
    private Double value;
    private String note;
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    public Transaction(TransactionDto transactionDto, Account account) {
        this.account = account;
        this.category = transactionDto.getCategory();
        this.date = transactionDto.getDate();
        this.value = transactionDto.getValue();
        this.note = transactionDto.getNote();
        this.transactionType = transactionDto.getTransactionType();
    }
}
