package tingeso_pep_1.controllers;

import tingeso_pep_1.entities.MarksEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tingeso_pep_1.services.MarkService;

import java.util.List;

@RestController
@RequestMapping("/marks")
@CrossOrigin("*")
public class MarksController {
    @Autowired
    MarkService markService;

    @GetMapping("/")
    public ResponseEntity<List<MarksEntity>> listMarks() {
        List<MarksEntity> marks = markService.getMarks();
        return ResponseEntity.ok(marks);
    }

    @PostMapping("/")
    public ResponseEntity<MarksEntity> saveMark(@RequestBody MarksEntity mark) {
        MarksEntity markNew = markService.saveMark(mark);
        return ResponseEntity.ok(markNew);
    }

    @PostMapping("/list")
    public ResponseEntity<List<MarksEntity>> saveMarks(@RequestBody List<MarksEntity> marks) {
        List<MarksEntity> savedMarks = markService.saveMarks(marks);
        return ResponseEntity.ok(savedMarks);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMark(@PathVariable Long id) throws Exception {
        var isDeleted = markService.deleteMark(id);
        return ResponseEntity.noContent().build();
    }
}
