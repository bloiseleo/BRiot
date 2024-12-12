package briot.helpers;

import java.net.URI;

public abstract class URIHelpers {
    public static URI createURI(String protocolWithHostAndPort, String route) {
        if (route.startsWith("/")) {
            return URI.create(protocolWithHostAndPort + route);
        }
        return URI.create(protocolWithHostAndPort + "/" + route);
    }
}
