package tingeso_pep_1.DataTransferObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairTypeSummaryTypeMotorR4 {
    private String typeRepairName;
    private int gasolineCount;
    private int dieselCount;
    private int hybridCount;
    private int electricCount;
    private int totalCost;
}