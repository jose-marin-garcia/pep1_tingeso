package tingeso_pep_1.services;

import tingeso_pep_1.entities.MarksEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso_pep_1.repositories.MarksRepository;

import java.util.List;

@Service
public class MarkService {
    @Autowired
    MarksRepository marksRepository;
    public List<MarksEntity> getMarks() { return (List<MarksEntity>) marksRepository.findAll(); }

    public MarksEntity saveMark(MarksEntity mark) { return marksRepository.save(mark); }

    public List<MarksEntity> saveMarks(List<MarksEntity> marks) {
        return marksRepository.saveAll(marks);
    }

    public boolean deleteMark(Long id) throws Exception {
        try{
            marksRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }
}
