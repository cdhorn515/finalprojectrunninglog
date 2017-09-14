package com.cdhorn.Classes;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ResourceBundle;


@Configuration
@PropertySource(value = {"classpath:apikey.properties"})
public class ApiKey {

    ResourceBundle bundle = ResourceBundle.getBundle("apikey");
    private String DIRECTIONS_API = bundle.getString("DIRECTIONS_KEY");
    private String STATIC_MAP_API = bundle.getString("STATIC_MAP_KEY");
    private String GEOCODING_API = bundle.getString("GEOCODING_KEY");
    private String PARKS_URL = bundle.getString("PARKS_URL");

    public String getDIRECTIONS_API() {
        return DIRECTIONS_API;
    }

    public String getSTATIC_MAP_API() {
        return STATIC_MAP_API;
    }

    public String getGEOCODING_API() {
        return GEOCODING_API;
    }

    public String getPARKS_URL() {
        return PARKS_URL;
    }
}
