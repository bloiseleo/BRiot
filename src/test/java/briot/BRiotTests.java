package briot;

import briot.apis.v1.Account;
import briot.models.Regions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BRiotTests {
    private BRiot bRiot;
    @Test
    public void givenCredentials_whenWithCredentials_thenReturnBriotSingleton() {
        String apiKey = "TEST-API-KEY";
        Regions region = Regions.AMERICAS;
        bRiot = BRiot.withCredentials(apiKey, region);
        Optional<BRiot> bRiot2 = BRiot.getInstance();

        Assertions.assertNotNull(bRiot);
        Assertions.assertInstanceOf(BRiot.class, bRiot);
        Assertions.assertTrue(bRiot2.isPresent());
        Assertions.assertSame(bRiot2.get(), bRiot);
    }
    @Test
    public void givenCredentials_whenAccountApi_thenReturnAccountApi() {
        Account account = bRiot.getAccountApi();
        Assertions.assertNotNull(account);
        Assertions.assertInstanceOf(Account.class, account);
    }
}
