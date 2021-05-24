package zdjavapol79.kalkulator.nbp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Main {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ExchangeRate[] rate = objectMapper.readValue(json, ExchangeRate[].class);
        System.out.println("koniec");
    }

    static String json = "[\n" +
            "  {\n" +
            "    \"table\":\"A\",\n" +
            "    \"no\":\"098/A/NBP/2021\",\n" +
            "    \"effectiveDate\":\"2021-05-24\",\n" +
            "    \"rates\":[\n" +
            "      {\n" +
            "        \"currency\":\"bat (Tajlandia)\",\n" +
            "        \"code\":\"THB\",\n" +
            "        \"mid\":0.1173\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"dolar amerykański\",\n" +
            "        \"code\":\"USD\",\n" +
            "        \"mid\":3.6795\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"dolar australijski\",\n" +
            "        \"code\":\"AUD\",\n" +
            "        \"mid\":2.84\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"dolar Hongkongu\",\n" +
            "        \"code\":\"HKD\",\n" +
            "        \"mid\":0.4739\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"dolar kanadyjski\",\n" +
            "        \"code\":\"CAD\",\n" +
            "        \"mid\":3.0473\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"dolar nowozelandzki\",\n" +
            "        \"code\":\"NZD\",\n" +
            "        \"mid\":2.6382\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"dolar singapurski\",\n" +
            "        \"code\":\"SGD\",\n" +
            "        \"mid\":2.7648\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"euro\",\n" +
            "        \"code\":\"EUR\",\n" +
            "        \"mid\":4.4877\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"forint (Węgry)\",\n" +
            "        \"code\":\"HUF\",\n" +
            "        \"mid\":0.012901\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"frank szwajcarski\",\n" +
            "        \"code\":\"CHF\",\n" +
            "        \"mid\":4.0935\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"funt szterling\",\n" +
            "        \"code\":\"GBP\",\n" +
            "        \"mid\":5.2022\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"hrywna (Ukraina)\",\n" +
            "        \"code\":\"UAH\",\n" +
            "        \"mid\":0.1342\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"jen (Japonia)\",\n" +
            "        \"code\":\"JPY\",\n" +
            "        \"mid\":0.033781\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"korona czeska\",\n" +
            "        \"code\":\"CZK\",\n" +
            "        \"mid\":0.1765\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"korona duńska\",\n" +
            "        \"code\":\"DKK\",\n" +
            "        \"mid\":0.6035\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"korona islandzka\",\n" +
            "        \"code\":\"ISK\",\n" +
            "        \"mid\":0.030139\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"korona norweska\",\n" +
            "        \"code\":\"NOK\",\n" +
            "        \"mid\":0.4405\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"korona szwedzka\",\n" +
            "        \"code\":\"SEK\",\n" +
            "        \"mid\":0.442\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"kuna (Chorwacja)\",\n" +
            "        \"code\":\"HRK\",\n" +
            "        \"mid\":0.5981\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"lej rumuński\",\n" +
            "        \"code\":\"RON\",\n" +
            "        \"mid\":0.911\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"lew (Bułgaria)\",\n" +
            "        \"code\":\"BGN\",\n" +
            "        \"mid\":2.2945\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"lira turecka\",\n" +
            "        \"code\":\"TRY\",\n" +
            "        \"mid\":0.4379\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"nowy izraelski szekel\",\n" +
            "        \"code\":\"ILS\",\n" +
            "        \"mid\":1.1295\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"peso chilijskie\",\n" +
            "        \"code\":\"CLP\",\n" +
            "        \"mid\":0.005103\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"peso filipińskie\",\n" +
            "        \"code\":\"PHP\",\n" +
            "        \"mid\":0.0764\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"peso meksykańskie\",\n" +
            "        \"code\":\"MXN\",\n" +
            "        \"mid\":0.1844\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"rand (Republika Południowej Afryki)\",\n" +
            "        \"code\":\"ZAR\",\n" +
            "        \"mid\":0.2628\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"real (Brazylia)\",\n" +
            "        \"code\":\"BRL\",\n" +
            "        \"mid\":0.6861\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"ringgit (Malezja)\",\n" +
            "        \"code\":\"MYR\",\n" +
            "        \"mid\":0.8883\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"rubel rosyjski\",\n" +
            "        \"code\":\"RUB\",\n" +
            "        \"mid\":0.0501\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"rupia indonezyjska\",\n" +
            "        \"code\":\"IDR\",\n" +
            "        \"mid\":0.00025634\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"rupia indyjska\",\n" +
            "        \"code\":\"INR\",\n" +
            "        \"mid\":0.05048\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"won południowokoreański\",\n" +
            "        \"code\":\"KRW\",\n" +
            "        \"mid\":0.003264\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"yuan renminbi (Chiny)\",\n" +
            "        \"code\":\"CNY\",\n" +
            "        \"mid\":0.5722\n" +
            "      },\n" +
            "      {\n" +
            "        \"currency\":\"SDR (MFW)\",\n" +
            "        \"code\":\"XDR\",\n" +
            "        \"mid\":5.3205\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]";
}


