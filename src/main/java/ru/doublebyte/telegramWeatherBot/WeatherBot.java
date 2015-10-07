package ru.doublebyte.telegramWeatherBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Weather bot class
 */
public class WeatherBot extends Bot {

    private static final Logger logger = LoggerFactory.getLogger(WeatherBot.class);

    private String weatherApiKey = "";

    public WeatherBot(String token, String weatherApiKey) {
        super(token);
        this.weatherApiKey = weatherApiKey;
    }

    ///////////////////////////////////////////////////////////////////////////

    //TODO do something useful

    ///////////////////////////////////////////////////////////////////////////

    public String getWeatherApiKey() {
        return weatherApiKey;
    }
}
