package com.cities;

import com.bundle.cache.CachingBundle;
import com.bundle.cache.services.GuavaCacheService;
import com.cities.api.City;
import com.cities.dao.CityDao;
import com.cities.dao.MongoCityDao;
import com.cities.filters.AddingHeaderFilter;
import com.cities.filters.MDCFilter;
import com.cities.googlemap.KeyAuthorization;
import com.cities.googlemap.GoogleApiService;
import com.cities.health.CacheBundleHealthCheck;
import com.cities.health.GoogleKeyHealthCheck;
import com.cities.managed.MongoManaged;
import com.cities.health.MongoHealthCheck;
import com.cities.resources.CityResource;
import com.cities.services.CityService;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.DispatcherType;
import java.util.EnumSet;


/**
 * Created by GKlymenko on 3/24/2016.
 */
public class CityApplication extends Application<CityConfiguration> {
    public static void main(String[] args) throws Exception {
        new CityApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<CityConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html", "static"));
        bootstrap.addBundle(new CachingBundle());
    }

    @Override
    public void run(CityConfiguration configuration,
                    Environment environment) throws Exception {
        MongoClient mongo = new MongoClient(
                configuration.getMongoConfig().getMongoHost(),
                configuration.getMongoConfig().getMongoPort()
        );
        MongoManaged mongoManaged = new MongoManaged(mongo);
        MongoDatabase db = mongo.getDatabase("dwapp");
        MongoCollection collection = db.getCollection("cities");

        final CityDao cityDao = new MongoCityDao(db, collection);
        KeyAuthorization.setKey(configuration.getGoogleAPIConfig().getKey());
        final GoogleApiService googleApiService = new GoogleApiService();
        final CityService cityService = new CityService(cityDao, googleApiService);
        final GuavaCacheService<String, City> guavaCacheService = new GuavaCacheService<String, City>();

        final CityResource resource = new CityResource(cityService, guavaCacheService);

        registerHealthChecks(environment, mongo, db, guavaCacheService);

        environment.lifecycle().manage(mongoManaged);
        environment.jersey().register(new AddingHeaderFilter());
        environment.servlets().addFilter("MDCFilter", new MDCFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        environment.jersey().register(resource);

    }

    private void registerHealthChecks(Environment environment, MongoClient mongo, MongoDatabase db, GuavaCacheService<String, City> guavaCacheService) {
        environment.healthChecks().register("database", new MongoHealthCheck(mongo, db));
        environment.healthChecks().register("GoogleAPI Key", new GoogleKeyHealthCheck());
        environment.healthChecks().register("Guava Cache Bundle", new CacheBundleHealthCheck(guavaCacheService));
    }

}
