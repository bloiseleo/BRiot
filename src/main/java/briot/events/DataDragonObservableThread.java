package briot.events;

import briot.helpers.FileUtils;

import java.io.File;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class DataDragonObservableThread extends Thread {
    public interface HandleDataDragonDownloadObserver {
        void handleDownloadStarted();
        void handleDataDragonDownloadFinished(File dataDragonFile);
        void handleDataDragonDownloadError(Exception e);
    }
    private final Collection<HandleDataDragonDownloadObserver> observers = new ArrayList<>();
    private final HttpResponse<InputStream> response;
    private final String fileName;
    public DataDragonObservableThread(HttpResponse<InputStream> response, String fileName) {
        this.response = response;
        this.fileName = fileName;
    }
    private void onDownloadStarted() {
        observers.forEach(observer -> {
            CompletableFuture.supplyAsync(() -> {
                observer.handleDownloadStarted();
                return null;
            });
        });
    }
    private void onDownloadComplete(File dataDragonFile) {
        observers.forEach(observer -> {
            CompletableFuture.supplyAsync(() -> {
                observer.handleDataDragonDownloadFinished(dataDragonFile);
                return null;
            });
        });
    }
    private void onDownloadError(Exception error) {
        observers.forEach(observer -> {
            CompletableFuture.supplyAsync(() -> {
                observer.handleDataDragonDownloadError(error);
                return null;
            });
        });
    }
    public void observe(HandleDataDragonDownloadObserver observer) {
        observers.add(observer);
    }
    @Override
    public void run() {
        File datadragonfile = FileUtils.createTempFileFromResponse(response, fileName);
        InputStream data = response.body();
        try {
            onDownloadStarted();
            Files.copy(data, datadragonfile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            data.close();
            onDownloadComplete(datadragonfile);
        } catch (Exception e) {
            onDownloadError(e);
        }
    }
}
