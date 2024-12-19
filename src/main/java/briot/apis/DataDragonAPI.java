package briot.apis;

import briot.helpers.URIHelpers;
import briot.models.errors.BadRequestException;
import briot.models.errors.InternalServerErrorException;
import briot.models.errors.RequestRedirectedException;
import briot.models.errors.RiotApiError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.xml.crypto.Data;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DataDragonAPI {
    private final String baseUrl = "https://ddragon.leagueoflegends.com";
    private final HttpClient client;
    public DataDragonAPI(HttpClient client) {
        this.client = client;
    }
    private InputStream processRequestAndExtractBody(HttpResponse<InputStream> response) {
        if (response.statusCode() >= 300 && response.statusCode() < 400) {
            throw new RequestRedirectedException("RequestRedirectedException while trying to retrieve the latest version of data dragon");
        }
        if (response.statusCode() >= 400 && response.statusCode() < 500) {
            throw new BadRequestException("BadRequestException while trying to retrieve the latest version of data dragon");
        }
        if (response.statusCode() >= 500) {
            throw new InternalServerErrorException("InternalServerErrorException while trying to retrieve the latest version of data dragon");
        }
        return response.body();
    }
    public List<String> getVersion() {
        URI uri = URIHelpers.createURI(baseUrl, "/api/versions.json");
        try {
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            InputStream body = processRequestAndExtractBody(response);
            TypeToken<List<String>> listTypeToken = new TypeToken<>() {};
            Gson gson = new Gson();
            return gson.fromJson(new InputStreamReader(body), listTypeToken);
        } catch (RiotApiError exception) {
            throw exception;
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
