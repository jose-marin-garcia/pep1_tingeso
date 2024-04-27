package tingeso_pep_1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table (name = "historic")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String patent; // Patente
    private String admissiondate; //ddmmyyyy
    private String admissionhour; //hhmm
    private int mount;
    private int sumaReparaciones;
    private int descuentos;
    private int recargos;
    private int iva;
    private Date enddate;
    private Date endhour;
    private Date clientdate;
    private Date clienthour;
}
