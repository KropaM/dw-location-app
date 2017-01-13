package com.cities;

/**
 * Created by GKlymenko on 3/24/2016.
 */

import com.bundle.cache.configurations.CacheBundleConfiguration;
import com.bundle.cache.configurations.CacheConfiguration;
import com.cities.configurations.GoogleAPIConfig;
import com.cities.configurations.MongoConfig;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CityConfiguration extends Configuration implements CacheConfiguration {

    @JsonProperty ("cache")
    private CacheBundleConfiguration cache = new CacheBundleConfiguration();

    @JsonProperty
    private GoogleAPIConfig googleAPIConfig;

    @JsonProperty
    private MongoConfig mongoConfig;

    public GoogleAPIConfig getGoogleAPIConfig() {
        return googleAPIConfig;
    }

    public void setGoogleAPIConfig(GoogleAPIConfig googleAPIConfig) {
        this.googleAPIConfig = googleAPIConfig;
    }

    public MongoConfig getMongoConfig() {
        return mongoConfig;
    }

    public void setMongoConfig(MongoConfig mongoConfig) {
        this.mongoConfig = mongoConfig;
    }

    @Override
    public CacheBundleConfiguration getCache() {
        return cache;
    }

    public void setCache(CacheBundleConfiguration cache) {
        this.cache = cache;
    }
}
