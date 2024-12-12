package briot.apis;

import briot.RiotHttpClient;
import briot.helpers.URIHelpers;
import briot.models.Regions;
import briot.models.ShortRegions;

import java.net.URI;

public abstract class RiotAPI {
    protected final RiotHttpClient client;
    protected final Regions region;
    protected final ShortRegions shortRegion;
    public RiotAPI(RiotHttpClient client, Regions region, ShortRegions shortRegions) {
        this.client = client;
        this.region = region;
        this.shortRegion = shortRegions;
    }
    protected URI createUriFromRouteAndRegion(String route, Regions region) {
        String base = "https://" + region.name().toLowerCase() + ".api.riotgames.com";
        return URIHelpers.createURI(base, route);
    }
    protected URI createUriFromRouteAndShortRegion(String route, ShortRegions region) {
        String base = "https://" + region.name().toLowerCase() + ".api.riotgames.com";
        return URIHelpers.createURI(base, route);
    }
}
