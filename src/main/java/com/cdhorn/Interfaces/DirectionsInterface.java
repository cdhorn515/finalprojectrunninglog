package com.cdhorn.Interfaces;

import com.cdhorn.Controllers.DirectionResponse;
import feign.Param;
import feign.RequestLine;


public interface DirectionsInterface {
    @RequestLine("GET /maps/api/directions/json?origin={origin}&destination={destination}&key={key}&mode=walking")
    DirectionResponse directionResponse(@Param("origin") String origin, @Param("destination") String destination, @Param("key") String key);

}
