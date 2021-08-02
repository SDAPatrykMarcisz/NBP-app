package zdjavapol86.kalkulator.nbp.ui;

import zdjavapol86.kalkulator.nbp.controller.Controller;
import zdjavapol86.kalkulator.nbp.model.dto.OperationSummary;

public class ConsoleMain {
    public static void main(String[] args) {
        Controller controller = new Controller();
        OperationSummary calculate = controller.calculate("PLN", "EUR", "10", "2021-08-02");
        System.out.println(calculate);
    }
}
