package com.cities.dao;

import com.cities.api.City;
import java.util.List;

/**
 * Created by GKlymenko on 4/6/2016.
 */
public interface CityDao {

    City addCity(City city);

    List<City> getAllCities();

    City getCityByName(String name);

    boolean updateCity(String name, City city);

    boolean removeCity(String name);
}
