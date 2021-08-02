package zdjavapol86.kalkulator.nbp.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Currency;

@Data
@Builder
public class OperationSummary {
    private String currencyFromName;
    private String currencyToName;
    private String exchangeDate;
    private String amountToConvert;
    private String operationResult;
}
