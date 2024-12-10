package briot;

import briot.models.Regions;
import briot.models.RiotCredentials;
import briot.models.errors.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

public class RiotHttpClientTests {
    record TestDTO(int a) { }
    static Stream<Arguments> routesProvider() {
        return Stream.of(
                Arguments.arguments("TEST-API-KEY", Regions.AMERICAS, "test"),
                Arguments.arguments("TEST-API-KEY-TWO", Regions.ASIA, "/test"),
                Arguments.arguments("TEST-API-KEY-THREE", Regions.ESPORTS, "espostsToTheGame"),
                Arguments.arguments("TEST-API-KEY-FOUR", Regions.EUROPE, "europe-server")
        );
    }
    static Stream<Arguments> statusCodeAndExceptionProvider() {
        return Stream.of(
            Arguments.of(
                    300, RequestRedirectedException.class
            ), Arguments.of(
                    400, BadRequestException.class
            ), Arguments.of(
                    500, InternalServerErrorException.class
            ), Arguments.of(
                    401, BadRequestException.class
            ), Arguments.of(
                    403, BadRequestException.class
            ), Arguments.of(
                    404, NotFoundException.class
            ), Arguments.of(
                    405, BadRequestException.class
            ), Arguments.of(
                    415, BadRequestException.class
            ), Arguments.of(
                    429, BadRequestException.class
            ), Arguments.of(
                    502, InternalServerErrorException.class
            ), Arguments.of(
                    503, InternalServerErrorException.class
            ),Arguments.of(
                    504, InternalServerErrorException.class
            )
        );
    }
    static String removeSlash(String route) {
        return route.startsWith("/") ? route.substring(1) : route;
    }
    @ParameterizedTest
    @MethodSource("statusCodeAndExceptionProvider")
    public void givenGetFailRequest_whenAnyRoute_thenRiotApiErrorShouldBeThrown(int statusCode, Class<? extends RiotApiError> exceptionClass) throws IOException, InterruptedException {
        // Arrange
        RiotCredentials creds = new RiotCredentials("TEST-API-KEY", Regions.AMERICAS);
        RiotErrorFactory riotErrorFactory = new RiotErrorFactory();
        HttpClient client = Mockito.mock(HttpClient.class);
        RiotHttpClient riotHttpClient = new RiotHttpClient(creds, riotErrorFactory, client);
        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(httpResponse.statusCode()).thenReturn(statusCode);
        Mockito.when(httpResponse.body()).thenReturn("{'test': 'test'}");
        Mockito.when(client.send(
                Mockito.any(HttpRequest.class),
                Mockito.eq(HttpResponse.BodyHandlers.ofString())
        )).then(invocationOnMock -> httpResponse);
        // Act and Assert
        Assertions.assertThrowsExactly(exceptionClass, () -> riotHttpClient.get("/test", TestDTO.class));
    }
    @ParameterizedTest
    @MethodSource("routesProvider")
    public void givenGetRequest_whenAnyRoute_thenRequestContaingXRiotTokenAndParameterRouteWasCalledWithCorrectRegionAndCorrectParsingAplied(
            String apiKey,
            Regions region,
            String route
    ) throws IOException, InterruptedException {
        // Arrange
        RiotCredentials creds = new RiotCredentials(apiKey, region);
        RiotErrorFactory errorFactory = Mockito.mock(RiotErrorFactory.class);
        HttpClient client = Mockito.mock(HttpClient.class);
        ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
        var responseMock = Mockito.mock(HttpResponse.class);
        var riotClient = new RiotHttpClient(
                creds,
                errorFactory,
                client
        );
        Mockito.when(responseMock.statusCode()).thenReturn(200);
        Mockito.when(responseMock.body()).thenReturn("{\"a\":1}");
        Mockito.when(
                client.send(
                        Mockito.any(HttpRequest.class),
                        Mockito.eq(HttpResponse.BodyHandlers.ofString())
                )
        ).then(answer -> responseMock);
        // Act
        TestDTO response = riotClient.get(route, TestDTO.class);
        // Assert
        Mockito.verify(client, Mockito.times(1)).send(requestCaptor.capture(), Mockito.eq(HttpResponse.BodyHandlers.ofString()));
        var request = requestCaptor.getValue();
        var riotToken = request.headers().firstValue("X-Riot-Token");
        URI uri = request.uri();
        Assertions.assertEquals(String.format("/%s", removeSlash(route)), uri.getPath());
        Assertions.assertEquals("https", uri.getScheme());
        Assertions.assertEquals(String.format("%s.api.riotgames.com", region.name().toLowerCase()), uri.getHost());
        Assertions.assertTrue(riotToken.isPresent());
        Assertions.assertEquals(creds.apiKey(), riotToken.get());
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.a());
        Mockito.verify(errorFactory, Mockito.never()).create(Mockito.any());
    }
}
