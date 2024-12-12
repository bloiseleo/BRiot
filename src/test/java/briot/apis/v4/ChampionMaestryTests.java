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
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChampionMaestryTests {
    @Test
    public void givenChampionMaestry_whenbyPuuidTotalScore_thenReturnTotalScore() {
        String puuid = "PUUID";
        URI uri = URIHelpers.createURI(
                "https://br1.api.riotgames.com",
                String.format("/lol/champion-mastery/v4/scores/by-puuid/%s", puuid)
        );
        RiotHttpClient riotHttpClient = Mockito.mock(RiotHttpClient.class);
        ChampionMaestry championMaestry = new ChampionMaestry(
                riotHttpClient, Regions.AMERICAS, ShortRegions.BR1
        );
        Mockito.when(
                riotHttpClient.get(uri, Integer.class)
        ).then(invocationOnMock -> 1);
        Integer score = championMaestry.byPuuidScore(puuid);

        Assertions.assertEquals(Integer.valueOf(1), score);
        Mockito.verify(riotHttpClient, Mockito.times(1)).get(uri, Integer.class);
    }
    @Test
    public void givenChampionMaestry_whenByPuuidTop_thenReturnChampionMaestryList() {
        String puuid = "PUUID";
        URI uri = URIHelpers.createURI(
                "https://br1.api.riotgames.com",
                String.format("/lol/champion-mastery/v4/champion-masteries/by-puuid/%s/top", puuid)
        );
        RiotHttpClient riotHttpClient = Mockito.mock(RiotHttpClient.class);
        ChampionMaestry championMaestry = new ChampionMaestry(
                riotHttpClient, Regions.AMERICAS, ShortRegions.BR1
        );
        ChampionMaestry.ChampionMaestryDTO championMaestryDTO = Mockito.mock(ChampionMaestry.ChampionMaestryDTO.class);
        Mockito.when(championMaestryDTO.puuid()).thenReturn(puuid);
        Mockito.when(championMaestryDTO.championId()).thenReturn(1L);
        Collection<ChampionMaestry.ChampionMaestryDTO> championMaestryDTOS = new ArrayList<>();
        championMaestryDTOS.add(championMaestryDTO);
        TypeToken<Collection<ChampionMaestry.ChampionMaestryDTO>> typeToken = new TypeToken<>() {};
        Mockito.when(
                riotHttpClient.get(uri, typeToken)
        ).then(invocationOnMock -> championMaestryDTOS);

        Collection<ChampionMaestry.ChampionMaestryDTO> maestries = championMaestry.byPuuidTop(puuid);

        Assertions.assertNotNull(maestries);
        Assertions.assertEquals(1, maestries.size());
        ChampionMaestry.ChampionMaestryDTO maestry = maestries.stream().findFirst().get();
        Assertions.assertEquals(puuid, maestry.puuid());
        Assertions.assertEquals(1L, maestry.championId());
        Mockito.verify(
                riotHttpClient, Mockito.times(1)
        ).get(uri, typeToken);
    }
    @Test
    public void givenChampionMaestry_whenByPuuidAndChampionId_thenReturnChampionMaestry() {
        String puuid = "PUUID";
        int championId = 1;
        URI uri = URIHelpers.createURI(
                "https://br1.api.riotgames.com",
                String.format("/lol/champion-mastery/v4/champion-masteries/by-puuid/%s/by-champion/%s", puuid, championId)
        );
        RiotHttpClient riotHttpClient = Mockito.mock(RiotHttpClient.class);
        ChampionMaestry championMaestry = new ChampionMaestry(
                riotHttpClient, Regions.AMERICAS, ShortRegions.BR1
        );
        ChampionMaestry.ChampionMaestryDTO championMaestryDTO = Mockito.mock(ChampionMaestry.ChampionMaestryDTO.class);
        Mockito.when(
                riotHttpClient.get(uri, ChampionMaestry.ChampionMaestryDTO.class)
        ).then(invocation -> championMaestryDTO);
        Mockito.when(championMaestryDTO.puuid()).thenReturn(puuid);
        Mockito.when(championMaestryDTO.championId()).thenReturn((long) championId);

        ChampionMaestry.ChampionMaestryDTO championMaestryDTO1 = championMaestry.byPuuidAndChampionId(puuid, championId);

        Assertions.assertNotNull(championMaestryDTO1);
        Assertions.assertEquals(puuid, championMaestryDTO1.puuid());
        Assertions.assertEquals(championId, championMaestryDTO1.championId());
        Mockito.verify(riotHttpClient, Mockito.times(1)).get(uri, ChampionMaestry.ChampionMaestryDTO.class);
    }
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
