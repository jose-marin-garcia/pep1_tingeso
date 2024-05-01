package tingeso_pep_1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tingeso_pep_1.entities.HistoricEntity;
import tingeso_pep_1.entities.TypeRepairsEntity;

import java.util.List;

@Repository
public interface HistoricRepository extends JpaRepository<HistoricEntity, Long> {
    HistoricEntity findByPatent(String patent);

    List<HistoricEntity> findAllByEndhourIsNullAndEnddateIsNull();

    List<HistoricEntity> findAllByEndhourIsNotNullAndEnddateIsNotNullAndClientdateIsNullAndClienthourIsNull();

    List<HistoricEntity> findAllByEndhourIsNotNullAndEnddateIsNotNull();
}
