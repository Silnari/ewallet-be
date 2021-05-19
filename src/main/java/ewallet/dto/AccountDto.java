package ewallet.dto;

import ewallet.entity.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class representing account data transfer object used in rests
 */
@Data
@NoArgsConstructor
public class AccountDto {

    private Long id;
    private Long userId;
    private String name;
    private Double startBalance;

    public AccountDto(Account account) {
        this.id = account.getId();
        this.userId = account.getUser().getId();
        this.name = account.getName();
        this.startBalance = account.getStartBalance();
    }
}
