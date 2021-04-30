package ewallet.entity;

import ewallet.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long id;
    private String login;
    private String password;
    private String email;

    public User(UserDto userDto) {
        this.login = userDto.getLogin();
        this.password = userDto.getPassword();
        this.email = userDto.getEmail();
    }
}
