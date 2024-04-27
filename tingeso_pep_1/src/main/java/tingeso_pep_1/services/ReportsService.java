package tingeso_pep_1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso_pep_1.DataTransferObjects.RepairTypeSummaryR2;
import tingeso_pep_1.DataTransferObjects.VehicleCostDetailsR1;
import tingeso_pep_1.entities.HistoricEntity;
import tingeso_pep_1.entities.HistoryRepairsEntity;
import tingeso_pep_1.entities.TypeRepairsEntity;
import tingeso_pep_1.entities.VehicleEntity;
import tingeso_pep_1.repositories.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ReportsService {

    @Autowired
    HistoricRepository historicRepository;

    @Autowired
    TypeRepairsRepository typeRepairsRepository;

    @Autowired
    HistoryRepairsRepository historyRepairsRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    PricesRepository pricesRepository;
    
    public List<VehicleCostDetailsR1>  calculateCostFormulaForVehicles() {
        List<HistoricEntity> historiales = historicRepository.findAll();
        List<VehicleCostDetailsR1> vehicleCostDetailsR1List = new ArrayList<>();

        for (HistoricEntity historic : historiales) {

            VehicleCostDetailsR1 details = new VehicleCostDetailsR1();

            details.setPatent(historic.getPatent());
            details.setMontoTotal(historic.getMount());
            details.setSumaReparaciones(historic.getSumaReparaciones());
            details.setDescuentos(historic.getDescuentos());
            details.setRecargos(historic.getRecargos());
            details.setIva(historic.getIva());

            vehicleCostDetailsR1List.add(details);
        }
        return vehicleCostDetailsR1List;
    }

    public List<RepairTypeSummaryR2> calculateVehicleCostsSummary() {
        List<TypeRepairsEntity> reparaciones = typeRepairsRepository.findAll();
        List<RepairTypeSummaryR2> repairTypeSummaryR2List = new ArrayList<>();

        for (TypeRepairsEntity reparacion : reparaciones) {
            RepairTypeSummaryR2 details = new RepairTypeSummaryR2();
            details.setTypeRepairName(reparacion.getRepairName());

            List<HistoryRepairsEntity> repairs = historyRepairsRepository.findByIdReparacion(reparacion.getId());
            for (HistoryRepairsEntity repair : repairs) {
                HistoricEntity historic = historicRepository.findById(repair.getIdHistorial()).get();
                VehicleEntity vehicle = vehicleRepository.findByPatent(historic.getPatent());
                String type = vehicle.getType();
                int cost = pricesRepository.findPriceByIdtyperepairAndMotortype(repair.getIdReparacion(), vehicle.getTypemotor());

                switch (type) {
                    case "Sedan":
                        details.setSedanCount(details.getSedanCount() + 1);
                        break;
                    case "Hatchback":
                        details.setHatchbackCount(details.getHatchbackCount() + 1);
                        break;
                    case "SUV":
                        details.setSuvCount(details.getSuvCount() + 1);
                        break;
                    case "Pickup":
                        details.setPickupCount(details.getPickupCount() + 1);
                        break;
                    case "Furgoneta":
                        details.setFurgonetaCount(details.getFurgonetaCount() + 1);
                        break;
                }
                details.setTotalCost(details.getTotalCost() + cost);
            }

            repairTypeSummaryR2List.add(details);
        }

        repairTypeSummaryR2List.sort(Comparator.comparing(RepairTypeSummaryR2::getTotalCost).reversed());
        return repairTypeSummaryR2List;
    }

}