package ewallet.repository;

import ewallet.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface used to communicate with Transfer table
 */
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    /**
     * Method to find all outgoing transfer
     * @param fromId transfer id
     * @return founded transfers
     */
    List<Transfer> findAllByFrom_Id(Long fromId);

    /**
     * Method to find all ingoing transfer
     * @param toId transfer id
     * @return founded transfers
     */
    List<Transfer> findAllByTo_Id(Long toId);
}
