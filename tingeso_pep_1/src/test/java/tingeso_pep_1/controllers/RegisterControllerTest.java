package tingeso_pep_1.controllers;

import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.MatcherAssert.assertThat;
import tingeso_pep_1.DataTransferObjects.RegisterDto;
import tingeso_pep_1.DataTransferObjects.VehicleDto;
import tingeso_pep_1.entities.BondEntity;
import tingeso_pep_1.entities.MarksEntity;
import tingeso_pep_1.entities.VehicleEntity;
import tingeso_pep_1.repositories.BondRepository;
import tingeso_pep_1.services.RegisterService;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BondRepository bondRepository;

    @MockBean
    private RegisterService registerService;

    /*
    @Test
    public void saveRegister_ShouldReturnSavedVehicleWithDiscountApplied() throws Exception {
        VehicleEntity vehicleNew = new VehicleEntity(1L,
                "ABCD34",
                1L,
                "Yaris",
                "Sedan",
                2010,
                "Gasolina",
                5,
                10000);
        List<Long> reparations = Arrays.asList(1L, 2L, 3L);

        BondEntity bond = new BondEntity(1L, 1L, 1000);

        RegisterDto vehicleDto = new RegisterDto(vehicleNew, reparations, 1L);

        given(registerService.saveVehicle(vehicleDto.getVehicle(),vehicleDto.getReparations(),vehicleDto.getIdBond())).willReturn(vehicleNew);

        String vehicleDtoJson = """
                {
                    "vehicle": {
                        "id": 1,
                        "patent": "ABCD34",
                        "id_mark": 1,
                        "model": "Yaris",
                        "type": "Sedan",
                        "year": 2010,
                        "fuel": "Gasolina",
                        "kilometers": 5,
                        "value": 10000
                    },
                    "reparations": [1, 2, 3],
                    "idBond": 2
                }
                """;

        mockMvc.perform(post("/register/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vehicleDtoJson)
                .accept((MediaType) status().isOk())
                .accept((MediaType) jsonPath("$.patent", is("ABCD34"))));
    }
    */

    @Test
    public void getBond_WhenIdMarkIsValid_ShouldReturnBondEntity() throws Exception {
        Long idMark = 1L;
        BondEntity bond = new BondEntity(1L, idMark, 5000);

        given(registerService.getBond(idMark)).willReturn(bond);

        mockMvc.perform(get("/register/bonds/{idMark}", idMark))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idmark").value(idMark))
                .andExpect(jsonPath("$.amount").value(5000));
    }

    @Test
    public void getBond_WhenIdMarkIsInvalid_ShouldReturnNotFound() throws Exception {
        Long idMark = 2L;

        given(registerService.getBond(idMark)).willReturn(null);

        mockMvc.perform(get("/register/bonds/{idMark}", idMark))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addBond_ShouldCreateBondAndReturnIt() throws Exception {
        Long idMark = 1L;
        int amount = 5000;
        BondEntity newBond = new BondEntity(1L, idMark, amount);

        given(registerService.addBond(idMark, amount)).willReturn(newBond);

        mockMvc.perform(post("/register/bonds/{idMark}/{amount}", idMark, amount))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.idmark").value(idMark))
                .andExpect(jsonPath("$.amount").value(amount));
    }

    @Test
    public void getVehiclesNotFinished_ShouldReturnVehicleList() throws Exception {
        List<VehicleDto> vehicleList = List.of(
                new VehicleDto(1L, "ABC123", "Toyota", "Corolla", "Sedán", 2020, "Gasolina", 5, 15000)
        );

        given(registerService.getVehiclesNotFinished()).willReturn(vehicleList);

        mockMvc.perform(get("/register/vehicles-not-finished/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].patent", is("ABC123")));
    }

    @Test
    public void finishVehicle_ShouldUpdateAndReturnVehicle() throws Exception {
        String patent = "ABC123";
        VehicleEntity vehicle = new VehicleEntity(1L, patent, 1L, "Corolla", "Sedán", 2020, "Gasolina", 5, 15000);

        given(registerService.finishVehicle(patent)).willReturn(vehicle);

        mockMvc.perform(put("/register/finish/{patent}", patent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.patent", is(patent)))
                .andExpect(jsonPath("$.type", is("Sedán")));
    }

    @Test
    public void getVehiclesNotRemoved_ShouldReturnVehicleList() throws Exception {
        List<VehicleDto> vehicleList = Arrays.asList(
                new VehicleDto(1L, "ABC123", "Toyota", "Corolla", "Sedán", 2020, "Gasolina", 5, 50000),
                new VehicleDto(2L, "XYZ789", "Honda", "Civic", "Coupé", 2019, "Híbrido", 4, 30000)
        );

        given(registerService.getVehiclesNotRemoved()).willReturn(vehicleList);

        mockMvc.perform(get("/register/vehicles-not-removed/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].patent", is("ABC123")))
                .andExpect(jsonPath("$[1].patent", is("XYZ789")));
    }

    @Test
    public void removeVehicle_WhenPatentIsValid_ShouldReturnVehicle() throws Exception {
        String patent = "ABC123";
        VehicleEntity vehicle = new VehicleEntity(1L, patent, 1L, "Corolla", "Sedán", 2020, "Gasolina", 5, 50000);

        given(registerService.removeVehicle(patent)).willReturn(vehicle);

        mockMvc.perform(put("/register/remove/{patent}", patent))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.patent", is(patent)))
                .andExpect(jsonPath("$.mark", is(1)));
    }


}
