package tingeso_pep_1.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tingeso_pep_1.entities.VehicleEntity;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private VehicleEntity vehicle;
    private List<Long> reparations;
    private Long idBond;
}
