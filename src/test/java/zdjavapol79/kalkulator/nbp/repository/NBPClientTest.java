package zdjavapol79.kalkulator.nbp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zdjavapol79.kalkulator.nbp.repository.nbp.NBPClient;

class NBPClientTest {

    private NBPClient client;

    @BeforeEach
    void setUp(){
        client = new NBPClient();
    }

    @Test
    void shouldParseJsonResponse(){
//        //given
//        //when
//        ExchangeRate[] rates = client.downloadExchangeRates();
//        //then
//        assertEquals("A", rates[0].getTable());
//        assertEquals("098/A/NBP/2021", rates[0].getNo());
//        assertEquals("2021-05-24", rates[0].getEffectiveDate());
//        assertEquals(35, rates[0].getRates().size());
//        assertEquals("THB", rates[0].getRates().get(0).getCode());
//        assertEquals("bat (Tajlandia)", rates[0].getRates().get(0).getCurrency());
//        assertEquals(0.1173, rates[0].getRates().get(0).getMid());
    }

    //"table": "A",
    //"no": "098/A/NBP/2021",
    //"effectiveDate": "2021-05-24",
}