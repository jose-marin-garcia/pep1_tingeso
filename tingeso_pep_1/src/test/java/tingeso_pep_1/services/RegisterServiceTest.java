package tingeso_pep_1.services;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import tingeso_pep_1.DataTransferObjects.VehicleDto;
import tingeso_pep_1.entities.*;
import tingeso_pep_1.repositories.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class RegisterServiceTest {

    @Autowired
    private RegisterService registerService;

    @MockBean
    private VehicleRepository vehicleRepository;
    @MockBean
    private BondRepository bondRepository;
    @MockBean
    private HistoricRepository historicRepository;
    @MockBean
    private HistoryRepairsRepository historyRepairsRepository;
    @MockBean
    private PricesRepository pricesRepository;
    @MockBean
    private MarksRepository markRepository;
    @Captor
    private ArgumentCaptor<HistoricEntity> historialArgumentCaptor;

    @Test
    void saveVehicle_WithBond_ShouldProcessAndSaveVehicle() {
        // Setup: Creación de un VehicleEntity, definición de reparaciones, y bond
        VehicleEntity vehicle = new VehicleEntity(1L, "ABC123", 1L, "Modelo", "Sedan", 2014, "Gasolina", 5, 40000);
        List<Long> repairs = List.of(1L, 2L, 3L);  // IDs de reparaciones
        Long idBond = 1L;
        BondEntity bond = new BondEntity(idBond, 1L, 500);  // ID y monto del bono

        // Configuración de los mocks
        given(bondRepository.findById(idBond)).willReturn(Optional.of(bond));
        given(historicRepository.save(any(HistoricEntity.class))).willReturn(new HistoricEntity());
        given(vehicleRepository.save(any(VehicleEntity.class))).willReturn(vehicle);
        given(pricesRepository.findPriceByIdtyperepairAndMotortype(anyLong(), anyString())).willReturn(100); // Precio por reparación

        // Actuar sobre el método a probar
        VehicleEntity savedVehicle = registerService.saveVehicle(vehicle, repairs, idBond);

        // Aserciones: Verificar que el vehículo se guardó correctamente y que los valores son los esperados
        assertThat(savedVehicle).isNotNull();
        assertThat(savedVehicle.getPatent()).isEqualTo("ABC123");
        verify(bondRepository).deleteById(idBond); // Verificar que se elimina el bono
        verify(historicRepository, times(1)).save(any(HistoricEntity.class)); // Verificar que se guarda el historial
        verify(historyRepairsRepository, times(repairs.size())).save(any(HistoryRepairsEntity.class)); // Verificar que se guardan las reparaciones
    }


    @Test
    void saveVehicleHatchBackHibrido_ShouldProcessAndSaveVehicle() {
        VehicleEntity vehicle = new VehicleEntity(1L, "ABC123", 1L, "Modelo", "Hatchback", 2014, "Hibrido", 5, 40000);
        List<Long> repairs = List.of();
        Long idBond = 1L;

        given(bondRepository.findById(idBond)).willReturn(Optional.of(new BondEntity(1L, idBond, 1000)));
        given(historicRepository.save(any(HistoricEntity.class))).willReturn(new HistoricEntity(1L, "ABC123", "05/05/2021", "10:00", 12000, 10000, 1000, 2000, 2280, null, null, null, null));
        given(vehicleRepository.save(any(VehicleEntity.class))).willReturn(vehicle);

        // Configuración para simular el guardado de HistoryRepairsEntity usando willAnswer
        given(historyRepairsRepository.save(any(HistoryRepairsEntity.class))).willAnswer(new Answer<HistoryRepairsEntity>() {
            private long idCounter = 1;  // Contador para simular la generación de ID

            @Override
            public HistoryRepairsEntity answer(InvocationOnMock invocation) throws Throwable {
                HistoryRepairsEntity entity = invocation.getArgument(0);
                return new HistoryRepairsEntity(idCounter++, entity.getIdHistorial(), entity.getIdReparacion());
            }
        });

        VehicleEntity savedVehicle = registerService.saveVehicle(vehicle, repairs, idBond);

        assertThat(savedVehicle).isNotNull();
        assertThat(savedVehicle.getPatent()).isEqualTo("ABC123");
        assertThat(savedVehicle.getMark()).isEqualTo(1L);
        assertThat(savedVehicle.getModel()).isEqualTo("Modelo");
        assertThat(savedVehicle.getType()).isEqualTo("Hatchback");
        assertThat(savedVehicle.getYear()).isEqualTo(2014);
        assertThat(savedVehicle.getTypemotor()).isEqualTo("Hibrido");
        // Verifica que se haya llamado a save con el vehículo
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void saveVehicleSuvDiesel_ShouldProcessAndSaveVehicle() {
        VehicleEntity vehicle = new VehicleEntity(1L, "ABC123", 1L, "Modelo", "SUV", 1980, "Diesel", 5, 12000);
        List<Long> repairs = new ArrayList<>();
        Long idBond = 1L;

        given(bondRepository.findById(idBond)).willReturn(Optional.of(new BondEntity(1L, idBond, 1000)));
        given(historicRepository.save(any(HistoricEntity.class))).willReturn(new HistoricEntity(1L, "ABC123", "05/05/2021", "10:00", 12000, 10000, 1000, 2000, 2280, null, null, null, null));
        given(vehicleRepository.save(any(VehicleEntity.class))).willReturn(vehicle);
        given(historyRepairsRepository.save(any(HistoryRepairsEntity.class))).willReturn(null);

        VehicleEntity savedVehicle = registerService.saveVehicle(vehicle, repairs, idBond);

        assertThat(savedVehicle).isNotNull();
        assertThat(savedVehicle.getPatent()).isEqualTo("ABC123");
    }

    @Test
    void saveVehicleHatchBackElectrico_ShouldProcessAndSaveVehicle() {
        VehicleEntity vehicle = new VehicleEntity(1L, "ABC123", 1L, "Modelo", "Hatchback", 2021, "Electrico", 5, 5000);
        List<Long> repairs = new ArrayList<>();
        Long idBond = 1L;

        given(bondRepository.findById(idBond)).willReturn(Optional.of(new BondEntity(1L, idBond, 1000)));
        given(historicRepository.save(any(HistoricEntity.class))).willReturn(new HistoricEntity(1L, "ABC123", "05/05/2021", "10:00", 12000, 10000, 1000, 2000, 2280, null, null, null, null));
        given(vehicleRepository.save(any(VehicleEntity.class))).willReturn(vehicle);
        given(historyRepairsRepository.save(any(HistoryRepairsEntity.class))).willReturn(null);

        VehicleEntity savedVehicle = registerService.saveVehicle(vehicle, repairs, idBond);

        assertThat(savedVehicle).isNotNull();
        assertThat(savedVehicle.getPatent()).isEqualTo("ABC123");
    }

        @Test
        void calcularDescuentoPorNumeroDeReparaciones_WithGasolineAndVariousRepairs_ShouldReturnCorrectDiscount() {
            // Instancia del servicio que está siendo probado
            RegisterService service = new RegisterService();

            // Pruebas para el tipo de motor "Gasolina" con diferentes números de reparaciones
            assertEquals(0.05, service.calcularDescuentoPorNumeroDeReparaciones("Gasolina", 2));
            assertEquals(0.10, service.calcularDescuentoPorNumeroDeReparaciones("Gasolina", 4));
            assertEquals(0.15, service.calcularDescuentoPorNumeroDeReparaciones("Gasolina", 7));
            assertEquals(0.20, service.calcularDescuentoPorNumeroDeReparaciones("Gasolina", 10));
        }

    @Test
    void calcularDescuentoPorNumeroDeReparaciones_WithDieselAndVariousRepairs_ShouldReturnCorrectDiscount() {
        // Instancia del servicio que está siendo probado
        RegisterService service = new RegisterService();

        assertEquals(0.07, service.calcularDescuentoPorNumeroDeReparaciones("Diesel", 2));
        assertEquals(0.12, service.calcularDescuentoPorNumeroDeReparaciones("Diesel", 4));
        assertEquals(0.17, service.calcularDescuentoPorNumeroDeReparaciones("Diesel", 7));
        assertEquals(0.22, service.calcularDescuentoPorNumeroDeReparaciones("Diesel", 10));
    }

    @Test
    void calcularDescuentoPorNumeroDeReparaciones_WithHibridoAndVariousRepairs_ShouldReturnCorrectDiscount() {
        // Instancia del servicio que está siendo probado
        RegisterService service = new RegisterService();

        assertEquals(0.10, service.calcularDescuentoPorNumeroDeReparaciones("Hibrido", 2));
        assertEquals(0.15, service.calcularDescuentoPorNumeroDeReparaciones("Hibrido", 4));
        assertEquals(0.20, service.calcularDescuentoPorNumeroDeReparaciones("Hibrido", 7));
        assertEquals(0.25, service.calcularDescuentoPorNumeroDeReparaciones("Hibrido", 10));
    }


    @Test
    void calcularDescuentoPorNumeroDeReparaciones_WithElectricoAndVariousRepairs_ShouldReturnCorrectDiscount() {
        // Instancia del servicio que está siendo probado
        RegisterService service = new RegisterService();

        assertEquals(0.08, service.calcularDescuentoPorNumeroDeReparaciones("Electrico", 2));
        assertEquals(0.13, service.calcularDescuentoPorNumeroDeReparaciones("Electrico", 4));
        assertEquals(0.18, service.calcularDescuentoPorNumeroDeReparaciones("Electrico", 7));
        assertEquals(0.23, service.calcularDescuentoPorNumeroDeReparaciones("Electrico", 10));
    }
    @Test
    void getBond_WhenIdMarkIsNull_ShouldReturnNull() {
        BondEntity result = registerService.getBond(null);
        assertThat(result).isNull();
    }

    @Test
    void getBond_WhenIdMarkIsNotNull_ShouldReturnBond() {
        Long idMark = 1L;
        BondEntity bond = new BondEntity(1L, idMark, 5000);
        when(bondRepository.findFirstByIdmark(idMark)).thenReturn(bond);

        BondEntity result = registerService.getBond(idMark);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bond.getId());
        assertThat(result.getIdmark()).isEqualTo(idMark);
        assertThat(result.getAmount()).isEqualTo(5000);
    }

    @Test
    void addBond_ShouldSaveAndReturnBond() {
        Long idMark = 1L;
        int amount = 5000;
        BondEntity newBond = new BondEntity(null, idMark, amount);
        BondEntity savedBond = new BondEntity(1L, idMark, amount);
        when(bondRepository.save(any(BondEntity.class))).thenReturn(savedBond);

        BondEntity result = registerService.addBond(idMark, amount);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedBond.getId());
        assertThat(result.getIdmark()).isEqualTo(idMark);
        assertThat(result.getAmount()).isEqualTo(amount);
    }

    @Test
    void getVehiclesNotFinished_ShouldReturnVehicles() {
        List<HistoricEntity> historiales = List.of(
                new HistoricEntity(1L, "ABC123", null, null, 0, 0, 0, 0, 0, null, null, null, null),
                new HistoricEntity(2L, "XYZ789", null, null, 0, 0, 0, 0, 0, null, null, null, null)
        );
        when(historicRepository.findAllByEndhourIsNullAndEnddateIsNull()).thenReturn(historiales);
        when(vehicleRepository.findByPatent("ABC123")).thenReturn(new VehicleEntity(1L, "ABC123", 1L, "Modelo A", "Sedan", 2020, "Gasolina", 5, 50000));
        when(vehicleRepository.findByPatent("XYZ789")).thenReturn(new VehicleEntity(2L, "XYZ789", 2L, "Modelo B", "Hatchback", 2021, "Diesel", 4, 30000));
        when(markRepository.findById(1L)).thenReturn(Optional.of(new MarksEntity(1L, "Toyota")));
        when(markRepository.findById(2L)).thenReturn(Optional.of(new MarksEntity(2L, "Nissan")));

        List<VehicleDto> result = registerService.getVehiclesNotFinished();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPatent()).isEqualTo("ABC123");
        assertThat(result.get(1).getPatent()).isEqualTo("XYZ789");
    }

    @Test
    void finishVehicle_ShouldUpdateHistoricAndReturnVehicle() {
        String patent = "ABC123";
        HistoricEntity historial = new HistoricEntity(1L, patent, null, null, 0, 0, 0, 0, 0, null, null, null, null);
        when(historicRepository.findByPatent(patent)).thenReturn(historial);
        when(historicRepository.save(any(HistoricEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VehicleEntity vehicle = new VehicleEntity(1L, patent, 1L, "Modelo A", "Sedan", 2020, "Gasolina", 5, 50000);
        when(vehicleRepository.findByPatent(patent)).thenReturn(vehicle);

        VehicleEntity result = registerService.finishVehicle(patent);

        assertThat(result).isNotNull();
        assertThat(result.getPatent()).isEqualTo(patent);
    }


    @Test
    void getVehiclesNotRemoved_ShouldReturnVehicleList() {
        // Given
        List<HistoricEntity> historiales = List.of(
                new HistoricEntity(1L, "ABC123", "01/01/2020", "10:00", 10000, 5000, 1000, 500, 1900, null, null, null, null),
                new HistoricEntity(2L, "XYZ789", "01/01/2020", "11:00", 20000, 10000, 2000, 1000, 3800, null, null, null, null)
        );
        when(historicRepository.findAllByEndhourIsNotNullAndEnddateIsNotNullAndClientdateIsNullAndClienthourIsNull()).thenReturn(historiales);
        when(vehicleRepository.findByPatent("ABC123")).thenReturn(new VehicleEntity(1L, "ABC123", 1L, "Modelo A", "Sedan", 2020, "Gasolina", 5, 50000));
        when(vehicleRepository.findByPatent("XYZ789")).thenReturn(new VehicleEntity(2L, "XYZ789", 2L, "Modelo B", "Hatchback", 2021, "Diesel", 4, 30000));
        when(markRepository.findById(1L)).thenReturn(Optional.of(new MarksEntity(1L, "Toyota")));
        when(markRepository.findById(2L)).thenReturn(Optional.of(new MarksEntity(2L, "Nissan")));

        // When
        List<VehicleDto> result = registerService.getVehiclesNotRemoved();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPatent()).isEqualTo("ABC123");
        assertThat(result.get(1).getPatent()).isEqualTo("XYZ789");
    }

    @Test
    void removeVehicle_ShouldUpdateHistoricAndReturnVehicle() {
        // Given
        String patent = "ABC123";
        HistoricEntity historial = new HistoricEntity(1L, patent, "01/01/2020", "10:00", 10000, 5000, 1000, 500, 1900, null, null, null, null);
        when(historicRepository.findByPatent(patent)).thenReturn(historial);
        when(vehicleRepository.findByPatent(patent)).thenReturn(new VehicleEntity(1L, patent, 1L, "Modelo A", "Sedan", 2020, "Gasolina", 5, 50000));

        // When
        VehicleEntity result = registerService.removeVehicle(patent);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPatent()).isEqualTo(patent);
        verify(historicRepository).save(historialArgumentCaptor.capture());
        HistoricEntity savedHistorial = historialArgumentCaptor.getValue();
        assertThat(savedHistorial.getClientdate()).isNotNull();
        assertThat(savedHistorial.getClienthour()).isNotNull();
    }


}
