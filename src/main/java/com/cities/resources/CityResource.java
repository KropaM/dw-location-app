package com.cities.resources;

/**
 * Created by GKlymenko on 3/24/2016.
 */

import com.bundle.cache.services.GuavaCacheService;
import com.cities.api.City;
import com.cities.services.CityService;
import com.cities.services.CityServiceException;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/cities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CityResource {

    private GuavaCacheService<String, City> guavaCacheService;
    private CityService cityService;

    public CityResource(CityService cityService, GuavaCacheService<String, City> guavaCacheService) {
        this.cityService = cityService;
        this.guavaCacheService = guavaCacheService;
    }

    @POST
    @Metered
    public Response addCity(City city) throws CityServiceException {
        City receivedCity = cityService.addCityWithCoordinates(city);
        if (receivedCity == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.ok(receivedCity).build();
    }

    @GET
    @Timed
    public List<City> getAllCities() {
        return cityService.getAll();
    }

    @Path("/{cityName}")
    @GET
    @Timed
    public City getCityByName(@PathParam("cityName") String name) {
        City city = guavaCacheService.get(name);
        if (city != null) {
            return city;
        }
        city = cityService.getCityByName(name);
        guavaCacheService.add(name, city);
        return city;
    }

    @PUT
    @Timed
    @Path("/{cityName}")
    public Response updateCity(@PathParam("cityName") String name, @Valid City city) throws CityServiceException {
        City recievedCity = cityService.updateCity(name, city);
        if (recievedCity == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (guavaCacheService.containsKey(name)) {
            guavaCacheService.remove(name);
        }
        return Response.ok(recievedCity).build();
    }

    @DELETE
    @Metered
    @Path("/{cityName}")
    public Response removeCity(@PathParam("cityName") String name) throws CityServiceException {
        cityService.removeCity(name);
        if (guavaCacheService.containsKey(name)) {
            guavaCacheService.remove(name);
        }
        return Response.status(Response.Status.NO_CONTENT).entity(name + " was deleted").build();
    }
}
