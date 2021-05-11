package ewallet.repository;

import ewallet.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findAllByFrom_Id(Long fromId);
    List<Transfer> findAllByTo_Id(Long toId);
}
