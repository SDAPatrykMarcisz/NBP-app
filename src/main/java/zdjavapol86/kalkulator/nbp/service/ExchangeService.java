package zdjavapol86.kalkulator.nbp.service;

import zdjavapol86.kalkulator.nbp.model.dto.ExchangeRate;
import zdjavapol86.kalkulator.nbp.model.dto.OperationSummary;
import zdjavapol86.kalkulator.nbp.repository.database.CurrencyEntity;
import zdjavapol86.kalkulator.nbp.repository.database.NBPDatabaseRepository;
import zdjavapol86.kalkulator.nbp.repository.nbp.CurrencyTable;
import zdjavapol86.kalkulator.nbp.repository.nbp.NBPClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    public OperationSummary convert(String currencyCodeFrom, String currencyCodeTo, Double amount, LocalDate date) {
        Optional<CurrencyEntity> from = getCurrencyCourseForDay(currencyCodeFrom, date);
        Optional<CurrencyEntity> to = getCurrencyCourseForDay(currencyCodeTo, date);
        if (currencyCodeFrom.equals("PLN")) {
            CurrencyEntity exchangeToValue = to.orElseThrow(missingCurrencyException(currencyCodeTo));
            return OperationSummary.builder()
                    .currencyFromName("PLN")
                    .currencyToName(exchangeToValue.getCode())
                    .amountToConvert(amount.toString())
                    .operationResult(String.format("%f", amount / exchangeToValue.getAvgRate()))
                    .exchangeDate(exchangeToValue.getEffectiveDate().format(DateTimeFormatter.ISO_DATE))
                    .build();
        } else if (currencyCodeTo.equals("PLN")) {
            CurrencyEntity exchangeFromValue = from.orElseThrow(missingCurrencyException(currencyCodeTo));
            return OperationSummary.builder()
                    .currencyFromName("PLN")
                    .currencyToName(exchangeFromValue.getCode())
                    .amountToConvert(amount.toString())
                    .operationResult(String.format("%f", amount * exchangeFromValue.getAvgRate()))
                    .exchangeDate(exchangeFromValue.getEffectiveDate().format(DateTimeFormatter.ISO_DATE))
                    .build();
        } else {
            CurrencyEntity exchangeFromValue = from.orElseThrow(missingCurrencyException(currencyCodeTo));
            CurrencyEntity exchangeToValue = to.orElseThrow(missingCurrencyException(currencyCodeTo));
            return OperationSummary.builder()
                    .currencyFromName(exchangeFromValue.getCode())
                    .currencyToName(exchangeToValue.getCode())
                    .amountToConvert(amount.toString())
                    .operationResult(String.format("%f", amount * exchangeFromValue.getAvgRate() / exchangeToValue.getAvgRate()))
                    .exchangeDate(exchangeFromValue.getEffectiveDate().format(DateTimeFormatter.ISO_DATE))
                    .build();
        }
    }

    private Supplier<NoSuchElementException> missingCurrencyException(String currencyCodeTo) {
        return () -> new NoSuchElementException(String.format("currency %s not found", currencyCodeTo));
    }

    private Optional<CurrencyEntity> getCurrencyCourseForDay(String currencyCode, LocalDate date) {
        return getOrDownload(currencyCode, date, CurrencyTable.TABLE_A)
                .or(() -> getOrDownload(currencyCode, date, CurrencyTable.TABLE_B));
    }

    private Optional<CurrencyEntity> getOrDownload(String currencyCode, LocalDate date, CurrencyTable source) {
        return getFromDatabase(currencyCode, date)
                .or(() -> getRatesForPresentedDay(currencyCode, date, source))
                .or(() -> getRatesForCurrentDay(currencyCode, date, source))
                .or(() -> getRatesForSevenDaysBack(currencyCode, date, source));
    }

    private Optional<CurrencyEntity> getFromDatabase(String currencyCode, LocalDate date) {
        return databaseRepository.findCourseForCurrencyForDate(currencyCode, date);
    }

    private Optional<CurrencyEntity> getRatesForPresentedDay(String currencyCode, LocalDate date, CurrencyTable source) {
        try {
            List<ExchangeRate> rates = nbpClient.downloadExchangeRateForDay(source, date);
            List<CurrencyEntity> currencies = convertExchangeRateToCurrencies(rates);
            saveAllCurrenciesToDatabase(currencies);
            return findCurrencyForCode(currencyCode, currencies);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<CurrencyEntity> getRatesForCurrentDay(String currencyCode, LocalDate date, CurrencyTable source) {
        if (isRequestForActualCurrencyRate(date)) {
            List<ExchangeRate> exchangeRates = nbpClient.downloadCurrentExchangeRates(source);
            List<CurrencyEntity> currencies = convertExchangeRateToCurrencies(exchangeRates, date);
//            saveAllCurrenciesToDatabase(exchangeRates);
            return findCurrencyForCode(currencyCode, currencies);
        } else {
            return Optional.empty();
        }
    }

    private boolean isRequestForActualCurrencyRate(LocalDate date) {
        return LocalDate.now().equals(date);
    }


    private Optional<CurrencyEntity> getRatesForSevenDaysBack(String currencyCode, LocalDate date, CurrencyTable source) {
        int WEEK_DURATION = 7;
        LocalDate searchDay = decreaseDateByOneDay(date);
        for (int i = 0; i < WEEK_DURATION; i++) {
            try {
                List<ExchangeRate> exchangeRates = nbpClient.downloadExchangeRateForDay(source, searchDay);
                List<CurrencyEntity> currencies = convertExchangeRateToCurrencies(exchangeRates, date);
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

    private Optional<CurrencyEntity> findCurrencyForCode(String currencyCodeFrom, List<CurrencyEntity> currencies) {
        return currencies.stream()
                .filter(x -> x.getCode().equals(currencyCodeFrom))
                .findFirst();
    }

    private void saveAllCurrenciesToDatabase(List<CurrencyEntity> rates) {
        saveAllCurrenciesToDatabase(rates, null);
    }

    private void saveAllCurrenciesToDatabase(List<CurrencyEntity> currencies, LocalDate date) {
        databaseRepository.addIfNotAvailable(currencies);
    }

    private List<CurrencyEntity> convertExchangeRateToCurrencies(List<ExchangeRate> rates) {
        return convertExchangeRateToCurrencies(rates, null);
    }

    private List<CurrencyEntity> convertExchangeRateToCurrencies(List<ExchangeRate> rates, LocalDate date) {
        List<CurrencyEntity> currencies = rates.stream().flatMap(x -> x.getRates().stream().map(y -> CurrencyEntity.builder()
                .code(y.getCode())
                .name(y.getCurrency())
                .effectiveDate(Optional.ofNullable(date).orElse(LocalDate.parse(x.getEffectiveDate(), DateTimeFormatter.ISO_DATE)))
                .avgRate(y.getMid())
                .build()))
                .collect(Collectors.toList());
        return currencies;
    }


}
