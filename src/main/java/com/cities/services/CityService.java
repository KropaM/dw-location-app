package com.cities.services;

import com.cities.api.City;
import com.cities.dao.CityDao;
import com.cities.googlemap.GoogleApiService;
import com.mongodb.MongoException;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by GKlymenko on 3/24/2016.
 */
public class CityService {

    private CityDao cityDao;
    private GoogleApiService googleApiService;

    private final Logger LOG = Logger.getLogger(CityService.class);

    public CityService(CityDao cityDao, GoogleApiService googleApiService) {
        this.cityDao = cityDao;
        this.googleApiService = googleApiService;
    }

    public List<City> getAll() {
        return cityDao.getAllCities();
    }

    public City getCityByName(String name) {
        LOG.info("Call to DB from getCityByName");
        return cityDao.getCityByName(name);
    }

    public City addCityWithCoordinates(City city) throws CityServiceException {
        String longLat = googleApiService.getLongLat(city.getName());
        String formAddr = googleApiService.getFullAddress(city.getName());
        if (longLat.length() > 0 && formAddr.length() > 0) {
            city.setLongLat(longLat);
            city.setFormattedAddress(formAddr);
            try {
                cityDao.addCity(city);
                return city;
            } catch (MongoException e) {
                throw new CityServiceException(city.getName() + " was found through GoogleAPI, " +
                        "but error when adding to MongoDB\n" + e.getMessage());
            }
        } else {
            return null;
        }
    }

    public City updateCity(String name, City city) throws CityServiceException {
        String longLat = googleApiService.getLongLat(city.getName());
        String formAddr = googleApiService.getFullAddress(city.getName());
        if (longLat.length() > 0 && formAddr.length() > 0) {
            city.setLongLat(longLat);
            city.setFormattedAddress(formAddr);
            try {
                cityDao.updateCity(name, city);
                return city;
            } catch (MongoException e) {
                throw new CityServiceException(city.getName() + " was found through GoogleAPI, " +
                        "but error when updating in MongoDB\n" + e.getMessage());
            }
        } else {
            return null;
        }
    }

    public boolean removeCity(String name) throws CityServiceException {
        try {
            return cityDao.removeCity(name);
        } catch (MongoException e) {
            throw new CityServiceException(name + " was not removes from MongoDB\n" + e.getMessage());
        }
    }
}
