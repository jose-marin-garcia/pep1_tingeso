package tingeso_pep_1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tingeso_pep_1.DataTransferObjects.RegisterDto;
import tingeso_pep_1.DataTransferObjects.VehicleDto;
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

    @GetMapping("/bonds/{idMark}")
    public ResponseEntity<BondEntity> getBond(@PathVariable Long idMark){
        BondEntity discount = registerService.getBond(idMark);
        if (discount != null) {
            return ResponseEntity.ok(discount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/bonds/{idMark}/{amount}")
    public ResponseEntity<BondEntity> addBond(@PathVariable Long idMark, @PathVariable int amount){
        BondEntity discount = registerService.addBond(idMark, amount);
        return ResponseEntity.ok(discount);
    }

    @PostMapping("/")
    public ResponseEntity<VehicleEntity> addRegister(@RequestBody RegisterDto vehicleDto) {
        System.out.println(vehicleDto);
        VehicleEntity vehicleNew = registerService.saveVehicle(vehicleDto.getVehicle(), vehicleDto.getReparations(), vehicleDto.getIdBond());
        return ResponseEntity.ok(vehicleNew);
    }

    @GetMapping("/vehicles-not-finished/")
    public ResponseEntity<List<VehicleDto>> getVehiclesNotFinished(){
        List<VehicleDto> vehicles = registerService.getVehiclesNotFinished();
        return ResponseEntity.ok(vehicles);
    }

    @PutMapping("/finish/{patent}")
    public ResponseEntity<VehicleEntity> finishVehicle(@PathVariable String patent){
        VehicleEntity vehicleNew = registerService.finishVehicle(patent);
        return ResponseEntity.ok(vehicleNew);
    }

    @GetMapping("/vehicles-not-removed/")
    public ResponseEntity<List<VehicleDto>> getVehiclesNotRemoved(){
        List<VehicleDto> vehicles = registerService.getVehiclesNotRemoved();
        return ResponseEntity.ok(vehicles);
    }

    @PutMapping("/remove/{patent}")
    public ResponseEntity<VehicleEntity> removeVehicle(@PathVariable String patent){
        VehicleEntity vehicleNew = registerService.removeVehicle(patent);
        return ResponseEntity.ok(vehicleNew);
    }

}
