package com.cities.filters;

import com.cities.api.City;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by GKlymenko on 4/4/2016.
 */
public class AddingHeaderFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        responseContext.getHeaders().add("Powered by Galya", City.class);

    }
}
