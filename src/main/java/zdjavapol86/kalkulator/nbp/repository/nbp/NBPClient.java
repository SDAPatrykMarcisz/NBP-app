package zdjavapol86.kalkulator.nbp.repository.nbp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import zdjavapol86.kalkulator.nbp.model.dto.ExchangeRate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NBPClient {

    public List<ExchangeRate> downloadCurrentExchangeRates(CurrencyTable table) {
        return callApi("/api/exchangerates/tables/" + table.getTableCode());
    }

    public List<ExchangeRate> downloadExchangeRateForDay(CurrencyTable table, LocalDate date) {
        return callApi("/api/exchangerates/tables/" + table.getTableCode() + "/" + date.format(DateTimeFormatter.ISO_DATE));
    }

    private List<ExchangeRate> callApi(String endpoint) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(URI.create("http://api.nbp.pl/" + endpoint))
                .build();
        try {
            System.out.println("calling endpoint " + endpoint);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            ObjectMapper mapper = new ObjectMapper();
            List<ExchangeRate> result = mapper.readValue(json, new TypeReference<>() {});
            return result;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
        throw new RuntimeException("Exchange rate not found");
    }

}
