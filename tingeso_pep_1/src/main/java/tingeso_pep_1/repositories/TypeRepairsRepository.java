package tingeso_pep_1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso_pep_1.entities.TypeRepairsEntity;

import java.util.Optional;

@Repository
public interface TypeRepairsRepository extends JpaRepository<TypeRepairsEntity, Long> {
}
