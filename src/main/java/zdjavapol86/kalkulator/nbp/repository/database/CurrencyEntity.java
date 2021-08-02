package zdjavapol86.kalkulator.nbp.repository.database;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "currencies")
public class CurrencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(length = 3)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "avg_rate")
    private double avgRate;

}
