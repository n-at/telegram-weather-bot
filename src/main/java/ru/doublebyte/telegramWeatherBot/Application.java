package ru.doublebyte.telegramWeatherBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Testing...");

        BotProperties properties = BotProperties.getInstance();
        logger.info("Telegram token: " + properties.getTelegramBotToken());
        logger.info("OpenWeatherMap api key: " + properties.getOpenWeatherMapApiKey());
    }

}
