package com.cities.managed;

import com.mongodb.Mongo;
import io.dropwizard.lifecycle.Managed;

/**
 * Created by GKlymenko on 3/24/2016.
 */
public class MongoManaged implements Managed {
    private Mongo mongo;

    public MongoManaged(Mongo mongo) {
        this.mongo = mongo;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
        mongo.close();
    }
}
