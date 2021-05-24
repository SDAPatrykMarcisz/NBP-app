package zdjavapol79.kalkulator.nbp.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import zdjavapol79.kalkulator.nbp.model.dto.ExchangeRate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NBPClient {

    public ExchangeRate[] downloadExchangeRates(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://api.nbp.pl/api/exchangerates/tables/A?format=json"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            ObjectMapper mapper = new ObjectMapper();
            ExchangeRate[] result = mapper.readValue(json, ExchangeRate[].class);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ExchangeRate[0];
    }
}
