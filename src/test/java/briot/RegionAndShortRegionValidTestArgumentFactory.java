package briot;

import briot.models.Regions;
import briot.models.ShortRegions;
import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.stream.Stream;

public class RegionAndShortRegionValidTestArgumentFactory {
    public static Stream<Arguments> validShortRegionsForSea() {
        return Stream.of(
                Arguments.of(
                        Regions.SEA, ShortRegions.OC1
                ),
                Arguments.of(
                        Regions.SEA, ShortRegions.PH2
                ),
                Arguments.of(
                        Regions.SEA, ShortRegions.SG2
                ),
                Arguments.of(
                        Regions.SEA, ShortRegions.TH2
                ),
                Arguments.of(
                        Regions.SEA, ShortRegions.TW2
                ),
                Arguments.of(
                        Regions.SEA, ShortRegions.VN2
                )
        );
    }
    public static Stream<Arguments> validShortRegionsForAsia() {
        return Stream.of(
            Arguments.of(
                    Regions.ASIA, ShortRegions.KR
            ),
            Arguments.of(
                    Regions.ASIA, ShortRegions.JP1
            )
        );
    }
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
    public static Stream<Arguments> validShortRegionsForEurope() {
        return Stream.of(
                Arguments.of(
                        Regions.EUROPE, ShortRegions.EUN1
                ),
                Arguments.of(
                        Regions.EUROPE, ShortRegions.EUW1
                ),
                Arguments.of(
                        Regions.EUROPE, ShortRegions.ME1
                ),
                Arguments.of(
                        Regions.EUROPE, ShortRegions.TR1
                ),
                Arguments.of(
                        Regions.EUROPE, ShortRegions.RU
                )
        );
    }
}
