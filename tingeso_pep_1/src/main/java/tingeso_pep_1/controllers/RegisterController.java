package tingeso_pep_1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tingeso_pep_1.DataTransferObjects.RegisterDto;
import tingeso_pep_1.entities.BondEntity;
import tingeso_pep_1.entities.VehicleEntity;
import tingeso_pep_1.services.RegisterService;


import java.util.List;

@RestController
@RequestMapping("/register")
@CrossOrigin("*")
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @GetMapping("/{id_mark}")
    public ResponseEntity<BondEntity> getBond(@PathVariable Long id_mark){
        BondEntity discount = registerService.getBond(id_mark);
        return ResponseEntity.ok(discount);
    }

    @PostMapping("/")
    public ResponseEntity<VehicleEntity> addRegister(@RequestBody RegisterDto vehicleDto) {
        VehicleEntity vehicleNew = registerService.saveVehicle(vehicleDto.getVehicle(), vehicleDto.getReparations());
        return ResponseEntity.ok(vehicleNew);
    }


    @PutMapping("/")
    public ResponseEntity<VehicleEntity> updateVehicle(@RequestBody VehicleEntity vehiculo, @RequestParam List<Long> reparaciones){
        VehicleEntity vehicleNew = registerService.saveVehicle(vehiculo, reparaciones);
        return ResponseEntity.ok(vehicleNew);
    }

}
