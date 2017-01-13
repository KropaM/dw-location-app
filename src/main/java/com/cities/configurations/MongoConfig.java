package com.cities.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by GKlymenko on 4/6/2016.
 */
public class MongoConfig {
    @JsonProperty
    private String mongoHost = "localhost";

    @JsonProperty
    @Min(1)
    @Max(65535)
    private int mongoPort = 27017;

    @JsonProperty
    private String mongoDB = "locals";

    public String getMongoHost() {
        return mongoHost;
    }

    public void setMongoHost(String mongoHost) {
        this.mongoHost = mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public void setMongoPort(int mongoPort) {
        this.mongoPort = mongoPort;
    }

    public String getMongoDB() {
        return mongoDB;
    }

    public void setMongoDB(String mongoDB) {
        this.mongoDB = mongoDB;
    }
}
