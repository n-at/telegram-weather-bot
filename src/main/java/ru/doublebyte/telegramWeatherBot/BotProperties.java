package ru.doublebyte.telegramWeatherBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.telegramWeatherBot.enums.WeatherUnits;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Bot properties class
 */
public class BotProperties {

    private static final Logger logger = LoggerFactory.getLogger(BotProperties.class);
    private static BotProperties instance = null;

    private String telegramBotToken = "";
    private String openWeatherMapApiKey = "";

    private long pollInterval = 5000;

    private String language = "en";
    private WeatherUnits units = WeatherUnits.metric;

    private BotProperties() {
        logger.info("Loading bot properties...");

        try {
            FileReader reader = new FileReader("bot.properties");
            Properties properties = new Properties();
            properties.load(reader);

            telegramBotToken = properties.getProperty("telegramBotToken");
            if(telegramBotToken == null) {
                logger.warn("No Telegram bot API key found!");
                telegramBotToken = "";
            }

            openWeatherMapApiKey = properties.getProperty("openWeatherMapApiKey");
            if(openWeatherMapApiKey == null) {
                logger.warn("No OpenWeatherMap API key found!");
                openWeatherMapApiKey = "";
            }

            try {
                pollInterval = Long.parseLong(properties.getProperty("pollInterval"));
            } catch (Exception e) {
                logger.error("Malformed poll interval");
                pollInterval = 5000;
            }

            language = properties.getProperty("language");
            if(language == null) {
                logger.warn("No language found! English used");
                language = "en";
            }

            try {
                units = WeatherUnits.valueOf(properties.getProperty("units"));
            } catch(Exception e) {
                logger.warn("Units not found! Metric used");
                units = WeatherUnits.metric;
            }

        } catch(IOException e) {
            logger.error("Unable to load bot properties");
        }
    }

    public static BotProperties getInstance() {
        if(instance == null) {
            instance = new BotProperties();
        }
        return instance;
    }

    ///////////////////////////////////////////////////////////////////////////


    public String getTelegramBotToken() {
        return telegramBotToken;
    }

    public String getOpenWeatherMapApiKey() {
        return openWeatherMapApiKey;
    }

    public long getPollInterval() {
        return pollInterval;
    }

    public String getLanguage() {
        return language;
    }

    public WeatherUnits getUnits() {
        return units;
    }
}
