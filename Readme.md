# DWAppExample

DWAppExample was created for education purposes and based on Dropwizard. It also used Google Location API for getting Longitude, Latitude and Zip Code for any address or city. For storing data there is implementation of Mongo DB.

You can add, update, remove or get cities information by REST API client as far as DWAPExample runs as an HTTP server and you have connection to your local Mongo DB.

#####To get all cities in DB use link:

```
http://localhost:8080/api/cities/
```

#####To get specific city or address add name of the city into the end of the URL.

 For example:
```
http://localhost:8080/api/cities/Rio
```

#####The same link is for removing and updating cities.

As DWAppExample was developed using Dropwizard, it was possible to add separate Guava Cache bundle. It works on local server.  Cache will be completely rewritten after each DWAppExample restarting.

For configuring count of objects that can be stored in cache and max duration of objects live you need to add next directives into .yml file:

```
cache:
  maxSize: 1000
  liveDurationMinutes: 30
  ```

If you want to change Google API Key you can do it in hello-world.yml file such as mongo db configurations.
For logging purposes DWAppExample uses Logger that is based on Log4J and is also part of Dropwizard core. Logs are displayed in console and in .log file with special logFormat that includes MDC value.

Special developed MDCFilter is responsible for adding MDC value from request header that is called Request-ID.