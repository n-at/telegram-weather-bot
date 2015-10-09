package ru.doublebyte.telegramWeatherBot.weatherTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties({"visibility", "rain", "snow", "clouds", "wind", "sys", "base", "id", "cod"})
public class CurrentWeather {

    @JsonProperty("coord")
    private Coordinates coordinates;

    @JsonProperty("weather")
    private List<WeatherCondition> weatherCondition;

    @JsonProperty("main")
    private WeatherMain weather;

    @JsonProperty("dt")
    private int date;

    @JsonProperty("dt_txt")
    private String dateText;

    @JsonProperty("name")
    private String cityName;

    public CurrentWeather() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<WeatherCondition> getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(List<WeatherCondition> weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public WeatherMain getWeather() {
        return weather;
    }

    public void setWeather(WeatherMain weather) {
        this.weather = weather;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDateText() {
        return dateText;
    }

    public void setDateText(String dateText) {
        this.dateText = dateText;
    }
}
