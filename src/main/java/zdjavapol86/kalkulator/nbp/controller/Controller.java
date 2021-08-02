package zdjavapol86.kalkulator.nbp.controller;

import lombok.AllArgsConstructor;
import zdjavapol86.kalkulator.nbp.model.dto.OperationSummary;
import zdjavapol86.kalkulator.nbp.service.ExchangeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public class Controller {

    private final ExchangeService exchangeService;

    public Controller(){
        this.exchangeService = new ExchangeService();
    }

    public OperationSummary calculate(String currencyFrom, String currencyTo, String amount, String date){
        return exchangeService.convert(currencyFrom, currencyTo, Double.valueOf(amount), LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
    }

}
