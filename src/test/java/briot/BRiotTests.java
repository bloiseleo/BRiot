package briot;

import briot.apis.v1.Account;
import briot.models.Regions;
import briot.models.ShortRegions;
import briot.models.errors.InvalidShortRegionForRegion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


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
    public void givenCredentials_whenWithCredentials_thenNotThrowInvalidRegionAndShortRegions(
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
}
