package services;


import com.cities.api.City;
import com.cities.dao.CityDao;
import com.cities.dao.MongoCityDao;
import com.cities.googlemap.GoogleApiService;
import com.cities.services.CityService;
import com.cities.services.CityServiceException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by GKlymenko on 3/29/2016.
 */
public class CityServiceTest {

    private final GoogleApiService googleApiService = mock(GoogleApiService.class);
    private final CityDao cityDao = mock(MongoCityDao.class);
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private CityService cityService;

    private City kyiv;
    private City lviv;
    private City onlyName;
    private City unvalidCity;

    private List<City> cities;

    @Before
    public void setup() throws Exception {
        cityService = new CityService(cityDao, googleApiService);
        givenValid();
        givenInvalid();

        cities = new ArrayList<>();
        cities.add(kyiv);
        cities.add(lviv);

        callCityDao();
        callGoogleApiService();
    }

    @Test
    public void getAllTest() {
        assertThat(cityService.getAll()).isEqualTo(cities);
        verify(cityDao).getAllCities();
    }

    @Test
    public void getCityByNameTest() {
        assertThat(cityService.getCityByName("kyiv")).isEqualTo(kyiv);
        verify(cityDao).getCityByName("kyiv");
    }

    @Test
    public void addCityWithCoordinatesTest() throws CityServiceException {
        assertThat(cityService.addCityWithCoordinates(lviv)).isEqualTo(lviv);
        verify(googleApiService).getLongLat("Lviv");
        verify(googleApiService).getFullAddress("Lviv");
        verify(cityDao).addCity(lviv);
    }

    @Test
    public void addUnvalidCityWithCoordinates() throws CityServiceException {
        assertThat(cityService.addCityWithCoordinates(unvalidCity)).isEqualTo(null);
        verify(googleApiService).getLongLat(unvalidCity.getName());
        verify(googleApiService).getFullAddress(unvalidCity.getName());
    }

    @Test
    public void updateCityTest() throws CityServiceException {
        assertThat(cityService.updateCity("lviv", onlyName)).isEqualTo(onlyName);
        verify(googleApiService).getLongLat(onlyName.getName());
        verify(googleApiService).getFullAddress(onlyName.getName());
        verify(cityDao).updateCity("lviv", onlyName);
    }

    @Test
    public void updateWithUnvalidCity() throws CityServiceException {
        assertThat(cityService.updateCity("lviv", unvalidCity)).isEqualTo(null);
    }

    private void givenValid() throws IOException {
        List<City> givenValidCities = MAPPER.readValue(fixture("fixtures/valid-cities.json"),new TypeReference<List<City>>(){});
        kyiv = givenValidCities.get(0);
        lviv = givenValidCities.get(1);
        onlyName = givenValidCities.get(2);
    }

    private void givenInvalid() throws IOException{
        List<City> givenUnvalidCities = MAPPER.readValue(fixture("fixtures/unvalid-cities.json"),new TypeReference<List<City>>(){});
        unvalidCity = givenUnvalidCities.get(0);
    }

    private void callGoogleApiService() {
        when(googleApiService.getLongLat("Lviv")).thenReturn("49.839683,24.029717");
        when(googleApiService.getFullAddress("Lviv")).thenReturn("Lviv, Lviv Oblast, Ukraine");
        when(googleApiService.getLongLat(unvalidCity.getName())).thenReturn("");
        when(googleApiService.getFullAddress(unvalidCity.getName())).thenReturn("");
        when(googleApiService.getLongLat(onlyName.getName())).thenReturn("41.008238,28.978359");
        when(googleApiService.getFullAddress(onlyName.getName())).thenReturn("Lviv, Lviv Oblast, Ukraine");
    }

    private void callCityDao() {
        when(cityDao.getAllCities()).thenReturn(cities);
        when(cityDao.getCityByName("kyiv")).thenReturn(kyiv);
        when(cityDao.addCity(lviv)).thenReturn(lviv);
        when(cityDao.updateCity("lviv", onlyName)).thenReturn(true);
    }
}