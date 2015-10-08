package ru.doublebyte.telegramWeatherBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.telegramWeatherBot.types.User;

public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        BotProperties properties = BotProperties.getInstance();
        WeatherBot bot = new WeatherBot(properties.getTelegramBotToken(),
                properties.getOpenWeatherMapApiKey());

        while(true) {

            try {
                User user = bot.getMe();
                logger.info("I am:");
                logger.info("id: " + user.getId());
                logger.info("first_name: " + user.getFirstName());
                logger.info("last_name: " + user.getLastName());
                logger.info("user_name: " + user.getUserName());
                logger.info("-------------------");
            } catch(Exception e) {
                logger.error("Cannot get bot info", e);
            }

            try {
                Thread.sleep(properties.getPollInterval());
            } catch(InterruptedException e) {
                logger.error("Bot thread interrupted", e);
                break;
            }
        }
    }

}
