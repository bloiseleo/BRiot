package briot.apis;

import briot.events.DataDragonObservableThread;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class DataDragonAPITests {
    private InputStream createInputStream() {
        List<String> result = Arrays.asList("14.24.1", "14.24.0");
        String data = new Gson().toJson(result);
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }
    private InputStream createInputStream(int byteSize) {
        Random random = new Random();
        byte[] b = new byte[byteSize];
        random.nextBytes(b);
        return new ByteArrayInputStream(b);
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
    @Test
    public void givenVersion_whenDragonTail_ThenDownloadDragonTailFile() throws IOException, InterruptedException {
        String version = "14.24.1";
        AtomicBoolean executed = new AtomicBoolean(false);
        HttpClient client = Mockito.mock(HttpClient.class);
        DataDragonAPI dataDragonAPI = new DataDragonAPI(client);
        HttpResponse response = Mockito.mock(HttpResponse.class);
        HttpHeaders headers = Mockito.mock(HttpHeaders.class);
        Mockito.when(client.send(Mockito.any(HttpRequest.class), Mockito.any(HttpResponse.BodyHandlers.ofInputStream().getClass()))).then(invocationOnMock -> response);
        Mockito.when(response.statusCode()).thenReturn(200);
        Mockito.when(response.body()).then(invocationOnMock -> createInputStream(1024));
        Mockito.when(response.headers()).then(invocationOnMock -> headers);
        Mockito.when(headers.firstValue("Content-Type")).then(invocationOnMock -> Optional.of("application/x-tar"));
        DataDragonObservableThread.HandleDataDragonDownloadObserver observer = Mockito.mock(DataDragonObservableThread.HandleDataDragonDownloadObserver.class);
        Mockito.doAnswer(anwser -> {
            executed.set(true);
            return null;
        }).when(observer).handleDataDragonDownloadFinished(Mockito.any(File.class));
        dataDragonAPI.dragonTail(version, observer);
        while(!executed.get()) { }
        ArgumentCaptor<File> captor = ArgumentCaptor.forClass(File.class);
        Mockito.verify(observer, Mockito.times(1)).handleDownloadStarted();
        Mockito.verify(observer, Mockito.times(1)).handleDataDragonDownloadFinished(captor.capture());
        Mockito.verify(observer, Mockito.times(0)).handleDataDragonDownloadError(Mockito.any(Exception.class));
        File dragontailFile = captor.getValue();
        Assertions.assertTrue(dragontailFile.exists());
        Assertions.assertTrue(dragontailFile.isFile());
        Assertions.assertTrue(dragontailFile.canRead());
        Assertions.assertTrue(dragontailFile.toString().contains(version));
    }
}
