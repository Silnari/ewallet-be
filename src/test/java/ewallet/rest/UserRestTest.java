package ewallet.rest;

import ewallet.dto.UserDto;
import ewallet.entity.User;
import ewallet.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRestTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRest userRest;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void generateData() {
        User user = new User();
        user.setLogin("testUser");
        user.setEmail("test@user.com");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
    }

    @AfterEach
    void deleteData() {
        userRepository.deleteById(getUserId());
    }

    private long getUserId() {
        return userRepository.findByLogin("testUser").getId();
    }

    @Test
    void getUser() {
        User expectedUser = userRepository.findById(getUserId()).get();
        User actualUser = userRest.getUser(getUserId());

        Assertions.assertEquals(actualUser.getLogin(), expectedUser.getLogin());
    }

    @Test
    void addUser() {
        UserDto userDto = new UserDto();
        userDto.setLogin("login");
        userDto.setEmail("email@mail.com");
        userDto.setPassword("password");
        userRest.addUser(userDto);

        Assertions.assertNotNull(userRepository.findByLogin("login"));
        Assertions.assertEquals(400, userRest.addUser(userDto).getStatusCodeValue());
    }

    @Test
    void authenticateUser() {
        UserDto userDto = new UserDto();
        userDto.setLogin("testUser");
        userDto.setEmail("test@user.com");
        userDto.setPassword("password");

        Assertions.assertEquals(200, userRest.authenticateUser(userDto).getStatusCodeValue());
        Assertions.assertEquals(getUserId(), userRest.authenticateUser(userDto).getBody());
        userDto.setPassword("wrongPassword");
        Assertions.assertEquals(401, userRest.authenticateUser(userDto).getStatusCodeValue());
    }
}