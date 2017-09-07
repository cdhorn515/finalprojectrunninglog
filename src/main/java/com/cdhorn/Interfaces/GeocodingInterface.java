package com.cdhorn.Interfaces;


import com.cdhorn.Classes.GeocodingResponse;
import feign.Param;
import feign.RequestLine;

public interface GeocodingInterface {
    @RequestLine("GET /maps/api/geocode/json?address={address}&key={key}")
    GeocodingResponse geocodingResponse(@Param("address") String address, @Param("key") String key);

}
