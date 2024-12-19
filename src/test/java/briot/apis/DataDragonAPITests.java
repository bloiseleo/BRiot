package briot.apis;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class DataDragonAPITests {
    private InputStream createInputStream() {
        List<String> result = Arrays.asList("14.24.1", "14.24.0");
        String data = new Gson().toJson(result);
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }
    @Test
    public void whenGetVersion_ThenReturnAllDataDragonVersions() throws IOException, InterruptedException {
        HttpClient client = Mockito.mock(HttpClient.class);
        DataDragonAPI dataDragonAPI = new DataDragonAPI(client);
        HttpResponse response = Mockito.mock(HttpResponse.class);
        Mockito.when(response.statusCode()).thenReturn(200);
        Mockito.when(response.body()).then(invocationOnMock -> createInputStream());
        Mockito.when(
                client.send(
                        Mockito.any(HttpRequest.class),
                        Mockito.any(HttpResponse.BodyHandler.class)
                )
        ).then(invocationOnMock -> response);
        List<String> versions = dataDragonAPI.getVersion();
        Assertions.assertEquals("14.24.1", versions.get(0));
        Assertions.assertEquals(2, versions.size());
        ArgumentCaptor<HttpRequest> captor = ArgumentCaptor.forClass(HttpRequest.class);
        Mockito.verify(client, Mockito.times(1)).send(captor.capture(), Mockito.any(HttpResponse.BodyHandler.class));
        HttpRequest request = captor.getValue();
        Assertions.assertEquals("https://ddragon.leagueoflegends.com/api/versions.json", request.uri().toString());
    }
}
