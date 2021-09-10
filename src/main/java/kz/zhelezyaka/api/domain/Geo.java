package kz.zhelezyaka.api.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Geo implements Serializable {
    private String lat;
    private String lng;
    private Map<String, Object> additionalProperties = new HashMap<>();
    private final static long serialVersionUID = -7919308727887378246L;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
