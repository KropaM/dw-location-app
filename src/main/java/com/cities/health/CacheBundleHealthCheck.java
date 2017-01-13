package com.cities.health;

import com.bundle.cache.services.GuavaCacheService;
import com.cities.api.City;
import com.codahale.metrics.health.HealthCheck;

/**
 * Created by GKlymenko on 4/13/2016.
 */
public class CacheBundleHealthCheck extends HealthCheck {
    private GuavaCacheService<String, City> cacheService;

    public CacheBundleHealthCheck(GuavaCacheService<String, City> cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    protected HealthCheck.Result check() throws Exception {
        if (cacheService == null) {
            return HealthCheck.Result.unhealthy("Guava Bundle was not added");
        }
        else if (cacheService.getLoadExceptionRate() > 1) {
            return HealthCheck.Result.unhealthy("Exceptions when added to cache is more then success addings");
        }
        else if (cacheService.getRequestCount() > 0) {
            return HealthCheck.Result.healthy("Request to cache counted as " + cacheService.getRequestCount());
        }
        return HealthCheck.Result.healthy();
    }
}
