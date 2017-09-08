package com.cdhorn.Interfaces;

import com.cdhorn.Classes.DirectionResponse;
import feign.Param;
import feign.RequestLine;


public interface DirectionsInterface {
    @RequestLine("GET /maps/api/directions/json?origin={origin}&destination={destination}&waypoints={waypoints}&mode=walking&key={key}")
    DirectionResponse directionResponse(@Param("origin") String origin, @Param("destination") String destination, @Param("waypoints") String waypoints, @Param("key") String key);

}
