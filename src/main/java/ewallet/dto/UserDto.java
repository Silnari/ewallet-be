package ewallet.dto;

import lombok.Data;

/**
 * Class representing user data transfer object used in rests
 */
@Data
public class UserDto {

    private String email;
    private String login;
    private String password;
}
