package tingeso_pep_1.controllers;

import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tingeso_pep_1.DataTransferObjects.RegisterDto;
import tingeso_pep_1.entities.BondEntity;
import tingeso_pep_1.entities.VehicleEntity;
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

    @Autowired
    private RegisterController registerController;

    @Autowired
    private RegisterService registerService;

    /*@Test
    public void saveRegister_ShouldReturnSavedVehicleWithDiscountApplied() throws Exception {
        VehicleEntity vehicleNew = new VehicleEntity(); // Inicializa esto correctamente
        List<Long> reparations = Arrays.asList(1L, 2L, 3L);
        Long idBond = 5L;

        RegisterDto vehicleDto = new RegisterDto();
        vehicleDto.setVehicle(vehicleNew);
        vehicleDto.setReparations(reparations);
        vehicleDto.setIdBond(idBond);

        when(registerController.addRegister(vehicleDto).thenReturn(vehicleNew);

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(vehicleNew)));
    }*/

    @Test
    public void getBond_ShouldReturnBond() throws Exception {
        BondEntity discount = new BondEntity(1L,1L,1000);

        given(registerService.getBond(1L)).willReturn(discount);

        mockMvc.perform(get("/register/{id_mark}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idmark", is(1L)));
    }
}
