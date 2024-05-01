package tingeso_pep_1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso_pep_1.DataTransferObjects.AverageTimeR3;
import tingeso_pep_1.DataTransferObjects.RepairTypeSummaryR2;
import tingeso_pep_1.DataTransferObjects.RepairTypeSummaryTypeMotorR4;
import tingeso_pep_1.DataTransferObjects.VehicleCostDetailsR1;
import tingeso_pep_1.entities.*;
import tingeso_pep_1.repositories.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    @Autowired
    MarksRepository markRepository;
    
    public List<VehicleCostDetailsR1>  calculateCostFormulaForVehicles() {
        List<HistoricEntity> historiales = historicRepository.findAll();
        List<VehicleCostDetailsR1> vehicleCostDetailsR1List = new ArrayList<>();

        for (HistoricEntity historic : historiales) {

            VehicleCostDetailsR1 details = new VehicleCostDetailsR1();

            details.setPatent(historic.getPatent());
            details.setMarca(markRepository.findById((vehicleRepository.findByPatent(historic.getPatent()).getMark())).get().getMarkName());
            details.setModelo(vehicleRepository.findByPatent(historic.getPatent()).getModel());
            details.setTipoVehiculo(vehicleRepository.findByPatent(historic.getPatent()).getType());
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

    public List<AverageTimeR3> calculateAverageTimeForVehiclesByMark(){
        List<MarksEntity> marcas = markRepository.findAll();
        List<AverageTimeR3> averageTimeR3List = new ArrayList<>();
        // Historiales con reparaciones terminadas para poder calcular el tiempo promedio
        List<HistoricEntity> historicFinished = historicRepository.findAllByEndhourIsNotNullAndEnddateIsNotNull();
        for (MarksEntity marca : marcas){
            System.out.println(marca.getMarkName());
            AverageTimeR3 details = new AverageTimeR3();
            details.setMark(marca.getMarkName());
            List<VehicleEntity> vehiclesByMark = vehicleRepository.findByMark(marca.getId());
            double average = 0;
            int numVehicles = 1;
            for (VehicleEntity vehicle : vehiclesByMark){
                System.out.println(vehicle);
                HistoricEntity historial = historicRepository.findByPatent(vehicle.getPatent());
                System.out.println(historial);
                if(historial.getEnddate() != null && historial.getEndhour() != null){
                    String admissionDate = historial.getAdmissiondate();
                    String admissionHour = historial.getAdmissionhour();
                    String endDate = historial.getEnddate();
                    String endHour = historial.getEndhour();
                    long duration = calculateDurationInSeconds(admissionHour, admissionDate, endHour, endDate);
                    average = (average + duration) / numVehicles;
                    numVehicles += 1;
                }
            }
            String averageToHMS = secondsToHHMMSS(average);
            details.setAverageTime(averageToHMS);
            averageTimeR3List.add(details);
        }
        averageTimeR3List.sort(Comparator.comparing(AverageTimeR3::getAverageTime).reversed());
        return averageTimeR3List;
    }

    public long calculateDurationInSeconds(String startHour, String startDate, String endHour, String endDate) {
        long startSeconds = convertToSeconds(startDate, startHour);
        long endSeconds = convertToSeconds(endDate, endHour);
        return endSeconds - startSeconds;  // Duración en segundos
    }

    public long convertToSeconds(String dma, String hms) {
        // Asumiendo que la fecha está en formato "dd/MM/yyyy"
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(dma, dateFormatter);

        // Asumiendo que la hora está en formato "HH:mm:ss"
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.parse(hms, timeFormatter);

        // Combinar fecha y hora en un objeto LocalDateTime
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        // Convertir LocalDateTime a ZonedDateTime usando la zona horaria del sistema
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());

        // Convertir ZonedDateTime a segundos desde la época Unix (1 de enero de 1970)
        return zonedDateTime.toEpochSecond();
    }

    public String secondsToHHMMSS(double seconds) {
        int hours = (int) seconds / 3600;
        int remainder = (int) seconds - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        return String.format("%d:%02d:%02d", hours, mins, secs);
    }

    public List<RepairTypeSummaryTypeMotorR4> calculateCostFormulaForVehiclesByMark() {
        List<TypeRepairsEntity> reparaciones = typeRepairsRepository.findAll();
        List<RepairTypeSummaryTypeMotorR4> repairTypeSummaryTypeMotorR4List = new ArrayList<>();

        for (TypeRepairsEntity reparacion : reparaciones) {
            RepairTypeSummaryTypeMotorR4 details = new RepairTypeSummaryTypeMotorR4();
            details.setTypeRepairName(reparacion.getRepairName());

            List<HistoryRepairsEntity> repairs = historyRepairsRepository.findByIdReparacion(reparacion.getId());
            for (HistoryRepairsEntity repair : repairs) {
                HistoricEntity historic = historicRepository.findById(repair.getIdHistorial()).get();
                VehicleEntity vehicle = vehicleRepository.findByPatent(historic.getPatent());
                String typeMotor = vehicle.getTypemotor();
                int cost = pricesRepository.findPriceByIdtyperepairAndMotortype(repair.getIdReparacion(), vehicle.getTypemotor());

                switch (typeMotor) {
                    case "Gasolina":
                        details.setGasolineCount(details.getGasolineCount() + 1);
                        break;
                    case "Diesel":
                        details.setDieselCount(details.getDieselCount() + 1);
                        break;
                    case "Híbrido":
                        details.setHybridCount(details.getHybridCount() + 1);
                        break;
                    case "Eléctrico":
                        details.setElectricCount(details.getElectricCount() + 1);
                        break;
                }
                details.setTotalCost(details.getTotalCost() + cost);
            }

            repairTypeSummaryTypeMotorR4List.add(details);
        }

        repairTypeSummaryTypeMotorR4List.sort(Comparator.comparing(RepairTypeSummaryTypeMotorR4::getTotalCost).reversed());
        return repairTypeSummaryTypeMotorR4List;
    }
}
