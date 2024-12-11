package briot;

import briot.models.Regions;
import briot.models.ShortRegions;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class RegionAndShortRegionValidTestArgumentFactory {
    public static Stream<Arguments> validShortRegionsForAmerica() {
        return Stream.of(
                Arguments.of(
                        Regions.AMERICAS, ShortRegions.NA1
                ),
                Arguments.of(
                        Regions.AMERICAS, ShortRegions.BR1
                ),
                Arguments.of(
                        Regions.AMERICAS, ShortRegions.LAN
                ),
                Arguments.of(
                        Regions.AMERICAS, ShortRegions.LA1
                ),
                Arguments.of(
                        Regions.AMERICAS, ShortRegions.LA2
                )
        );
    }
}
