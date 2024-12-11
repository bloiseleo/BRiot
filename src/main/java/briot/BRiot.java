package briot;

import briot.apis.v1.Account;
import briot.apis.v4.ChampionMaestry;
import briot.models.Regions;
import briot.models.RiotCredentials;
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
    private static boolean hasShortRegionInRegion(ShortRegions[] valids, ShortRegions shortRegion) {
        Arrays.sort(valids);
        return Arrays.binarySearch(valids, shortRegion) >= 0;
    }
    private static void validateRegion(Regions region, ShortRegions shortRegions) {
        switch (region) {
            case AMERICAS -> {
                var validAmericaShortRegions = new ShortRegions[] {
                        ShortRegions.NA1,
                        ShortRegions.BR1,
                        ShortRegions.LAN,
                        ShortRegions.LA1,
                        ShortRegions.LA2,
                };
                if (!hasShortRegionInRegion(validAmericaShortRegions, shortRegions)) {
                    throw new InvalidShortRegionForRegion(region, shortRegions, validAmericaShortRegions);
                }
            }
            case ASIA -> {
                var validAsiaShortRegions = new ShortRegions[] {
                        ShortRegions.JP1,
                        ShortRegions.KR
                };
                if (!hasShortRegionInRegion(validAsiaShortRegions, shortRegions)) {
                    throw new InvalidShortRegionForRegion(region, shortRegions, validAsiaShortRegions);
                }
            }
            case EUROPE -> {
                var validEuropeShortRegions = new ShortRegions[] {
                        ShortRegions.EUN1,
                        ShortRegions.EUW1,
                        ShortRegions.ME1,
                        ShortRegions.TR1,
                        ShortRegions.RU,
                };
                if (!hasShortRegionInRegion(validEuropeShortRegions, shortRegions)) {
                    throw new InvalidShortRegionForRegion(region, shortRegions, validEuropeShortRegions);
                }
            }
        }
    }
    public static BRiot withCredentials(String apikey, Regions regions, ShortRegions shortRegions) {
        validateRegion(regions, shortRegions);
        return new BRiot(new RiotCredentials(apikey, regions, shortRegions), HttpClient.newBuilder().build(), new RiotErrorFactory());
    }
}
