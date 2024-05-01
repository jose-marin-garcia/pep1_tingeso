package tingeso_pep_1.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tingeso_pep_1.entities.MarksEntity;
import tingeso_pep_1.services.MarkService;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MarksController.class)
public class MarksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarkService markService;

    @Test
    public void listMarks_ShouldReturnMarks() throws Exception {
        MarksEntity mark1 = new MarksEntity(1L, "Toyota");

        MarksEntity mark2 = new MarksEntity(2L, "Nissan");

        List<MarksEntity> markList = new ArrayList<>(Arrays.asList(mark1, mark2));

        given(markService.getMarks()).willReturn((List<MarksEntity>) markList);

        mockMvc.perform(get("/marks/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //Que lo que recibo es en formato json
                .andExpect(jsonPath("$", hasSize(2))) //Que sean 2 marcas
                .andExpect(jsonPath("$[0].markName", is("Toyota"))) //Verifico los nombres
                .andExpect(jsonPath("$[1].markName", is("Nissan")));
    }

    @Test
    public void saveMark_ShouldReturnMark() throws Exception {
        MarksEntity mark = new MarksEntity(1L, "Toyota");

        given(markService.saveMark(Mockito.any(MarksEntity.class))).willReturn(mark);

        String markJson = """
            {
                "id": 1,
                "mark_name": "Toyota"
            }
            """;

        mockMvc.perform(post("/marks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(markJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.markName", is("Toyota")));
    }

    @Test
    public void saveMarks_ShouldReturnMarks() throws Exception {
        MarksEntity mark1 = new MarksEntity(1L, "Toyota");
        MarksEntity mark2 = new MarksEntity(2L, "Nissan");

        List<MarksEntity> markList = new ArrayList<>(Arrays.asList(mark1, mark2));

        given(markService.saveMarks(Mockito.anyList())).willReturn((List<MarksEntity>) markList);

        String markJson = """
            [
                {
                    "id": 1,
                    "mark_name": "Toyota"
                },
                {
                    "id": 2,
                    "mark_name": "Nissan"
                }
            ]
            """;

        mockMvc.perform(post("/marks/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(markJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].markName", is("Toyota")))
                .andExpect(jsonPath("$[1].markName", is("Nissan")));
    }

    @Test
    public void deleteMark_ShouldReturnNoContent() throws Exception {
        given(markService.deleteMark(1L)).willReturn(true);

        mockMvc.perform(delete("/marks/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
