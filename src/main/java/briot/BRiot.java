package briot;

import briot.apis.v1.Account;
import briot.apis.v4.ChampionMaestry;
import briot.models.Regions;
import briot.models.RiotCredentials;
import briot.models.RiotRegion;
import briot.models.ShortRegions;
import briot.models.errors.InvalidShortRegionForRegion;

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
