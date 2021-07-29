package zdjavapol79.kalkulator.nbp.model.dto;

import lombok.Data;

@Data
public class Rate {
    private String currency;
    private String code;
    private Double mid;
}
