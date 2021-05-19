package ewallet.repository;

import ewallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface used to communicate with User table
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Method checking if given user is present in db
     * @param login user login
     * @return true if user exists or false
     */
    boolean existsByLogin(String login);


    /**
     * Method to find user by login
     * @param login user login
     * @return founded user
     */
    User findByLogin(String login);
}
