package briot.apis.v1;

import briot.RiotHttpClient;
import briot.helpers.URIHelpers;
import briot.models.Regions;
import briot.models.ShortRegions;
import briot.models.errors.NotFoundException;
import briot.models.errors.RiotApiError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

public class AccountTests {
    @Test
    public void givenPuuid_whenByPuuid_thenThrowNotFoundException() {
        String puuid = "INVALID-PUUID";
        RiotHttpClient client = Mockito.mock(RiotHttpClient.class);
        Account account = new Account(client, Regions.AMERICAS, ShortRegions.BR1);
        var uri = URIHelpers.createURI(
                String.format("https://%s.api.riotgames.com", Regions.AMERICAS.name().toLowerCase()),
                "/riot/account/v1/accounts/by-puuid/" + puuid
        );
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
        var uri = URIHelpers.createURI(
                String.format("https://%s.api.riotgames.com", Regions.AMERICAS.name().toLowerCase()),
                "/riot/account/v1/accounts/by-puuid/" + puuid
        );
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
        var uri = URIHelpers.createURI(
                String.format("https://%s.api.riotgames.com", Regions.AMERICAS.name().toLowerCase()),
                "/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine
        );
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
    @ParameterizedTest
    @MethodSource("briot.ErrorArgumentFactory#errorFactory")
    public void  givenInvalidGameNameAndTagLine_whenByRiotId_thenThrowError(Class<? extends RiotApiError> riotApiError) {
        String gameName = "invalidGameName";
        String tagLine = "invalidTagLine";
        RiotHttpClient client = Mockito.mock(RiotHttpClient.class);
        Account accountApiV1 = new Account(client, Regions.AMERICAS, ShortRegions.BR1);
        var uri = URIHelpers.createURI(
                String.format("https://%s.api.riotgames.com", Regions.AMERICAS.name().toLowerCase()),
                "/riot/account/v1/accounts/by-riot-id/" + gameName + "/" + tagLine
        );
        Mockito.when(client.get(
                        uri,
                        Account.AccountDTO.class
                )
        ).thenThrow(riotApiError);
        Assertions.assertThrowsExactly(riotApiError, () -> accountApiV1.byRiotId(gameName, tagLine));
    }
}
