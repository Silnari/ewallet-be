package ewallet.dto;

import lombok.Data;

@Data
public class AccountDto {

    private Long userId;
    private String name;
    private Integer startBalance;
}
