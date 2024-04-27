package tingeso_pep_1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso_pep_1.entities.TypeRepairsEntity;
import tingeso_pep_1.repositories.TypeRepairsRepository;

import java.util.List;

@Service
public class TypeRepairsService {

    @Autowired
    TypeRepairsRepository typeRepairsRepository;

    public List<TypeRepairsEntity> listTypeRepairs() { return typeRepairsRepository.findAll(); }
    public TypeRepairsEntity getTypeRepairByID(Long id) { return typeRepairsRepository.findById(id).get(); }

    public TypeRepairsEntity saveTypeRepair(TypeRepairsEntity typeRepair) { return typeRepairsRepository.save(typeRepair); }

    public List<TypeRepairsEntity> saveTypesRepair(List<TypeRepairsEntity> typeRepair) { return typeRepairsRepository.saveAll(typeRepair);  }

}
