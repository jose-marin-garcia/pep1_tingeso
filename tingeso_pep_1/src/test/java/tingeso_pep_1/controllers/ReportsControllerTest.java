package tingeso_pep_1.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tingeso_pep_1.DataTransferObjects.AverageTimeR3;
import tingeso_pep_1.DataTransferObjects.RepairTypeSummaryTypeMotorR4;
import tingeso_pep_1.DataTransferObjects.VehicleCostDetailsR1;
import tingeso_pep_1.DataTransferObjects.RepairTypeSummaryR2;
import tingeso_pep_1.services.ReportsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.is;

import java.util.List;

@WebMvcTest(ReportsController.class)
public class ReportsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportsService reportsService;

    @Test
    public void getVehicleCosts_ShouldReturnVehicleCosts() throws Exception {
        VehicleCostDetailsR1 vehicleCost = new VehicleCostDetailsR1(
                "ABC123",      // patent
                "Toyota",      // marca
                "Corolla",     // modelo
                "Sedán",       // tipoVehiculo
                10000,         // montoTotal
                5,             // sumaReparaciones
                500,           // descuentos
                300,           // recargos
                1900           // iva
        );
        List<VehicleCostDetailsR1> vehicleCosts = List.of(vehicleCost);

        given(reportsService.calculateCostFormulaForVehicles()).willReturn(vehicleCosts);

        mockMvc.perform(get("/reportes/costos-vehiculos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].patent", is("ABC123")));
    }

    @Test
    public void getVehicleCostsSummary_ShouldReturnRepairTypeSummary() throws Exception {
        RepairTypeSummaryR2 repairSummary = new RepairTypeSummaryR2(
                "Mantenimiento General", // typeRepairName
                10,                      // sedanCount
                5,                       // hatchbackCount
                3,                       // suvCount
                2,                       // pickupCount
                1,                       // furgonetaCount
                21000                    // totalCost
        );
        List<RepairTypeSummaryR2> summaries = List.of(repairSummary);

        given(reportsService.calculateVehicleCostsSummary()).willReturn(summaries);

        mockMvc.perform(get("/reportes/reparaciones-resumen-tipo"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].typeRepairName", is("Mantenimiento General")))
                .andExpect(jsonPath("$[0].sedanCount", is(10)))
                .andExpect(jsonPath("$[0].hatchbackCount", is(5)));
    }

    @Test
    public void getAverageTimeByMark_ShouldReturnAverageTime() throws Exception {
        AverageTimeR3 averageTimeToyota = new AverageTimeR3("Toyota", "2:30:02");
        AverageTimeR3 averageTimeFord = new AverageTimeR3("Ford", "3:00:00");
        List<AverageTimeR3> averageTimes = List.of(averageTimeToyota, averageTimeFord);

        given(reportsService.calculateAverageTimeForVehiclesByMark()).willReturn(averageTimes);

        mockMvc.perform(get("/reportes/tiempo-promedio-reparaciones-marcas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].mark", is("Toyota")))
                .andExpect(jsonPath("$[0].averageTime", is("2:30:02")))
                .andExpect(jsonPath("$[1].mark", is("Ford")))
                .andExpect(jsonPath("$[1].averageTime", is("3:00:00")));
    }

    @Test
    public void getVehicleCostsByMark_ShouldReturnCostsSummary() throws Exception {
        RepairTypeSummaryTypeMotorR4 repairSummaryGasoline = new RepairTypeSummaryTypeMotorR4(
                "Cambio de aceite", // typeRepairName
                15,                // gasolineCount
                5,                 // dieselCount
                2,                 // hybridCount
                1,                 // electricCount
                23000              // totalCost
        );

        RepairTypeSummaryTypeMotorR4 repairSummaryDiesel = new RepairTypeSummaryTypeMotorR4(
                "Revisión de motor", // typeRepairName
                10,                  // gasolineCount
                10,                  // dieselCount
                3,                   // hybridCount
                0,                   // electricCount
                20000                // totalCost
        );

        List<RepairTypeSummaryTypeMotorR4> repairSummaries = List.of(repairSummaryGasoline, repairSummaryDiesel);

        given(reportsService.calculateCostFormulaForVehiclesByMark()).willReturn(repairSummaries);

        mockMvc.perform(get("/reportes/reparaciones-resumen-marcas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].typeRepairName", is("Cambio de aceite")))
                .andExpect(jsonPath("$[0].gasolineCount", is(15)))
                .andExpect(jsonPath("$[0].dieselCount", is(5)))
                .andExpect(jsonPath("$[0].hybridCount", is(2)))
                .andExpect(jsonPath("$[0].electricCount", is(1)))
                .andExpect(jsonPath("$[0].totalCost", is(23000)))
                .andExpect(jsonPath("$[1].typeRepairName", is("Revisión de motor")))
                .andExpect(jsonPath("$[1].gasolineCount", is(10)))
                .andExpect(jsonPath("$[1].dieselCount", is(10)))
                .andExpect(jsonPath("$[1].hybridCount", is(3)))
                .andExpect(jsonPath("$[1].electricCount", is(0)))
                .andExpect(jsonPath("$[1].totalCost", is(20000)));
    }


}
