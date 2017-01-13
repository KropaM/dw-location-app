package resources;

import com.bundle.cache.services.GuavaCacheService;
import com.cities.api.City;
import com.cities.resources.CityResource;
import com.cities.services.CityService;
import com.cities.services.CityServiceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.*;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static io.dropwizard.testing.FixtureHelpers.fixture;

/**
 * Created by GKlymenko on 3/29/2016.
 */
@RunWith(DataProviderRunner.class)
public class CityResourcesTest {

    private static final CityService cityService = mock(CityService.class);
    private static final GuavaCacheService<String, City> guavaCacheService = mock(GuavaCacheService.class);
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static int statusCode400 = Response.status(400).build().getStatus();
    private static int statusCode422 = Response.status(422).build().getStatus();
    private static int statusCode200 = Response.status(200).build().getStatus();
    private int statusCode204 = Response.status(204).build().getStatus();

    private static City kyiv;
    private static City lviv;
    private static City lvivEmpty;
    private static City unvalidNameCity;
    private static City emptyCity;
    private static City digitNameCity;

    private static List<City> givenValidCities;

    @DataProvider
    public static Object[][] data() {
        Object[][] retcity={{new City(0L,"aaaadabra","","")},
                {new City(0L,"12343255","","")}};
        return(retcity);
    }

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new CityResource(cityService,guavaCacheService))
            .build();

    @Before
    public void setup() throws CityServiceException,IOException {
        givenInvalid();
        givenValid();

        getFromSityCervice();
        addCallToCityService();
        updateCallToCityService();

        when(guavaCacheService.remove(kyiv.getName())).thenReturn(kyiv);
    }

    @Test
    public void testGetCityByName() {
        assertThat((resources.client().target("/cities/kyiv/").request().get(City.class)))
                .isEqualTo(kyiv);
        verify(cityService).getCityByName("kyiv");
        verify(guavaCacheService).add("kyiv", kyiv);
    }

    @Test
    public void testGetAll() {
        assertThat(resources.client().target("/cities/").request().get(
                new GenericType<List<City>>() {
                }))
                .isEqualTo(givenValidCities);
        verify(cityService).getAll();
    }

    @Test
    public void testAddValidCity() throws CityServiceException {
        Response response = resources.client().target("/cities/").request().post(Entity.json(lvivEmpty));
        assertThat(getStatus(response)).isEqualTo(statusCode200);
        assertThat(response.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.readEntity(City.class)).isEqualTo(lviv);
        verify(cityService).addCityWithCoordinates(lvivEmpty);
    }

    @Test
    public void testRemoveCity() {
        Response response = resources.client().target("/cities/kyiv").request().delete();
        assertThat(getStatus(response)).isEqualTo(statusCode204);
    }

    @Test
    public void testUpdateCity() throws CityServiceException {
        Response response = resources.client().target("/cities/kyiv").request().put(Entity.json(lvivEmpty));
        assertThat(getStatus(response)).isEqualTo(statusCode200);
        assertThat(response.getMediaType()).isEqualTo(MediaType.APPLICATION_JSON_TYPE);
        assertThat(response.readEntity(City.class)).isEqualTo(lviv);
        verify(cityService).updateCity("kyiv", lvivEmpty);
        verify(guavaCacheService).containsKey("kyiv");
    }

    @Test
    @UseDataProvider("data")
    public void testAddUnvalidCity(City input) {
        Response response = resources.client().target("/cities/").request().post(Entity.json(input));
        assertThat(getStatus(response)).isEqualTo(statusCode400);
    }

    @Test
    @UseDataProvider("data")
    public void testUpdateCityWithUnvalidName(City input) throws CityServiceException {
        Response response = resources.client().target("/cities/kyiv").request().put(Entity.json(input));
        assertThat(getStatus(response)).isEqualTo(statusCode400);
        verify(cityService).updateCity("kyiv", input);
    }

    @Test
    public void testUpdateCityWithEmptyName() throws CityServiceException {
        Response response = resources.client().target("/cities/kyiv").request().put(Entity.json(emptyCity));
        assertThat(getStatus(response)).isEqualTo(statusCode422);
    }

    private static void givenValid() throws IOException{
        givenValidCities = MAPPER.readValue(fixture("fixtures/valid-cities.json"),new TypeReference<List<City>>(){});
        kyiv = givenValidCities.get(0);
        lviv = givenValidCities.get(1);
        lvivEmpty = givenValidCities.get(2);
    }

    private static void givenInvalid() throws IOException{
        List<City> givenUnvalidCities = MAPPER.readValue(fixture("fixtures/unvalid-cities.json"),new TypeReference<List<City>>(){});
        unvalidNameCity = givenUnvalidCities.get(0);
        digitNameCity = givenUnvalidCities.get(1);
        emptyCity = givenUnvalidCities.get(2);
    }

    private int getStatus(Response response) {
        return response.getStatusInfo().getStatusCode();
    }

    private void addCallToCityService() throws CityServiceException {
        when(cityService.addCityWithCoordinates(lvivEmpty)).thenReturn(lviv);
    }

    private void getFromSityCervice() {
        when(cityService.getCityByName(eq("kyiv"))).thenReturn(kyiv);
        when(cityService.getAll()).thenReturn(givenValidCities);
    }

    private void updateCallToCityService() throws CityServiceException {
        when(cityService.updateCity("kyiv", digitNameCity)).thenReturn(null);
        when(cityService.updateCity("kyiv", unvalidNameCity)).thenReturn(null);
        when(cityService.updateCity("kyiv", emptyCity)).thenReturn(null);
        when(cityService.updateCity("kyiv", lvivEmpty)).thenReturn(lviv);
    }
}