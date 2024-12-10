package briot;

import briot.models.Regions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class BRiotTests {
    @Test
    public void givenCredentials_whenWithCredentials_thenReturnBriotSingleton() {
        String apiKey = "TEST-API-KEY";
        Regions region = Regions.AMERICAS;

        BRiot bRiot = BRiot.withCredentials(apiKey, region);
        Optional<BRiot> bRiot2 = BRiot.getInstance();

        Assertions.assertNotNull(bRiot);
        Assertions.assertInstanceOf(BRiot.class, bRiot);
        Assertions.assertTrue(bRiot2.isPresent());
        Assertions.assertSame(bRiot2.get(), bRiot);
    }
}
