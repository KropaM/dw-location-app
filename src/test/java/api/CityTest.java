package api;

/**
 * Created by GKlymenko on 3/29/2016.
 */

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.api.Assertions.assertThat;

import com.cities.api.City;
import io.dropwizard.jackson.Jackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class CityTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final City city = new City(0L, "Kyiv", "50.450100,30.523400", "Kiev, Ukraine, 02000");

    @Test
    public void serializesToJSON() throws Exception {
        assertThat(MAPPER.writeValueAsString(city)).isEqualTo(fixture("fixtures/kyiv-city.json"));
    }

    @Test
    public void deserealizedToJSON() throws Exception {
        assertThat(MAPPER.readValue(fixture("fixtures/kyiv-city.json"), City.class)).isEqualTo(city);
    }

}