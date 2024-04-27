package tingeso_pep_1.repositories;

import tingeso_pep_1.entities.MarksEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarksRepository extends JpaRepository<MarksEntity, Long> {

}
