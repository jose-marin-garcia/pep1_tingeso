package tingeso_pep_1.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleCostDetailsR1 {
    private String patent;
    private String marca;
    private String modelo;
    private String tipoVehiculo;
    private int montoTotal;
    private int sumaReparaciones;
    private int descuentos;
    private int recargos;
    private int iva;
}
