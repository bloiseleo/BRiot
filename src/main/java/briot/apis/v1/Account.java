package briot.apis.v1;

import briot.RiotHttpClient;
import briot.apis.RiotAPI;
import briot.models.Regions;
import briot.models.ShortRegions;
import briot.models.errors.RiotApiError;

public class Account extends RiotAPI {
    public record AccountDTO(String puuid, String gameName, String tagLine) { };
    public Account(RiotHttpClient client, Regions region, ShortRegions shortRegions) {
        super(client, region, shortRegions);
    }
    public AccountDTO byRiotId(String gameName, String tagLine) throws RiotApiError, RuntimeException {
        var uri = createUriFromRouteAndRegion("/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine, this.region);
        return client.get(uri, AccountDTO.class);
    }
    public AccountDTO byPuuid(String puuid) throws RiotApiError, RuntimeException {
        var uri = createUriFromRouteAndRegion("/riot/account/v1/accounts/by-puuid/" + puuid, this.region);
        return client.get(uri, AccountDTO.class);
    }
}
