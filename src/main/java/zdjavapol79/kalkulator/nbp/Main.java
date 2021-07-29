package zdjavapol79.kalkulator.nbp;

import zdjavapol79.kalkulator.nbp.service.ExchangeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws Exception {
        ExchangeService service = new ExchangeService();
//        System.out.println(service.convert("EUR", "THB", 20.0));
        System.out.println(service.convert("USD", "AFN", 100.0, LocalDate.parse("2021-06-01", DateTimeFormatter.ISO_DATE)));
        //System.out.println(service.convert("THB", "PLN", 20.0));
//        System.out.println(service.convert("THB", "EUR", 20.0));
//        System.out.println(service.getCurrencyCourseForDay("THB", "2021-06-02"));
//        System.out.println(service.getCurrencyCourseForDay("THB", "2021-06-02"));
    }

}


