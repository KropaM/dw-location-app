package com.cities.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Random;

/**
 * Created by GKlymenko on 4/7/2016.
 */

public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestID = ((HttpServletRequest) request).getHeader("Request-ID");
        Cookie[] coolies = ((HttpServletRequest) request).getCookies();
        boolean successfulRequest = false;

        if (requestID == null || requestID.length() == 0) {
            requestID = String.valueOf(generateRandomID());
        }
        successfulRequest = registerRequestID(requestID);
        chain.doFilter(request, response);
    }

    private boolean registerRequestID(String requestID) {
        if (requestID != null && requestID.trim().length() > 0) {
            MDC.put("Request-ID", requestID);
            return true;
        }
        return false;
    }

    private long generateRandomID() {
        Random random = new Random();
        return random.nextLong();
    }

    public void init(FilterConfig filterConfig)
            throws ServletException {

    }

    public void destroy() {
    }
}
