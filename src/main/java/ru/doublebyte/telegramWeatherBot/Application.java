package ru.doublebyte.telegramWeatherBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        BotProperties properties = BotProperties.getInstance();
        WeatherBot bot = new WeatherBot(properties.getTelegramBotToken(),
                properties.getOpenWeatherMapApiKey());

        while(true) {
            logger.info("Polling...");

            bot.getMe();

            try {
                Thread.sleep(properties.getPollInterval());
            } catch(InterruptedException e) {
                logger.error("Bot thread interrupted", e);
                break;
            }
        }
    }

}
