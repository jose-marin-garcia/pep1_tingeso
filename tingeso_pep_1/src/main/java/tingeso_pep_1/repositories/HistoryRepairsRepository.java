package tingeso_pep_1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso_pep_1.entities.HistoryRepairsEntity;

import java.util.List;

@Repository
public interface HistoryRepairsRepository extends JpaRepository<HistoryRepairsEntity, Long> {

    List<HistoryRepairsEntity> findByIdReparacion(Long id);
}
