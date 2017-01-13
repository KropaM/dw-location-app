package com.cities.googlemap;

import com.google.maps.GeoApiContext;

/**
 * Created by GKlymenko on 3/28/2016.
 */
public class KeyAuthorization {
    private static GeoApiContext context;

    public static void setKey(String key) {
        context = new GeoApiContext().setApiKey(key);
    }

    public static GeoApiContext getContext() {
        return context;
    }

    public static void setContext(GeoApiContext context) {
        KeyAuthorization.context = context;
    }
}
