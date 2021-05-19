package ewallet.repository;

import ewallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface used to communicate with Transaction table
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Method to find all transactions by account id
     * @param accountId account id
     * @return founded transactions
     */
    List<Transaction> findAllByAccount_Id(Long accountId);

    /**
     * Method to find all transactions by user id
     * @param userId user id
     * @return founded transactions
     */
    List<Transaction> findAllByAccount_User_Id(Long userId);
}
