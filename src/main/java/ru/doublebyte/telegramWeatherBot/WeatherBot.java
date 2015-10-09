package ru.doublebyte.telegramWeatherBot;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.telegramWeatherBot.enums.WeatherUnits;
import ru.doublebyte.telegramWeatherBot.types.Message;
import ru.doublebyte.telegramWeatherBot.types.User;
import ru.doublebyte.telegramWeatherBot.utils.Command;
import ru.doublebyte.telegramWeatherBot.utils.JsonUtil;
import ru.doublebyte.telegramWeatherBot.weatherTypes.CurrentWeather;

import java.util.List;

/**
 * Weather bot class
 */
public class WeatherBot extends Bot {

    private static final Logger logger = LoggerFactory.getLogger(WeatherBot.class);

    private String weatherApiKey = "";
    private String language = "en";
    private WeatherUnits units = WeatherUnits.metric;

    private final String weatherApiUrl = "http://api.openweathermap.org/data/2.5/{endpoint}";

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

        for(Message message: updates) {
            User from = message.getFrom();
            Command command;

            try {
                command = new Command(message.getText());
            } catch(Exception e) {
                sendReply(from, "This is not a command :(");
                continue;
            }

            switch(command.getCommand()) {
                case "weather":
                    sendCurrentWeather(from, command);
                    break;
                case "help":
                    sendReply(from, "/weather <city,country> - Current weather for city");
                    break;
                default:
                    sendReply(from, "Unknown command :(");
            }
        }
    }

    /**
     * Send weather to user
     * @param user User
     * @param command Command from user
     */
    private void sendCurrentWeather(User user, Command command) {
        try {
            String[] args = command.getArgs();
            if(args.length == 0) {
                sendReply(user, "Provide city name!");
                return;
            }
            String city = args[0];

            CurrentWeather currentWeather = getCurrentWeather(city);

            //TODO more info, conditions

            String format = "Now in city \"%s\" is %d °C\n" +
                            "Humidity: %d%%\n" +
                            "Pressure: %d hpa";

            int temperature = (int)Math.round(currentWeather.getWeather().getTemperature());

            String weather = String.format(format,
                    currentWeather.getCityName(),
                    temperature,
                    currentWeather.getWeather().getHumidity(),
                    currentWeather.getWeather().getPressure());

            sendReply(user, weather);

        } catch(Exception e) {
            sendReply(user, "Failed to get weather :(");
        }
    }

    /**
     * Get current weather
     * @return Current weather
     * @throws Exception
     */
    private CurrentWeather getCurrentWeather(String city) throws Exception {
        try {
            HttpResponse<JsonNode> response = Unirest.get(weatherApiUrl)
                    .routeParam("endpoint", "weather")
                    .queryString("APPID", weatherApiKey)
                    .queryString("lang", language)
                    .queryString("units", units.toString())
                    .queryString("q", city)
                    .asJson();
            JSONObject weatherObject = response.getBody().getObject();
            CurrentWeather weather = JsonUtil.toObject(weatherObject, CurrentWeather.class);

            if(weather == null) {
                throw new Exception("Cannot parse weather");
            }

            return weather;
        } catch(Exception e) {
            logger.error("Cannot get weather data", e);
            throw e;
        }
    }

    /**
     * Send reply message to user without throwing an exception
     * @param user User
     * @param message Message
     */
    private void sendReply(User user, String message) {
        try {
            sendMessage(user.getId(), message);
        } catch(Exception e) {
            logger.error("Canot send reply to " + user.toString(), e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getWeatherApiKey() {
        return weatherApiKey;
    }

    public String getWeatherApiUrl() {
        return weatherApiUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public WeatherUnits getUnits() {
        return units;
    }

    public void setUnits(WeatherUnits units) {
        this.units = units;
    }
}
