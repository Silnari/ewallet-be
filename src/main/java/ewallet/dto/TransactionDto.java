package ewallet.dto;

import ewallet.entity.enums.TransactionType;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionDto {

    private Long accountId;
    private String category;
    private Date date;
    private Integer value;
    private String note;
    private TransactionType transactionType;
}
