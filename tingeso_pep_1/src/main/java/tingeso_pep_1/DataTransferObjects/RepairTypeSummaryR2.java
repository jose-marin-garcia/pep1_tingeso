package tingeso_pep_1.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairTypeSummaryR2 {
    private String typeRepairName;
    private int sedanCount;
    private int hatchbackCount;
    private int suvCount;
    private int pickupCount;
    private int furgonetaCount;
    private int totalCost;
}
