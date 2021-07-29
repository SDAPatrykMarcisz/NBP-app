package zdjavapol79.kalkulator.nbp.service;

import zdjavapol79.kalkulator.nbp.model.dto.ExchangeRate;
import zdjavapol79.kalkulator.nbp.repository.database.Currency;
import zdjavapol79.kalkulator.nbp.repository.database.NBPDatabaseRepository;
import zdjavapol79.kalkulator.nbp.repository.nbp.CurrencyTable;
import zdjavapol79.kalkulator.nbp.repository.nbp.NBPClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExchangeService {

    private final NBPClient nbpClient;
    private final NBPDatabaseRepository databaseRepository;

    public ExchangeService() {
        this.nbpClient = new NBPClient();
        databaseRepository = new NBPDatabaseRepository();
    }

    public ExchangeService(NBPClient nbpClient, NBPDatabaseRepository databaseRepository) {
        this.nbpClient = nbpClient;
        this.databaseRepository = databaseRepository;
    }

    public Double convert(String currencyCodeFrom, String currencyCodeTo, Double amount) {
        return convert(currencyCodeFrom, currencyCodeTo, amount, LocalDate.now());
    }

    public Double convert(String currencyCodeFrom, String currencyCodeTo, Double amount, LocalDate date) {
        Optional<Currency> from = getCurrencyCourseForDay(currencyCodeFrom, date);
        Optional<Currency> to = getCurrencyCourseForDay(currencyCodeTo, date);
        if (currencyCodeFrom.equals("PLN")) {
            Currency exchangeToValue = to.orElseThrow(missingCurrencyException(currencyCodeTo));
            return amount / exchangeToValue.getAvgRate();
        } else if (currencyCodeTo.equals("PLN")) {
            Currency exchangeFromValue = from.orElseThrow(missingCurrencyException(currencyCodeTo));
            return amount * exchangeFromValue.getAvgRate();
        } else {
            Currency exchangeFromValue = from.orElseThrow(missingCurrencyException(currencyCodeTo));
            Currency exchangeToValue = to.orElseThrow(missingCurrencyException(currencyCodeTo));
            return amount * exchangeFromValue.getAvgRate() / exchangeToValue.getAvgRate();
        }
    }

    private Supplier<NoSuchElementException> missingCurrencyException(String currencyCodeTo) {
        return () -> new NoSuchElementException(String.format("currency %s not found", currencyCodeTo));
    }

    private Optional<Currency> getCurrencyCourseForDay(String currencyCode, LocalDate date) {
        return getOrDownload(currencyCode, date, CurrencyTable.TABLE_A)
                .or(() -> getOrDownload(currencyCode, date, CurrencyTable.TABLE_B));
    }

    private Optional<Currency> getOrDownload(String currencyCode, LocalDate date, CurrencyTable source) {
        return getFromDatabase(currencyCode, date)
                .or(() -> getRatesForPresentedDay(currencyCode, date, source))
                .or(() -> getRatesForCurrentDay(currencyCode, date, source))
                .or(() -> getRatesForSevenDaysBack(currencyCode, date, source));
    }

    private Optional<Currency> getFromDatabase(String currencyCode, LocalDate date) {
        return databaseRepository.findCourseForCurrencyForDate(currencyCode, date);
    }

    private Optional<Currency> getRatesForPresentedDay(String currencyCode, LocalDate date, CurrencyTable source) {
        try {
            List<ExchangeRate> rates = nbpClient.downloadExchangeRateForDay(source, date);
            List<Currency> currencies = convertExchangeRateToCurrencies(rates);
            saveAllCurrenciesToDatabase(currencies);
            return findCurrencyForCode(currencyCode, currencies);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<Currency> getRatesForCurrentDay(String currencyCode, LocalDate date, CurrencyTable source) {
        if (isRequestForActualCurrencyRate(date)) {
            List<ExchangeRate> exchangeRates = nbpClient.downloadCurrentExchangeRates(source);
            List<Currency> currencies = convertExchangeRateToCurrencies(exchangeRates, date);
//            saveAllCurrenciesToDatabase(exchangeRates);
            return findCurrencyForCode(currencyCode, currencies);
        } else {
            return Optional.empty();
        }
    }

    private boolean isRequestForActualCurrencyRate(LocalDate date) {
        return LocalDate.now().equals(date);
    }


    private Optional<Currency> getRatesForSevenDaysBack(String currencyCode, LocalDate date, CurrencyTable source) {
        int WEEK_DURATION = 7;
        LocalDate searchDay = decreaseDateByOneDay(date);
        for (int i = 0; i < WEEK_DURATION; i++) {
            try {
                List<ExchangeRate> exchangeRates = nbpClient.downloadExchangeRateForDay(source, searchDay);
                List<Currency> currencies = convertExchangeRateToCurrencies(exchangeRates, date);
                saveAllCurrenciesToDatabase(currencies, date);
                return findCurrencyForCode(currencyCode, currencies);
            } catch (Exception ex) {
                searchDay = decreaseDateByOneDay(searchDay);
            }
        }
        return Optional.empty();
    }

    private LocalDate decreaseDateByOneDay(LocalDate searchDay) {
        return searchDay.minusDays(1);
    }

    private Optional<Currency> findCurrencyForCode(String currencyCodeFrom, List<Currency> currencies) {
        return currencies.stream()
                .filter(x -> x.getCode().equals(currencyCodeFrom))
                .findFirst();
    }

    private void saveAllCurrenciesToDatabase(List<Currency> rates) {
        saveAllCurrenciesToDatabase(rates, null);
    }

    private void saveAllCurrenciesToDatabase(List<Currency> currencies, LocalDate date) {
        databaseRepository.addIfNotAvailable(currencies);
    }

    private List<Currency> convertExchangeRateToCurrencies(List<ExchangeRate> rates) {
        return convertExchangeRateToCurrencies(rates, null);
    }

    private List<Currency> convertExchangeRateToCurrencies(List<ExchangeRate> rates, LocalDate date) {
        List<Currency> currencies = rates.stream().flatMap(x -> x.getRates().stream().map(y -> Currency.builder()
                .code(y.getCode())
                .name(y.getCurrency())
                .effectiveDate(Optional.ofNullable(date).orElse(LocalDate.parse(x.getEffectiveDate(), DateTimeFormatter.ISO_DATE)))
                .avgRate(y.getMid())
                .build()))
                .collect(Collectors.toList());
        return currencies;
    }


}
