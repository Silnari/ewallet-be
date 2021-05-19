package ewallet.entity;

import ewallet.dto.TransferDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Class representing transfer from one account to another
 */
@Data
@Entity
@NoArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Account from;
    @ManyToOne
    private Account to;
    private Double value;
    private String note;
    private Date date;

    public Transfer(TransferDto transferDto, Account from, Account to) {
        this.id = transferDto.getId();
        this.from = from;
        this.to = to;
        this.value = transferDto.getValue();
        this.note = transferDto.getNote();
        this.date = transferDto.getDate();
    }
}
