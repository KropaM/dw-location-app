package com.cities.health;

import com.cities.googlemap.KeyAuthorization;
import com.cities.googlemap.GoogleApiService;
import com.codahale.metrics.health.HealthCheck;
import com.google.maps.GeoApiContext;

/**
 * Created by GKlymenko on 3/28/2016.
 */
public class GoogleKeyHealthCheck extends HealthCheck {
    private GeoApiContext context = KeyAuthorization.getContext();
    private GoogleApiService googleApiService = new GoogleApiService();

    @Override
    protected HealthCheck.Result check() throws Exception {
        if (context == null) {
            return Result.unhealthy("Google API Key was not asigned");
        }
        String responce = googleApiService.getLongLat("Kyiv");
        if (responce.length() == 0) {
            return Result.unhealthy("Google API Key is not correct");
        }
        return Result.healthy();
    }
}
