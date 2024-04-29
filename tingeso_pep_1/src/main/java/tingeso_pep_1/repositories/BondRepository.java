package tingeso_pep_1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso_pep_1.entities.BondEntity;

@Repository
public interface BondRepository extends JpaRepository<BondEntity, Long> {

    BondEntity findFirstByIdmark(Long id);
}
