package com.cities.googlemap;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

/**
 * Created by GKlymenko on 3/28/2016.
 */
public class GoogleApiService {

    private GeoApiContext context = KeyAuthorization.getContext();

    public String getLongLat(String cityName) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, cityName).await();
            if (results.length > 0)
                return results[0].geometry.location.toString();
            else return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getFullAddress(String cityName) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context,
                    cityName).await();
            if (results.length > 0)
                return results[0].formattedAddress;
            else return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
