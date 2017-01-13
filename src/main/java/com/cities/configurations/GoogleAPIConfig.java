package com.cities.configurations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GKlymenko on 4/6/2016.
 */
public class GoogleAPIConfig {
    private String key;

    public GoogleAPIConfig(@JsonProperty ("key") String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
