package tingeso_pep_1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tingeso_pep_1.DataTransferObjects.AverageTimeR3;
import tingeso_pep_1.DataTransferObjects.RepairTypeSummaryR2;
import tingeso_pep_1.DataTransferObjects.RepairTypeSummaryTypeMotorR4;
import tingeso_pep_1.DataTransferObjects.VehicleCostDetailsR1;
import tingeso_pep_1.services.ReportsService;

import java.util.List;

@RestController
@RequestMapping("/reportes")
@CrossOrigin("*")
public class ReportsController {

    @Autowired
    ReportsService reportsService;

    // R1
    @GetMapping("/costos-vehiculos")
    public ResponseEntity<List<VehicleCostDetailsR1>> getVehicleCosts() {
        return ResponseEntity.ok(reportsService.calculateCostFormulaForVehicles());
    }

    // R2
    @GetMapping("/reparaciones-resumen-tipo")
    public ResponseEntity<List<RepairTypeSummaryR2>> getVehicleCostsSummary() {
        return ResponseEntity.ok(reportsService.calculateVehicleCostsSummary());
    }

    // R3
    @GetMapping("/tiempo-promedio-reparaciones-marcas")
    public ResponseEntity<List<AverageTimeR3>> getAverageTimeByMark() {
        return ResponseEntity.ok(reportsService.calculateAverageTimeForVehiclesByMark());
    }

    // R4
    @GetMapping("/reparaciones-resumen-marcas")
    public ResponseEntity<List<RepairTypeSummaryTypeMotorR4>> getVehicleCostsByMark() {
        return ResponseEntity.ok(reportsService.calculateCostFormulaForVehiclesByMark());
    }
}
