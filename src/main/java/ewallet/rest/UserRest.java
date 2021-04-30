package ewallet.rest;

import ewallet.dto.UserDto;
import ewallet.entity.User;
import ewallet.repository.AccountRepository;
import ewallet.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserRest {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserRest(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody UserDto user) {
        if (userRepository.existsByLogin(user.getLogin()))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return new ResponseEntity<>(userRepository.save(new User(user)), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Long> authenticateUser(@RequestBody UserDto userDto) {
        User user = userRepository.findByLogin(userDto.getLogin());
        if (user == null || !passwordEncoder.matches(userDto.getPassword(), user.getPassword()))
            return new ResponseEntity<>(-1L, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        accountRepository.deleteAll(accountRepository.findAllByUser_Id(id));
        userRepository.deleteById(id);
    }
}
