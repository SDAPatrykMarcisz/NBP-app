package zdjavapol86.kalkulator.nbp.ui.javafx;

import javafx.concurrent.Task;
import lombok.Builder;
import zdjavapol86.kalkulator.nbp.controller.Controller;
import zdjavapol86.kalkulator.nbp.model.dto.OperationSummary;

@Builder
public class CalculationTask extends Task<OperationSummary> {

    private final String currencyFrom;
    private final String currencyTo;
    private final String amount;
    private final String date;

    @Override
    protected OperationSummary call() throws Exception {
        Controller controller = new Controller();
        OperationSummary calculate = controller.calculate(currencyFrom, currencyTo, amount, date);
        return calculate;
    }
}
