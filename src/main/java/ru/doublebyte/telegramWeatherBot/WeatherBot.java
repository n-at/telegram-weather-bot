package ru.doublebyte.telegramWeatherBot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.telegramWeatherBot.types.Message;

import java.util.List;

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

    /**
     * Handle users requests
     */
    public void handleRequests() {
        List<Message> updates = getUpdates();

        logger.info("Got " + updates.size() + " update(s)");

        for(Message message: updates) {
            logger.info(String.format("Message id=%s, from=%s, text=%s",
                    message.getMessageId(),
                    message.getFrom().getFirstName(),
                    message.getText()));
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getWeatherApiKey() {
        return weatherApiKey;
    }
}
