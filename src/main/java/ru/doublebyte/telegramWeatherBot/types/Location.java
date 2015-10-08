package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object represents a point on the map
 * https://core.telegram.org/bots/api#location
 */
public class Location {

    @JsonProperty("longitude")
    double longitude;

    @JsonProperty("latitude")
    double latitude;

    public Location() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
