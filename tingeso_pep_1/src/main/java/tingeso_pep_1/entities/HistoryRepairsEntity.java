package tingeso_pep_1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historyrepairs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRepairsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private Long idHistorial; //A qué historial pertenece
    private Long idReparacion; //ID Reparaciones
}
