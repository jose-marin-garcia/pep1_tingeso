package tingeso_pep_1.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import tingeso_pep_1.entities.TypeRepairsEntity;
import tingeso_pep_1.services.TypeRepairsService;
import org.mockito.Mockito;

import java.util.List;


@WebMvcTest(TypeRepairsController.class)
public class TypeRepairsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TypeRepairsService typeRepairsService;

    @Test
    public void listTypeRepairs_ShouldReturnList() throws Exception {
        List<TypeRepairsEntity> repairs = List.of(
                new TypeRepairsEntity(1L, "Cambio de aceite"),
                new TypeRepairsEntity(2L, "Revisión de frenos")
        );

        given(typeRepairsService.listTypeRepairs()).willReturn(repairs);

        mockMvc.perform(get("/typerepairs/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].repairName", is("Cambio de aceite")))
                .andExpect(jsonPath("$[1].repairName", is("Revisión de frenos")));
    }

    @Test
    public void getTypeRepairByID_ShouldReturnRepair() throws Exception {
        TypeRepairsEntity repair = new TypeRepairsEntity(1L, "Cambio de aceite");

        given(typeRepairsService.getTypeRepairByID(1L)).willReturn(repair);

        mockMvc.perform(get("/typerepairs/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.repairName", is("Cambio de aceite")));
    }

    @Test
    public void saveTypeRepair_ShouldReturnSavedRepair() throws Exception {
        TypeRepairsEntity repairToSave = new TypeRepairsEntity(null, "Cambio de aceite");
        TypeRepairsEntity savedRepair = new TypeRepairsEntity(1L, "Cambio de aceite");

        given(typeRepairsService.saveTypeRepair(Mockito.any(TypeRepairsEntity.class))).willReturn(savedRepair);

        mockMvc.perform(post("/typerepairs/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"repairName\":\"Cambio de aceite\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repairName", is("Cambio de aceite")));
    }

    @Test
    public void saveTypeRepairsList_ShouldReturnSavedRepairsList() throws Exception {
        List<TypeRepairsEntity> repairsToSave = List.of(new TypeRepairsEntity(null, "Cambio de aceite"), new TypeRepairsEntity(null, "Revisión de frenos"));
        List<TypeRepairsEntity> savedRepairs = List.of(new TypeRepairsEntity(1L, "Cambio de aceite"), new TypeRepairsEntity(2L, "Revisión de frenos"));

        given(typeRepairsService.saveTypesRepair(anyList())).willReturn(savedRepairs);

        mockMvc.perform(post("/typerepairs/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"repairName\":\"Cambio de aceite\"}, {\"repairName\":\"Revisión de frenos\"}]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].repairName", is("Cambio de aceite")))
                .andExpect(jsonPath("$[1].repairName", is("Revisión de frenos")));
    }


}
