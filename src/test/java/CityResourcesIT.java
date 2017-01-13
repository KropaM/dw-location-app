import com.cities.CityApplication;
import com.cities.CityConfiguration;
import com.cities.api.City;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.fest.assertions.api.Assertions;
import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import java.io.IOException;

/**
 * Created by GKlymenko on 4/19/2016.
 */
public class CityResourcesIT {

    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test.yml");
    private Client client;

    @ClassRule
    public static final DropwizardAppRule<CityConfiguration> RULE = new DropwizardAppRule<>(
            CityApplication.class, CONFIG_PATH);

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testConnection() throws IOException {
        Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/api/cities").request().head();
        int responseCode = response.getStatus();
        Assertions.assertThat(200).isEqualTo(responseCode);
    }

    @Test
    public void postNewCity() {
        final City city = new City(0L, "New York", "56.4", "34,5");
        final City newCity = client.target("http://localhost:" + RULE.getLocalPort() + "/api/cities").request()
                .post(Entity.json(city)).readEntity(City.class);
        Assertions.assertThat(newCity.getName()).isEqualTo(city.getName());
    }
}
