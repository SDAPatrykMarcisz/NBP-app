package zdjavapol79.kalkulator.nbp.service;

import zdjavapol79.kalkulator.nbp.model.dto.ExchangeRate;
import zdjavapol79.kalkulator.nbp.repository.CurrencyTable;
import zdjavapol79.kalkulator.nbp.repository.NBPClient;
import zdjavapol79.kalkulator.nbp.repository.NBPDatabaseRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ExchangeService {

    private NBPClient nbpClient;
    private NBPDatabaseRepository databaseRepository;

    public ExchangeService() {
        this.nbpClient = new NBPClient();
        databaseRepository = new NBPDatabaseRepository();
    }

    public ExchangeService(NBPClient nbpClient, NBPDatabaseRepository databaseRepository) {
        this.nbpClient = nbpClient;
        this.databaseRepository = databaseRepository;
    }

    public ExchangeService(NBPClient nbpClient) {
        this.nbpClient = nbpClient;
    }

    public Double convert(String currencyCodeFrom, String currencyCodeTo, Double amount) {
        return convert(currencyCodeFrom, currencyCodeTo, amount, LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    }

    public Double convert(String currencyCodeFrom, String currencyCodeTo, Double amount, String date) {
        Optional<Double> from = getCurrencyCourseForDay(currencyCodeFrom, date);
        Optional<Double> to = getCurrencyCourseForDay(currencyCodeTo, date);
        if (currencyCodeFrom.equals("PLN")) {
            Double exchangeToValue = to.get();
            return amount / exchangeToValue;
        } else if (currencyCodeTo.equals("PLN")) {
            Double exchangeFromValue = from.get();
            return amount * exchangeFromValue;
        } else {
            Double exchangeFromValue = from.get();
            Double exchangeToValue = to.get();
            return amount * exchangeFromValue / exchangeToValue;
        }
    }

    public Optional<Double> getCurrencyCourseForDay(String currencyCode, String date) {
        return getOrDownload(currencyCode, date, CurrencyTable.TABLE_A).or(() -> getOrDownload(currencyCode, date, CurrencyTable.TABLE_B));
    }


    private Optional<Double> getOrDownload(String currencyCode, String date, CurrencyTable source) {
        Optional<Double> course = databaseRepository.findCourseForCurrencyForDate(currencyCode, date);
        if (course.isPresent()) {
            System.out.println("downloaded from database");
            return course;
        } else {
            try {
                List<ExchangeRate> rates = nbpClient.downloadExchangeRateForDay(source, date);
                saveAllCurrenciesToDatabase(rates);
                Optional<Double> downloadedCourse = getMidForCurrency(currencyCode, rates.stream());
                return downloadedCourse;
            } catch (Exception e) {
                if (requestForActualCurrencyRate(date)) {
                    List<ExchangeRate> exchangeRates = nbpClient.downloadCurrentExchangeRates(source);
                    saveAllCurrenciesToDatabase(exchangeRates);
                    return getMidForCurrency(currencyCode, exchangeRates.stream());
                } else {
                    int WEEK_DURATION = 7;
                    String searchDay = decreaseDateByOneDay(date);
                    for (int i = 0; i < WEEK_DURATION; i++) {
                        try {
                            List<ExchangeRate> exchangeRates = nbpClient.downloadExchangeRateForDay(source, searchDay);
                            saveAllCurrenciesToDatabase(exchangeRates, searchDay);
                            Optional<Double> midForCurrency = getMidForCurrency(currencyCode, exchangeRates.stream());
                            return midForCurrency;
                        } catch (Exception ex) {
                            searchDay = decreaseDateByOneDay(searchDay);
                        }
                    }
                }
            }
//            saveAllCurrenciesToDatabase(nbpClient.downloadExchangeRateForDay(CurrencyTable.TABLE_B, date));
        }
        return Optional.empty();
    }

    private String decreaseDateByOneDay(String searchDay) {
        return LocalDate.parse(searchDay, DateTimeFormatter.ISO_DATE).minusDays(1).format(DateTimeFormatter.ISO_DATE);
    }

    private boolean requestForActualCurrencyRate(String date) {
        return LocalDate.now().format(DateTimeFormatter.ISO_DATE).equals(date);
    }

    private Optional<Double> getMidFor(String currencyCode, Supplier<List<ExchangeRate>> sourceA, Supplier<List<ExchangeRate>> sourceB) {
        return getMidForCurrency(currencyCode, sourceA).or(() -> getMidForCurrency(currencyCode, sourceB));
    }

    private Optional<Double> getMidForCurrency(String currencyCode, Supplier<List<ExchangeRate>> sourceA) {
        return getMidForCurrency(currencyCode, sourceA.get().stream());
    }

    private Optional<Double> getMidForCurrency(String currencyCodeFrom, Stream<ExchangeRate> rateStream) {
        return rateStream
                .flatMap(exchangeRate -> exchangeRate.getRates().stream())
                .filter(x -> x.getCode().equals(currencyCodeFrom))
                .map(x -> x.getMid())
                .findFirst();
    }

    private void saveAllCurrenciesToDatabase(List<ExchangeRate> rates) {
        saveAllCurrenciesToDatabase(rates, null);
    }

    private void saveAllCurrenciesToDatabase(List<ExchangeRate> rates, String date) {
        rates.forEach(x -> x.getRates().forEach(y -> databaseRepository.save(y.getCode(), Optional.ofNullable(date).orElse(x.getEffectiveDate()), y.getMid())));
    }


    //NIEUZYWANE, ALE TAKIE LADNE ZE SZKODA WYRZUCIC

    private Optional<Double> getMidForCurrencyForLastPublishedDay(String currencyCode) {
        return getMidFor(
                currencyCode,
                () -> nbpClient.downloadCurrentExchangeRates(CurrencyTable.TABLE_A),
                () -> nbpClient.downloadCurrentExchangeRates(CurrencyTable.TABLE_B)
        );
//        return getMidForCurrency(currencyCode, nbpClient.downloadExchangeRates(CurrencyTable.TABLE_A).stream())
//                .or(() -> getMidForCurrency(currencyCode, nbpClient.downloadExchangeRates(CurrencyTable.TABLE_B).stream()));
    }

    private Optional<Double> getMidForCurrencyForADay(String currencyCode, String date) {
        return getMidFor(
                currencyCode,
                () -> nbpClient.downloadExchangeRateForDay(CurrencyTable.TABLE_A, date),
                () -> nbpClient.downloadExchangeRateForDay(CurrencyTable.TABLE_B, date)
        );
//        return getMidForCurrency(currencyCode, nbpClient.downloadExchangeRateForDay(CurrencyTable.TABLE_A, date).stream())
//                .or(() -> getMidForCurrency(currencyCode, nbpClient.downloadExchangeRateForDay(CurrencyTable.TABLE_B, date).stream()));
    }


}
