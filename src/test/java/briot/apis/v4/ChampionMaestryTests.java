package briot.apis.v4;

import briot.RiotHttpClient;
import briot.helpers.URIHelpers;
import briot.models.Regions;
import briot.models.ShortRegions;
import briot.models.errors.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class ChampionMaestryTests {
    @Test
    public void givenChampionMaestry_whenByPuuid_thenReturnChampionMaestry() {
        String puuid = "PUUID";
        URI uri = URIHelpers.createURI(
                "https://br1.api.riotgames.com",
                "/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid
        );
        RiotHttpClient riotHttpClient = Mockito.mock(RiotHttpClient.class);
        ChampionMaestry championMaestry = new ChampionMaestry(
                riotHttpClient, Regions.AMERICAS, ShortRegions.BR1
        );
        ChampionMaestry.ChampionMaestryDTO championMaestryDTO = Mockito.mock(ChampionMaestry.ChampionMaestryDTO.class);
        Mockito.when(championMaestryDTO.puuid()).thenReturn(puuid);
        TypeToken<Collection<ChampionMaestry.ChampionMaestryDTO>> typeToken = new TypeToken<>() {};
        Mockito.when(
                riotHttpClient.get(
                    uri, typeToken
                )
        ).then(invocationOnMock -> List.of(championMaestryDTO));
        Collection<ChampionMaestry.ChampionMaestryDTO> maestries = championMaestry.byPuuid(puuid);
        Assertions.assertEquals(1, maestries.size());
        Assertions.assertEquals(championMaestryDTO.puuid(), maestries.stream().findFirst().get().puuid());
    }

    @ParameterizedTest
    @MethodSource("briot.ErrorArgumentFactory#errorFactory")
    public void givenChampionMaestry_whenByPuuid_thenThrowsRiotExceptions(Class<? extends RiotApiError> errorClass) {
        String puuid = "NOT-FOUND";
        URI uri = URIHelpers.createURI(
                "https://br1.api.riotgames.com",
                "/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid
        );
        RiotHttpClient riotHttpClient = Mockito.mock(RiotHttpClient.class);
        TypeToken<Collection<ChampionMaestry.ChampionMaestryDTO>> typeToken = new TypeToken<>() {};
        ChampionMaestry championMaestry = new ChampionMaestry(
                riotHttpClient, Regions.AMERICAS, ShortRegions.BR1
        );

        Mockito.when(riotHttpClient.get(uri, typeToken)).thenThrow(errorClass);

        Assertions.assertThrows(errorClass, () -> championMaestry.byPuuid(puuid));
    }
}
