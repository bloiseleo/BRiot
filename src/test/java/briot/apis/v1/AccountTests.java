package briot.apis.v1;

import briot.RiotHttpClient;
import briot.models.Regions;
import briot.models.ShortRegions;
import briot.models.errors.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;

public class AccountTests {
    @Test
    public void givenPuuid_whenByPuuid_thenThrowNotFoundException() {
        String puuid = "INVALID-PUUID";
        RiotHttpClient client = Mockito.mock(RiotHttpClient.class);
        Account account = new Account(client, Regions.AMERICAS, ShortRegions.BR1);
        var uri = URI.create(String.format("/riot/account/v1/accounts/by-puuid/%s", puuid));
        Mockito.when(
                client.get(
                        uri,
                        Account.AccountDTO.class
                )
        ).thenThrow(NotFoundException.class);

        Assertions.assertThrows(NotFoundException.class, () -> account.byPuuid(puuid));
        Mockito.verify(client, Mockito.times(1)).get(uri, Account.AccountDTO.class);
    }
    @Test
    public void givenPuuid_whenByPuuid_thenReturnAccoutInformation() {
        String puuid = "PUUID-TEST";
        RiotHttpClient client = Mockito.mock(RiotHttpClient.class);
        Account.AccountDTO response = new Account.AccountDTO(puuid, "GAME-NAME", "5423");
        Account accountv1 = new Account(client, Regions.AMERICAS, ShortRegions.BR1);
        var uri = URI.create(String.format("/riot/account/v1/accounts/by-puuid/%s", puuid));
        Mockito.when(
                client.get(
                        uri,
                        Account.AccountDTO.class
                )
        ).then(invocationOnMock -> response);

        var accountDTO = accountv1.byPuuid(puuid);

        Assertions.assertNotNull(accountDTO);
        Assertions.assertEquals(response.puuid(), accountDTO.puuid());
        Assertions.assertEquals(response.gameName(), accountDTO.gameName());
        Assertions.assertEquals(response.tagLine(), accountDTO.tagLine());
        Mockito.verify(client, Mockito.times(1)).get(uri, Account.AccountDTO.class);
    }
    @Test
    public void givenGameNameAndTagLine_whenByRiotId_thenReturnAccountInformation() {
        String gameName = "testGameName";
        String tagLine = "testTagLine";
        RiotHttpClient client = Mockito.mock(RiotHttpClient.class);
        Account.AccountDTO response = new Account.AccountDTO("test-puuid", gameName, String.format("#%s", tagLine));
        var uri = URI.create(String.format("/riot/account/v1/accounts/by-riot-id/%s/%s", gameName, tagLine));
        Account accountv1 = new Account(client, Regions.AMERICAS, ShortRegions.BR1);
        Mockito.when(client.get(
                uri,
                Account.AccountDTO.class
            )
        ).then(invocationOnMock -> response);

        var accountDTO = accountv1.byRiotId(gameName, tagLine);

        Assertions.assertEquals(response.puuid(), accountDTO.puuid());
        Assertions.assertEquals(response.gameName(), accountDTO.gameName());
        Assertions.assertEquals(response.tagLine(), accountDTO.tagLine());
        Mockito.verify(client, Mockito.times(1)).get(uri, Account.AccountDTO.class);
    }
    @Test
    public void  givenInvalidGameNameAndTagLine_whenByRiotId_thenThrowNotFound() {
        String gameName = "invalidGameName";
        String tagLine = "invalidTagLine";
        RiotHttpClient client = Mockito.mock(RiotHttpClient.class);
        Account accountApiV1 = new Account(client, Regions.AMERICAS, ShortRegions.BR1);
        var uri = URI.create(String.format("/riot/account/v1/accounts/by-riot-id/%s/%s", gameName, tagLine));
        Mockito.when(client.get(
                        uri,
                        Account.AccountDTO.class
                )
        ).thenThrow(NotFoundException.class);
        Assertions.assertThrowsExactly(NotFoundException.class, () -> accountApiV1.byRiotId(gameName, tagLine));
    }
}
