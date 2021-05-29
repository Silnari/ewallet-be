package ewallet.rest;

import ewallet.dto.UserDto;
import ewallet.entity.User;
import ewallet.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserRest {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserRest(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Rest to get user by id
     *
     * @param id user id
     * @return founded user
     */
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Rest to add new user
     *
     * @param userDto user data transfer object
     * @return added user
     */
    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
        if (userRepository.existsByLogin(userDto.getLogin()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return new ResponseEntity<>(userRepository.save(new User(userDto)), HttpStatus.OK);
    }

    /**
     * Rest to authenticate user
     *
     * @param userDto user data transfer object
     * @return id of authenticated user or -1
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Long> authenticateUser(@RequestBody UserDto userDto) {
        User user = userRepository.findByLogin(userDto.getLogin());
        if (user == null || !passwordEncoder.matches(userDto.getPassword(), user.getPassword()))
            return new ResponseEntity<>(-1L, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }
}
