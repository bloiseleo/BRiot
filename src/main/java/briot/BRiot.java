package briot;

import briot.apis.DataDragonAPI;
import briot.apis.v1.Account;
import briot.apis.v4.ChampionMaestry;
import briot.events.DataDragonObservableThread;
import briot.models.Regions;
import briot.models.RiotCredentials;
import briot.models.RiotRegion;
import briot.models.ShortRegions;
import briot.models.errors.InvalidShortRegionForRegion;

import java.io.File;
import java.net.http.HttpClient;
import java.security.PrivilegedAction;
import java.util.Arrays;

public class BRiot {
    private RiotCredentials credentials;
    private HttpClient httpClient;
    private RiotErrorFactory errorFactory;
    private BRiot(RiotCredentials credentials, HttpClient httpClient, RiotErrorFactory errorFactory) {
        this.credentials = credentials;
        this.httpClient = httpClient;
        this.errorFactory = errorFactory;
    }

    public static void main(String[] args) throws InterruptedException {
        DataDragonAPI dataDragonAPI = new DataDragonAPI(HttpClient.newBuilder().build());
        dataDragonAPI.dragonTail("14.24.1", new DataDragonObservableThread.HandleDataDragonDownloadObserver() {
            @Override
            public void handleDownloadStarted() {
                System.out.println("Download started! It can take some time until it completes. Pour a coffee...");
            }

            @Override
            public void handleDataDragonDownloadFinished(File dataDragonFile) {
                System.out.println("File downloaded and stored at " + dataDragonFile.getAbsolutePath());
            }

            @Override
            public void handleDataDragonDownloadError(Exception e) {
                System.out.println("Error while downloading the file :( -> " + e.getMessage());
            }
        });
    }
    public ChampionMaestry getChampioMaestryApi() {
        return new ChampionMaestry(
                new RiotHttpClient(
                        credentials,
                        errorFactory,
                        httpClient
                ),
                credentials.region(),
                credentials.shortRegion()
        );
    }
    public Account getAccountApi() {
        return new Account(
                new RiotHttpClient(
                        credentials,
                        errorFactory,
                        httpClient
                ),
                credentials.region(),
                credentials.shortRegion()

        );
    }
    public static BRiot withCredentials(String apikey, Regions regions, ShortRegions shortRegions) {
        return new BRiot(new RiotCredentials(apikey, regions, shortRegions), HttpClient.newBuilder().build(), new RiotErrorFactory());
    }
}
