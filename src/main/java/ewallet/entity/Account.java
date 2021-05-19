package ewallet.entity;

import ewallet.dto.AccountDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Class representing user's account
 */
@Data
@Entity
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    @Column(name = "start_balance")
    private Double startBalance;

    public Account(AccountDto accountDto, User user) {
        this.user = user;
        this.name = accountDto.getName();
        this.startBalance = accountDto.getStartBalance();
    }
}
