package tingeso_pep_1.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Long id;

    private String patent;
    private String mark;
    private String model;
    private String type;
    private int year;
    private String typemotor;
    private int numberseats;
    private int kilometers;
}
