package briot.apis.v1;

import briot.RiotHttpClient;
import briot.models.errors.RiotApiError;

public class Account {
    public record AccountDTO(String puuid, String gameName, String tagLine) { };
    private final RiotHttpClient client;
    public Account(RiotHttpClient client) {
        this.client = client;
    }
    public AccountDTO byRiotId(String gameName, String tagLine) throws RiotApiError, RuntimeException {
        return client.get("/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine, AccountDTO.class);
    }
}
