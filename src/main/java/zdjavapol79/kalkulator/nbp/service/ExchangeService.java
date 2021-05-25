package zdjavapol79.kalkulator.nbp.service;

import zdjavapol79.kalkulator.nbp.model.dto.ExchangeRate;
import zdjavapol79.kalkulator.nbp.model.dto.Rate;
import zdjavapol79.kalkulator.nbp.repository.NBPClient;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ExchangeService {

    private NBPClient nbpClient;

    public ExchangeService(){
        this.nbpClient = new NBPClient();
    }

    public Double convert(String currencyCodeFrom, String currencyCodeTo, Double amount){
        Optional<Double> from = getMidForCurrency(currencyCodeFrom);
        Optional<Double> to = getMidForCurrency(currencyCodeTo);
        if(from.isPresent()){
            Double value = from.get();
        }

        throw new UnsupportedOperationException("not implemented yet");
    }

    private Optional<Double> getMidForCurrency(String currencyCodeFrom) {
        return Stream.of(nbpClient.downloadExchangeRates())
                .flatMap(exchangeRate -> exchangeRate.getRates().stream())
                .filter(x -> x.getCode().equals(currencyCodeFrom))
                .map(x -> x.getMid())
                .findFirst();
    }

}
