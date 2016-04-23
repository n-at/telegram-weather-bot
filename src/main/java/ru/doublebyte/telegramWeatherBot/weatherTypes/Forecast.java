package ru.doublebyte.telegramWeatherBot.weatherTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Weather forecast
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {

    @JsonProperty("city")
    private City city;

    @JsonProperty("list")
    List<CurrentWeather> list;

    public Forecast() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<CurrentWeather> getList() {
        return list;
    }

    public void setList(List<CurrentWeather> list) {
        this.list = list;
    }
}
