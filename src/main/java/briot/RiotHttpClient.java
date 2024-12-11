package briot;

import briot.models.RiotCredentials;
import briot.models.errors.RiotApiError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RiotHttpClient {
    private final RiotCredentials credentials;
    private final RiotErrorFactory riotErrorFactory;
    private final HttpClient client;
    public RiotHttpClient(
            RiotCredentials credentials,
            RiotErrorFactory riotErrorFactory,
            HttpClient httpClient
    ) {
        this.credentials = credentials;
        this.riotErrorFactory = riotErrorFactory;
        this.client = httpClient;
    }
    private HttpRequest buildGetRequest(URI uri) {
        return HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header("X-Riot-Token", credentials.apiKey())
                .build();
    }
    public <T> T get(URI route, Class<T> responseType) throws RiotApiError, RuntimeException {
        try {
            HttpRequest request = buildGetRequest(route);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw this.riotErrorFactory.create(response);
            }
            var gson = new Gson();
            return gson.fromJson(response.body(), responseType);
        } catch (RiotApiError error) {
            throw error;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public <T> T get(URI route, TypeToken<T> responseType) throws RiotApiError, RuntimeException {
        try {
            HttpRequest request = buildGetRequest(route);
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) {
                throw this.riotErrorFactory.create(response);
            }
            var gson = new Gson();
            return gson.fromJson(response.body(), responseType);
        } catch (RiotApiError error) {
            throw error;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
