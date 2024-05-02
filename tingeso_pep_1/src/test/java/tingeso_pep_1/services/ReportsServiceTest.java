package tingeso_pep_1.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import tingeso_pep_1.DataTransferObjects.RepairTypeSummaryTypeMotorR4;
import tingeso_pep_1.DataTransferObjects.VehicleCostDetailsR1;
import tingeso_pep_1.DataTransferObjects.RepairTypeSummaryR2;
import tingeso_pep_1.entities.*;
import tingeso_pep_1.repositories.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
class ReportsServiceTest {

    @MockBean
    private HistoricRepository historicRepository;

    @MockBean
    private HistoryRepairsRepository historyRepairsRepository;

    @MockBean
    private PricesRepository pricesRepository;

    @MockBean
    private TypeRepairsRepository typeRepairsRepository;

    @MockBean
    private VehicleRepository vehicleRepository;

    @MockBean
    private MarksRepository markRepository;

    @Autowired
    private ReportsService reportsService;

    @Test
    void calculateCostFormulaForVehicles_ShouldReturnVehicleCostDetails() {
        // Configura los mocks
        List<HistoricEntity> historials = List.of(
                new HistoricEntity(1L, "ABC123", "01/01/2020", "10:00", 15000, 12000, 1000, 2000, 2850, null, null, null, null),
                new HistoricEntity(2L, "XYZ789", "01/02/2020", "11:00", 20000, 17000, 1500, 2500, 3800, null, null, null, null)
        );

        VehicleEntity vehicle1 = new VehicleEntity(1L, "ABC123", 1L, "Model A", "Sedan", 2020, "Gasolina", 5, 50000);
        VehicleEntity vehicle2 = new VehicleEntity(2L, "XYZ789", 2L, "Model B", "Hatchback", 2021, "Diesel", 4, 30000);

        when(historicRepository.findAll()).thenReturn(historials);
        when(vehicleRepository.findByPatent("ABC123")).thenReturn(vehicle1);
        when(vehicleRepository.findByPatent("XYZ789")).thenReturn(vehicle2);
        when(markRepository.findById(1L)).thenReturn(Optional.of(new MarksEntity(1L, "Toyota")));
        when(markRepository.findById(2L)).thenReturn(Optional.of(new MarksEntity(2L, "Nissan")));

        // Ejecutar el método bajo prueba
        List<VehicleCostDetailsR1> result = reportsService.calculateCostFormulaForVehicles();

        // Verificar resultados
        assertNotNull(result);
        assertEquals(2, result.size(), "El tamaño de la lista resultante debe ser 2.");
        assertEquals("ABC123", result.get(0).getPatent());
        assertEquals("Toyota", result.get(0).getMarca());
        assertEquals("XYZ789", result.get(1).getPatent());
        assertEquals("Nissan", result.get(1).getMarca());
    }

    @Test
    void calculateCostFormulaForVehicles_WithNoHistoricData_ShouldReturnEmptyList() {
        when(historicRepository.findAll()).thenReturn(emptyList());
        List<VehicleCostDetailsR1> result = reportsService.calculateCostFormulaForVehicles();
        assertEquals(0, result.size());
    }

    @Test
    void calculateVehicleCostsSummary_ShouldReturnCorrectSummary() {
        // Preparar datos de prueba
        List<TypeRepairsEntity> reparaciones = List.of(
                new TypeRepairsEntity(1L, "Cambio de aceite")
        );
        when(typeRepairsRepository.findAll()).thenReturn(reparaciones);

        HistoryRepairsEntity historyRepairs1 = new HistoryRepairsEntity(1L, 1L, 1L);
        HistoryRepairsEntity historyRepairs2 = new HistoryRepairsEntity(2L, 2L, 1L);
        HistoryRepairsEntity historyRepairs3 = new HistoryRepairsEntity(3L, 3L, 1L);
        HistoryRepairsEntity historyRepairs4 = new HistoryRepairsEntity(4L, 4L, 1L);
        HistoryRepairsEntity historyRepairs5 = new HistoryRepairsEntity(5L, 5L, 1L);
        when(historyRepairsRepository.findByIdReparacion(1L)).thenReturn(List.of(historyRepairs1, historyRepairs2, historyRepairs3, historyRepairs4, historyRepairs5));

        // Configuración de vehículos con diferentes tipos de motor
        VehicleEntity vehicleSedan = new VehicleEntity(1L, "ABC123", 1L, "Modelo X", "Sedan", 2020, "Gasolina", 5, 50000);
        VehicleEntity vehicleHatchback = new VehicleEntity(2L, "XYZ789", 1L, "Modelo Y", "Hatchback", 2021, "Gasolina", 5, 60000);
        VehicleEntity vehicleSUV = new VehicleEntity(3L, "DEF456", 1L, "Modelo Z", "SUV", 2022, "Gasolina", 5, 70000);
        VehicleEntity vehiclePickup = new VehicleEntity(4L, "GHI012", 1L, "Modelo W", "Pickup", 2023, "Gasolina", 5, 80000);
        VehicleEntity vehicleFurgoneta = new VehicleEntity(5L, "JJSU03", 1L, "Modelo W", "Furgoneta", 2023, "Gasolina", 5, 80000);

        when(vehicleRepository.findByPatent("ABC123")).thenReturn(vehicleSedan);
        when(vehicleRepository.findByPatent("XYZ789")).thenReturn(vehicleHatchback);
        when(vehicleRepository.findByPatent("DEF456")).thenReturn(vehicleSUV);
        when(vehicleRepository.findByPatent("GHI012")).thenReturn(vehiclePickup);
        when(vehicleRepository.findByPatent("JJSU03")).thenReturn(vehicleFurgoneta);

        when(pricesRepository.findPriceByIdtyperepairAndMotortype(1L, "Gasolina")).thenReturn(100);

        HistoricEntity historic1 = new HistoricEntity(1L, "ABC123", null, null, 0, 0, 0, 0, 0, null, null, null, null);
        HistoricEntity historic2 = new HistoricEntity(2L, "XYZ789", null, null, 0, 0, 0, 0, 0, null, null, null, null);
        HistoricEntity historic3 = new HistoricEntity(3L, "DEF456", null, null, 0, 0, 0, 0, 0, null, null, null, null);
        HistoricEntity historic4 = new HistoricEntity(4L, "GHI012", null, null, 0, 0, 0, 0, 0, null, null, null, null);
        HistoricEntity historic5 = new HistoricEntity(5L, "JJSU03", null, null, 0, 0, 0, 0, 0, null, null, null, null);

        when(historicRepository.findById(1L)).thenReturn(Optional.of(historic1));
        when(historicRepository.findById(2L)).thenReturn(Optional.of(historic2));
        when(historicRepository.findById(3L)).thenReturn(Optional.of(historic3));
        when(historicRepository.findById(4L)).thenReturn(Optional.of(historic4));
        when(historicRepository.findById(5L)).thenReturn(Optional.of(historic5));

        // Ejecutar el método bajo prueba
        List<RepairTypeSummaryR2> result = reportsService.calculateVehicleCostsSummary();

        // Verificar los resultados
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cambio de aceite", result.get(0).getTypeRepairName());
        assertEquals(1, result.get(0).getSedanCount());
        assertEquals(1, result.get(0).getHatchbackCount());
        assertEquals(1, result.get(0).getSuvCount());
        assertEquals(1, result.get(0).getPickupCount());
        assertEquals(1, result.get(0).getFurgonetaCount());
    }

    @Test
    void testCalculateDurationInSeconds() {
        // Caso de prueba con fechas y horas válidas
        String startHour = "09:00";
        String startDate = "01/01/2020";
        String endHour = "10:00";
        String endDate = "01/01/2020";
        long expectedDuration = 3600L; // 1 hora
        assertEquals(expectedDuration, reportsService.calculateDurationInSeconds(startHour, startDate, endHour, endDate));
    }

    @Test
    void testCalculateDurationWithDifferentDays() {
        // Extender el test a diferentes días
        String startHour = "23:00";
        String startDate = "01/01/2020";
        String endHour = "01:00";
        String endDate = "02/01/2020";
        long expectedDuration = 7200L; // 2 horas
        assertEquals(expectedDuration, reportsService.calculateDurationInSeconds(startHour, startDate, endHour, endDate));
    }

    @Test
    void testConvertToSecondsWithValidDateAndTime() {
        // Datos válidos
        String date = "01/01/2020";
        String time = "12:00";
        long result = reportsService.convertToSeconds(date, time);
        assertTrue(result > 0);
    }

    @Test
    void testConvertToSecondsWithEmptyArguments() {
        // Argumentos vacíos
        assertThrows(IllegalArgumentException.class, () -> {
            reportsService.convertToSeconds("", "");
        });
    }

    @Test
    void testSecondsToHHMMSS() {
        assertEquals("1:00:00", reportsService.secondsToHHMMSS(3600));
        assertEquals("2:30:00", reportsService.secondsToHHMMSS(9000));
        assertEquals("0:01:40", reportsService.secondsToHHMMSS(100));
    }

    @Test
    void testCalculateCostFormulaForVehicleGasolinasByMarkSingleInstance() {
        // Preparar datos de prueba
        TypeRepairsEntity reparacion = new TypeRepairsEntity(1L, "Cambio de aceite");
        List<TypeRepairsEntity> reparaciones = List.of(reparacion);
        when(typeRepairsRepository.findAll()).thenReturn(reparaciones);

        HistoryRepairsEntity history = new HistoryRepairsEntity(1L, 1L, 1L);
        when(historyRepairsRepository.findByIdReparacion(1L)).thenReturn(List.of(history));

        HistoricEntity historic = new HistoricEntity(1L, "ABC123", null, null, 0, 0, 0, 0, 0, null, null, null, null);
        when(historicRepository.findById(1L)).thenReturn(Optional.of(historic));

        VehicleEntity vehicle = new VehicleEntity(1L, "ABC123", 1L, "Modelo X", "SUV", 2020, "Gasolina", 5, 50000);
        when(vehicleRepository.findByPatent("ABC123")).thenReturn(vehicle);

        when(pricesRepository.findPriceByIdtyperepairAndMotortype(1L, "Gasolina")).thenReturn(100);

        // Ejecutar el método bajo prueba
        List<RepairTypeSummaryTypeMotorR4> result = reportsService.calculateCostFormulaForVehiclesByMark();

        // Verificar los resultados
        assertNotNull(result);
        assertEquals(1, result.size());

        RepairTypeSummaryTypeMotorR4 summary = result.get(0);
        assertEquals("Cambio de aceite", summary.getTypeRepairName());
        assertEquals(1, summary.getGasolineCount());
        assertEquals(100, summary.getTotalCost());
    }

    /*
    @Test
    void calculateAverageTimeForVehiclesByMarkTest() {
        // Data setup
        MarksEntity toyota = new MarksEntity(1L, "Toyota");
        MarksEntity nissan = new MarksEntity(2L, "Nissan");
        when(markRepository.findAll()).thenReturn(Arrays.asList(toyota, nissan));

        HistoricEntity toyotaHistoric = new HistoricEntity(1L, "ABC123", "01/01/2020", "10:00", 0, 0, 0, 0, 0, "02/01/2020", "11:00", null, null);
        HistoricEntity nissanHistoric = new HistoricEntity(2L, "XYZ789", "01/01/2020", "09:00", 0, 0, 0, 0, 0, "02/01/2020", "10:00", null, null);
        when(historicRepository.findAllByEndhourIsNotNullAndEnddateIsNotNull()).thenReturn(Arrays.asList(toyotaHistoric, nissanHistoric));

        VehicleEntity toyotaVehicle = new VehicleEntity(1L, "ABC123", 1L, "Camry", "Sedan", 2020, "Gasolina", 5, 50000);
        VehicleEntity nissanVehicle = new VehicleEntity(2L, "XYZ789", 2L, "Altima", "Sedan", 2020, "Gasolina", 5, 30000);
        when(vehicleRepository.findByMark(1L)).thenReturn(Arrays.asList(toyotaVehicle));
        when(vehicleRepository.findByMark(2L)).thenReturn(Arrays.asList(nissanVehicle));

        when(historicRepository.findByPatent("ABC123")).thenReturn(toyotaHistoric);
        when(historicRepository.findByPatent("XYZ789")).thenReturn(nissanHistoric);

        // Service
        ReportsService service = new ReportsService(markRepository, historicRepository, vehicleRepository);

        // Test
        List<AverageTimeR3> result = service.calculateAverageTimeForVehiclesByMark();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Toyota", result.get(0).getMark());
        assertEquals("24:00:00", result.get(0).getAverageTime());
        assertEquals("Nissan", result.get(1).getMark());
        assertEquals("24:00:00", result.get(1).getAverageTime());
    }
    */

    @Test
    void testCalculateCostFormulaForVehiclesByMarkMultipleMotorTypes() {
        // Preparar datos de prueba
        TypeRepairsEntity reparacion = new TypeRepairsEntity(1L, "Cambio de aceite");
        List<TypeRepairsEntity> reparaciones = List.of(reparacion);
        when(typeRepairsRepository.findAll()).thenReturn(reparaciones);

        // Historial y reparaciones
        HistoryRepairsEntity history1 = new HistoryRepairsEntity(1L, 1L, 1L);
        HistoryRepairsEntity history2 = new HistoryRepairsEntity(2L, 2L, 1L);
        HistoryRepairsEntity history3 = new HistoryRepairsEntity(3L, 3L, 1L);
        HistoryRepairsEntity history4 = new HistoryRepairsEntity(4L, 4L, 1L);
        when(historyRepairsRepository.findByIdReparacion(1L)).thenReturn(List.of(history1, history2, history3, history4));

        // Configuración de vehículos con diferentes tipos de motor
        VehicleEntity vehicleGasolina = new VehicleEntity(1L, "ABC123", 1L, "Modelo X", "SUV", 2020, "Gasolina", 5, 50000);
        VehicleEntity vehicleDiesel = new VehicleEntity(2L, "XYZ789", 1L, "Modelo Y", "SUV", 2021, "Diesel", 5, 60000);
        VehicleEntity vehicleHibrido = new VehicleEntity(3L, "DEF456", 1L, "Modelo Z", "SUV", 2022, "Híbrido", 5, 70000);
        VehicleEntity vehicleElectrico = new VehicleEntity(4L, "GHI012", 1L, "Modelo W", "SUV", 2023, "Eléctrico", 5, 80000);

        // Uso de Optional para evitar NoSuchElementException
        when(historicRepository.findById(1L)).thenReturn(Optional.of(new HistoricEntity(1L, "ABC123", null, null, 0, 0, 0, 0, 0, null, null, null, null)));
        when(historicRepository.findById(2L)).thenReturn(Optional.of(new HistoricEntity(2L, "XYZ789", null, null, 0, 0, 0, 0, 0, null, null, null, null)));
        when(historicRepository.findById(3L)).thenReturn(Optional.of(new HistoricEntity(3L, "DEF456", null, null, 0, 0, 0, 0, 0, null, null, null, null)));
        when(historicRepository.findById(4L)).thenReturn(Optional.of(new HistoricEntity(4L, "GHI012", null, null, 0, 0, 0, 0, 0, null, null, null, null)));

        when(vehicleRepository.findByPatent("ABC123")).thenReturn(vehicleGasolina);
        when(vehicleRepository.findByPatent("XYZ789")).thenReturn(vehicleDiesel);
        when(vehicleRepository.findByPatent("DEF456")).thenReturn(vehicleHibrido);
        when(vehicleRepository.findByPatent("GHI012")).thenReturn(vehicleElectrico);

        // Precios por tipo de motor
        when(pricesRepository.findPriceByIdtyperepairAndMotortype(1L, "Gasolina")).thenReturn(100);
        when(pricesRepository.findPriceByIdtyperepairAndMotortype(1L, "Diesel")).thenReturn(150);
        when(pricesRepository.findPriceByIdtyperepairAndMotortype(1L, "Híbrido")).thenReturn(200);
        when(pricesRepository.findPriceByIdtyperepairAndMotortype(1L, "Eléctrico")).thenReturn(250);

        // Ejecutar el método bajo prueba
        List<RepairTypeSummaryTypeMotorR4> result = reportsService.calculateCostFormulaForVehiclesByMark();

        // Verificar los resultados
        assertNotNull(result);
        assertEquals(1, result.size());

        RepairTypeSummaryTypeMotorR4 summary = result.get(0);
        assertEquals("Cambio de aceite", summary.getTypeRepairName());
        assertEquals(1, summary.getGasolineCount());
        assertEquals(1, summary.getDieselCount());
        assertEquals(1, summary.getHybridCount());
        assertEquals(1, summary.getElectricCount());
        assertEquals(700, summary.getTotalCost());  // Suma de todos los costos
    }

}

