package briot;

import briot.apis.v1.Account;
import briot.models.Regions;
import briot.models.RiotCredentials;
import java.net.http.HttpClient;
import java.util.Optional;

public class BRiot {
    private RiotCredentials credentials;
    private HttpClient httpClient;
    private RiotErrorFactory errorFactory;
    private BRiot(RiotCredentials credentials, HttpClient httpClient, RiotErrorFactory errorFactory) {
        this.credentials = credentials;
        this.httpClient = httpClient;
        this.errorFactory = errorFactory;
    }
    public Account getAccountApi() {
        return new Account(
                new RiotHttpClient(
                        credentials,
                        errorFactory,
                        httpClient
                )
        );
    }
    private static BRiot instance = null;
    public static BRiot withCredentials(String apikey, Regions regions) {
        if (instance != null) {
            throw new RuntimeException("BRiot already created");
        }
        var creds = new RiotCredentials(apikey, regions);
        var briot = new BRiot(creds, HttpClient.newBuilder().build(), new RiotErrorFactory());
        instance = briot;
        return instance;
    }
    public static Optional<BRiot> getInstance() {
        return Optional.ofNullable(instance);
    }
}
