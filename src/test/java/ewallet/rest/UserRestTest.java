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

    /**
     * Method to generate test data before each test
     */
    @BeforeEach
    void generateData() {
        userRepository.save(dataGenerator.generateUser());
    }

    /**
     * Cleanup method
     */
    @AfterEach
    void deleteData() {
        userRepository.deleteAll();
    }

    /**
     * Method to return test user id
     *
     * @return test user id
     */
    private long getUserId() {
        return userRepository.findByLogin("testUser").getId();
    }

    /**
     * Test method to verify rest GET "/api/user/{id}" - get user
     * Verifies if returned user is correct
     */
    @Test
    void getUser() {
        User expectedUser = userRepository.findById(getUserId()).get();
        User actualUser = userRest.getUser(getUserId());

        Assertions.assertEquals(actualUser.getLogin(), expectedUser.getLogin());
    }

    /**
     * Test method to verify rest POST "/api/user/register" - register new user
     * Verifies if added user is present in db and if new user login already exists in db rest returns status code 400
     */
    @Test
    void addUser() {
        UserDto userDto = dataGenerator.generateUserDto();
        userRest.addUser(userDto);

        Assertions.assertNotNull(userRepository.findByLogin("testUser"));
        Assertions.assertEquals(400, userRest.addUser(userDto).getStatusCodeValue());
    }

    /**
     * Test method to verify rest POST "/api/user/authenticate" - authenticate user
     * Verifies if rests return correct id and status code 200 when verification passed correctly and if rest returns
     * status code 401 if password does not match one in db
     */
    @Test
    void authenticateUser() {
        UserDto userDto = dataGenerator.generateUserDto();

        Assertions.assertEquals(200, userRest.authenticateUser(userDto).getStatusCodeValue());
        Assertions.assertEquals(getUserId(), userRest.authenticateUser(userDto).getBody());
        userDto.setPassword("wrongPassword");
        Assertions.assertEquals(401, userRest.authenticateUser(userDto).getStatusCodeValue());
    }
}