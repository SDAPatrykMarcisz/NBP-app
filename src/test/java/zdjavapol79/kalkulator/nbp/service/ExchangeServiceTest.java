package zdjavapol79.kalkulator.nbp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zdjavapol79.kalkulator.nbp.model.dto.ExchangeRate;
import zdjavapol79.kalkulator.nbp.model.dto.Rate;
import zdjavapol79.kalkulator.nbp.repository.NBPClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    private ExchangeService exchangeService;

    @Mock
    private NBPClient client;

    @BeforeEach
    void setUp(){
        exchangeService = new ExchangeService(client);
    }

    @Test
    void shouldReturnCorrectResultWhenFirstCurrencyPln(){
//        ExchangeRate responseFromClient = new ExchangeRate();
//        Rate rate1 = new Rate();
//        Rate rate2 = new Rate();
//        responseFromClient.setNo();
//        responseFromClient.setTable();
//        responseFromClient.setEffectiveDate();
//        responseFromClient.setRates(List.of(rate1, rate2));
//
//        Mockito.when(client).downloadExchangeRates().thenReturn(new ExchangeRate[]{responseFromClient});
    }

}