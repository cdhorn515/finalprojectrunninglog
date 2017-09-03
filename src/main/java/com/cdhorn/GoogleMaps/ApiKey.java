package com.cdhorn.GoogleMaps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


@Configuration
@PropertySource("file:apikey.properties")
public class ApiKey {

    @Autowired
    private Environment env;

    public String staticMapKey() {
        env.getProperty("STATIC_MAP_KEY");
        return "STATIC_MAP_KEY";
    }

    public String directionsApiKey() {
        env.getProperty("DIRECTIONS_API_KEY");
        return "DIRECTIONS_API_KEY";
    }

    public String geoCodeApiKey() {
        env.getProperty("GEOCODING_KEY");
        return "GEOCODING_KEY";
    }

}
