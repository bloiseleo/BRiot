package briot.models.errors;

import briot.models.Regions;
import briot.models.ShortRegions;

import java.util.Arrays;

public class InvalidShortRegionForRegion extends RuntimeException {
    public InvalidShortRegionForRegion(Regions regions, ShortRegions shortRegions, ShortRegions[] valids) {
        super(
                String.format("Invalid %s for %s - Valid short regions: %s",
                            shortRegions.name().toLowerCase(),
                        regions.name().toLowerCase(),
                        Arrays.toString(valids)
                )
        );
    }
}
