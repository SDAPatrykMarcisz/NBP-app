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

    public Double convert(String currencyCodeFrom, String currencyCodeTo, Double amount){
        Optional<Double> from = getMidForCurrencyForCurrentDay(currencyCodeFrom);
        Optional<Double> to = getMidForCurrencyForCurrentDay(currencyCodeTo);
        throw new UnsupportedOperationException("not implemented yet");
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
