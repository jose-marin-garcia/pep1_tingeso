package tingeso_pep_1.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.springframework.test.context.ActiveProfiles;
import tingeso_pep_1.entities.MarksEntity;
import tingeso_pep_1.repositories.MarksRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class MarkServiceTest {

    @Autowired
    private MarkService markService;

    @MockBean
    private MarksRepository marksRepository;

    @Test
    void whenGetMarks_thenCorrect() {
        // Given
        List<MarksEntity> mockMarks = new ArrayList<>();
        mockMarks.add(new MarksEntity(1L, "Toyota"));
        mockMarks.add(new MarksEntity(2L, "Nissan"));

        given(marksRepository.findAll()).willReturn(mockMarks);

        // When
        List<MarksEntity> marks = markService.getMarks();

        // Then
        assertThat(marks).isNotNull();
        assertThat(marks.size()).isEqualTo(2);
        assertThat(marks.get(0).getMarkName()).isEqualTo("Toyota");
        assertThat(marks.get(1).getMarkName()).isEqualTo("Nissan");
    }

    @BeforeEach
    void setUp() {
        // Preparación común para cada prueba
        MarksEntity mark1 = new MarksEntity(1L, "Toyota");
        MarksEntity mark2 = new MarksEntity(2L, "Nissan");

        when(marksRepository.save(mark1)).thenReturn(mark1);
        when(marksRepository.saveAll(Arrays.asList(mark1, mark2))).thenReturn(Arrays.asList(mark1, mark2));
    }

    @Test
    void saveMark_ShouldReturnSavedMark() {
        // Given
        MarksEntity markToSave = new MarksEntity(null, "Toyota");
        MarksEntity savedMark = new MarksEntity(1L, "Toyota");

        // Configurando el mock para retornar 'savedMark' cuando 'save' es llamado con cualquier objeto MarksEntity
        when(marksRepository.save(any(MarksEntity.class))).thenReturn(savedMark);

        // When
        MarksEntity result = markService.saveMark(markToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedMark.getId());
        assertThat(result.getMarkName()).isEqualTo(savedMark.getMarkName());
    }


    @Test
    void saveMarks_ShouldReturnSavedMarks() {
        // Given
        MarksEntity mark1 = new MarksEntity(null, "Toyota");
        MarksEntity mark2 = new MarksEntity(null, "Nissan");
        List<MarksEntity> marks = Arrays.asList(mark1, mark2);

        // Mock the behaviour of saveAll to return the input marks
        when(marksRepository.saveAll(anyList())).thenReturn(marks);

        // When
        List<MarksEntity> savedMarks = markService.saveMarks(marks);

        // Then
        assertThat(savedMarks).isNotNull();
        assertThat(savedMarks.size()).isEqualTo(2);
        assertThat(savedMarks.get(0).getMarkName()).isEqualTo("Toyota");
        assertThat(savedMarks.get(1).getMarkName()).isEqualTo("Nissan");
    }


    @Test
    void deleteMark_ShouldReturnTrueWhenDeleted() throws Exception {
        // Given
        Long markId = 1L;
        doNothing().when(marksRepository).deleteById(markId);

        // When
        boolean result = markService.deleteMark(markId);

        // Then
        assertThat(result).isTrue();
        verify(marksRepository, times(1)).deleteById(markId);
    }

    @Test
    void deleteMark_ShouldThrowExceptionWhenError() {
        // Given
        Long markId = 1L;
        doThrow(new RuntimeException("Error deleting mark")).when(marksRepository).deleteById(markId);

        // When
        Throwable thrown = catchThrowable(() -> { markService.deleteMark(markId); });

        // Then
        assertThat(thrown).isInstanceOf(Exception.class).hasMessageContaining("Error deleting mark");
    }
}
