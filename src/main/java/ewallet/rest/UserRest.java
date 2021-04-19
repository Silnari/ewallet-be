package ewallet.rest;

import ewallet.dto.UserDto;
import ewallet.entity.User;
import ewallet.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserRest {

    private final UserRepository userRepository;

    public UserRest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping
    public User addUser(@RequestBody UserDto user) {
        return userRepository.save(new User(user));
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
