package briot.models;


import briot.models.errors.InvalidShortRegionForRegion;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RiotRegion {
    private final Regions region;
    private final ShortRegions[] validShortRegions;
    private RiotRegion(Regions region,  ShortRegions[] validShortRegions) {
        Arrays.sort(validShortRegions);
        this.region = region;
        this.validShortRegions = validShortRegions;
    }
    private static RiotRegion AMERICA = new RiotRegion(Regions.AMERICAS, new ShortRegions[] {
            ShortRegions.NA1,
            ShortRegions.BR1,
            ShortRegions.LAN,
            ShortRegions.LA1,
            ShortRegions.LA2,
    });
    private static RiotRegion ASIA = new RiotRegion(Regions.ASIA, new ShortRegions[] {
            ShortRegions.JP1,
            ShortRegions.KR
    });
    private static RiotRegion EUROPE = new RiotRegion(Regions.EUROPE, new ShortRegions[] {
            ShortRegions.EUN1,
            ShortRegions.EUW1,
            ShortRegions.ME1,
            ShortRegions.TR1,
            ShortRegions.RU,
    });
    private static RiotRegion SEA = new RiotRegion(Regions.SEA, new ShortRegions[] {
            ShortRegions.OC1,
            ShortRegions.PH2,
            ShortRegions.SG2,
            ShortRegions.TH2,
            ShortRegions.TW2,
            ShortRegions.VN2
    });
    private static Map<Regions, RiotRegion> regions = new HashMap<>();
    static {
        regions.put(Regions.AMERICAS, AMERICA);
        regions.put(Regions.ASIA, ASIA);
        regions.put(Regions.EUROPE, EUROPE);
        regions.put(Regions.SEA, SEA);
    }
    private boolean validateShortRegion(ShortRegions shortRegion) {
        return Arrays.binarySearch(validShortRegions, shortRegion) >= 0;
    }
    public static RiotRegion of(Regions region, ShortRegions shortRegion) {
        if (!regions.containsKey(region)) {
            throw new RuntimeException("Region " + region.name() + " not found");
        }
        var riotRegion = regions.get(region);
        if (!riotRegion.validateShortRegion(shortRegion)) {
            throw new InvalidShortRegionForRegion(region, shortRegion, riotRegion.validShortRegions);
        }
        return riotRegion;
    }
}
