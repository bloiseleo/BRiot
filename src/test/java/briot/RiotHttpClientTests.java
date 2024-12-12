package briot;

import briot.models.Regions;
import briot.models.RiotCredentials;
import briot.models.ShortRegions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RiotHttpClientTests {
    record TestDTO(String test) { }
    @Test
    public void givenUriAndReturnClass_whenGet_thenInjectCredentialsAtRequestHeader() throws IOException, InterruptedException {
        RiotCredentials riotCredentials = new RiotCredentials("TEST-API-KEY", Regions.AMERICAS, ShortRegions.BR1);
        RiotErrorFactory riotErrorFactory = new RiotErrorFactory();
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("{\"test\": \"api_result\"}");
        Mockito.when(
                httpClient.send(Mockito.any(HttpRequest.class), Mockito.eq(HttpResponse.BodyHandlers.ofString()))
        ).then(invocationOnMock -> httpResponse);
        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        URI uri = URI.create("http://test.com/route");
        RiotHttpClient client = new RiotHttpClient(
                riotCredentials,
                riotErrorFactory,
                httpClient
        );
        TestDTO data = client.get(uri, TestDTO.class);
        Mockito.verify(
                httpClient,
                Mockito.times(1)
        ).send(requestCaptor.capture(), Mockito.eq(HttpResponse.BodyHandlers.ofString()));
        var request = requestCaptor.getValue();
        var riotToken = request.headers().firstValue("X-Riot-Token");
        Assertions.assertTrue(riotToken.isPresent());
        Assertions.assertEquals(riotToken.get(), riotCredentials.apiKey());
        Assertions.assertNotNull(data);
        Assertions.assertEquals("api_result", data.test());
        Assertions.assertEquals(uri, request.uri());
    }
}
