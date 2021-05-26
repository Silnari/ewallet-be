package ewallet.repository;

import ewallet.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface used to communicate with Account table
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Method to find list of accounts by user id
     * @param userId user id
     * @return founded list of accounts
     */
    List<Account> findAllByUser_Id(Long userId);

    /**
     * Method to find account by its name and user id
     * @param name account name
     * @param userId user id
     * @return founded account
     */
    Account findByNameAndUser_Id(String name, Long userId);
}
