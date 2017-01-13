package com.cities.health;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.Mongo;
import com.mongodb.client.MongoDatabase;

/**
 * Created by GKlymenko on 3/24/2016.
 */
public class MongoHealthCheck extends HealthCheck {
    private Mongo mongo;
    private MongoDatabase db;

    public MongoHealthCheck(Mongo mongo, MongoDatabase db) {
        this.mongo = mongo;
        this.db = db;
    }

    @Override
    protected HealthCheck.Result check() throws Exception {
        if (db == null) {
            return Result.unhealthy("Database was not find");
        }
        else if (!db.listCollections().iterator().hasNext()) {
            return Result.unhealthy("Database has no collections");
        }
        else if (mongo.isLocked()) {
            return Result.healthy("Database is locked (only for read)");
        }
        return Result.healthy();
    }
}
