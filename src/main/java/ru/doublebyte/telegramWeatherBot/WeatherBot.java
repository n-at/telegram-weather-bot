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
import ru.doublebyte.telegramWeatherBot.weatherTypes.WeatherCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Weather bot class
 */
public class WeatherBot extends Bot {

    private static final Logger logger = LoggerFactory.getLogger(WeatherBot.class);

    private String weatherApiKey = "";
    private String language = "en";
    private WeatherUnits units = WeatherUnits.metric;

    private final String weatherApiUrl = "http://api.openweathermap.org/data/2.5/{endpoint}";
    private final String messagesBundleName = "weatherBot.messages";

    private ResourceBundle messages = ResourceBundle.getBundle(messagesBundleName, Locale.ENGLISH);

    ///////////////////////////////////////////////////////////////////////////

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
                sendReply(from, messages.getString("not_a_command"));
                continue;
            }

            switch(command.getCommand()) {
                case "weather":
                    sendCurrentWeather(from, command);
                    break;
                case "help":
                    sendReply(from, messages.getString("help_weather"));
                    break;
                default:
                    sendReply(from, messages.getString("unknown_command"));
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
                sendReply(user, messages.getString("need_city_name"));
                return;
            }
            String city = args[0];

            CurrentWeather currentWeather = getCurrentWeather(city);

            List<String> conditions = new ArrayList<>();
            for(WeatherCondition condition: currentWeather.getWeatherCondition()) {
                conditions.add(condition.getDescription());
            }

            int temperature = (int)Math.round(currentWeather.getWeather().getTemperature());
            int humidity = currentWeather.getWeather().getHumidity();
            int pressure = (int)Math.round(currentWeather.getWeather().getPressure() * 0.75006375541921);

            String weather = String.format(messages.getString("current_weather_format"),
                    currentWeather.getCityName(),
                    temperature, String.join(", ", conditions), humidity, pressure);

            sendReply(user, weather);

        } catch(Exception e) {
            sendReply(user, messages.getString("weather_get_error"));
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
        messages = ResourceBundle.getBundle(messagesBundleName, new Locale(language));
    }

    public WeatherUnits getUnits() {
        return units;
    }

    public void setUnits(WeatherUnits units) {
        this.units = units;
    }
}
