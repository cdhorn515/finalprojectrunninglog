package com.cdhorn.GoogleMaps;


import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Legs extends GenericJson{

    @Key("start_location")
    private StartObject startObject;

    @Key("end_location")
    private EndObject endObject;

    public StartObject getStartObject() {
        return startObject;
    }

    public EndObject getEndObject() {
        return endObject;
    }
}
