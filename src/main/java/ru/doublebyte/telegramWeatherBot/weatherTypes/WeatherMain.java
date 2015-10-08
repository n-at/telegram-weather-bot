package ru.doublebyte.telegramWeatherBot.weatherTypes;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherMain {

    @JsonProperty("temp")
    private double temperature;

    @JsonProperty("pressure")
    private int pressure;

    @JsonProperty("humidity")
    private int humidity;

    @JsonProperty("temp_min")
    private double minTemperature;

    @JsonProperty("temp_max")
    private double maxTemperature;

    @JsonProperty("sea_level")
    private int pressureSeaLevel;

    @JsonProperty("grnd_level")
    private int pressureGroundLevel;

    public WeatherMain() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public int getPressureSeaLevel() {
        return pressureSeaLevel;
    }

    public void setPressureSeaLevel(int pressureSeaLevel) {
        this.pressureSeaLevel = pressureSeaLevel;
    }

    public int getPressureGroundLevel() {
        return pressureGroundLevel;
    }

    public void setPressureGroundLevel(int pressureGroundLevel) {
        this.pressureGroundLevel = pressureGroundLevel;
    }
}
