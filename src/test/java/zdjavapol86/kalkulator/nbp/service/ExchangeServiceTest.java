package zdjavapol86.kalkulator.nbp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zdjavapol86.kalkulator.nbp.model.dto.OperationSummary;
import zdjavapol86.kalkulator.nbp.repository.database.CurrencyEntity;
import zdjavapol86.kalkulator.nbp.repository.database.NBPDatabaseRepository;
import zdjavapol86.kalkulator.nbp.repository.nbp.NBPClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class ExchangeServiceTest {

    //obiekt na ktorym beda wykonywane testy
    private ExchangeService exchangeService;

    //zaleznosci do obiektu
    private NBPClient nbpClient;
    private NBPDatabaseRepository nbpDatabaseRepository;

    @BeforeEach
    void setUp() {
        nbpClient = Mockito.mock(NBPClient.class);
        nbpDatabaseRepository = Mockito.mock(NBPDatabaseRepository.class);

        exchangeService = new ExchangeService(nbpClient, nbpDatabaseRepository);
    }

    @Test
    void shouldConvertFromPlnToEurWhenEuroRateInDatabase() {
        //given
        //sekcja w ktorej przygotowujemy parametry potrzebne do testu
        //lub przygotowujemy mocki
        String currencyFrom = "PLN";
        String currencyTo = "EUR";

        Mockito.when(nbpDatabaseRepository.findCourseForCurrencyForDate(any(), any()))
                .thenReturn(Optional.of(CurrencyEntity.builder().effectiveDate(LocalDate.of(2021,6,1)).avgRate(4.7101).build()));
        //when
        //kod testowany
        OperationSummary convert = exchangeService.convert(
                currencyFrom,
                currencyTo,
                20.0,
                LocalDate.parse("2021-06-01", DateTimeFormatter.ISO_DATE)
        );

        //then
        //asercje na wyniki
        assertEquals(convert.getOperationResult(), "4,246194");
    }

}