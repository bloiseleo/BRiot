package briot;

import briot.apis.v1.Account;
import briot.apis.v4.ChampionMaestry;
import briot.models.Regions;
import briot.models.ShortRegions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BRiotTests {
    @Test
    public void givenCredentials_whenWithCredentials_thenReturnBriot() {
        String apiKey = "TEST-API-KEY";
        Regions region = Regions.AMERICAS;
        ShortRegions shortRegions = ShortRegions.BR1;
        BRiot bRiot = BRiot.withCredentials(apiKey, region, shortRegions);
        Assertions.assertNotNull(bRiot);
        Assertions.assertInstanceOf(BRiot.class, bRiot);
    }
    @ParameterizedTest
    @MethodSource("briot.RegionAndShortRegionValidTestArgumentFactory#validShortRegionsForAmerica")
    public void givenCredentials_whenWithCredentials_thenNotThrowInvalidAmericaRegionAndShortRegions(
            Regions region, ShortRegions shortRegions
    ) {
        String apiKey = "TEST-API-KEY";
        Assertions.assertDoesNotThrow(() -> BRiot.withCredentials(apiKey, region, shortRegions));
    }
    @ParameterizedTest
    @MethodSource("briot.RegionAndShortRegionValidTestArgumentFactory#validShortRegionsForAsia")
    public void givenCredentials_whenWithCredentials_thenNotThrowInvalidAsiaRegionAndShortRegions(
            Regions region, ShortRegions shortRegions
    ) {
        String apiKey = "TEST-API-KEY";
        Assertions.assertDoesNotThrow(() -> BRiot.withCredentials(apiKey, region, shortRegions));
    }
    @ParameterizedTest
    @MethodSource("briot.RegionAndShortRegionValidTestArgumentFactory#validShortRegionsForEurope")
    public void givenCredentials_whenWithCredentials_thenNotThrowInvalidEuropeRegionAndShortRegions(
            Regions region, ShortRegions shortRegions
    ) {
        String apiKey = "TEST-API-KEY";
        Assertions.assertDoesNotThrow(() -> BRiot.withCredentials(apiKey, region, shortRegions));
    }
    @ParameterizedTest
    @MethodSource("briot.RegionAndShortRegionValidTestArgumentFactory#validShortRegionsForSea")
    public void givenCredentials_whenWithCredentials_thenNotThrowInvalidSeaRegionAndShortRegions(
            Regions region, ShortRegions shortRegions
    ) {
        String apiKey = "TEST-API-KEY";
        Assertions.assertDoesNotThrow(() -> BRiot.withCredentials(apiKey, region, shortRegions));
    }
    @Test
    public void givenCredentials_whenAccountApi_thenReturnAccountApi() {
        String apiKey = "TEST-API-KEY";
        Regions region = Regions.AMERICAS;
        ShortRegions shortRegions = ShortRegions.BR1;
        BRiot bRiot = BRiot.withCredentials(apiKey, region, shortRegions);
        Account account = bRiot.getAccountApi();
        Assertions.assertNotNull(account);
        Assertions.assertInstanceOf(Account.class, account);
    }
    @Test
    public void givenAccountApi_whenAccountApi_thenReturnChampionMaestryApi() {
        String apiKey = "TEST-API-KEY";
        Regions region = Regions.AMERICAS;
        ShortRegions shortRegions = ShortRegions.BR1;
        BRiot bRiot = BRiot.withCredentials(apiKey, region, shortRegions);
        ChampionMaestry championMaestry = bRiot.getChampioMaestryApi();
        Assertions.assertNotNull(championMaestry);
        Assertions.assertInstanceOf(ChampionMaestry.class, championMaestry);
    }
}
