package zdjavapol79.kalkulator.nbp.service;

import zdjavapol79.kalkulator.nbp.model.dto.ExchangeRate;
import zdjavapol79.kalkulator.nbp.repository.NBPClient;
import zdjavapol79.kalkulator.nbp.repository.NBPDatabaseRepository;

import java.util.Optional;
import java.util.stream.Stream;

public class ExchangeService {

    private NBPClient nbpClient;
    private NBPDatabaseRepository databaseRepository;

    public ExchangeService(){
        this.nbpClient = new NBPClient();
        databaseRepository = new NBPDatabaseRepository();
    }

    public Double getCurrencyCourseForDay(String currencyCode, String date) throws Exception {
        Optional<Double> course = databaseRepository.findCourseForCurrencyForDate(currencyCode, date);
        if(course.isPresent()){
            System.out.println("downloaded from database");
            return course.get();
        } else {
            Optional<Double> downloadedCourse = getMidForCurrencyForADay(currencyCode, date);
            System.out.println("downloaded from external api and saved into database");
            if(downloadedCourse.isPresent()) {
                Double value = downloadedCourse.get();
                databaseRepository.save(currencyCode, date, value);
                return value;
            } else {
                throw new Exception("course for currency not found for the date");
            }
        }
    }

    public Double convert(String currencyCodeFrom, String currencyCodeTo, Double amount){
        Optional<Double> from = getMidForCurrencyForCurrentDay(currencyCodeFrom);
        Optional<Double> to = getMidForCurrencyForCurrentDay(currencyCodeTo);
        if(currencyCodeFrom.equals("PLN")){
            Double exchangeToValue = to.get();
            System.out.println(exchangeToValue);
            return amount / exchangeToValue;

        } else if(currencyCodeTo.equals("PLN")){
            Double exchangeFromValue = from.get();
            System.out.println(exchangeFromValue);
            return amount * exchangeFromValue;

        } else {
            Double exchangeFromValue = from.get();
            Double exchangeToValue = to.get();
            return exchangeToValue * exchangeFromValue;
        }
    }

    private Optional<Double> getMidForCurrencyForCurrentDay(String currencyCodeFrom){
        return getMidForCurrency(currencyCodeFrom, Stream.of(nbpClient.downloadExchangeRates()));
    }

    private Optional<Double> getMidForCurrencyForADay(String currencyCodeFrom, String date){
        return getMidForCurrency(currencyCodeFrom, Stream.of(nbpClient.downloadExchangeRateForDay(date)));
    }

    private Optional<Double> getMidForCurrency(String currencyCodeFrom, Stream<ExchangeRate> rateStream) {
        return rateStream
                .flatMap(exchangeRate -> exchangeRate.getRates().stream())
                .filter(x -> x.getCode().equals(currencyCodeFrom))
                .map(x -> x.getMid())
                .findFirst();
    }

}
