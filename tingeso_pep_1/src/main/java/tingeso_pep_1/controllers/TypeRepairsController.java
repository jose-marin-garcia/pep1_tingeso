package tingeso_pep_1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tingeso_pep_1.entities.TypeRepairsEntity;
import tingeso_pep_1.services.TypeRepairsService;

import java.util.List;

@RestController
@RequestMapping("/typerepairs")
@CrossOrigin("*")
public class TypeRepairsController {

    @Autowired
    TypeRepairsService typeRepairsService;

    @GetMapping("/")
    public ResponseEntity<List<TypeRepairsEntity>> listTypeRepairs() {
        List<TypeRepairsEntity> listTypeRepairs = typeRepairsService.listTypeRepairs();
        return ResponseEntity.ok(listTypeRepairs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeRepairsEntity> getTypeRepairByID(@PathVariable Long id) {
        TypeRepairsEntity typeRepair = typeRepairsService.getTypeRepairByID(id);
        return ResponseEntity.ok(typeRepair);
    }

    @PostMapping("/")
    public ResponseEntity<TypeRepairsEntity> saveTypeRepair(@RequestBody TypeRepairsEntity typeRepair) {
        TypeRepairsEntity typeRepairNew = typeRepairsService.saveTypeRepair(typeRepair);
        return ResponseEntity.ok(typeRepairNew);
    }

    @PostMapping("/list")
    public ResponseEntity<List<TypeRepairsEntity>> typeRepairsService(@RequestBody List<TypeRepairsEntity> typeRepair) {
        List<TypeRepairsEntity> typesRepairNew = typeRepairsService.saveTypesRepair(typeRepair);
        return ResponseEntity.ok(typesRepairNew);

    }
}