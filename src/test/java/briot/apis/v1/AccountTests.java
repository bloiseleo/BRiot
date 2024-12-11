package briot.apis.v1;

import briot.RiotHttpClient;
import briot.models.errors.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AccountTests {
    @Test
    public void givenPuuid_whenByPuuid_thenReturnAccoutInformation() {
        String puuid = "PUUID-TEST";
        RiotHttpClient client = Mockito.mock(RiotHttpClient.class);
        Account.AccountDTO response = new Account.AccountDTO(puuid, "GAME-NAME", "5423");
        Account accountv1 = new Account(client);
        Mockito.when(
                client.get(
                        String.format("/riot/account/v1/accounts/by-puuid/%s", puuid),
                        Account.AccountDTO.class
                )
        ).then(invocationOnMock -> response);

        var accountDTO = accountv1.byPuuid(puuid);

        Assertions.assertNotNull(accountDTO);
        Assertions.assertEquals(response.puuid(), accountDTO.puuid());
        Assertions.assertEquals(response.gameName(), accountDTO.gameName());
        Assertions.assertEquals(response.tagLine(), accountDTO.tagLine());
        Mockito.verify(client, Mockito.times(1)).get(String.format("/riot/account/v1/accounts/by-puuid/%s", puuid), Account.AccountDTO.class);
    }
    @Test
    public void givenGameNameAndTagLine_whenByRiotId_thenReturnAccountInformation() {
        String gameName = "testGameName";
        String tagLine = "testTagLine";
        RiotHttpClient client = Mockito.mock(RiotHttpClient.class);
        Account.AccountDTO response = new Account.AccountDTO("test-puuid", gameName, String.format("#%s", tagLine));
        Account accountv1 = new Account(client);
        Mockito.when(client.get(
                String.format("/riot/account/v1/accounts/by-riot-id/%s/%s", gameName, tagLine),
                Account.AccountDTO.class
            )
        ).then(invocationOnMock -> response);

        var accountDTO = accountv1.byRiotId(gameName, tagLine);

        Assertions.assertEquals(response.puuid(), accountDTO.puuid());
        Assertions.assertEquals(response.gameName(), accountDTO.gameName());
        Assertions.assertEquals(response.tagLine(), accountDTO.tagLine());
        Mockito.verify(client, Mockito.times(1)).get(String.format("/riot/account/v1/accounts/by-riot-id/%s/%s", gameName, tagLine), Account.AccountDTO.class);
    }
    @Test
    public void  givenInvalidGameNameAndTagLine_whenByRiotId_thenThrowNotFound() {
        String gameName = "invalidGameName";
        String tagLine = "invalidTagLine";
        RiotHttpClient client = Mockito.mock(RiotHttpClient.class);
        Account accountApiV1 = new Account(client);
        Mockito.when(client.get(
                        String.format("/riot/account/v1/accounts/by-riot-id/%s/%s", gameName, tagLine),
                        Account.AccountDTO.class
                )
        ).thenThrow(NotFoundException.class);
        Assertions.assertThrowsExactly(NotFoundException.class, () -> accountApiV1.byRiotId(gameName, tagLine));
    }
}
