package ewallet.rest;

import ewallet.DataGenerator;
import ewallet.dto.UserDto;
import ewallet.entity.User;
import ewallet.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRestTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRest userRest;

    @Autowired
    private DataGenerator dataGenerator;

    @BeforeEach
    void generateData() {
        userRepository.save(dataGenerator.generateUser());
    }

    @AfterEach
    void deleteData() {
        userRepository.deleteAll();
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
        UserDto userDto = dataGenerator.generateUserDto();
        userRest.addUser(userDto);

        Assertions.assertNotNull(userRepository.findByLogin("testUser"));
        Assertions.assertEquals(400, userRest.addUser(userDto).getStatusCodeValue());
    }

    @Test
    void authenticateUser() {
        UserDto userDto = dataGenerator.generateUserDto();

        Assertions.assertEquals(200, userRest.authenticateUser(userDto).getStatusCodeValue());
        Assertions.assertEquals(getUserId(), userRest.authenticateUser(userDto).getBody());
        userDto.setPassword("wrongPassword");
        Assertions.assertEquals(401, userRest.authenticateUser(userDto).getStatusCodeValue());
    }
}