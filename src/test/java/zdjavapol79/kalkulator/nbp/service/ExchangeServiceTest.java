package zdjavapol79.kalkulator.nbp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zdjavapol79.kalkulator.nbp.repository.database.NBPDatabaseRepository;
import zdjavapol79.kalkulator.nbp.repository.nbp.NBPClient;

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
//                .thenReturn(Optional.of(4.20));
                .thenReturn(null);
        //when
        //kod testowany
        Double result = exchangeService.convert(currencyFrom, currencyTo, 20.0);

        //then
        //asercje na wyniki
        assertEquals(result, 4.761904761904762);
    }

}